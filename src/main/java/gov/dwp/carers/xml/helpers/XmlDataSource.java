package gov.dwp.carers.xml.helpers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathExpressionException;
import java.util.Arrays;

/**
 * Created by peterwhitehead on 26/04/2016.
 */
public class XmlDataSource {
    private final Logger logger = LoggerFactory.getLogger(XmlDataSource.class);

    private Document claimDocument;

    public XmlDataSource(Document document) {
        this.claimDocument = document;
    }

    private boolean isClaim() throws XPathExpressionException {
        NodeList list = claimDocument.getElementsByTagName("DWPCAClaim");
        return list.getLength() != 0;
    }
    
    public Document decryptSource() throws XPathExpressionException {
        try {
            XmlNodeDecryptor xmlNodeDecryptor = new XmlNodeDecryptor();
            if (isClaim()) {
                // New claim
                Document xml = xmlNodeDecryptor.decryptValueForOptionalNode(claimDocument, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Claimant", "Surname", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Claimant", "NationalInsuranceNumber", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Claimant", "Address", "Answer", "PostCode"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Caree", "Surname", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Caree", "NationalInsuranceNumber", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Caree", "Address", "Answer", "PostCode"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Partner", "Surname", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Partner", "NationalInsuranceNumber", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Partner", "Address", "Answer", "PostCode"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Payment", "Account", "HolderName", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Payment", "Account", "BuildingSocietyDetails", "AccountNumber", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAClaim", "Payment", "Account", "BuildingSocietyDetails", "SortCode", "Answer"));
                return xml;
            } else {
                //Change Of Circumstances
                Document xml = xmlNodeDecryptor.decryptValueForNode(claimDocument, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "ClaimantDetails", "FullName", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "ClaimantDetails", "NationalInsuranceNumber", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "CareeDetails", "FullName", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "PaymentChange", "AccountDetails", "HolderName", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "PaymentChange", "AccountDetails", "BuildingSocietyDetails", "AccountNumber", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "PaymentChange", "AccountDetails", "BuildingSocietyDetails", "SortCode", "Answer"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "AddressChange", "PreviousAddress", "Answer", "PostCode"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "AddressChange", "NewAddress", "Answer", "PostCode"));
                xml = xmlNodeDecryptor.decryptValueForOptionalNode(xml, "DWPCATransaction", Arrays.asList("DWPCAChangeOfCircumstances", "AddressChange", "CareeAddress", "Answer", "PostCode"));
                return xml;
            }
        } catch (RuntimeException e){
            logger.error("Sensitive data decryption failed." + e.getMessage(), e);
            throw e;
        }
    }
}