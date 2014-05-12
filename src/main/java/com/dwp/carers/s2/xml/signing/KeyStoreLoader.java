package com.dwp.carers.s2.xml.signing;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

/**
 * Interface of the classes loading a keystore containing certificates and keys.
 *
 * @author Jorge Migueis
 *         Date: 28/06/2013
 */
interface KeyStoreLoader {

    /**
     * Load the keystore containing the certificate and the private key.
     * It uses system property <i>carers.keystore</i> to get the name and location of the keystore.
     * @return The keystore
     * @throws java.security.GeneralSecurityException
     * @throws java.io.IOException
     */
    KeyStore loadKeyStore() throws GeneralSecurityException, IOException;

}
