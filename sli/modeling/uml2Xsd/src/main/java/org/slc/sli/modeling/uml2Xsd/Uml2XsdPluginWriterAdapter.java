package org.slc.sli.modeling.uml2Xsd;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slc.sli.modeling.uml.Occurs;
import org.slc.sli.modeling.xsd.XsdAttributeName;
import org.slc.sli.modeling.xsd.XsdNamespace;

public class Uml2XsdPluginWriterAdapter implements Uml2XsdPluginWriter {
    
    public void minOccurs(final Occurs value) {
        if (!value.equals(Occurs.ONE)) {
            try {
                xsw.writeAttribute(XsdAttributeName.MIN_OCCURS.getLocalName(), toString(value));
            } catch (final XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void maxOccurs(final Occurs value) {
        if (!value.equals(Occurs.ONE)) {
            try {
                xsw.writeAttribute(XsdAttributeName.MAX_OCCURS.getLocalName(), toString(value));
            } catch (final XMLStreamException e) {
                throw new RuntimeException(e);
            }
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
    
    private final String prefix;
    
    private final XMLStreamWriter xsw;
    
    public Uml2XsdPluginWriterAdapter(final XMLStreamWriter xsw, final String prefix) {
        this.xsw = xsw;
        this.prefix = prefix;
    }
    
    @Override
    public void annotation() {
        try {
            xsw.writeStartElement(prefix, "annotation", XsdNamespace.URI);
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void characters(final String text) {
        try {
            xsw.writeCharacters(text);
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void choice() {
        try {
            xsw.writeStartElement(prefix, "choice", XsdNamespace.URI);
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void comment(final String data) {
        try {
            xsw.writeComment(data);
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void documentation() {
        try {
            xsw.writeStartElement(prefix, "documentation", XsdNamespace.URI);
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void element() {
        try {
            xsw.writeStartElement(prefix, "element", XsdNamespace.URI);
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void end() {
        try {
            xsw.writeEndElement();
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void name(final QName name) {
        try {
            xsw.writeAttribute("name", name.getLocalPart());
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void type(final QName name) {
        try {
            xsw.writeAttribute("type", name.getLocalPart());
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
}
