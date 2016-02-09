package gov.dwp.carers.xml.validation.v2;

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
        final String xml = readXMLFile("future/0.2/DWPCarerCOCWithMandatoryValues.xml");
        assertTrue(!validator.validate(xml).hasFoundErrorOrWarning());
    }

    @Test
    public void testValidateFailWithUnkownXmlVersion() throws IOException {
        final String xml = readXMLFile("future/0.2/DWPCarerCOCWithMandatoryValuesWithUnknownVersion.xml");
        assertFalse(!validator.validate(xml).hasFoundErrorOrWarning());
    }
}
