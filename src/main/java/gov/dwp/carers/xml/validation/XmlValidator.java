package gov.dwp.carers.xml.validation;

/**
 * Interface for all the XML validators.
 * <br></br>
 * User: Jorge Migueis
 * Date: 27/06/2013
 */
public interface XmlValidator {

    /**
     * Perfoms the validation of provided XML against the schema specified by the concrete subclass.
     * @param xml XML to parse and validate.
     * @return true if XML valid, false if XML valid or if any error happened when reading schemas or XML.
     * All the errors are logged into log file.
     */
    boolean validate(String xml);

    String getGlobalXmlns();

    String getSchemaLocation();

    String getMessageClass();


}
