package com.dwp.carers.s2.xml.validation;

/**
 * Factory to use to get a XMLValidator.
 * <br>
 * User: Jorge Migueis
 * Date: 27/06/2013
 */
public class XmlValidatorFactory {

    /**
     * Ensure nobody can create an instance.
     */
    private XmlValidatorFactory() {}

    /**
     * Build relevant validator according to name of the main node provided.
     * @param nodeName name of the main node of the XML to validate
     * @return an XmlValidator
     * @throws RuntimeException if no Xml validator found for the name provided.
     */
    public static synchronized XmlValidator buildFor(final String nodeName) {
        switch (nodeName) {
            case "DWPCAClaim" : return  buildCaLegacyValidator();
            case "DWPCAChangeOfCircumstances"  : return buildCocLegacyValidator();
            default : throw new RuntimeException("Unknown type of XML message: " + nodeName);
        }
    }

    /**
     * Builds an XMLValidator to validate a Carer Allowance Claim XML with the legacy schema
     * @return  a Carer Allowance Claim XML Validator
     */
    public static synchronized XmlValidator buildCaLegacyValidator() {
        return new CaLegacyXmlValidatorImpl();
    }

    /**
     * Builds an XMLValidator to validate a Change of Circumstances XML with the legacy schema
     * @return  a Change of Circumstances XML Validator
     */
    public static synchronized XmlValidator buildCocLegacyValidator() {
        return new CocLegacyXmlValidatorImpl();
    }

    /**
     * Builds an XMLValidator to validate a Carer Allowance Claim XML with the future schema
     * @return  a Carer Allowance Claim XML Validator
     */
    public static synchronized XmlValidator buildCaFutureValidator() {
        return new CaFutureXmlValidatorImpl();
    }

    /**
     * Builds an XMLValidator to validate a Change of Circumstances XML with the future schema
     * @return  a Change of Circumstances XML Validator
     */
    public static synchronized XmlValidator buildCocFutureValidator() {
        return new CocFutureXmlValidatorImpl();
    }
}
