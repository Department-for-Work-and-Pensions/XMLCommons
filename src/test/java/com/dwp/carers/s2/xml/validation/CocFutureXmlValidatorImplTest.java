package com.dwp.carers.s2.xml.validation;

import com.dwp.carers.s2.xml.XmlTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class CocFutureXmlValidatorImplTest extends XmlTestBase {

    private XmlValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = XmlValidatorFactory.buildCocFutureValidator();
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
}
