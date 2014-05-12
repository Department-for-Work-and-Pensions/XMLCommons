package com.dwp.carers.s2.xml.signing;

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
        return new DsaSha1XmlSignatureImpl(new KeyStoreLoaderJKS());
    }


}
