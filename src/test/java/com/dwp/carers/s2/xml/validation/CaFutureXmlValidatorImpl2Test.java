package com.dwp.carers.s2.xml.validation;

import com.dwp.carers.s2.xml.XmlTestBase;
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
        final String xml = readXMLFile("future/DWPCarerClaimGeneratedFromXML1.xml");
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateSuccessFullWithAllFields() throws IOException {
        final String xml = readXMLFile("future/DWPCarerClaimGeneratedFromXMLWithOptionals1.xml");
        assertTrue(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithEmptyXml()  {
        assertFalse(validator.validate(""));
    }

    @Test
    public void testValidateFailWithUnreadableSchema() throws IOException {
        final String xml = readXMLFile("future/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("future/doesNotExistFile.xsd");
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithSchemaReferencingUnknownSchema() throws IOException {
        final String xml = readXMLFile("future/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("future/SchemaReferencingUnknownSchema.xsd");
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithUnkownXmlVersion() throws IOException {
        final String xml = readXMLFile("future/DWPCarerClaimGeneratedFromXMLUnknownVersion.xml");
        assertFalse(validator.validate(xml));
    }
}
