package com.dwp.carers.s2.xml.signing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

/**
 * It reads system property <i>carers.keystore</i> to get the name and location of the JKS keystore.
 *
 * @author Jorge Migueis
 *         Date: 28/06/2013
 */
class KeyStoreLoaderJKS implements KeyStoreLoader {

    private static final String KEY_STORE_TYPE = "JKS";
    private static final String KEY_STORE_PROVIDER = "SUN";
    private final Logger logger = LoggerFactory.getLogger(KeyStoreLoaderJKS.class);

    /**
     * Load the keystore containing the certificate and the private key.
     * It reads system property <i>carers.keystore</i> to get the name and location of the keystore.
     *
     * @return The keystore
     * @throws java.security.GeneralSecurityException
     * @throws java.io.IOException
     */
    @Override
    public KeyStore loadKeyStore() throws GeneralSecurityException, IOException {
        final String keystoreFile = System.getProperty("carers.keystore");
        if (null == keystoreFile) {
            logger.error("System Property carers.keystore is not defined.");
            throw new RuntimeException("Could not locate keystore. Check configuration.");
        }
        final InputStream is = new FileInputStream(keystoreFile);
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE, KEY_STORE_PROVIDER);
            keyStore.load(is, PasswordManager.getKeyStorePassword().toCharArray());
            return keyStore;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.warn(e.getLocalizedMessage(), e);
            }
        }
    }
}
