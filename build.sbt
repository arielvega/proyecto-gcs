import RpmConstants._

lazy val gcsApp = (project in file("gcs-app"))
  .enablePlugins(PlayScala, RpmPlugin, SystemVPlugin)
  .settings(
    name := """gcs-app""",
    version := "2.8.x",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      "com.typesafe.play" %% "play-slick" % "5.0.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
      "org.postgresql" % "postgresql" % "9.4.1209",
      specs2 % Test,
    ),
    rpmBrpJavaRepackJars := true,
    rpmRelease := sys.env.get("BUILD_NUMBER").getOrElse("1"),
    rpmVendor := "UAGRM",
    rpmGroup := Some("uagrm.edu.bo"),
    rpmUrl := Some("http://uagrm.edu.bo"),
    rpmLicense := Some("Open Source"),
    rpmRequirements := Seq("nginx","java-1.8.0-openjdk"),
    maintainerScripts in Rpm := maintainerScriptsAppend((maintainerScripts in Rpm).value)(
      Post -> "sudo chown -R gcs-app:gcs-app /usr/share/gcs-app/",
      Postun -> "sudo rm -Rf /usr/share/gcs-app && sudo rm -Rf /var/log/gcs-app"
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

lazy val gcsAppIT = (project in file("gcs-it"))
  .enablePlugins(PlayScala)
  .settings(
    name := """gcp-it""",
    version := "2.8.x",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      "com.typesafe.play" %% "play-slick" % "5.0.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
      "org.postgresql" % "postgresql" % "9.4.1209",
      "org.postgresql" % "postgresql" % "9.3-1103-jdbc41",
      specs2 % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  ).dependsOn(gcsApp)


onLoad in Global := (Command.process("project gcsApp", _: State)) compose (onLoad in Global).value
