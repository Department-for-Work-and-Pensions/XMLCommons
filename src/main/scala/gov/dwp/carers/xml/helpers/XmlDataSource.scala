package gov.dwp.carers.xml.helpers

import play.api.Logger

import scala.xml.NodeSeq

/**
 * Receives XML and decrypts the sensitive data fields.
 * @param source XML to decrypt.
 */
case class XmlDataSource(source: NodeSeq) {
  val claimElement = source \\ "DWPCATransaction" \\ "DWPCAClaim"
  val circsElement = source \\ "DWPCATransaction" \\ "DWPCAChangeOfCircumstances"

  def decryptSource(): NodeSeq = {
    try {
      if (!(claimElement.isEmpty)) {
        // New claim
        var xml = XmlNodeDecryptor.decryptValueForOptionalNode(source(0), List("DWPCATransaction", "DWPCAClaim", "Claimant", "Surname", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Claimant", "NationalInsuranceNumber", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Claimant", "Address", "Answer", "PostCode"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Caree", "Surname", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Caree", "NationalInsuranceNumber", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Caree", "Address", "Answer", "PostCode"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Partner", "Surname", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Partner", "NationalInsuranceNumber", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Partner", "Address", "Answer", "PostCode"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Payment", "Account", "HolderName", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Payment", "Account", "BuildingSocietyDetails", "AccountNumber", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAClaim", "Payment", "Account", "BuildingSocietyDetails", "SortCode", "Answer"))
        xml
      } else {
        //Change Of Circumstances
        var xml = XmlNodeDecryptor.decryptValueForOptionalNode(source(0), List("DWPCATransaction", "DWPCAChangeOfCircumstances", "ClaimantDetails", "FullName", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "ClaimantDetails", "NationalInsuranceNumber", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "CareeDetails", "FullName", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "PaymentChange", "AccountDetails", "HolderName", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "PaymentChange", "AccountDetails", "BuildingSocietyDetails", "AccountNumber", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "PaymentChange", "AccountDetails", "BuildingSocietyDetails", "SortCode", "Answer"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "AddressChange", "PreviousAddress", "Answer", "PostCode"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "AddressChange", "NewAddress", "Answer", "PostCode"))
        xml = XmlNodeDecryptor.decryptValueForOptionalNode(xml, List("DWPCATransaction", "DWPCAChangeOfCircumstances", "AddressChange", "CareeAddress", "Answer", "PostCode"))
        xml
      }
    } catch {
      case e: RuntimeException =>
        Logger.error(s"Sensitive data decryption failed. ${e.getMessage}")
        throw e
    }

  }
}
