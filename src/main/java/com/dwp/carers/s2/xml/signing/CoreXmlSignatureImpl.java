package com.dwp.carers.s2.xml.signing;

import com.dwp.carers.security.PasswordManager;
import com.dwp.carers.security.keystore.KeyStoreLoader;
import com.sun.org.apache.xpath.internal.XPathAPI;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyStore;

/**
 * Base implementation of the XmlSignature interface for carer's XML and Keystore.
 */
abstract class CoreXmlSignatureImpl implements XmlSignature {

    private final String privateKeyAlias;
    private final KeyStoreLoader keyStoreLoader;

    private final Logger logger = LoggerFactory.getLogger(CoreXmlSignatureImpl.class);
    private final String algoIdSignature;
    private final String algoIdDigest;

    protected CoreXmlSignatureImpl(final KeyStoreLoader keyStoreLoader, final String storeKey, final String algoSignature, final String algoDigest) {
        this.keyStoreLoader = keyStoreLoader;
        algoIdSignature = algoSignature;
        algoIdDigest = algoDigest;
        privateKeyAlias = storeKey;
    }

    /**
     * Sign the provided XML and returns a String containing the original XML with the digital signature.
     * Complies with http://www.w3.org/TR/xmldsig-core.
     *
     * @param xml             XML to sign.
     * @param elementToDigest URI of the element within the XML that identifies section to digest and
     *                        use to generate signature. In our project we use the DWPCAClaim id; thus,
     *                        the value should be #\<transaction id\>
     * @return XML signed.
     * @throws com.dwp.carers.s2.xml.signing.SigningException Thrown if an error occurred and could not sign XML.
     */
    @Override
    public String sign(final String xml, final String elementToDigest) {
        try {
            return sign(new ByteArrayInputStream(xml.getBytes("UTF-8")), elementToDigest);
        } catch (UnsupportedEncodingException e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not sign XML.", e);
        }
    }

    @Override
    public String sign(final InputStream xml, final String elementToDigest) {
        Document doc;
        try {
            Init.init();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true); // Bingo !!!
            doc = dbf.newDocumentBuilder().parse(xml);
            final KeyStore keyStore = this.keyStoreLoader.loadKeyStore();
            // Canonicalization done by XMLSignature automatically
            final XMLSignature sig = new XMLSignature(doc, null, algoIdSignature);
            // We do not apply any transformation -> null
            sig.addDocument("#" + elementToDigest, null, algoIdDigest);
            Key privateKey = keyStore.getKey(privateKeyAlias, PasswordManager.getKeyStoreEntryPassword().toCharArray());
            doc.getElementsByTagName("DWPBody").item(0).appendChild(sig.getElement());
            sig.sign(privateKey);
            return docToString(doc);
        } catch (final Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not sign XML.", e);
        }
    }

    /**
     * Check the digital signatue of an XML.
     * Complies with http://www.w3.org/TR/xmldsig-core.
     *
     * @param xml XML which signature must be verified.
     * @return true if it is a valid signature, false otherwise.
     */
    @Override
    public Boolean verifySignature(String xml) {
        try {
            return verifySignature(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not verify signature of XML.", e);
        }
    }

    @Override
    public Boolean verifySignature(InputStream xml) {
        Document doc;
        try {
            Init.init();
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            doc = dbf.newDocumentBuilder().parse(xml);
            final KeyStore keyStore = this.keyStoreLoader.loadKeyStore();
            final Element context = XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
            final Element sigElement = (Element) XPathAPI.selectSingleNode(doc, "//ds:Signature", context);
            final XMLSignature sig = new XMLSignature(sigElement, algoIdSignature);
            final Key publicKey = keyStore.getCertificate(privateKeyAlias).getPublicKey();
            return sig.checkSignatureValue(publicKey);
        } catch (final Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not verify signature of XML.", e);
        }
    }

    private String docToString(Document doc) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtils.outputDOM(doc, baos);
        return new String(baos.toByteArray());
    }
}
