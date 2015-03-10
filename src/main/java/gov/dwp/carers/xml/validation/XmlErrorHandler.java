package gov.dwp.carers.xml.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs all the errors and warnings found during validation. <i>hasFoundErrorOrWarning</i> return true
 * if at least one error or warning was found.
 * User:  Jorge Migueis
 * Date: 24/06/2013
 */
class XmlErrorHandler implements ErrorHandler {

    /* Have we found any error or warning? */
    private boolean hasFoundErrorOrWarning;

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
        logger.warn("XmlErrorHandler: " + exception.toString(), exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        hasFoundErrorOrWarning = true;
        logger.error("XmlErrorHandler: " + exception.toString(), exception);

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        hasFoundErrorOrWarning = true;
        logger.error("XmlErrorHandler: " + exception.toString(), exception);
    }

    /**
     * Have we found an error or warning?
     * @return true if at least one error or warning was found.
     */
    public boolean hasFoundErrorOrWarning() {
        return hasFoundErrorOrWarning;
    }
}
