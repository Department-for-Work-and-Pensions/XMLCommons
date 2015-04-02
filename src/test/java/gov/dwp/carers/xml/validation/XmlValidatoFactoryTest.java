package gov.dwp.carers.xml.validation;

import gov.dwp.carers.xml.validation.CaLegacyXmlValidatorImpl;
import gov.dwp.carers.xml.validation.CocLegacyXmlValidatorImpl;
import gov.dwp.carers.xml.validation.XmlValidator;
import gov.dwp.carers.xml.validation.XmlValidatorFactory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for XmlValidatorFactory.
 *
 * @author Jorge Migueis
 *         Date: 13/09/2013
 */
public class XmlValidatoFactoryTest {

    @Test
    public void TestBuildCaValidator() {
        final XmlValidator validator = XmlValidatorFactory.buildFor("DWPCAClaim");
        assertTrue(validator instanceof CaLegacyXmlValidatorImpl);
    }

    @Test
    public void TestBuildCocValidator() {
        final XmlValidator validator = XmlValidatorFactory.buildFor("DWPCAChangeOfCircumstances");
        assertTrue(validator instanceof CocLegacyXmlValidatorImpl);
    }
}

