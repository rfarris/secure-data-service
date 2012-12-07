//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CohortYearType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CohortYearType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Eighth grade"/>
 *     &lt;enumeration value="Eleventh grade"/>
 *     &lt;enumeration value="Fifth grade"/>
 *     &lt;enumeration value="First grade"/>
 *     &lt;enumeration value="Fourth grade"/>
 *     &lt;enumeration value="Ninth grade"/>
 *     &lt;enumeration value="Second grade"/>
 *     &lt;enumeration value="Seventh grade"/>
 *     &lt;enumeration value="Sixth grade"/>
 *     &lt;enumeration value="Tenth grade"/>
 *     &lt;enumeration value="Third grade"/>
 *     &lt;enumeration value="Twelfth grade"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CohortYearType")
@XmlEnum
public enum CohortYearType {

    @XmlEnumValue("Eighth grade")
    EIGHTH_GRADE("Eighth grade"),
    @XmlEnumValue("Eleventh grade")
    ELEVENTH_GRADE("Eleventh grade"),
    @XmlEnumValue("Fifth grade")
    FIFTH_GRADE("Fifth grade"),
    @XmlEnumValue("First grade")
    FIRST_GRADE("First grade"),
    @XmlEnumValue("Fourth grade")
    FOURTH_GRADE("Fourth grade"),
    @XmlEnumValue("Ninth grade")
    NINTH_GRADE("Ninth grade"),
    @XmlEnumValue("Second grade")
    SECOND_GRADE("Second grade"),
    @XmlEnumValue("Seventh grade")
    SEVENTH_GRADE("Seventh grade"),
    @XmlEnumValue("Sixth grade")
    SIXTH_GRADE("Sixth grade"),
    @XmlEnumValue("Tenth grade")
    TENTH_GRADE("Tenth grade"),
    @XmlEnumValue("Third grade")
    THIRD_GRADE("Third grade"),
    @XmlEnumValue("Twelfth grade")
    TWELFTH_GRADE("Twelfth grade");
    private final String value;

    CohortYearType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CohortYearType fromValue(String v) {
        for (CohortYearType c: CohortYearType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
