package org.opentosca.model.tosca.utils;

import org.opentosca.model.tosca.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jose on 04/10/14.
 */
public class DefinitionUtils {

    private static Unmarshaller unmarshaller=null;


    private static void buildUnmarshaller(){
        try {
            if (unmarshaller==null) {
                JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
                unmarshaller = context.createUnmarshaller();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TDefinitions getDefinitions(File f){
        buildUnmarshaller();
        TDefinitions tDefinition=null;
        try{
            tDefinition = (TDefinitions) unmarshaller.unmarshal(f);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return tDefinition;
    }

    public static TServiceTemplate getServiceTemplate(TDefinitions tDefinitions){
        TServiceTemplate serviceTemplate=null;
        for (TExtensibleElements element : tDefinitions.getServiceTemplateOrNodeTypeOrNodeTypeImplementation()) {
            if(element instanceof TServiceTemplate)
                return (TServiceTemplate) element;
        }
        return serviceTemplate;
    }

    public static TServiceTemplate getServiceTemplate(File file){
        return getServiceTemplate(getDefinitions(file));
    }

    public static TTopologyTemplate getTopology(TServiceTemplate serviceTemplate){
        return serviceTemplate.getTopologyTemplate();
    }

    public static TTopologyTemplate getTopologyTemplate(File f){
        return getTopology(getServiceTemplate(getDefinitions(f)));
    }

    public static List<TNodeTemplate> getNodeTemplates(TTopologyTemplate topology){
        return topology.getNodeTempaltes();
    }

    public static List<TRelationshipTemplate> getRelationshipTemplates(TTopologyTemplate topology){
        return topology.getRelationshipTempaltes();
    }

    public static List<TNodeTemplate> getNodeTemplates(File f){
        return getNodeTemplates(getTopology(getServiceTemplate(getDefinitions(f))));
    }

    public static List<TRelationshipTemplate> getRelationshipTemplates(File f){
        return getRelationshipTemplates(getTopology(getServiceTemplate(getDefinitions(f))));
    }

    public static String getTypeName(TNodeTemplate nodeTemplate){
        return nodeTemplate.getType().getLocalPart();
    }

    public static Map<String, String> getProperties(TEntityTemplate template) {
        Map<String, String > propertyMap=null;
        TEntityTemplate.Properties tproperties = template.getProperties();
        if (tproperties != null) {
            Element el = (Element) tproperties.getAny();
            if (el != null) {
                NodeList childNodes = el.getChildNodes();
                propertyMap=getChildAsMap(childNodes);
            }
        }
        return propertyMap;
    }

    private static Map<String, String> getChildAsMap(NodeList childNodes){
        Map<String, String > propertyMap=new HashMap<String, String>();
        if(childNodes!=null){
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if (item instanceof Element) {
                    propertyMap.put(item.getLocalName(), item.getTextContent());
                }
            }
        }
        return propertyMap;
    }
}
