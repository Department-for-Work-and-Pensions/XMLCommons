package gov.dwp.carers.xml.validation;

/**
 * This class is to be used to validate a Change of circumstances XML file.
 * It uses the schema <i>sdfiles/schema/ca/dwp-coc-claim-v1_10.xsd</i>.
 * It then resolves the nested schemas using {@link S2LocalResourceResolver} setting it up to look
 * for the schemas under the root <i>xsdfiles/</i>.<br></br>
 * User: Jorge Migueis
 * Date: 27/06/2013
 */
public class CocLegacyXmlValidatorImpl extends S2XmlValidator {

    /**
     * Default constructor has package visibility so user uses {@link XmlValidatorFactory}.
     */
    CocLegacyXmlValidatorImpl() {
        setMainSchemaFile("schema/ca/dwp-ca-coc-v1_10.xsd");
        setSchemasPath("/legacy/");
    }

    @Override
    public String getGlobalXmlns() {
        return "http://www.govtalk.gov.uk/dwp/ca/coc";
    }

    @Override
    public String getMessageClass() {
        return "DWP-CA-COC-0T0";
    }

}
