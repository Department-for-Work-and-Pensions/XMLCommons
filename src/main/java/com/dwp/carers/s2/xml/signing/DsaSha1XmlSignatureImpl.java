package com.dwp.carers.s2.xml.signing;

import com.dwp.carers.security.keystore.KeyStoreLoader;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To use to sign Carers XML messages to Government Gateway. Uses SHA1 and DSA-SHA1 algorithms.
 * It reads system property <i>carers.keystore</i> to get the name and location of the keystore
 * containing private key to use to encrypt signature.
 *
 * @author Jorge Migueis
 *         Date: 27/06/2013
 */
public class DsaSha1XmlSignatureImpl extends CoreXmlSignatureImpl {

    private final Logger logger = LoggerFactory.getLogger(DsaSha1XmlSignatureImpl.class);

    /**
     * Default constructor has package visibility so user uses {@link XmlSignatureFactory}.
     */
    DsaSha1XmlSignatureImpl(final KeyStoreLoader keyStoreLoader) {
        super(keyStoreLoader, "ezgov",XMLSignature.ALGO_ID_SIGNATURE_DSA, Constants.ALGO_ID_DIGEST_SHA1);
    }

}
