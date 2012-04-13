package org.slc.sli.modeling.uml2Xsd;

import static org.slc.sli.modeling.xml.XmlTools.collapseWhitespace;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slc.sli.modeling.psm.PsmClassType;
import org.slc.sli.modeling.uml.AssociationEnd;
import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.DataType;
import org.slc.sli.modeling.uml.EnumLiteral;
import org.slc.sli.modeling.uml.EnumType;
import org.slc.sli.modeling.uml.Generalization;
import org.slc.sli.modeling.uml.Identifier;
import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.Multiplicity;
import org.slc.sli.modeling.uml.Occurs;
import org.slc.sli.modeling.uml.Range;
import org.slc.sli.modeling.uml.TagDefinition;
import org.slc.sli.modeling.uml.TaggedValue;
import org.slc.sli.modeling.uml.Type;
import org.slc.sli.modeling.xml.IndentingXMLStreamWriter;
import org.slc.sli.modeling.xsd.XsdAttributeName;
import org.slc.sli.modeling.xsd.XsdElementName;
import org.slc.sli.modeling.xsd.XsdNamespace;

/**
 * Writes a UML {@link Model} to a file (by name) or {@link OutputStream}.
 */
final class Uml2XsdWriter {
    
    private static final String NAMESPACE_SLI = "http://slc-sli/ed-org/0.1";
    private static final String NAMESPACE_XS = XsdNamespace.URI;
    
    private static final String PREFIX_SLI = "sli";
    private static final String PREFIX_XS = "xs";
    private static final QName SLI_REFERENCE_TYPE = new QName(NAMESPACE_SLI, "ReferenceType", PREFIX_SLI);
    /**
     * xs:string is the default type that we use for a data type that has no generalizations.
     */
    private static final QName STRING = new QName(NAMESPACE_XS, "string");
    /**
     * xs:token is the default type that we use for a data type that has no generalizations.
     */
    private static final QName TOKEN = new QName(NAMESPACE_XS, "token");
    
