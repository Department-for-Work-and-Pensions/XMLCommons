package com.dwp.carers.s2.xml.validation;

import com.dwp.carers.s2.xml.XmlTestBase;
import com.dwp.carers.s2.xml.validation.XmlValidator;
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory;
import org.junit.Before;
import org.junit.Test;

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
}
