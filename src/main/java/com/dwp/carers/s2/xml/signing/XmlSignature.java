package com.dwp.carers.s2.xml.signing;

import java.io.InputStream;

/**
 * Interface of all the classes that can sign an XML string.
 *
 * @author Jorge Migueis
 *         Date: 27/06/2013
 */
public interface XmlSignature {

    /**
     * Sign the provided XML and returns a String containing the original XML with the digital signature.
     * Complies with http://www.w3.org/TR/xmldsig-core.
     * @param xml XML to sign.
     * @param  elementToDigest   URI of the element within the XML that identifies section to digest and
     *                           use to generate signature. In our project we use the DWPCAClaim id; thus,
     *                           the value should be #\<transaction id\>
     * @return XML signed.
     * @throws SigningException  Thrown if an error occurred and could not sign XML.
     */
    String sign(final String xml, final String elementToDigest);

    String sign(final InputStream xml, final String elementToDigest);


    /**
     * Check the digital signatue of an XML.
     * Complies with http://www.w3.org/TR/xmldsig-core.
     * @param xml XML which signature must be verified.
     * @return  true if it is a valid signature, false otherwise.
     */
    Boolean verifySignature(final String xml);
    Boolean verifySignature(final InputStream xml);
}
