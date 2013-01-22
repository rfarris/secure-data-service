//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * GraduationPlan record with key fields: GraduationPlanType and EducationOrganizationReference (StateOrganizationId). Changed type of EducationOrganizationReference to SLC reference types.
 * 
 * <p>Java class for SLC-GraduationPlan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-GraduationPlan">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="GraduationPlanType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://ed-fi.org/0100}GraduationPlanType">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IndividualPlan" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="TotalCreditsRequired" type="{http://ed-fi.org/0100}Credits"/>
 *         &lt;element name="CreditsBySubject" type="{http://ed-fi.org/0100}CreditsBySubject" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CreditsByCourse" type="{http://ed-fi.org/0100}CreditsByCourse" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-GraduationPlan", propOrder = {
    "graduationPlanType",
    "individualPlan",
    "totalCreditsRequired",
    "creditsBySubject",
    "creditsByCourse",
    "educationOrganizationReference"
})
public class SLCGraduationPlan
    extends ComplexObjectType
{

    @XmlElement(name = "GraduationPlanType", required = true)
    protected GraduationPlanType graduationPlanType;
    @XmlElement(name = "IndividualPlan")
    protected Boolean individualPlan;
    @XmlElement(name = "TotalCreditsRequired", required = true)
    protected Credits totalCreditsRequired;
    @XmlElement(name = "CreditsBySubject")
    protected List<CreditsBySubject> creditsBySubject;
    @XmlElement(name = "CreditsByCourse")
    protected List<CreditsByCourse> creditsByCourse;
    @XmlElement(name = "EducationOrganizationReference")
    protected List<SLCEducationalOrgReferenceType> educationOrganizationReference;

    /**
     * Gets the value of the graduationPlanType property.
     * 
     * @return
     *     possible object is
     *     {@link GraduationPlanType }
     *     
     */
    public GraduationPlanType getGraduationPlanType() {
        return graduationPlanType;
    }

    /**
     * Sets the value of the graduationPlanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GraduationPlanType }
     *     
     */
    public void setGraduationPlanType(GraduationPlanType value) {
        this.graduationPlanType = value;
    }

    /**
     * Gets the value of the individualPlan property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIndividualPlan() {
        return individualPlan;
    }

    /**
     * Sets the value of the individualPlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIndividualPlan(Boolean value) {
        this.individualPlan = value;
    }

    /**
     * Gets the value of the totalCreditsRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getTotalCreditsRequired() {
        return totalCreditsRequired;
    }

    /**
     * Sets the value of the totalCreditsRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setTotalCreditsRequired(Credits value) {
        this.totalCreditsRequired = value;
    }

    /**
     * Gets the value of the creditsBySubject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creditsBySubject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreditsBySubject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreditsBySubject }
     * 
     * 
     */
    public List<CreditsBySubject> getCreditsBySubject() {
        if (creditsBySubject == null) {
            creditsBySubject = new ArrayList<CreditsBySubject>();
        }
        return this.creditsBySubject;
    }

    /**
     * Gets the value of the creditsByCourse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creditsByCourse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreditsByCourse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreditsByCourse }
     * 
     * 
     */
    public List<CreditsByCourse> getCreditsByCourse() {
        if (creditsByCourse == null) {
            creditsByCourse = new ArrayList<CreditsByCourse>();
        }
        return this.creditsByCourse;
    }

    /**
     * Gets the value of the educationOrganizationReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationOrganizationReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationOrganizationReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCEducationalOrgReferenceType }
     * 
     * 
     */
    public List<SLCEducationalOrgReferenceType> getEducationOrganizationReference() {
        if (educationOrganizationReference == null) {
            educationOrganizationReference = new ArrayList<SLCEducationalOrgReferenceType>();
        }
        return this.educationOrganizationReference;
    }

}
