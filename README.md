
# Wolt Meetup

This repository contains the code used at the Scala Wolt Meetup on 2024-09-12:

- [Meetup page](https://www.meetup.com/scala-stockholm/events/301871841/)

Each branch contains the code for each small demo of the presentation:
- main branch: Initial setup with Vite, a plain javascript app
- scalajs branch: Scala.js minimal hello-world app replacing the plain javascript code with compiled Scala.js code
- scalajs-tyrian: Scala.js counter app with Tyrian library for UI taken from the official docs
- laminar-hello: Laminar library for UI, a minimal example taken from the docs
- laminar-ui-derivation: A partial implementation of deriving case classes to produce a laminar based UI, based on previous work by Kit Langton but using scala3 meta-programming with givens and Mirrors instead of external library (magnolia)
  Partial because it is missing sum types derivation. Also, would be nice to support Options and Lists for example. But it was a starting point to show what's possible.
- laminar-scalablytyped: A small example of using the ScalablyTyped plugin that generates Scala.js bindings for TypeScript libraries, ie facades that can be used from your scala.js code to integrate with the huge typescript ecosystem. This example used chart.js to render a Chart.


## Vanilla Javascript App (created with Vite)

## How to run this branch

```bash
npm install
```

Run Vite server
```bash
npm run dev
```

## How to run other branches (that are scala aware)
```bash
sbt
```

Compile scala code to javascript
```bash
~fastLinkJS   # or fastOptJS (less optimized) or fullOptJS (more optimized)
```

```bash
npm run dev
```



## How it was created

```code
npm create vite@latest
- Project name: … wolt-meetup
- Select a framework: › Vanilla
- Select a variant: › JavaScript
