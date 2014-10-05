//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2013.07.10 at 12:45:26 PM CEST
//
// TOSCA version: TOSCA-v1.0-cs02.xsd
//

package org.opentosca.model.tosca;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tTopologyTemplate complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="tTopologyTemplate">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/tosca/ns/2011/12}tExtensibleElements">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="NodeTemplate" type="{http://docs.oasis-open.org/tosca/ns/2011/12}tNodeTemplate"/>
 *         &lt;element name="RelationshipTemplate" type="{http://docs.oasis-open.org/tosca/ns/2011/12}tRelationshipTemplate"/>
 *       &lt;/choice>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTopologyTemplate", propOrder = {"nodeTemplateOrRelationshipTemplate"})
public class TTopologyTemplate extends TExtensibleElements {
	
	@XmlElements({@XmlElement(name = "NodeTemplate", type = TNodeTemplate.class), @XmlElement(name = "RelationshipTemplate", type = TRelationshipTemplate.class)})
	protected List<TEntityTemplate> nodeTemplateOrRelationshipTemplate;
	
	
	/**
	 * Gets the value of the nodeTemplateOrRelationshipTemplate property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the nodeTemplateOrRelationshipTemplate
	 * property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getNodeTemplateOrRelationshipTemplate().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link TNodeTemplate } {@link TRelationshipTemplate }
	 * 
	 * 
	 */
	public List<TEntityTemplate> getNodeTemplateOrRelationshipTemplate() {
		if (this.nodeTemplateOrRelationshipTemplate == null) {
			this.nodeTemplateOrRelationshipTemplate = new ArrayList<TEntityTemplate>();
		}
		return this.nodeTemplateOrRelationshipTemplate;
	}


    public List<TRelationshipTemplate> getRelationshipTempaltes(){
        List<TRelationshipTemplate> relationshipTemplates=new LinkedList<TRelationshipTemplate>();
        for (TEntityTemplate entityTemplate: getNodeTemplateOrRelationshipTemplate()){
            if(entityTemplate instanceof TRelationshipTemplate){
                relationshipTemplates.add(((TRelationshipTemplate)entityTemplate));
            }
        }
        return relationshipTemplates;
    }


    public List<TNodeTemplate> getNodeTempaltes(){
        List<TNodeTemplate> nodeTemplates=new LinkedList<TNodeTemplate>();
        for (TEntityTemplate entityTemplate: getNodeTemplateOrRelationshipTemplate()){
            if(entityTemplate instanceof TNodeTemplate){
                nodeTemplates.add(((TNodeTemplate) entityTemplate));
            }
        }
        return nodeTemplates;
    }
	
}
