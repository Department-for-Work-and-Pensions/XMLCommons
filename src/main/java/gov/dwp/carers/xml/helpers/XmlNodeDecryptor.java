package gov.dwp.carers.xml.helpers;

import gov.dwp.carers.security.encryption.EncryptorAES;
import gov.dwp.carers.security.encryption.NotEncryptedException;
import gov.dwp.exceptions.DwpRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.bind.DatatypeConverter;
import java.util.List;

/**
 * Created by peterwhitehead on 26/04/2016.
 */
public class XmlNodeDecryptor {
    private final Logger logger = LoggerFactory.getLogger(XmlNodeDecryptor.class);

    /**
     * To be used when one wants to decrypt an optional node of an XML.
     * @param xml XML containing node to decrypt.
     * @param paths Path to the node to decrypt.
     * @return If the node does not exist it returns the original XML, otherwise returns a new XML with the node decrypted.
     */
    public Document decryptValueForOptionalNode(Document xml, String startPath, List<String> paths) {
        try {
            return decryptValueForNode(xml, startPath, paths, true);
        } catch (IndexOutOfBoundsException iobe) {
            return xml;
        }
    }

    /**
     * To be used when one wants to decrypt a mandatory node of an XML.
     * If the node does not exist it throws a runtime exception.
     * @param xml XML containing node to decrypt.
     * @param paths Path to the node to decrypt.
     * @return The new XML with the node decrypted.
     */
    public Document decryptValueForNode(Document xml, String startPath, List<String> paths) {
        return decryptValueForNode(xml, startPath, paths, false);
    }

    private Document decryptValueForNode(Document xml, String startPath, List<String> paths, boolean optional) {
        Node list = xml.getElementsByTagName(startPath).item(0);
        if (list == null) return xml;
        NodeList nodeList = list.getChildNodes();
        String lastPath = paths.get(paths.size() - 1);
        for (String path : paths) {
            nodeList = changeNode(path, lastPath, nodeList, optional);
        }
        return xml;
    }

    private NodeList changeNode(String path, String lastPath, NodeList nodeList, Boolean optional) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(path)) {
                if (lastPath.equals(path)) {
                    String text = node.getFirstChild().getTextContent();
                    if ((optional && text != null) || !optional)node.getFirstChild().setTextContent(decryptString(text));
                    break;
                } else nodeList = node.getChildNodes();
            }
        }
        return nodeList;
    }

    /**
     * Decrypt a text.
     * @param text text to decrypt.
     * @return A new string containing the text decrypted. If the text was not encrypted, it throws a DwpRuntimeException.
     */
    public String decryptString(String text) {
        try {
            return (new EncryptorAES()).decrypt(DatatypeConverter.parseBase64Binary(text));
        } catch (NotEncryptedException nee) {
            // Means field was not encrypted.
            logger.warn("An XML node that should be encrypted was not encrypted.");
            return text;
        } catch (DwpRuntimeException dre) {
            logger.error("Could not decrypt node.", dre);
            throw dre;
        }
    }
}
