package com.dwp.carers.s2.xml.signing;

import com.dwp.carers.s2.xml.XmlTestBase;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Unit test of {@link com.dwp.carers.s2.xml.signing.DsaSha1XmlSignatureImpl}.
 *
 * @author Jorge Migueis
 *         Date: 27/06/2013
 */
public class DsaSha1XmlSignatureImplTest  extends XmlTestBase {

    private final Logger logger = LoggerFactory.getLogger(DsaSha1XmlSignatureImpl.class);
    private XmlSignature sign;

    @Before
    public void setUp() {
        sign = XmlSignatureFactory.buildDsaSha1Generator();
    }

    @Test
    public void testSuccessfulSign() throws Exception {
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
        final String XmlSigned = sign.sign(fileInputStream("future/0.4/DWPCarerClaimGeneratedFromXML1.xml"),"NFM33DB");
        assertTrue(XmlSigned.contains("Signature"));
        assertTrue(XmlSigned.contains("Reference URI=\"#NFM33DB\""));
        assertTrue(XmlSigned.contains("SignatureValue"));
        assertTrue(XmlSigned.contains("DigestValue"));
    }


    @Test
    public void testSuccessfulVerification() throws Exception {
        final String XmlSigned = sign.sign(fileInputStream("future/0.4/DWPCarerClaimGeneratedFromXML1.xml"),"NFM33DB");
        System.out.println(XmlSigned);
        assertTrue(sign.verifySignature(XmlSigned));
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
