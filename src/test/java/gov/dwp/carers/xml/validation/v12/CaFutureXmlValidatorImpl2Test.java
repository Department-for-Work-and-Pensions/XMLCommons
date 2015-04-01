package gov.dwp.carers.xml.validation.v12;

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
    private String version = "0.12";


    @Before
    public void setUp() {
        validator = XmlValidatorFactory.buildCaFutureValidator();
    }

    @Test
    public void testValidateSuccessFullWithOnlyMandatoryFields() throws IOException {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerClaimGeneratedFromXML1.xml", version));
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateSuccessFullWithAllFields() throws IOException {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerClaimGeneratedFromXMLWithOptionals1.xml", version));
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithEmptyXml() throws Exception {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerClaimEmptyXMLWithVersion.xml", version));
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithUnkownXmlVersion() throws IOException {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerClaimGeneratedFromXMLUnknownVersion.xml", version));
        assertFalse(validator.validate(xml));
    }


    @Test
    public void testValidateFailWithInvalidCharactersInFields() throws IOException {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerClaimWithInvalidCharacters.xml", version));
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithInvalidLanguage() throws Exception {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerClaimGeneratedFromXMLWithInvalidLanguage.xml", version));
        assertFalse(validator.validate(xml));
    }
}
