package gov.dwp.carers.xml.helpers;

import gov.dwp.carers.xml.validation.XmlErrorHandler;
import gov.dwp.carers.xml.validation.XmlValidator;
import gov.dwp.carers.xml.validation.XmlValidatorFactory;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by peterwhitehead on 27/04/2016.
 */
public class XmlSchemaDecryptorTest {
    @Test
    public void testXmlSchemaDecryptor() throws Exception {
        String xmlString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/future/0.20/DWPCarerClaimEncryption.xml").toURI())));
        XmlSchemaDecryptor xmlSchemaDecryptor = XmlSchemaDecryptorFactory.buildSchemaDecrytor("0.20");
        String xmlString2 = xmlSchemaDecryptor.decryptSchema(xmlString);
        assertThat(xmlString2.contains("<Surname><QuestionLabel>Last name</QuestionLabel><Answer>version1lastname</Answer></Surname>"), is(true));
        assertThat(xmlString2.contains("<NationalInsuranceNumber><QuestionLabel>National Insurance number</QuestionLabel><Answer>AB121212</Answer></NationalInsuranceNumber>"), is(true));
    }

    @Test
    public void testSchemaValidation()  throws Exception {
        String xmlString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/future/0.20/DWPCarerClaimEncryption.xml").toURI())));
        XmlValidator validator = XmlValidatorFactory.buildCaFutureValidator();
        XmlErrorHandler xmlErrorHandler = validator.validate(xmlString);
        assertThat(xmlErrorHandler.hasFoundErrorOrWarning(), is(false));
    }
}
