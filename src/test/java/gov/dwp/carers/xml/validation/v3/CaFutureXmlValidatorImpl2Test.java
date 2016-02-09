package gov.dwp.carers.xml.validation.v3;

import gov.dwp.carers.xml.XmlTestBase;
import gov.dwp.carers.xml.validation.XmlValidator;
import gov.dwp.carers.xml.validation.XmlValidatorFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CaFutureXmlValidatorImpl2Test extends XmlTestBase {

    private XmlValidator validator;


    @Before
    public void setUp() {
        validator = XmlValidatorFactory.buildCaFutureValidator();
    }

    @Test
    public void testValidateSuccessFullWithOnlyMandatoryFields() throws IOException {
        final String xml = readXMLFile("future/0.3/DWPCarerClaimGeneratedFromXML1.xml");
        assertTrue(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateSuccessFullWithAllFields() throws IOException {
        final String xml = readXMLFile("future/0.3/DWPCarerClaimGeneratedFromXMLWithOptionals1.xml");
        assertTrue(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithEmptyXml() throws Exception {
        final String xml = readXMLFile("future/0.3/DWPCarerClaimEmptyXMLWithVersion.xml");
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithUnkownXmlVersion() throws IOException {
        final String xml = readXMLFile("future/0.3/DWPCarerClaimGeneratedFromXMLUnknownVersion.xml");
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithInvalidLanguage() throws Exception {
        final String xml = readXMLFile("future/0.3/DWPCarerClaimGeneratedFromXMLWithInvalidLanguage.xml");
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }
}
