package org.opentosca.model.tosca.utils;

import org.opentosca.model.tosca.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jose on 04/10/14.
 */
public class DefinitionUtils {

    private static Unmarshaller unmarshaller=null;


    private static void buildUnmarshaller(){
    try {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        unmarshaller = context.createUnmarshaller();
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

    public static TTopologyTemplate getTopology(TServiceTemplate serviceTemplate){
        return serviceTemplate.getTopologyTemplate();
    }

    public static List<TNodeTemplate> getNodeTempaltes(TTopologyTemplate topology){
        List<TNodeTemplate> nodeTemplates=new LinkedList<TNodeTemplate>();
        for(TEntityTemplate nodeTemplate: topology.getNodeTemplateOrRelationshipTemplate()){
            if(nodeTemplate instanceof TNodeTemplate)
                nodeTemplates.add(((TNodeTemplate)nodeTemplate));
        }
        return nodeTemplates;
    }

    public static List<TNodeTemplate> getNodeTempaltes(File f){
        return getNodeTempaltes(getTopology(getServiceTemplate(getDefinitions(f))));
    }
}
