package gov.dwp.carers.xml.helpers;

/**
  * Created by peterwhitehead on 26/04/2016.
  */
public class XmlSchemaDecryptorFactory {
  public static XmlSchemaDecryptor buildSchemaDecrytor(String version) {
    String versionNo = (version == null) ? "" : version.substring(version.indexOf(".") + 1);
    return createInstance(versionNo);
  }

  private static XmlSchemaDecryptor createInstance(String version) {
    try {
      String packageName = "gov.dwp.carers.xml";
      String clsName = "XmlSchemaDecryptor";
      Class className = Class.forName(packageName + version + clsName);
      return (XmlSchemaDecryptor)className.newInstance();
    } catch (Exception e) {
      return new XmlSchemaDecryptor();
    }
  }
}
