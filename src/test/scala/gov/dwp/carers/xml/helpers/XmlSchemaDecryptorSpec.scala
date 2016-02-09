package gov.dwp.carers.xml.helpers

import gov.dwp.carers.xml.validation.XmlValidatorFactory
import org.specs2.mutable._
import scala.io.Source

/**
 * Created by peterwhitehead on 03/02/2016.
 */
class XmlSchemaDecryptorSpec extends Specification {
  "When decrypting xml" should {
    "create a new instance of decryptor and validate" in {
      val xmlString = Source.fromURL(getClass getResource "/future/0.20/DWPCarerClaimEncryption.xml").mkString
      val xmlSchemaDecryptor = XmlSchemaDecryptorFactory.buildSchemaDecrytor("0.20")
      val xmlString2 = xmlSchemaDecryptor.decryptSchema(xmlString)
      xmlString2 must contain("<Surname><QuestionLabel>Last name</QuestionLabel><Answer>version1lastname</Answer></Surname>")
      xmlString2 must contain("<NationalInsuranceNumber><QuestionLabel>National Insurance number</QuestionLabel><Answer>AB121212</Answer></NationalInsuranceNumber>")
      val validator = XmlValidatorFactory.buildCaFutureValidator
      val xmlErrorHandler = validator.validate(xmlString)
      !xmlErrorHandler.hasFoundErrorOrWarning
    }
  }
}
