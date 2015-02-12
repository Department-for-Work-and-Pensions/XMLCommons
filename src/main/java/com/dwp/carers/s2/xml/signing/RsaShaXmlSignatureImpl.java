package com.dwp.carers.s2.xml.signing;

import com.dwp.carers.security.keystore.KeyStoreLoader;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;

/**
 * To use to sign Carers XML messages to Government Gateway. Uses RSA and RSA-SHA256 algorithms.
 * It reads system property <i>carers.keystore</i> to get the name and location of the keystore
 * containing private key to use to encrypt signature.
 */
public class RsaShaXmlSignatureImpl extends CoreXmlSignatureImpl {

    RsaShaXmlSignatureImpl(final KeyStoreLoader keyStoreLoader) {
        super(keyStoreLoader, "cadsrsascer", XMLSignature.ALGO_ID_SIGNATURE_RSA, Constants.ALGO_ID_DIGEST_SHA1);
    }
}
