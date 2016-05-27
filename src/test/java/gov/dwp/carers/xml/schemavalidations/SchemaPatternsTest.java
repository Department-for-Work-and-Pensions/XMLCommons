package gov.dwp.carers.xml.schemavalidations;

import akka.serialization.Serialization;
import gov.dwp.carers.xml.validation.XmlErrorHandler;
import gov.dwp.carers.xml.validation.XmlValidator;
import gov.dwp.carers.xml.validation.XmlValidatorFactory;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemaPatternsTest {

    @Test
    public void testValidateAmountXsdPattern() {
//        String xmlString = this.getClass().getResource("/future/0.20/DWPCarerClaimEncryption.xml").toString();
        try{
            //URL xmlFile = this.getClass().getResource("/future/SchemaReferencingUnknownSchema.xsd");

            InputStream in = Serialization.Settings.class.getClassLoader().getResourceAsStream("DWPAmount.xml");
            if( in == null){
                System.out.println("ERROR in stream is null");
            }
            else{
                System.out.println("INput length:"+in.available());
            }
            DocumentBuilderFactory dbfactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=dbfactory.newDocumentBuilder();
            Document document=builder.parse(in);


            InputStream in2 = Serialization.Settings.class.getClassLoader().getResourceAsStream("CG.xsd");
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaFile = new StreamSource(in2);
            Schema schema = factory.newSchema(schemaFile);
            Validator validator=schema.newValidator();

            try{
                validator.validate(new DOMSource(document));
            }
            catch( Exception e){
                System.out.println("Failed to validate document got exception:"+e.toString());
            }

            /*
            XmlValidator validator=XmlValidatorFactory.buildCaFutureValidator();
            XmlErrorHandler error=validator.validate(xml);
            if( error.hasFoundErrorOrWarning()){
                for( String e : error.getWarningAndErrors() ){
                    System.out.println("Validate got error:"+e);
                }
            }
            else{
                System.out.println("Validated OK");
            }
            */
        }catch( Exception e){
            System.out.println("EXCEPTION "+e.toString());
        }

/*
        val xmlString = Source.fromURL(getClass getResource "/future/0.20/DWPCarerClaimEncryption.xml").mkString
        val xmlSchemaDecryptor = XmlSchemaDecryptorFactory.buildSchemaDecrytor("0.20")
        val xmlString2 = xmlSchemaDecryptor.decryptSchema(xmlString)
        xmlString2 must contain("<Surname><QuestionLabel>Last name</QuestionLabel><Answer>version1lastname</Answer></Surname>")
        xmlString2 must contain("<NationalInsuranceNumber><QuestionLabel>National Insurance number</QuestionLabel><Answer>AB121212</Answer></NationalInsuranceNumber>")
        val validator = XmlValidatorFactory.buildCaFutureValidator
        val xmlErrorHandler = validator.validate(xmlString)
        !xmlErrorHandler.hasFoundErrorOrWarning
        */
    }

}