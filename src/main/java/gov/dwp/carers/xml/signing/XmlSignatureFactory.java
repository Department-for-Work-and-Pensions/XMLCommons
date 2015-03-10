package gov.dwp.carers.xml.signing;

import gov.dwp.carers.security.keystore.KeyStoreLoaderJCEKS;

/**
 * Factory to use to get a XMLSignature.
 * <br>
 * @author Jorge Migueis
 *         Date: 28/06/2013
 */
public class XmlSignatureFactory {

    /**
     * Ensure nobody can create an instance.
     */
    private XmlSignatureFactory() {}

    /**
     * Builds a signature generator using SHA1 to digest XML and DSA-SHA1 to encrypt signature
     * @return DSA-SHA1 signature generator.
     */
    public static synchronized XmlSignature buildDsaSha1Generator() {
        return new DsaSha1XmlSignatureImpl(new KeyStoreLoaderJCEKS());
    }

    /**
     * Builds a signature generator using SHA1 to digest XML and RSA-SHA1 to encrypt signature
     * @return DSA-SHA1 signature generator.
     */
    public static synchronized XmlSignature buildRsaSha1Generator() {
        return new RsaShaXmlSignatureImpl(new KeyStoreLoaderJCEKS());
    }

}
