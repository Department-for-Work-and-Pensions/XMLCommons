package gov.dwp.carers.xml.validation;

/**
 * This class is to be used to validate a Change of circumstances XML file.
 * It uses the schema <i>sdfiles/schema/ca/dwp-coc-claim-v1_10.xsd</i>.
 * It then resolves the nested schemas using {@link gov.dwp.carers.xml.validation.S2LocalResourceResolver} setting it up to look
 * for the schemas under the root <i>xsdfiles/</i>.<br></br>
 * User: Jorge Migueis
 * Date: 27/06/2013
 */
public class CocFutureXmlValidatorImpl extends S2XmlValidator {

    /**
     * Default constructor has package visibility so user uses {@link gov.dwp.carers.xml.validation.XmlValidatorFactory}.
     */
    CocFutureXmlValidatorImpl() {
        setMainSchemaFile("schema/ca/CarersAllowance_Schema.xsd");
        setSchemasPath("/future/");
    }

    @Override
    public String getGlobalXmlns() {
        return "http://www.govtalk.gov.uk/dwp/carers-allowance";
    }

    @Override
    public String getMessageClass() {
        return "DWP-CA-COC-0T0";
    }

}
