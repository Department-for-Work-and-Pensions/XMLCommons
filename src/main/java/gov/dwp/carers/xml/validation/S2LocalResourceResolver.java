package gov.dwp.carers.xml.validation;

import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


/**
 * The resource resolver redirect the XML parser to local resources to get the schemas.
 * Specify the root of the search for schemas in the constructor.<br></br>
 * User: Jorge Migueis
 * Date: 24/06/2013
 */
class S2LocalResourceResolver implements LSResourceResolver {
    /**
     * slf4j logger used to send the log messages to the log file/database
     */
    private final Logger logger = LoggerFactory.getLogger(S2LocalResourceResolver.class);

    private final String  schemasPath;

    /**
     * Builds a new resolver looking at the root <i>schemasPath</i>.
     * @param schemasPath Root of path to look for schemas.
     */
    public S2LocalResourceResolver(final String schemasPath) {
        this.schemasPath = schemasPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LSInput resolveResource(String type,
                                   String namespaceURI,
                                   String publicId,
                                   String systemId,
                                   String baseURI) {
        logger.debug(">> Received type: " + type + " namespaceURI: " + namespaceURI + " publicId: " + publicId + " systemId: " + systemId + " baseURI: " + baseURI);
        final LSInput s2LSInput = new S2LSInput();
        s2LSInput.setBaseURI(baseURI);
        s2LSInput.setPublicId(publicId);
        s2LSInput.setSystemId(schemasPath + systemId);
        logger.debug("New systemId: " + s2LSInput.getSystemId() + " <<");
        InputStream is = getClass().getResourceAsStream(s2LSInput.getSystemId());
        if (null == is) {
            final String message = "Did not find resource: " + s2LSInput.getSystemId();
            logger.error(message);
            throw new LSException(LSException.PARSE_ERR,message);
        }
        s2LSInput.setByteStream(is);
        return s2LSInput;
    }

}
