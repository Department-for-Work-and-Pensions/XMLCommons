package gov.dwp.carers.xml.signing;

import org.apache.xml.security.signature.XMLSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jmi on 11/02/15.
 */
public class XmlSignatureValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlSignatureValidator.class);

    public static boolean validate(String xml) {
        if (xml.contains(XMLSignature.ALGO_ID_SIGNATURE_RSA)) {
            return XmlSignatureFactory.buildRsaSha1Generator().verifySignature(xml);
        } else if (xml.contains(XMLSignature.ALGO_ID_SIGNATURE_DSA)){
            return XmlSignatureFactory.buildDsaSha1Generator().verifySignature(xml);
        } else {
            LOGGER.warn("Unknwon signature alogirthm npt RSA and not DSA");
            return false;
        }
    }
}
