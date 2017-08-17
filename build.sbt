name := "pusdieno"

version := "0.1"

val scalaV = "2.12.3"

resolvers += Resolver.jcenterRepo
resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


lazy val shared = (project in file("shared")).settings(
  scalaVersion := scalaV
)

lazy val server = (project in file("server")).enablePlugins(PlayScala).dependsOn(shared).settings(
  routesImport += "play.api.mvc.PathBindable.bindableUUID",
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    filters,
    ws,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
  ) ++ slick ++ scalatags ++ scalacss ++ silhouette ++ guice ++ ficus,
  // These 2 lines "should" disable including API documentation in the production build´
  sources in(Compile, doc) := Seq.empty,
  publishArtifact in(Compile, packageDoc) := false
)

lazy val android = (project in file("android")).enablePlugins(AndroidApp).dependsOn(shared).settings(
  scalaVersion := scalaV,
  platformTarget := "android-26",
  minSdkVersion := "21",
  libraryDependencies ++= Seq(
    "com.android.support" % "appcompat-v7" % "24.0.0",
    "com.android.support.test" % "runner" % "0.5" % "androidTest",
    "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest"
  ),
  versionCode := Some(1),
  javacOptions in Compile ++= "-source" :: "1.8" :: "-target" :: "1.8" :: Nil
)

scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-unchecked",
  "-deprecation",
  "-Yno-adapted-args",
  "-Ywarn-dead-code"
)

val slick = Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
)

val scalatags = Seq("com.lihaoyi" %% "scalatags" % "0.6.5")

val scalacss = Seq(
  "com.github.japgolly.scalacss" %% "core" % "0.5.3",
  "com.github.japgolly.scalacss" %% "ext-scalatags" % "0.5.3"
)

val silhouette = Seq(
  "com.mohiva" %% "play-silhouette" % "5.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.0" % Test
)

val guice = Seq("net.codingwell" %% "scala-guice" % "4.1.0")

val ficus = Seq("com.iheart" %% "ficus" % "1.4.1") // typesafe importing values from config files