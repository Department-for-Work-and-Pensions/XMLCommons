package gov.dwp.carers.xml.validation;

/**
 * This class is to be used to validate a Claim Allowance XML file.
 * it uses the schema <i>sdfiles/schema/ca/dwp-ca-claim-v1_10.xsd</i>.
 * It then resolves the nested schemas using {@link S2LocalResourceResolver} setting it up to look
 * for the schemas under the root <i>xsdfiles/</i>.<br></br>
 * User: Jorge Migueis
 * Date: 27/06/2013
 */
public class CaLegacyXmlValidatorImpl extends S2XmlValidator {

    /**
     * Default constructor has package visibility so user uses {@link XmlValidatorFactory}.
     */
    CaLegacyXmlValidatorImpl() {
        setMainSchemaFile("schema/ca/dwp-ca-claim-v1_10.xsd");
        setSchemasPath("/legacy/");
    }

    @Override
    public String getGlobalXmlns() {
        return "http://www.govtalk.gov.uk/dwp/ca/claim";
    }

    @Override
    public String getMessageClass() {
        return "DWP-CA-CLAIM-0T0";
    }

}
