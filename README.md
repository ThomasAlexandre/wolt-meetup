Sample Scala.js project

## How to run

```bash
npm install
```

Compile scala.js code
```bash
sbt
fastOptJS
```

Run Vite server
```bash
npm run dev
```

## How it was created

```code
npm create vite@latest
- Project name: … wolt-meetup
- Select a framework: › Vanilla
- Select a variant: › JavaScript

> cd wolt-meetup
> npm install
> npm run dev
```

- Then added build.properties with latest sbt version, plugins.sbt (with scala.js plugin) and minimal build.sbt
- Added a simple scala.js code in src/main/scala/Main.scala and replace main.js to point to the scala.js output
(If needed, copy some files from scala3 project generated from default template in giter8:
sbt new scala/scala3.g8)