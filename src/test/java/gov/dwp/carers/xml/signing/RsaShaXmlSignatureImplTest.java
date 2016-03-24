package gov.dwp.carers.xml.signing;

import gov.dwp.carers.xml.XmlTestBase;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;
import java.security.Security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jmi on 06/02/15.
 */
public class RsaShaXmlSignatureImplTest  extends XmlTestBase {
    private final Logger logger = LoggerFactory.getLogger(RsaShaXmlSignatureImpl.class);
    private XmlSignature sign;

    @Before
    public void setUp() {
        sign = XmlSignatureFactory.buildRsaSha1Generator();
    }

    @Test
    public void testSuccessfulSign() throws Exception {
        for (Provider p : Security.getProviders()) System.out.println(p.getName());
        final String XmlSigned = sign.sign(fileInputStream("legacy/DWPCarerClaimGeneratedFromXML1.xml"), "CGNJM9K");
        assertTrue(XmlSigned.contains("Signature"));
        assertTrue(XmlSigned.contains("Reference URI=\"#CGNJM9K\""));
        assertTrue(XmlSigned.contains("SignatureValue"));
        assertTrue(XmlSigned.contains("DigestValue"));
    }

    @Test
    public void testSuccessfulSign2() throws Exception {
        final String xml = readXMLFile("legacy/Claim.xml");
        final String XmlSigned = sign.sign(xml , "TY6TV9G");
//        System.out.println(XmlSigned);
        assertFalse(XmlSigned.equals(xml));
        assertTrue(XmlSigned.contains("Signature"));
        assertTrue(XmlSigned.contains("Reference URI=\"#TY6TV9G\""));
        assertTrue(XmlSigned.contains("SignatureValue"));
        assertTrue(XmlSigned.contains("DigestValue"));
    }

    @Test
    public void testSuccessfulSignFuture() throws Exception {
        final String XmlSigned = sign.sign(fileInputStream("future/0.20/DWPCarerClaimGeneratedFromXML1.xml"),"14080000001");
        assertTrue(XmlSigned.contains("Signature"));
        assertTrue(XmlSigned.contains("Reference URI=\"#14080000001\""));
        assertTrue(XmlSigned.contains("SignatureValue"));
        assertTrue(XmlSigned.contains("DigestValue"));
    }

    @Test
    public void testSuccessfulVerification() throws Exception {
        final String XmlSigned = sign.sign(fileInputStream("future/0.20/DWPCarerClaimGeneratedFromXML1.xml"),"14080000001");
        System.out.println(XmlSigned);
        assertTrue(sign.verifySignature(XmlSigned));
        assertTrue(XmlSignatureValidator.validate(XmlSigned));
    }

    @Test(expected = SigningException.class)
    public void testFailSignWithInvalidURI() throws Exception {
        final String xml = readXMLFile("legacy/DWPCarerClaimGeneratedFromXML1.xml");
        sign.sign(xml, "Invalid element id");
    }

    @Test(expected = SigningException.class)
    public void testFailSignWithInvalidXML() throws Exception {
        sign.sign("Invalid XML", "#CGNJM9K");
    }
}
