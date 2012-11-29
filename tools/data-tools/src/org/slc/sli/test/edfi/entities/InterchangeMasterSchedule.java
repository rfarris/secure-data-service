//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="CourseOffering" type="{http://ed-fi.org/0100}SLC-CourseOffering"/>
 *         &lt;element name="Section" type="{http://ed-fi.org/0100}SLC-Section"/>
 *         &lt;element name="BellSchedule" type="{http://ed-fi.org/0100}SLC-BellSchedule"/>
 *         &lt;element name="MeetingTime" type="{http://ed-fi.org/0100}SLC-MeetingTimeReferenceType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "courseOfferingOrSectionOrBellSchedule"
})
@XmlRootElement(name = "InterchangeMasterSchedule")
public class InterchangeMasterSchedule {

    @XmlElements({
        @XmlElement(name = "MeetingTime", type = SLCMeetingTimeReferenceType.class),
        @XmlElement(name = "CourseOffering", type = SLCCourseOffering.class),
        @XmlElement(name = "Section", type = SLCSection.class),
        @XmlElement(name = "BellSchedule", type = SLCBellSchedule.class)
    })
    protected List<Object> courseOfferingOrSectionOrBellSchedule;

    /**
     * Gets the value of the courseOfferingOrSectionOrBellSchedule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseOfferingOrSectionOrBellSchedule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseOfferingOrSectionOrBellSchedule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCMeetingTimeReferenceType }
     * {@link SLCCourseOffering }
     * {@link SLCSection }
     * {@link SLCBellSchedule }
     * 
     * 
     */
    public List<Object> getCourseOfferingOrSectionOrBellSchedule() {
        if (courseOfferingOrSectionOrBellSchedule == null) {
            courseOfferingOrSectionOrBellSchedule = new ArrayList<Object>();
        }
        return this.courseOfferingOrSectionOrBellSchedule;
    }

}
