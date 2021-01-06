name := "user-session"

version := "0.1"

scalaVersion := "2.11.12"


libraryDependencies += "com.typesafe" % "config" % "1.3.2"
//spark
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.4.5" % "provided"


libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.14.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.14.0"

libraryDependencies += "junit" % "junit" % "4.13.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

resolvers += MavenCache("mavenCache", file("cache"))
resolvers += Resolver.mavenCentral
