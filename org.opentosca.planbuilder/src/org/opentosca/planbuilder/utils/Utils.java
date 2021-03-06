package org.opentosca.planbuilder.utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.opentosca.planbuilder.model.tosca.AbstractNodeTemplate;
import org.opentosca.planbuilder.model.tosca.AbstractNodeType;
import org.opentosca.planbuilder.model.tosca.AbstractRelationshipTemplate;
import org.opentosca.planbuilder.model.tosca.AbstractRelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <p>
 * This class holds utility methods
 * </p>
 * Copyright 2013 IAAS University of Stuttgart <br>
 * <br>
 * 
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 * 
 */
public class Utils {
	
	private final static Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	// these are the baseTypes of the PlanBuilder -> TODO refactor into some
	// kind of baseType-Configuration
	public static final QName TOSCABASETYPE_CONNECTSTO = new QName("http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes", "ConnectsTo");
	public static final QName TOSCABASETYPE_HOSTEDON = new QName("http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes", "HostedOn");
	public static final QName TOSCABASETYPE_DEPENDSON = new QName("http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes", "DependsOn");
	public static final QName TOSCABASETYPE_SERVER = new QName("http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes", "Server");
	public static final QName TOSCABASETYPE_OS = new QName("http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes", "OperatingSystem");
	
	
	/**
	 * Looks for a childelement with an attribute with the given name and value
	 * 
	 * @param element the element to look in
	 * @param attributeName the name of the attribute
	 * @param attributeValue the value of the attribute
	 * @return true if the given element has a child element with an attribute
	 *         where attrname.equals(attributeName) &
	 *         attr.value(attributeValue), else false
	 */
	public static boolean hasChildElementWithAttribute(Element element, String attributeName, String attributeValue) {
		if (element == null) {
			return false;
		}
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			Node child = element.getChildNodes().item(i);
			if ((child.getAttributes().getNamedItem(attributeName) != null) && child.getAttributes().getNamedItem(attributeName).getNodeValue().equals(attributeValue)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a ordered list of QNames. The order represents the inheritance of
	 * NodeTypes defining the given NodeType. E.g. NodeType "someNodeType"
	 * inherits properties from "someOtherNodeType". The returns list would have
	 * {someNs}someNodeType,{someNs}someOtherNodeType inside, in the exact same
	 * order.
	 * 
	 * @param nodeType the nodeType to get the hierarchy for
	 * @return a List containing an order of inheritance of NodeTypes for this
	 *         NodeType with itself at the first spot in the list.
	 */
	public static List<QName> getNodeTypeHierarchy(AbstractNodeType nodeType) {
		Utils.LOG.debug("Beginning calculating NodeType Hierarchy for: " + nodeType.getId().toString());
		List<QName> typeHierarchy = new ArrayList<QName>();
		typeHierarchy.add(nodeType.getId());
		
		boolean wasNotNull = true;
		// changed from search with qname to search with abstract classes and
		// typeref
		AbstractNodeType lastFoundNodeType = nodeType;
		while (wasNotNull) {
			
			AbstractNodeType referencedNodeType = lastFoundNodeType.getTypeRef();
			
			if (referencedNodeType == null) {
				wasNotNull = false;
			} else {
				Utils.LOG.debug("Found referenced NodeType: " + referencedNodeType.getId().toString());
				typeHierarchy.add(referencedNodeType.getId());
				lastFoundNodeType = referencedNodeType;
			}
		}
		
		return typeHierarchy;
	}
	
	/**
	 * Returns a ordered list of QNames. The order represents the inheritance of
	 * RelationshipTypes defining the given RelationshipType. E.g. Relationship
	 * "someRelationType" and it inherits properties from
	 * "someOtherRelationType". The returns list would have
	 * {someNs}someRelationType,{someNs}someOtherRelationType inside, in the
	 * exact same order.
	 * 
	 * @param definitions the Definitions to look in
	 * @param relationshipType the RelationshipType to get the hierarchy for
	 * @return a List containing an order of inheritance of RelationshipTypes of
	 *         the given RelationshipType
	 */
	public static List<QName> getRelationshipTypeHierarchy(AbstractRelationshipType relationshipType) {
		List<QName> typeHierarchy = new ArrayList<QName>();
		typeHierarchy.add(relationshipType.getId());
		
		boolean wasNotNull = true;
		AbstractRelationshipType lastFoundRelationshipType = relationshipType;
		while (wasNotNull) {
			AbstractRelationshipType referencedRelationshipType = lastFoundRelationshipType.getReferencedType();
			if (referencedRelationshipType == null) {
				wasNotNull = false;
			} else {
				typeHierarchy.add(referencedRelationshipType.getId());
				lastFoundRelationshipType = referencedRelationshipType;
			}
		}
		return typeHierarchy;
	}
	
	/**
	 * Returns the baseType of the given NodeTemplate
	 * 
	 * @param nodeTemplate an AbstractNodeTemplate
	 * @return a QName which represents the baseType of the given NodeTemplate
	 */
	public static QName getNodeBaseType(AbstractNodeTemplate nodeTemplate) {
		Utils.LOG.debug("Beginning search for basetype of: " + nodeTemplate.getId());
		List<QName> typeHierarchy = Utils.getNodeTypeHierarchy(nodeTemplate.getType());
		for (QName type : typeHierarchy) {
			Utils.LOG.debug("Checking Type in Hierarchy, type: " + type.toString());
			if (type.toString().equals(Utils.TOSCABASETYPE_SERVER.toString())) {
				return type;
			} else if (type.toString().equals(Utils.TOSCABASETYPE_OS.toString())) {
				return type;
			}
		}
		// FIXME: when there are no basetypes we're screwed
		return typeHierarchy.get(typeHierarchy.size() - 1);
	}
	
	/**
	 * Returns the baseType of the given RelationshipTemplate
	 * 
	 * @param relationshipTemplate an AbstractRelationshipTemplate
	 * @return a QName representing the baseType of the given
	 *         RelationshipTemplate
	 */
	public static QName getRelationshipBaseType(AbstractRelationshipTemplate relationshipTemplate) {
		Utils.LOG.debug("Beginning search for basetype of: " + relationshipTemplate.getId());
		List<QName> typeHierarchy = Utils.getRelationshipTypeHierarchy(relationshipTemplate.getRelationshipType());
		for (QName type : typeHierarchy) {
			Utils.LOG.debug("Checking Type QName: " + type.toString());
			if (type.toString().equals(Utils.TOSCABASETYPE_CONNECTSTO.toString())) {
				return type;
			} else if (type.toString().equals(Utils.TOSCABASETYPE_DEPENDSON.toString())) {
				return type;
			} else if (type.toString().equals(Utils.TOSCABASETYPE_HOSTEDON.toString())) {
				return type;
			}
		}
		// FIXME: when there are no basetypes we're screwed
		return typeHierarchy.get(typeHierarchy.size() - 1);
	}
	
	/**
	 * Calculates all Infrastructure Nodes of all Infrastructure Paths
	 * originating from the given NodeTemplate
	 * 
	 * @param nodeTemplate AbstractNodeTemplate from where the search for
	 *            Infrstructure Nodes begin
	 * @param infrastructureNodes a List of AbstractNodeTemplates which
	 *            represent Infrastructure Nodes of the given NodeTemplate
	 * @Info the infrastructureNodes List must be empty
	 */
	public static void getInfrastructureNodes(AbstractNodeTemplate nodeTemplate, List<AbstractNodeTemplate> infrastructureNodes) {
		Utils.LOG.debug("BaseType of NodeTemplate " + nodeTemplate.getId() + " is " + Utils.getNodeBaseType(nodeTemplate));
		if (Utils.getNodeBaseType(nodeTemplate).toString().equals(Utils.TOSCABASETYPE_OS.toString()) || Utils.getNodeBaseType(nodeTemplate).toString().equals(Utils.TOSCABASETYPE_SERVER.toString())) {
			Utils.LOG.debug("Found infrastructure node: " + nodeTemplate.getId());
			infrastructureNodes.add(nodeTemplate);
		}
		for (AbstractRelationshipTemplate relation : nodeTemplate.getOutgoingRelations()) {
			Utils.LOG.debug("Checking if relation is infrastructure edge, relation: " + relation.getId());
			if (Utils.getRelationshipBaseType(relation).toString().equals(Utils.TOSCABASETYPE_DEPENDSON.toString()) || Utils.getRelationshipBaseType(relation).toString().equals(Utils.TOSCABASETYPE_HOSTEDON.toString())) {
				Utils.LOG.debug("traversing edge to node: " + relation.getTarget().getId());
				Utils.getInfrastructureNodes(relation.getTarget(), infrastructureNodes);
			}
		}
		Utils.cleanDuplciates(infrastructureNodes);
	}
	
	/**
	 * Adds InfrastructureNodes of the given RelaitonshipTemplate to the given
	 * List of NodeTemplates
	 * 
	 * @param relationshipTemplate an AbstractRelationshipTemplate to search its
	 *            InfrastructureNodes
	 * @param infrastructureNodes a List of AbstractNodeTemplate where the
	 *            InfrastructureNodes will be added
	 * @param forSource whether to search for InfrastructureNodes along the
	 *            SourceInterface or TargetInterface
	 */
	public static void getInfrastructureNodes(AbstractRelationshipTemplate relationshipTemplate, List<AbstractNodeTemplate> infrastructureNodes, boolean forSource) {
		if (forSource) {
			Utils.getInfrastructureNodes(relationshipTemplate.getSource(), infrastructureNodes);
		} else {
			Utils.getInfrastructureNodes(relationshipTemplate.getTarget(), infrastructureNodes);
		}
		
	}
	
	/**
	 * Removes duplicates from the given List
	 * 
	 * @param nodeTemplates a List of AbstractNodeTemplate
	 */
	private static void cleanDuplciates(List<AbstractNodeTemplate> nodeTemplates) {
		List<AbstractNodeTemplate> list = new ArrayList<AbstractNodeTemplate>();
		for (AbstractNodeTemplate template : nodeTemplates) {
			boolean match = false;
			for (AbstractNodeTemplate template2 : list) {
				if (template.getId().equals(template2.getId()) & (template == template2)) {
					match = true;
				}
			}
			if (!match) {
				list.add(template);
			}
		}
		nodeTemplates.clear();
		nodeTemplates.addAll(list);
		
	}
	
	/**
	 * Removes duplicates from the given List
	 * 
	 * @param relationshipTemplates a List of AbstractRelationshipTemplate
	 */
	private static void cleanDuplicates(List<AbstractRelationshipTemplate> relationshipTemplates) {
		List<AbstractRelationshipTemplate> list = new ArrayList<AbstractRelationshipTemplate>();
		for (AbstractRelationshipTemplate template : relationshipTemplates) {
			boolean match = false;
			for (AbstractRelationshipTemplate template2 : list) {
				if (template.getId().equals(template2.getId()) & (template == template2)) {
					match = true;
				}
			}
			if (!match) {
				list.add(template);
			}
		}
		relationshipTemplates.clear();
		relationshipTemplates.addAll(list);
	}
	
	/**
	 * Adds the InfrastructureEdges of the given NodeTemplate to the given List
	 * 
	 * @param nodeTemplate an AbstractNodeTemplate
	 * @param infrastructureEdges a List of AbstractRelationshipTemplate to add
	 *            the InfrastructureEdges to
	 */
	public static void getInfrastructureEdges(AbstractNodeTemplate nodeTemplate, List<AbstractRelationshipTemplate> infrastructureEdges) {
		
		// fetch all infrastructureNodes
		List<AbstractNodeTemplate> infraNodes = new ArrayList<AbstractNodeTemplate>();
		Utils.getInfrastructureNodes(nodeTemplate, infraNodes);
		
		// check all outgoing edges on those nodes, if they are infrastructure
		// edges
		for (AbstractNodeTemplate infraNode : infraNodes) {
			for (AbstractRelationshipTemplate outgoingEdge : infraNode.getOutgoingRelations()) {
				if (Utils.getRelationshipBaseType(outgoingEdge).toString().equals(Utils.TOSCABASETYPE_DEPENDSON.toString()) || Utils.getRelationshipBaseType(outgoingEdge).toString().equals(Utils.TOSCABASETYPE_HOSTEDON.toString())) {
					infrastructureEdges.add(outgoingEdge);
				}
			}
		}
		Utils.cleanDuplicates(infrastructureEdges);
	}
	
	/**
	 * Returns all NodeTemplates from the given RelationshipTemplate going along
	 * all occuring Relationships using the Target
	 * 
	 * @param relationshipTemplate an AbstractRelationshipTemplate
	 * @param nodes a List of AbstractNodeTemplate to add the result to
	 */
	public static void getNodesFromRelationToSink(AbstractRelationshipTemplate relationshipTemplate, List<AbstractNodeTemplate> nodes) {
		AbstractNodeTemplate nodeTemplate = relationshipTemplate.getTarget();
		nodes.add(nodeTemplate);
		for (AbstractRelationshipTemplate outgoingTemplate : nodeTemplate.getOutgoingRelations()) {
			Utils.getNodesFromRelationToSink(outgoingTemplate, nodes);
		}
		Utils.cleanDuplciates(nodes);
	}
	
	/**
	 * Returns all NodeTemplates from the given NodeTemplate going along the
	 * path of relation following the target interfaces
	 * 
	 * @param nodeTemplate an AbstractNodeTemplate
	 * @param nodes a List of AbstractNodeTemplate to add the result to
	 */
	public static void getNodesFromNodeToSink(AbstractNodeTemplate nodeTemplate, List<AbstractNodeTemplate> nodes) {
		nodes.add(nodeTemplate);
		for (AbstractRelationshipTemplate outgoingTemplate : nodeTemplate.getOutgoingRelations()) {
			Utils.getNodesFromRelationToSink(outgoingTemplate, nodes);
		}
		Utils.cleanDuplciates(nodes);
	}
	
	/**
	 * Adds the InfrastructureEdges of the given RelationshipTemplate to the
	 * given List
	 * 
	 * @param relationshipTemplate an AbstractRelationshipTemplate
	 * @param infraEdges a List of AbstractRelationshipTemplate to add the
	 *            InfrastructureEdges to
	 * @param forSource whether to search for InfrastructureEdges along the
	 *            SourceInterface or TargetInterface
	 */
	public static void getInfrastructureEdges(AbstractRelationshipTemplate relationshipTemplate, List<AbstractRelationshipTemplate> infraEdges, boolean forSource) {
		if (forSource) {
			Utils.getInfrastructureEdges(relationshipTemplate.getSource(), infraEdges);
		} else {
			Utils.getInfrastructureEdges(relationshipTemplate.getTarget(), infraEdges);
		}
	}
}
