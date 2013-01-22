//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProgramSponsorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProgramSponsorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Federal"/>
 *     &lt;enumeration value="State Education Agency"/>
 *     &lt;enumeration value="Education Service Center"/>
 *     &lt;enumeration value="Local Education Agency"/>
 *     &lt;enumeration value="School"/>
 *     &lt;enumeration value="Private Organization"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProgramSponsorType")
@XmlEnum
public enum ProgramSponsorType {

    @XmlEnumValue("Federal")
    FEDERAL("Federal"),
    @XmlEnumValue("State Education Agency")
    STATE_EDUCATION_AGENCY("State Education Agency"),
    @XmlEnumValue("Education Service Center")
    EDUCATION_SERVICE_CENTER("Education Service Center"),
    @XmlEnumValue("Local Education Agency")
    LOCAL_EDUCATION_AGENCY("Local Education Agency"),
    @XmlEnumValue("School")
    SCHOOL("School"),
    @XmlEnumValue("Private Organization")
    PRIVATE_ORGANIZATION("Private Organization");
    private final String value;

    ProgramSponsorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProgramSponsorType fromValue(String v) {
        for (ProgramSponsorType c: ProgramSponsorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
