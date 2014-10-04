package test;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.opentosca.model.tosca.*;
import org.opentosca.model.tosca.utils.DefinitionUtils;

import java.io.File;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ParserTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ParserTest.class);
    }

    @Test
    public void awsTest() {
        List<TNodeTemplate> l= DefinitionUtils.getNodeTempaltes(new File("resources/AWS-Location-Sample.xml"));
        assertEquals(l.size(), 1);
        assertEquals(l.get(0).getLocation().getLocationId(),"AWS");
        assertTrue(true);
    }

}
