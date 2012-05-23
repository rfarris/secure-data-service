package org.slc.sli.modeling.wadl;

/**
 * Symbolic constants used for element names in XMI.
 */
public enum WadlElementName {
    /**
     * An <code>application</code> element.
     */
    APPLICATION("application"),
    /**
     * A <code>documentation</code> element.
     */
    DOCUMENTATION("doc"),
    /**
     * A <code>fault</code> element.
     */
    FAULT("fault"),
    /**
     * A <code>grammars</code> element.
     */
    GRAMMARS("grammars"),
    /**
     * An <code>include</code> element.
     */
    INCLUDE("include"),
    /**
     * A <code>method</code> element.
     */
    METHOD("method"),
    /**
     * A <code>option</code> element.
     */
    OPTION("option"),
    /**
     * A <code>param</code> element.
     */
    PARAM("param"),
    /**
     * A <code>representation</code> element.
     */
    REPRESENTATION("representation"),
    /**
     * A <code>request</code> element.
     */
    REQUEST("request"),
    /**
     * A <code>resource</code> element.
     */
    RESOURCE("resource"),
    /**
     * A <code>resources</code> element.
     */
    RESOURCES("resources"),
    /**
     * A <code>resource_type</code> element.
     */
    RESOURCE_TYPE("resource_type"),
    /**
     * A <code>response</code> element.
     */
    RESPONSE("response");

    public static final WadlElementName getElementName(final String localName) {
        for (final WadlElementName name : values()) {
            if (name.localName.equals(localName)) {
                return name;
            }
        }
        return null;
    }

    private final String localName;

    WadlElementName(final String localName) {
        if (localName == null) {
            throw new NullPointerException("localName");
        }
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }
}
