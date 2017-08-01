name := "movie-ticket"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= List(
  "de.heikoseeberger" %% "akka-http-circe" % "1.17.0",
  "org.scalikejdbc" %% "scalikejdbc-config"  % "3.0.2"
)

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-testkit_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http-testkit_2.11" % "10.0.9" % "test"

// https://mvnrepository.com/artifact/io.circe/circe-core_2.11
libraryDependencies += "io.circe" % "circe-core_2.11" % "0.8.0"

// https://mvnrepository.com/artifact/io.circe/circe-generic_2.11
libraryDependencies += "io.circe" % "circe-generic_2.11" % "0.8.0"

// https://mvnrepository.com/artifact/io.circe/circe-parser_2.11
libraryDependencies += "io.circe" % "circe-parser_2.11" % "0.8.0"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http_2.11" % "10.0.9"

// https://mvnrepository.com/artifact/de.heikoseeberger/akka-http-circe_2.11
libraryDependencies += "de.heikoseeberger" % "akka-http-circe_2.11" % "1.17.0"

// https://mvnrepository.com/artifact/org.scalatest/scalatest_2.11
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.2.0-SNAP7" % "test"

// https://mvnrepository.com/artifact/com.h2database/h2
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// https://mvnrepository.com/artifact/org.scalikejdbc/scalikejdbc_2.11
libraryDependencies += "org.scalikejdbc" % "scalikejdbc_2.11" % "3.0.2"



