package gov.dwp.carers.xml.validation;

import gov.dwp.carers.xml.XmlTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

/**
 * Created with IntelliJ IDEA.
 * User: Jorge Migueis
 * Date: 27/06/2013
 * To change this template use File | Settings | File Templates.
 */
public class CocLegacyXmlValidatorImplTest extends XmlTestBase {

    private XmlValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = XmlValidatorFactory.buildCocLegacyValidator();
    }

    @Test
    public void testValidateFailWithEmptyXml()  {
        assertFalse(!validator.validate("").hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithUnreadableSchema() throws IOException {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("legacy/doesNotExistFile.xsd");
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithSchemaReferencingUnknownSchema() throws IOException {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("legacy/SchemaReferencingUnknownSchema.xsd");
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }
}
