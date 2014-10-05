package test;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.opentosca.model.tosca.TNodeTemplate;
import org.opentosca.model.tosca.utils.DefinitionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class ParserTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ParserTest.class);
    }

    @Test
    public void awsTest() {
        List<TNodeTemplate> l= DefinitionUtils.getNodeTemplates(new File("resources/AWS-Location-Sample.xml"));
        System.out.println(l.get(0).getType().getLocalPart());
        System.out.println(l.get(0).getName());
        System.out.println(l.get(0).getId());
        assertEquals(l.size(), 1);
        assertEquals(l.get(0).getLocation().getLocationId(),"AWS");
        assertTrue(true);
    }






}
