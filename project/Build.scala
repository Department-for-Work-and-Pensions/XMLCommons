import sbt._
import sbt.Keys._

// ensure the keystore is defined e.g.
// export _JAVA_OPTIONS="-Dcarers.keystore=/Users/valtechuk/Dev/carerskeystore"
// or start sbt with "-Dcarers.keystore=/Users/valtechuk/Dev/carerskeystore"

object ApplicationBuild extends Build {
  val appName = "xmlCommons"

  val appVersion = "1.3"

  val appDependencies = Seq(
    libraryDependencies += "org.specs2" %% "specs2" % "2.3.6" % "test",
    libraryDependencies += "org.apache.santuario" % "xmlsec" % "1.4.8",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.5",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13",
    libraryDependencies += "log4j" % "log4j" % "1.2.12",
    libraryDependencies += "junit" % "junit" % "4.11" % "test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test->default",
    libraryDependencies += "org.jasypt" % "jasypt" % "1.9.1" classifier "lite"
    // Add your project dependencies here,
  )

  var sV: Seq[Def.Setting[_]] = Seq(scalaVersion := "2.10.3")

  var sR: Seq[Def.Setting[_]] = Seq(resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/")

  var sO: Seq[Def.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  var sAppN: Seq[Def.Setting[_]] = Seq(name := appName)
  var sAppV: Seq[Def.Setting[_]] = Seq(version := appVersion)
  var sOrg: Seq[Def.Setting[_]] = Seq(organization := "com.dwp.carers")

  var publ: Seq[Def.Setting[_]] = Seq(
    publishTo := Some("Artifactory Realm" at "http://build.3cbeta.co.uk:8080/artifactory/repo/"),
    publishTo <<= version {
      (v: String) =>
        Some("releases" at "http://build.3cbeta.co.uk:8080/artifactory/libs-release-local")
    })

  var creds: Seq[Def.Setting[_]] = Seq(credentials += Credentials("Artifactory Realm", "build.3cbeta.co.uk", "admin", "{DESede}GwYNYWCGg88uVuPjHixZ4g=="))

  var appSettings: Seq[Def.Setting[_]] = Project.defaultSettings ++ sV ++ sO ++ sR ++ sAppN ++ sAppV ++ sOrg ++ appDependencies ++ publ ++ creds

  val main = Project(id = appName, base = file("."), settings = appSettings)
}