    private static final void attributeFormDefault(final boolean qualified, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.ATTRIBUTE_FORM_DEFAULT.getLocalName(), qualified ? "qualified"
                : "unqualified");
    }
    
    private static final void base(final QName name, final XMLStreamWriter xsw) throws XMLStreamException {
        if (name.getNamespaceURI().equals(NAMESPACE_XS)) {
            xsw.writeAttribute(XsdAttributeName.BASE.getLocalName(), PREFIX_XS.concat(":").concat(name.getLocalPart()));
        } else {
            xsw.writeAttribute(XsdAttributeName.BASE.getLocalName(), name.getLocalPart());
        }
    }
    
    private static final void base(final Type type, final Uml2XsdPlugin plugin, final XMLStreamWriter xsw)
            throws XMLStreamException {
        final QName name = plugin.getTypeName(type.getName());
        base(name, xsw);
    }
    
    private static final void closeQuiet(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException e) {
            // Ignore.
        }
    }
    
    private static final void elementFormDefault(final boolean qualified, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.ELEMENT_FORM_DEFAULT.getLocalName(), qualified ? "qualified"
                : "unqualified");
    }
    
    private static final Type getBase(final Type type) {
        final List<Generalization> generalizationBase = type.getGeneralizationBase();
        if (generalizationBase.isEmpty()) {
            return null;
        } else {
            if (generalizationBase.size() == 1) {
                return generalizationBase.get(0).getParent();
            } else {
                throw new AssertionError(type.getName());
            }
        }
    }
    
    /**
     * Not all association ends have names so we synthesize a name based upon the type.
     */
    private static final QName getName(final AssociationEnd element) {
        if (!element.getName().getLocalPart().trim().isEmpty()) {
            return element.getName();
        } else {
            // Name using the element type. Could be more sophisticated here.
            return new Uml2XsdSyntheticHasName(element).getName();
        }
    }
    
    private static final boolean isW3cXmlSchemaType(final QName name) {
        final String namespace = name.getNamespaceURI();
        return namespace.equals(NAMESPACE_XS);
    }
    
    private static final String lexicalName(final QName name) {
        if (isW3cXmlSchemaType(name)) {
            return PREFIX_XS.concat(":").concat(name.getLocalPart());
        } else {
            return name.getLocalPart();
        }
    }
    
    private static final void maxOccurs(final Occurs value, final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.MAX_OCCURS.getLocalName(), toString(value));
    }
    
    private static final void minOccurs(final Occurs value, final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.MIN_OCCURS.getLocalName(), toString(value));
    }
    
    private static final void name(final QName name, final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.NAME.getLocalName(), name.toString());
    }
    
    private static final void occurrences(final Multiplicity multiplicity, final XMLStreamWriter xsw)
            throws XMLStreamException {
        final Range range = multiplicity.getRange();
        if (!range.getLower().equals(Occurs.ONE)) {
            minOccurs(range.getLower(), xsw);
        }
        if (!range.getUpper().equals(Occurs.ONE)) {
            maxOccurs(range.getUpper(), xsw);
        }
    }
    
    /**
     * Writes the UML model to the XML stream in XMI format.
     * 
     * @param model
     *            The UML model.
     * @param xsw
     *            The XML stream.
     * @throws XMLStreamException
     *             if anything bad happens.
     */
    private static final void schema(final List<PsmClassType<Type>> elements, final Model model,
            final Uml2XsdPlugin plugin, final XMLStreamWriter xsw) throws XMLStreamException {
        writeStartElement(XsdElementName.SCHEMA, xsw);
        try {
            xsw.writeNamespace(PREFIX_XS, NAMESPACE_XS);
            if (plugin.isEnabled(SLI_REFERENCE_TYPE)) {
                xsw.writeNamespace(PREFIX_SLI, NAMESPACE_SLI);
            }
            attributeFormDefault(false, xsw);
            elementFormDefault(true, xsw);
            for (final PsmClassType<Type> element : elements) {
                writeElement(element, plugin, xsw);
            }
            final Map<Identifier, DataType> dataTypeMap = model.getDataTypeMap();
            for (final Identifier key : dataTypeMap.keySet()) {
                final DataType type = dataTypeMap.get(key);
                simpleType(type, plugin, xsw);
            }
            final Map<Identifier, EnumType> enumTypeMap = model.getEnumTypeMap();
            for (final Identifier key : enumTypeMap.keySet()) {
                final EnumType type = enumTypeMap.get(key);
                simpleType(type, plugin, xsw);
            }
            final Map<Identifier, ClassType> classTypeMap = model.getClassTypeMap();
            for (final Identifier key : classTypeMap.keySet()) {
                final ClassType type = classTypeMap.get(key);
                writeComplexType(type, plugin, xsw);
            }
        } finally {
            xsw.writeEndElement();
        }
    }
    
    private static final void simpleType(final DataType simpleType, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        if (!isW3cXmlSchemaType(simpleType.getName())) {
            writeStartElement(XsdElementName.SIMPLE_TYPE, xsw);
            try {
                name(plugin.getTypeName(simpleType.getName()), xsw);
                writeStartElement(XsdElementName.ANNOTATION, xsw);
                try {
                    for (final TaggedValue taggedValue : simpleType.getTaggedValues()) {
                        final TagDefinition tagDefinition = taggedValue.getTagDefinition();
                        final QName name = tagDefinition.getName();
                        if (TagDefinition.NAME_DOCUMENTATION.equals(name)) {
                            writeDocumentation(taggedValue, xsw);
                        }
                    }
                } finally {
                    writeEndElement(xsw);
                }
                writeStartElement(XsdElementName.RESTRICTION, xsw);
                try {
                    final Type base = getBase(simpleType);
                    if (base != null) {
                        base(base, plugin, xsw);
                    } else {
                        base(STRING, xsw);
                    }
                    for (final TaggedValue taggedValue : simpleType.getTaggedValues()) {
                        final TagDefinition tagDefinition = taggedValue.getTagDefinition();
                        final QName name = tagDefinition.getName();
                        if (TagDefinition.NAME_MAX_LENGTH.equals(name)) {
                            writeFacet(XsdElementName.MAX_LENGTH, taggedValue, xsw);
                        } else if (TagDefinition.NAME_MIN_LENGTH.equals(name)) {
                            writeFacet(XsdElementName.MIN_LENGTH, taggedValue, xsw);
                        } else if (TagDefinition.NAME_MAX_INCLUSIVE.equals(name)) {
                            writeFacet(XsdElementName.MAX_INCLUSIVE, taggedValue, xsw);
                        } else if (TagDefinition.NAME_MIN_INCLUSIVE.equals(name)) {
                            writeFacet(XsdElementName.MIN_INCLUSIVE, taggedValue, xsw);
                        } else if (TagDefinition.NAME_MAX_EXCLUSIVE.equals(name)) {
                            writeFacet(XsdElementName.MAX_EXCLUSIVE, taggedValue, xsw);
                        } else if (TagDefinition.NAME_MIN_EXCLUSIVE.equals(name)) {
                            writeFacet(XsdElementName.MIN_EXCLUSIVE, taggedValue, xsw);
                        } else if (TagDefinition.NAME_TOTAL_DIGITS.equals(name)) {
                            writeFacet(XsdElementName.TOTAL_DIGITS, taggedValue, xsw);
                        } else if (TagDefinition.NAME_FRACTION_DIGITS.equals(name)) {
                            writeFacet(XsdElementName.FRACTION_DIGITS, taggedValue, xsw);
                        } else if (TagDefinition.NAME_LENGTH.equals(name)) {
                            writeFacet(XsdElementName.LENGTH, taggedValue, xsw);
                        } else if (TagDefinition.NAME_PATTERN.equals(name)) {
                            writeFacet(XsdElementName.PATTERN, taggedValue, xsw);
                        }
                    }
                } finally {
                    writeEndElement(xsw);
                }
            } finally {
                writeEndElement(xsw);
            }
        }
    }
    
    private static final void simpleType(final EnumType simpleType, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        writeStartElement(XsdElementName.SIMPLE_TYPE, xsw);
        try {
            name(plugin.getTypeName(simpleType.getName()), xsw);
            writeStartElement(XsdElementName.ANNOTATION, xsw);
            try {
                for (final TaggedValue taggedValue : simpleType.getTaggedValues()) {
                    final TagDefinition tagDefinition = taggedValue.getTagDefinition();
                    final QName name = tagDefinition.getName();
                    if (TagDefinition.NAME_DOCUMENTATION.equals(name)) {
                        writeDocumentation(taggedValue, xsw);
                    }
                }
            } finally {
                writeEndElement(xsw);
            }
            writeStartElement(XsdElementName.RESTRICTION, xsw);
            try {
                final Type base = getBase(simpleType);
                if (base != null) {
                    base(base, plugin, xsw);
                } else {
                    base(TOKEN, xsw);
                }
                for (final EnumLiteral literal : simpleType.getLiterals()) {
                    writeStartElement(XsdElementName.ENUMERATION, xsw);
                    value(collapseWhitespace(literal.getName().getLocalPart()), xsw);
                    xsw.writeEndElement();
                }
            } finally {
                xsw.writeEndElement();
            }
        } finally {
            xsw.writeEndElement();
        }
    }
    
    private static final String toString(final Occurs value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        switch (value) {
            case ZERO: {
                return "0";
            }
            case ONE: {
                return "1";
            }
            case UNBOUNDED: {
                return "unbounded";
            }
            default: {
                throw new AssertionError(value);
            }
        }
    }
    
    private static final void type(final QName name, final Uml2XsdPlugin plugin, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.TYPE.getLocalName(), lexicalName(name));
    }
    
    private static final void value(final String value, final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeAttribute(XsdAttributeName.VALUE.getLocalName(), value);
    }
    
    @SuppressWarnings("unused")
    private static final void writeAssociationElement(final ClassType complexType, final AssociationEnd element,
            final Uml2XsdPlugin plugin, final XMLStreamWriter xsw) throws XMLStreamException {
        writeStartElement(XsdElementName.ELEMENT, xsw);
        try {
            name(plugin.getElementName(getName(element), true), xsw);
            type(plugin.getElementType(element.getType().getName(), true), plugin, xsw);
            occurrences(element.getMultiplicity(), xsw);
            writeStartElement(XsdElementName.ANNOTATION, xsw);
            try {
                for (final TaggedValue taggedValue : element.getTaggedValues()) {
                    final TagDefinition tagDefinition = taggedValue.getTagDefinition();
                    if (TagDefinition.NAME_DOCUMENTATION.equals(tagDefinition.getName())) {
                        writeDocumentation(taggedValue, xsw);
                    }
                }
                if (plugin.isEnabled(SLI_REFERENCE_TYPE)) {
                    writeStartElement(XsdElementName.APPINFO, xsw);
                    try {
                        xsw.writeStartElement(SLI_REFERENCE_TYPE.getPrefix(), SLI_REFERENCE_TYPE.getLocalPart(),
                                SLI_REFERENCE_TYPE.getNamespaceURI());
                        xsw.writeCharacters(plugin.getTypeName(element.getType().getName()).getLocalPart());
                        xsw.writeEndElement();
                    } finally {
                        writeEndElement(xsw);
                    }
                }
            } finally {
                writeEndElement(xsw);
            }
        } finally {
            writeEndElement(xsw);
        }
    }
    
    private static final void writeAssociationElements(final ClassType complexType, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        for (final AssociationEnd element : complexType.getAssociationEnds()) {
            plugin.writeAssociationElement(complexType, element, new Uml2XsdPluginWriterAdapter(xsw, PREFIX_XS));
        }
    }
    
    private static final void writeAttributeElements(final ClassType complexType, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        for (final Attribute element : complexType.getAttributes()) {
            writeElement(complexType, element, plugin, xsw);
        }
    }
    
    private static final void writeComplexType(final ClassType complexType, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        writeStartElement(XsdElementName.COMPLEX_TYPE, xsw);
        try {
            final QName name = plugin.getTypeName(complexType.getName());
            name(name, xsw);
            final Type base = getBase(complexType);
            if (base == null) {
                writeElements(complexType, plugin, xsw);
            } else {
                writeStartElement(XsdElementName.COMPLEX_CONTENT, xsw);
                try {
                    writeStartElement(XsdElementName.EXTENSION, xsw);
                    base(base, plugin, xsw);
                    try {
                        writeElements(complexType, plugin, xsw);
                    } finally {
                        writeEndElement(xsw);
                    }
                } finally {
                    writeEndElement(xsw);
                }
            }
        } finally {
            writeEndElement(xsw);
        }
    }
    
    public static final void writeDocument(final List<PsmClassType<Type>> elements, final Model model,
            final Uml2XsdPlugin plugin, final OutputStream outstream) {
        final XMLOutputFactory xof = XMLOutputFactory.newInstance();
        try {
            final XMLStreamWriter xsw = new IndentingXMLStreamWriter(xof.createXMLStreamWriter(outstream, "UTF-8"));
            xsw.writeStartDocument("UTF-8", "1.0");
            try {
                schema(elements, model, plugin, xsw);
            } finally {
                xsw.writeEndDocument();
            }
            xsw.flush();
            xsw.close();
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static final void writeDocument(final List<PsmClassType<Type>> elements, final Model model,
            final Uml2XsdPlugin plugin, final String fileName) {
        try {
            final OutputStream outstream = new BufferedOutputStream(new FileOutputStream(fileName));
            try {
                writeDocument(elements, model, plugin, outstream);
            } finally {
                closeQuiet(outstream);
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static final void writeDocumentation(final TaggedValue taggedValue, final XMLStreamWriter xsw)
            throws XMLStreamException {
        writeStartElement(XsdElementName.DOCUMENTATION, xsw);
        try {
            xsw.writeCharacters(collapseWhitespace(taggedValue.getValue()));
        } finally {
            writeEndElement(xsw);
        }
    }
    
    private static final void writeElement(final ClassType complexType, final Attribute element,
            final Uml2XsdPlugin plugin, final XMLStreamWriter xsw) throws XMLStreamException {
        writeStartElement(XsdElementName.ELEMENT, xsw);
        try {
            final QName name = plugin.getElementName(element.getName(), false);
            name(name, xsw);
            final QName type = plugin.getElementType(element.getType().getName(), false);
            type(type, plugin, xsw);
            occurrences(element.getMultiplicity(), xsw);
            writeStartElement(XsdElementName.ANNOTATION, xsw);
            try {
                for (final TaggedValue taggedValue : element.getTaggedValues()) {
                    final TagDefinition tagDefinition = taggedValue.getTagDefinition();
                    if (TagDefinition.NAME_DOCUMENTATION.equals(tagDefinition.getName())) {
                        writeDocumentation(taggedValue, xsw);
                    }
                }
            } finally {
                writeEndElement(xsw);
            }
        } finally {
            writeEndElement(xsw);
        }
    }
    
    private static final void writeElement(final PsmClassType<Type> element, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        writeStartElement(XsdElementName.ELEMENT, xsw);
        try {
            final QName name = plugin.getElementName(element);
            name(name, xsw);
            final QName type = plugin.getElementType(element.getType().getName(), false);
            type(type, plugin, xsw);
            writeStartElement(XsdElementName.ANNOTATION, xsw);
            try {
                for (final TaggedValue taggedValue : element.getType().getTaggedValues()) {
                    final TagDefinition tagDefinition = taggedValue.getTagDefinition();
                    if (TagDefinition.NAME_DOCUMENTATION.equals(tagDefinition.getName())) {
                        writeDocumentation(taggedValue, xsw);
                    }
                }
            } finally {
                writeEndElement(xsw);
            }
        } finally {
            writeEndElement(xsw);
        }
    }
    
    private static final void writeElements(final ClassType complexType, final Uml2XsdPlugin plugin,
            final XMLStreamWriter xsw) throws XMLStreamException {
        /**
         * We are virtually forced to write all the elements inside the xs:sequence compositor
         * because of the restrictions on xs:all:
         * 1) xs:all is not a particle so it cannot be embedded inside a compositor.
         * 2) xs:all does not accept elements whose occurences are greater than one.
         */
        writeStartElement(XsdElementName.SEQUENCE, xsw);
        try {
            writeAttributeElements(complexType, plugin, xsw);
            writeAssociationElements(complexType, plugin, xsw);
        } finally {
            writeEndElement(xsw);
        }
    }
    
    private static final void writeEndElement(final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeEndElement();
    }
    
    private static final void writeFacet(final XsdElementName name, TaggedValue taggedValue, final XMLStreamWriter xsw)
            throws XMLStreamException {
        writeStartElement(name, xsw);
        try {
            value(collapseWhitespace(taggedValue.getValue()), xsw);
        } finally {
            writeEndElement(xsw);
        }
    }
    
    private static final void writeStartElement(final XsdElementName name, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeStartElement(PREFIX_XS, name.getLocalName(), NAMESPACE_XS);
    }
}
