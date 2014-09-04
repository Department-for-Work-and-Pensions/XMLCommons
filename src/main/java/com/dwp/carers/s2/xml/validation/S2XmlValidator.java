package com.dwp.carers.s2.xml.validation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSException;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;


/**
 * This class is to be used to validate a Claim Allowance XML file.
 * It uses the schema <i>sdfiles/schema/ca/dwp-ca-claim-v1_10.xsd</i>.
 * It then resolves the nested schemas using {@link S2LocalResourceResolver} setting it up to look
 * for the schemas under the root <i>xsdfiles/</i>.<br></br>
 * User: Jorge Migueis
 * Date: 26/06/2013
 */
abstract class S2XmlValidator implements XmlValidator {

    /**
     * slf4j logger used to send the log messages to the log file/database
     */
    private final Logger logger = LoggerFactory.getLogger(S2XmlValidator.class);
    private String mainSchemaFile;
    private String schemasPath;


    public String getSchemaLocation() {
        return getGlobalXmlns() + " file:" + getSchemasPath() + getMainSchemaFile();
    }
    /**
     * Allow to define the main schema to be used when parsing an XML file.
     * If an instance of this class is created and no schema is set then it will look for
     * <i>xsdfiles/schema/ca/dwp-ca-claim-v1_10.xsd</i>
     * @param file file Name with relative path to classpath
     */
    void setMainSchemaFile(final String file) {
        this.mainSchemaFile = file;
    }

    void setSchemasPath(final String path) {
        this.schemasPath = path;
    }

    protected String getMainSchemaFile() {return this.mainSchemaFile; }

    protected String getSchemasPath() { return  this.schemasPath; }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(final String xml) {
        logger.info("Start validation");
        boolean isValid;
        final String schemaLang = "http://www.w3.org/2001/XMLSchema";
        final SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
        // Get version number of XML so we know which version of schema to use
        // <Version>0.1</Version>
        final String version = StringUtils.substringBetween(xml,"<Version>","</Version>");
        final String realSchemaPath = schemasPath + ((null == version || version.isEmpty()) ? "" : version + "/" );
        //Need to tell the factory to look at a different location to get the schemas.
        //By default the schema are on http://www.govtalk.gov.uk, we want to use the locally stored versions
        factory.setResourceResolver(new S2LocalResourceResolver(realSchemaPath));
        try {
            final InputStream is = S2XmlValidator.class.getResourceAsStream(realSchemaPath + mainSchemaFile);
            if (null == is) {
                throw new IOException("Did not find resource: " + realSchemaPath + mainSchemaFile);
            }
            final Schema schema = factory.newSchema(new StreamSource(is));
            final Validator validator = schema.newValidator();
            //Output more information than the default error handler
            final XmlErrorHandler  errorHandler= new XmlErrorHandler();
            validator.setErrorHandler(errorHandler);
            //Validation of the XML
            validator.validate(new StreamSource(new StringReader(xml)));
            isValid = !errorHandler.hasFoundErrorOrWarning();
        }catch (final LSException ls) {
            logger.error(ls.getMessage(), ls);
            isValid = false;
        } catch (final SAXException sax) {
            logger.error(sax.getMessage(), sax);
            isValid = false;
        } catch (final IOException io) {
            logger.error(io.getMessage(), io);
            isValid = false;
        }
        return isValid;
    }


}
