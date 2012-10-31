/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.05.31 at 09:35:49 AM EDT
//


package org.slc.sli.test.edfi.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This educational entity represents the collection of student grades for courses taken during a grading period.
 *
 *
 * <p>Java class for ReportCard complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReportCard">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="GradeReference" type="{http://ed-fi.org/0100}GradeReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="StudentCompetencyReference" type="{http://ed-fi.org/0100}ReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GPAGivenGradingPeriod" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="GPACumulative" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="NumberOfDaysAbsent" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NumberOfDaysInAttendance" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NumberOfDaysTardy" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *         &lt;element name="GradingPeriodReference" type="{http://ed-fi.org/0100}GradingPeriodReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportCard", propOrder = {
    "gradeReference",
    "studentCompetencyReference",
    "gpaGivenGradingPeriod",
    "gpaCumulative",
    "numberOfDaysAbsent",
    "numberOfDaysInAttendance",
    "numberOfDaysTardy",
    "studentReference",
    "gradingPeriodReference"
})
@XmlRootElement(name = "ReportCard")
public class ReportCard
    extends ComplexObjectType
{

    @XmlElement(name = "GradeReference")
    protected List<GradeReferenceType> gradeReference;
    @XmlElement(name = "StudentCompetencyReference")
    protected List<ReferenceType> studentCompetencyReference;
    @XmlElement(name = "GPAGivenGradingPeriod", required = true)
    protected BigDecimal gpaGivenGradingPeriod;
    @XmlElement(name = "GPACumulative", required = true)
    protected BigDecimal gpaCumulative;
    @XmlElement(name = "NumberOfDaysAbsent")
    protected BigDecimal numberOfDaysAbsent;
    @XmlElement(name = "NumberOfDaysInAttendance")
    protected BigDecimal numberOfDaysInAttendance;
    @XmlElement(name = "NumberOfDaysTardy")
    protected Integer numberOfDaysTardy;
    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;
    @XmlElement(name = "GradingPeriodReference", required = true)
    protected GradingPeriodReferenceType gradingPeriodReference;

    /**
     * Gets the value of the gradeReference property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gradeReference property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGradeReference().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     *
     *
     */
    public List<GradeReferenceType> getGradeReference() {
        if (gradeReference == null) {
            gradeReference = new ArrayList<GradeReferenceType>();
        }
        return this.gradeReference;
    }

    /**
     * Gets the value of the studentCompetencyReference property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studentCompetencyReference property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudentCompetencyReference().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     *
     *
     */
    public List<ReferenceType> getStudentCompetencyReference() {
        if (studentCompetencyReference == null) {
            studentCompetencyReference = new ArrayList<ReferenceType>();
        }
        return this.studentCompetencyReference;
    }

    /**
     * Gets the value of the gpaGivenGradingPeriod property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getGPAGivenGradingPeriod() {
        return gpaGivenGradingPeriod;
    }

    /**
     * Sets the value of the gpaGivenGradingPeriod property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setGPAGivenGradingPeriod(BigDecimal value) {
        this.gpaGivenGradingPeriod = value;
    }

    /**
     * Gets the value of the gpaCumulative property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getGPACumulative() {
        return gpaCumulative;
    }

    /**
     * Sets the value of the gpaCumulative property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setGPACumulative(BigDecimal value) {
        this.gpaCumulative = value;
    }

    /**
     * Gets the value of the numberOfDaysAbsent property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getNumberOfDaysAbsent() {
        return numberOfDaysAbsent;
    }

    /**
     * Sets the value of the numberOfDaysAbsent property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setNumberOfDaysAbsent(BigDecimal value) {
        this.numberOfDaysAbsent = value;
    }

    /**
     * Gets the value of the numberOfDaysInAttendance property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getNumberOfDaysInAttendance() {
        return numberOfDaysInAttendance;
    }

    /**
     * Sets the value of the numberOfDaysInAttendance property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setNumberOfDaysInAttendance(BigDecimal value) {
        this.numberOfDaysInAttendance = value;
    }

    /**
     * Gets the value of the numberOfDaysTardy property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumberOfDaysTardy() {
        return numberOfDaysTardy;
    }

    /**
     * Sets the value of the numberOfDaysTardy property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumberOfDaysTardy(Integer value) {
        this.numberOfDaysTardy = value;
    }

    /**
     * Gets the value of the studentReference property.
     *
     * @return
     *     possible object is
     *     {@link StudentReferenceType }
     *
     */
    public StudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     *
     * @param value
     *     allowed object is
     *     {@link StudentReferenceType }
     *
     */
    public void setStudentReference(StudentReferenceType value) {
        this.studentReference = value;
    }

    /**
     * Gets the value of the gradingPeriodReference property.
     *
     * @return
     *     possible object is
     *     {@link GradingPeriodReferenceType }
     *
     */
    public GradingPeriodReferenceType getGradingPeriodReference() {
        return gradingPeriodReference;
    }

    /**
     * Sets the value of the gradingPeriodReference property.
     *
     * @param value
     *     allowed object is
     *     {@link GradingPeriodReferenceType }
     *
     */
    public void setGradingPeriodReference(GradingPeriodReferenceType value) {
        this.gradingPeriodReference = value;
    }

}
