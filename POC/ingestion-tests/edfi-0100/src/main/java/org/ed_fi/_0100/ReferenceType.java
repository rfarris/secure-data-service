//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;


/**
 * This is the base type for association references.
 * 
 * <p>Java class for ReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceType")
@XmlSeeAlso({
    StudentReferenceType.class,
    AssessmentReferenceType.class,
    ParentReferenceType.class,
    LearningObjectiveReferenceType.class,
    SLCAssessmentFamilyReferenceType.class,
    CalendarDateReferenceType.class,
    StudentCompetencyObjectiveReferenceType.class,
    EducationalOrgReferenceType.class,
    StudentSectionAssociationReferenceType.class,
    GradingPeriodReferenceType.class,
    BellScheduleReferenceType.class,
    ObjectiveAssessmentReferenceType.class,
    AccountReferenceType.class,
    LocationReferenceType.class,
    ProgramReferenceType.class,
    CourseReferenceType.class,
    CredentialFieldDescriptorType.class,
    SectionReferenceType.class,
    LearningStandardReferenceType.class,
    AssessmentItemReferenceType.class,
    BehaviorDescriptorType.class,
    CompetencyLevelDescriptorType.class,
    SLCAssessmentReferenceType.class,
    DisciplineIncidentReferenceType.class,
    SessionReferenceType.class,
    AssessmentPeriodDescriptorType.class,
    StaffReferenceType.class,
    CourseOfferingReferenceType.class,
    ClassPeriodReferenceType.class,
    PerformanceLevelDescriptorType.class,
    AccountCodeDescriptorType.class,
    SLCClassPeriodReferenceType.class,
    CohortReferenceType.class,
    DisciplineDescriptorType.class,
    AssessmentFamilyReferenceType.class,
    ServiceDescriptorType.class
})
public class ReferenceType implements Locatable
{

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ref;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRef(Object value) {
        this.ref = value;
    }

    public Locator sourceLocation() {
        return locator;
    }

    public void setSourceLocation(Locator newLocator) {
        locator = newLocator;
    }

}
