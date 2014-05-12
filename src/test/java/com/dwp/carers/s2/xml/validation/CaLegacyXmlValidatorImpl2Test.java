package com.dwp.carers.s2.xml.validation;

import com.dwp.carers.s2.xml.XmlTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests of {@link com.dwp.carers.s2.xml.validation.CaLegacyXmlValidatorImpl}.
 * @author Jorge Migueis
 *         Date: 27/06/2013
 */
public class CaLegacyXmlValidatorImpl2Test extends XmlTestBase {

    private XmlValidator validator;


    @Before
    public void setUp() {
        validator = XmlValidatorFactory.buildCaLegacyValidator();
    }

    @Test
    public void testValidateSuccessFullWithOnlyMandatoryFields() throws IOException {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXML1.xml");
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateSuccessFullWithAllFields() throws IOException {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXMLWithOptionals1.xml");
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithWrongProductionXML() throws Exception {
        final String xml = readXMLFile("legacy/XmlFromProductionWithErrors_Test_1.xml");
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateSuccessfulWithProductionXML() throws IOException {
        final String xml = readXMLFile("legacy/XmlFromProduction_Test_1.xml");
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithEmptyXml()  {
        assertFalse(validator.validate(""));
    }

    @Test
    public void testValidateFailWithUnreadableSchema() throws IOException {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("legacy/doesNotExistFile.xsd");
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithSchemaReferencingUnknownSchema() throws IOException {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("legacy/SchemaReferencingUnknownSchema.xsd");
        assertFalse(validator.validate(xml));
    }
}
