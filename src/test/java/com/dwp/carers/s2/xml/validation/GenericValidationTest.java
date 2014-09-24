package com.dwp.carers.s2.xml.validation;

import com.dwp.carers.s2.xml.XmlTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

/**
 * Created with IntelliJ IDEA.
 * User: valtechuk
 * Date: 24/09/2014
 * Time: 10:28
 *
 */
public class GenericValidationTest extends XmlTestBase {


    private XmlValidator validator;
    private XmlValidator cocValidator;


    @Before
    public void setUp() {
        validator = XmlValidatorFactory.buildCaFutureValidator();
        cocValidator = XmlValidatorFactory.buildCocFutureValidator();
    }

    @Test
    public void testValidateFailWithUnreadableSchema() throws IOException {
        final String xml = readXMLFile("future/0.1/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("future/doesNotExistFile.xsd");
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testValidateFailWithSchemaReferencingUnknownSchema() throws IOException {
        final String xml = readXMLFile("future/0.1/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)validator).setMainSchemaFile("future/SchemaReferencingUnknownSchema.xsd");
        assertFalse(validator.validate(xml));
    }

    @Test
    public void testCOCValidateFailWithUnreadableSchema() throws IOException {
        final String xml = readXMLFile("future/0.1/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)cocValidator).setMainSchemaFile("future/doesNotExistFile.xsd");
        assertFalse(cocValidator.validate(xml));
    }

    @Test
    public void testCOCValidateFailWithSchemaReferencingUnknownSchema() throws IOException {
        final String xml = readXMLFile("future/0.1/DWPCarerClaimGeneratedFromXML1.xml");
        ((S2XmlValidator)cocValidator).setMainSchemaFile("future/SchemaReferencingUnknownSchema.xsd");
        assertFalse(cocValidator.validate(xml));
    }
}
