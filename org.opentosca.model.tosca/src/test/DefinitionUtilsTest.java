package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.model.tosca.TRelationshipTemplate;
import org.opentosca.model.tosca.utils.DefinitionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class DefinitionUtilsTest {

    TNodeTemplate nodeTemplateAWSMAl;
    List<TNodeTemplate> nodeTemplatesAWSSample;
    List<TRelationshipTemplate> relationshipTemplateListMalFormedTopology, relationshipTemplatesAWSSample;
    String AWSFileMalFormedRelation = "resources/AWS-Location-Sample-MalFormedRelation.xml";
    String AWSFile = "resources/AWS-Location-Sample.xml";
    String jBossPayWebServerId = "JBossPayWebServer".toLowerCase();

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DefinitionUtilsTest.class);
    }

    @Before
    public void setUp() {
        nodeTemplatesAWSSample = DefinitionUtils.getNodeTemplates(new File(AWSFile));
        relationshipTemplateListMalFormedTopology = DefinitionUtils.getRelationshipTemplates(new File(AWSFileMalFormedRelation));
        relationshipTemplatesAWSSample = DefinitionUtils.getRelationshipTemplates(new File(AWSFile));
        nodeTemplateAWSMAl = nodeTemplatesAWSSample.get(0);
    }

    @Test
    public void nodeTemplateIdTest() {
        assertEquals(nodeTemplatesAWSSample.size(), 1);
        assertEquals(nodeTemplatesAWSSample.get(0).getType().getLocalPart().toLowerCase(), "jbosswebserver");
    }

    @Test
    public void locationTest() {
        assertEquals(nodeTemplatesAWSSample.get(0).getLocation().getLocationId(), "AWS");
    }

    @Test
    public void relationshipNullTest() {
        TNodeTemplate source = (TNodeTemplate) relationshipTemplateListMalFormedTopology.get(0).getSourceElement().getRef();
        TNodeTemplate target = (TNodeTemplate) relationshipTemplateListMalFormedTopology.get(0).getTargetElement().getRef();
        assertEquals(source.getId().toLowerCase(), jBossPayWebServerId);
        assertNull(target);
    }

    @Test
    public void relationshipNotNullTest() {
        assertEquals(relationshipTemplatesAWSSample.size(), 1);
        TNodeTemplate source = (TNodeTemplate) relationshipTemplatesAWSSample.get(0).getSourceElement().getRef();
        TNodeTemplate target = (TNodeTemplate) relationshipTemplatesAWSSample.get(0).getTargetElement().getRef();
        assertEquals(source.getId().toLowerCase(), jBossPayWebServerId);
        assertEquals(target.getId().toLowerCase(), jBossPayWebServerId);
    }

    @Test
    public void nodeTemplateProperties() {
        Map<String, String> propertyMap =
                DefinitionUtils.getProperties(nodeTemplatesAWSSample.get(0));
        assertEquals(propertyMap.keySet().size(), 1);
        assertEquals(propertyMap.containsKey("httpdport"), true);
        assertEquals(propertyMap.get("httpdport"), "80");
    }

}
