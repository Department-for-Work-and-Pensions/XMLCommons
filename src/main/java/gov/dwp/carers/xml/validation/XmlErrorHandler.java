package gov.dwp.carers.xml.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs all the errors and warnings found during validation. <i>hasFoundErrorOrWarning</i> return true
 * if at least one error or warning was found.
 * User:  Jorge Migueis
 * Date: 24/06/2013
 */
public class XmlErrorHandler implements ErrorHandler {

    /* Have we found any error or warning? */
    private boolean hasFoundErrorOrWarning;

    private List<String> errors = new ArrayList<>();

    /**
     * slf4j logger used to send the log messages to the log file/database
     */
    private final Logger logger = LoggerFactory.getLogger(XmlErrorHandler.class);

    /**
     * Default constructor. Set internal state to no error or warning found.
     */
    public XmlErrorHandler () {
        hasFoundErrorOrWarning = false;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        hasFoundErrorOrWarning = true;
        errors.add(exception.toString());
        logger.warn("warning: " + exception.toString(), exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        hasFoundErrorOrWarning = true;
        errors.add(exception.toString());
        logger.warn("error: " + exception.toString(), exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        hasFoundErrorOrWarning = true;
        errors.add(exception.toString());
        logger.error("fatalError: " + exception.toString(), exception);
    }

    public void addGenericException(Exception exception) {
        hasFoundErrorOrWarning = true;
        errors.add(exception.toString());
        logger.error("addGenericException: " + exception.toString(), exception);
    }

    /**
     * Have we found an error or warning?
     * @return true if at least one error or warning was found.
     */
    public boolean hasFoundErrorOrWarning() {
        return hasFoundErrorOrWarning;
    }

    public List<String> getWarningAndErrors() { return errors; }
}
