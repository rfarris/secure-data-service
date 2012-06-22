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
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents the competency assessed or evaluated for the student against a specific Learning Objective.
 * 
 * <p>Java class for StudentCompetency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentCompetency">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}LearningObjectiveReferenceType"/>
 *           &lt;element name="StudentCompetencyObjectiveReference" type="{http://ed-fi.org/0100}StudentCompetencyObjectiveReferenceType"/>
 *         &lt;/choice>
 *         &lt;element name="CompetencyLevel" type="{http://ed-fi.org/0100}CompetencyLevelDescriptorType"/>
 *         &lt;element name="DiagnosticStatement" type="{http://ed-fi.org/0100}text" minOccurs="0"/>
 *         &lt;element name="StudentSectionAssociationReference" type="{http://ed-fi.org/0100}StudentSectionAssociationReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentCompetency", propOrder = {
    "learningObjectiveReference",
    "studentCompetencyObjectiveReference",
    "competencyLevel",
    "diagnosticStatement",
    "studentSectionAssociationReference"
})
public class StudentCompetency
    extends ComplexObjectType
{

    @XmlElement(name = "LearningObjectiveReference")
    protected LearningObjectiveReferenceType learningObjectiveReference;
    @XmlElement(name = "StudentCompetencyObjectiveReference")
    protected StudentCompetencyObjectiveReferenceType studentCompetencyObjectiveReference;
    @XmlElement(name = "CompetencyLevel", required = true)
    protected CompetencyLevelDescriptorType competencyLevel;
    @XmlElement(name = "DiagnosticStatement")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String diagnosticStatement;
    @XmlElement(name = "StudentSectionAssociationReference")
    protected StudentSectionAssociationReferenceType studentSectionAssociationReference;

    /**
     * Gets the value of the learningObjectiveReference property.
     * 
     * @return
     *     possible object is
     *     {@link LearningObjectiveReferenceType }
     *     
     */
    public LearningObjectiveReferenceType getLearningObjectiveReference() {
        return learningObjectiveReference;
    }

    /**
     * Sets the value of the learningObjectiveReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link LearningObjectiveReferenceType }
     *     
     */
    public void setLearningObjectiveReference(LearningObjectiveReferenceType value) {
        this.learningObjectiveReference = value;
    }

    /**
     * Gets the value of the studentCompetencyObjectiveReference property.
     * 
     * @return
     *     possible object is
     *     {@link StudentCompetencyObjectiveReferenceType }
     *     
     */
    public StudentCompetencyObjectiveReferenceType getStudentCompetencyObjectiveReference() {
        return studentCompetencyObjectiveReference;
    }

    /**
     * Sets the value of the studentCompetencyObjectiveReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentCompetencyObjectiveReferenceType }
     *     
     */
    public void setStudentCompetencyObjectiveReference(StudentCompetencyObjectiveReferenceType value) {
        this.studentCompetencyObjectiveReference = value;
    }

    /**
     * Gets the value of the competencyLevel property.
     * 
     * @return
     *     possible object is
     *     {@link CompetencyLevelDescriptorType }
     *     
     */
    public CompetencyLevelDescriptorType getCompetencyLevel() {
        return competencyLevel;
    }

    /**
     * Sets the value of the competencyLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompetencyLevelDescriptorType }
     *     
     */
    public void setCompetencyLevel(CompetencyLevelDescriptorType value) {
        this.competencyLevel = value;
    }

    /**
     * Gets the value of the diagnosticStatement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosticStatement() {
        return diagnosticStatement;
    }

    /**
     * Sets the value of the diagnosticStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosticStatement(String value) {
        this.diagnosticStatement = value;
    }

    /**
     * Gets the value of the studentSectionAssociationReference property.
     * 
     * @return
     *     possible object is
     *     {@link StudentSectionAssociationReferenceType }
     *     
     */
    public StudentSectionAssociationReferenceType getStudentSectionAssociationReference() {
        return studentSectionAssociationReference;
    }

    /**
     * Sets the value of the studentSectionAssociationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentSectionAssociationReferenceType }
     *     
     */
    public void setStudentSectionAssociationReference(StudentSectionAssociationReferenceType value) {
        this.studentSectionAssociationReference = value;
    }

}
