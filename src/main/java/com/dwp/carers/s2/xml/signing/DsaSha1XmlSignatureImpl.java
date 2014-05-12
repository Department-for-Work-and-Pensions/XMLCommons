package com.dwp.carers.s2.xml.signing;

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
 * To use to sign Carers XML messages to Government Gateway. Uses SHA1 and DSA-SHA1 algorithms.
 * It reads system property <i>carers.keystore</i> to get the name and location of the keystore
 * containing private key to use to encrypt signature.
 *
 * @author Jorge Migueis
 *         Date: 27/06/2013
 */
public class DsaSha1XmlSignatureImpl implements XmlSignature {

    private static final String PRIVATE_KEY_ALIAS = "ezgov";
    private final KeyStoreLoader keyStoreLoader;

    private final Logger logger = LoggerFactory.getLogger(DsaSha1XmlSignatureImpl.class);

    /**
     * Default constructor has package visibility so user uses {@link XmlSignatureFactory}.
     */
    DsaSha1XmlSignatureImpl(final KeyStoreLoader keyStoreLoader) {
        this.keyStoreLoader = keyStoreLoader;
    }

    /**
     * Sign the provided XML and returns a String containing the orginal XML with the digital signature.
     * Complies with http://www.w3.org/TR/xmldsig-core.<br>
     * Uses SHA1 to digest and DSA-SHA1 to sign.
     *
     * @param xml               XML to sign.
     * @param idElementToDigest Id of the element within the XML that identifies section to digest and
     *                          use to generate signature.
     * @return XML signed.
     */
    @Override
    public String sign(String xml, String idElementToDigest) {
        try {
            return sign(new ByteArrayInputStream(xml.getBytes("UTF-8")), idElementToDigest);
        } catch (UnsupportedEncodingException e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not sign XML.", e);
        }
    }


    public String sign(InputStream xml, String idElementToDigest) {
        Document doc;
        try {
            Init.init();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true); // Bingo !!!
            doc = dbf.newDocumentBuilder().parse(xml);
            final KeyStore keyStore = this.keyStoreLoader.loadKeyStore();
            // Canonicalization done by XMLSignature automatically
            final XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_DSA);
            // We do not apply any transformation -> null
            sig.addDocument("#" + idElementToDigest, null, Constants.ALGO_ID_DIGEST_SHA1);
            Key privateKey = keyStore.getKey(PRIVATE_KEY_ALIAS,PasswordManager.getKeyStoreEntryPassword().toCharArray());
            doc.getElementsByTagName("DWPBody").item(0).appendChild(sig.getElement());
            sig.sign(privateKey);
            return docToString(doc);
        } catch (final Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not sign XML.", e);
        }
    }

    @Override
    public Boolean verifySignature(String xml) {
        try {
            return verifySignature(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not verify signature of XML.", e);
        }    }

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
            final XMLSignature sig = new XMLSignature(sigElement, XMLSignature.ALGO_ID_SIGNATURE_DSA);
            final Key publicKey = keyStore.getCertificate(PRIVATE_KEY_ALIAS).getPublicKey();
            return sig.checkSignatureValue(publicKey);
        } catch (final Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new SigningException("Could not verify signature of XML.", e);
        }
    }

    private String docToString(Document doc) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtils.outputDOM(doc,baos);
        return new String(baos.toByteArray());
    }

}
