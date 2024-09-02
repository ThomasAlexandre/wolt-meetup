import org.scalajs.linker.interface.ModuleSplitStyle

lazy val `wolt-meetup` = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    scalaVersion := "3.5.0",
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature"),

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "testvite" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("woltmeetup")))
    },

    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.0.0", // "2.8.0",
      "com.raquo" %%% "laminar" % "0.14.2" // "0.14.2" // "16.0.0" or "15.0.1"
    ),
  )
