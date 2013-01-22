//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;


/**
 * New SLC natural reference type for StudentAssessment.
 * 
 * <p>Java class for SLC-StudentAssessmentReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentAssessmentReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentAssessmentIdentity" type="{http://ed-fi.org/0100}SLC-StudentAssessmentIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentAssessmentReferenceType", propOrder = {
    "studentAssessmentIdentity"
})
public class SLCStudentAssessmentReferenceType
    implements Locatable
{

    @XmlElement(name = "StudentAssessmentIdentity", required = true)
    protected SLCStudentAssessmentIdentityType studentAssessmentIdentity;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    /**
     * Gets the value of the studentAssessmentIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentAssessmentIdentityType }
     *     
     */
    public SLCStudentAssessmentIdentityType getStudentAssessmentIdentity() {
        return studentAssessmentIdentity;
    }

    /**
     * Sets the value of the studentAssessmentIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentAssessmentIdentityType }
     *     
     */
    public void setStudentAssessmentIdentity(SLCStudentAssessmentIdentityType value) {
        this.studentAssessmentIdentity = value;
    }

    public Locator sourceLocation() {
        return locator;
    }

    public void setSourceLocation(Locator newLocator) {
        locator = newLocator;
    }

}
