package gov.dwp.carers.xml.schemavalidations;

import org.junit.Test;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemaValidationTest {

    @Test
    public void testGetTypeNames() {
        SchemaValidation.clearSchema();
        SchemaValidation validate = new SchemaValidation("TestExample1.xsd");
        Document doc = validate.getDoc();
        Set<String> typeNames = validate.getTypeNames(doc);
        assertTrue(typeNames.contains("QuestionTextThreeThousandType"));
        assertTrue(typeNames.contains("ThreeThousandTextType"));
        assertEquals(5, typeNames.size());
    }

    @Test
    public void testSimpleRestrictions() {
        SchemaValidation validate = new SchemaValidation("TestExample1.xsd");
        Document doc = validate.getDoc();
        Map<String, Restriction> restrictions = validate.getSimpleRestrictions(doc);
        assertEquals(1, restrictions.size());
        assertTrue(restrictions.containsKey("ThreeThousandTextType"));
        assertEquals(15, restrictions.get("ThreeThousandTextType").getMinlength().intValue());
        assertEquals(3000, restrictions.get("ThreeThousandTextType").getMaxlength().intValue());
    }


    @Test
    public void testPayment() {
        // <xs:element minOccurs="0" name="Payment"> ... <xs:element minOccurs="0" name="PaymentFrequency" type="QuestionTextType"/>
        // <xs:element minOccurs="0" name="Payment"> ... <xs:element minOccurs="0" name="Account" type="AccountDetailsType"/>
        // <xs:complexType name="AccountDetailsType"> ... <xs:element minOccurs="0" name="HolderName" type="QuestionTextType"/>
        // <xs:complexType name="QuestionTextType"> ... <xs:element name="Answer" type="HundredTwentyTextType"/>
        // <xs:simpleType name="HundredTwentyTextType"> ... <xs:restriction base="RestrictedStringType"> ... <xs:maxLength value="120"/>
        // Payment//Account//AccountDetails//HolderName -> HundredTwentyTextType
        SchemaValidation validate = new SchemaValidation("Payment.xsd");
        Restriction restriction = validate.getRestriction("Payment//Account//HolderName//Answer");
        assertTrue(restriction != null);
        assertEquals("HundredTwentyTextType", restriction.getName());
        assertEquals(1, restriction.getMinlength().intValue());
        assertEquals(120, restriction.getMaxlength().intValue());
    }

    @Test
    public void testAdditionalInfo() {
        // <xs:element minOccurs="0" name="OtherInformation"> ... <xs:element minOccurs="0" maxOccurs="1" name="AdditionalInformation" type="QuestionYesNoWhyThreeThousandType"/>
        // <xs:complexType name="QuestionYesNoWhyThreeThousandType"> ... <xs:element minOccurs="0" name="Why" type="QuestionTextThreeThousandType"/>
        // <xs:complexType name="QuestionTextThreeThousandType"> ... <xs:element name="Answer" type="ThreeThousandTextType"/>
        // <xs:simpleType name="ThreeThousandTextType"> ... <xs:restriction base="RestrictedStringType"> -> <xs:maxLength value="3000"/>
        // OtherInformation//AdditionalInformation//Why//Answer -> QuestionTextType
        // Full path
        SchemaValidation.clearSchema();
        SchemaValidation validate = new SchemaValidation("AdditionalInformation.xsd");

        Restriction restriction1=validate.getRestriction("OtherInformation//WelshCommunication//Answer");
        assertTrue(restriction1!=null);
        assertEquals("HundredTwentyTextType", restriction1.getName());
        assertEquals(1, restriction1.getMinlength().intValue());
        assertEquals(120, restriction1.getMaxlength().intValue());


        Restriction restriction2 = validate.getRestriction("OtherInformation//AdditionalInformation//Why//Answer");
        assertTrue(restriction2 != null);
        assertEquals("ThreeThousandTextType", restriction2.getName());
        assertEquals(15, restriction2.getMinlength().intValue());
        assertEquals(3000, restriction2.getMaxlength().intValue());

        Restriction restriction3=validate.getRestriction("OtherInformation//AdditionalInformation//Answer");
        assertTrue(restriction3 != null);
        assertEquals("HundredTwentyTextType", restriction3.getName());
        assertEquals(1, restriction3.getMinlength().intValue());
        assertEquals(120, restriction3.getMaxlength().intValue());
    }


    @Test
    public void testGetAllRestrictionsFromFullXsd() {
        SchemaValidation.clearSchema();
        SchemaValidation validate = new SchemaValidation("0.20");
        Map<String, Restriction> restrictions = validate.expandRestrictions(validate.getDoc());

        // At version 0.20 we have 1605 restrictions if we include all path variations including ..
        // DWPCATransaction//DWPCAChangeOfCircumstances//BreakFromCaring...
        // DWPBody//DWPCATransaction//DWPCAClaim
        // Lets just check we got more than 100.
        assertTrue(restrictions.keySet().size() > 100);
    }


    @Test
    public void testGetSingleRestrictionFromFullXsd() {
        SchemaValidation.clearSchema();
        SchemaValidation validate = new SchemaValidation("0.20");
        Restriction restriction = validate.getRestriction("OtherInformation//AdditionalInformation//Why//Answer");
        assertTrue(restriction != null);
        assertEquals(1, restriction.getMinlength().intValue());
        assertEquals(3000, restriction.getMaxlength().intValue());
    }


    @Test
    public void testDateOfClaimWithPattern(){
        SchemaValidation.clearSchema();
        SchemaValidation validate = new SchemaValidation("0.20");
        Restriction restriction = validate.getRestriction("DateOfClaim//Answer");
        assertTrue(restriction != null);
        assertTrue(restriction.getPattern().length() > 0);
    }


    @Test
    public void testGetDefaultSchemaFile() {
        SchemaValidation validate = new SchemaValidation("0.20");
        String schemaFile = validate.schemaDefaultFilename("0.20");
        assertEquals("future/0.20/schema/ca/CarersAllowance_Schema.xsd", schemaFile);
        assertTrue(validate.getDocFromXmlFile(schemaFile) != null);
    }


    @Test
    public void testDeduplicatePath() {
        Set<String> typeNames = new HashSet<>();
        typeNames.add("QuestionTextThreeThousandType");
        typeNames.add("QuestionYesNoWhyThreeThousandType");
        typeNames.add("ThreeThousandTextType");
        String path = "AccountDetailsType//AccountDetailsType//HolderName";
        String newPath = SchemaValidation.deduplicatePath(path, typeNames);
        assertEquals("AccountDetailsType//HolderName", newPath);

        String path2 = "OtherInformation//OtherInformation//AdditionalInformation//QuestionYesNoWhyThreeThousandType//QuestionYesNoWhyThreeThousandType//Why//QuestionTextThreeThousandType//QuestionTextThreeThousandType//Answer//ThreeThousandTextType";
        String newPath2 = SchemaValidation.deduplicatePath(path2, typeNames);
        System.out.println("NEWPATH:" + newPath2);
        assertEquals("OtherInformation//AdditionalInformation//Why//Answer", newPath2);
    }

}