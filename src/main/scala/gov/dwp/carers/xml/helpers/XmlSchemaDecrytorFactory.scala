package gov.dwp.carers.xml.helpers

import scala.reflect.runtime.{universe => ru}
import scala.util.{Failure, Success, Try}
import scala.xml.XML

/**
 * Created by peterwhitehead on 03/02/2016.
 */
object XmlSchemaDecryptorFactory {
  val packageName = "gov.dwp.carers.xml"
  val clsName = "XmlSchemaDecryptor"
  def buildSchemaDecrytor(version: String): XmlSchemaDecryptor = {
    val versionNo = Try(s"v${version.drop(version.indexOf(".") + 1)}") match {
      case Success(ver) => ver
      case Failure(e) => ""
    }
    createInstance(versionNo)
  }

  private def createInstance(version: String): XmlSchemaDecryptor = {
    val mirror = ru.runtimeMirror(getClass.getClassLoader)
    Try(mirror.classSymbol(Class.forName(s"$packageName.$version.$clsName"))) match {
      case Failure(e) => new XmlSchemaDecryptor()
      case Success(cls) => {
        val module = cls.companionSymbol.asModule
        mirror.reflectModule(module).instance.asInstanceOf[XmlSchemaDecryptor]
      }
    }
  }
}

class XmlSchemaDecryptor {
  def decryptSchema(xmlString: String) = {
    val xmlDataSource = new XmlDataSource(XML.loadString(xmlString))
    xmlDataSource.decryptSource().mkString
  }
}


