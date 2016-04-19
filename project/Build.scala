import sbt._
import sbt.Keys._

// ensure the keystore is defined e.g.
// export _JAVA_OPTIONS="-Dcarers.keystore=/Users/valtechuk/Dev/carerskeystore"
// or start sbt with "-Dcarers.keystore=/Users/valtechuk/Dev/carerskeystore"

object ApplicationBuild extends Build {
  val appName = "xmlCommons"

  val appVersion = "7.19-SNAPSHOT"

  val appDependencies = Seq(
    libraryDependencies += "org.specs2" %% "specs2-core" % "3.3.1" % "test" withSources() withJavadoc(),
    libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "3.3.1" % "test" withSources() withJavadoc(),
    libraryDependencies += "org.specs2" %% "specs2-junit" % "3.3.1" % "test" withSources() withJavadoc(),
    libraryDependencies += "org.apache.santuario" % "xmlsec" % "1.4.8",
    libraryDependencies += "junit" % "junit" % "4.12" % "test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test->default",
    libraryDependencies += "gov.dwp.carers" %% "carerscommon" % "7.12-SNAPSHOT"
    // Add your project dependencies here,
  )

  var sV: Seq[Def.Setting[_]] = Seq(scalaVersion := "2.10.5")

  var sR: Seq[Def.Setting[_]] = Seq(resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/")

  var sO: Seq[Def.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  var sAppN: Seq[Def.Setting[_]] = Seq(name := appName,publishArtifact in (Compile, packageDoc) := false)
  var sAppV: Seq[Def.Setting[_]] = Seq(version := appVersion)
  var sOrg: Seq[Def.Setting[_]] = Seq(organization := "gov.dwp.carers")

  val isSnapshotBuild = appVersion.endsWith("-SNAPSHOT")
  var publ: Seq[Def.Setting[_]] = Seq(
    publishTo := Some("Artifactory Realm" at "http://build.3cbeta.co.uk:8080/artifactory/repo/"),
    publishTo <<= version {
      (v: String) =>
        if (isSnapshotBuild)
          Some("snapshots" at "http://build.3cbeta.co.uk:8080/artifactory/libs-snapshot-local")
        else
          Some("releases" at "http://build.3cbeta.co.uk:8080/artifactory/libs-release-local")
    })


  var appSettings: Seq[Def.Setting[_]] = sV ++ sO ++ sR ++ sAppN ++ sAppV ++ sOrg ++ appDependencies ++ publ

  val main = Project(id = appName, base = file(".")).settings(appSettings: _*)
}

