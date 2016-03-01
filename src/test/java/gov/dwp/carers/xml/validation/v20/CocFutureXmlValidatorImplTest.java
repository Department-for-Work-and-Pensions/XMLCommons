package gov.dwp.carers.xml.validation.v20;

import gov.dwp.carers.xml.XmlTestBase;
import gov.dwp.carers.xml.validation.XmlValidator;
import gov.dwp.carers.xml.validation.XmlValidatorFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CocFutureXmlValidatorImplTest extends XmlTestBase {

    private XmlValidator validator;
    private String version = "0.20";

    @Before
    public void setUp() throws Exception {
        validator = XmlValidatorFactory.buildCocFutureValidator();
    }


    @Test
    public void testValidateFailWithEmptyXml()  {
        assertFalse(!validator.validate("").hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateSuccessFullWithOnlyMandatoryFields() throws IOException {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerCOCWithMandatoryValues.xml",version));
        assertTrue(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithUnkownXmlVersion() throws IOException {
        final String xml = readXMLFile(String.format("future/%s/DWPCarerCOCWithMandatoryValuesWithUnknownVersion.xml", version));
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }
}
