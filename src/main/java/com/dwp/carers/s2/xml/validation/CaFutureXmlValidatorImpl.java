package com.dwp.carers.s2.xml.validation;

/**
 * This class is to be used to validate a Claim Allowance XML file.
 * it uses the schema <i>sdfiles/schema/ca/dwp-ca-claim-v1_10.xsd</i>.
 * It then resolves the nested schemas using {@link com.dwp.carers.s2.xml.validation.S2LocalResourceResolver} setting it up to look
 * for the schemas under the root <i>xsdfiles/</i>.<br></br>
 * User: Jorge Migueis
 * Date: 27/06/2013
 */
public class CaFutureXmlValidatorImpl extends S2XmlValidator {

    /**
     * Default constructor has package visibility so user uses {@link com.dwp.carers.s2.xml.validation.XmlValidatorFactory}.
     */
    CaFutureXmlValidatorImpl() {
        setMainSchemaFile("schema/ca/CarersAllowance_Schema.xsd");
        setSchemasPath("/future/");
    }

    @Override
    public String getGlobalXmlns() {
        return "http://www.govtalk.gov.uk/dwp/carers-allowance";
    }

    @Override
    public String getMessageClass() {
        return "DWP-CA-CLAIM-0T0";
    }

}
