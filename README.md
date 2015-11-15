# leJOS EV3 Gradle plugin

Gradle plugin for leJOS EV3 projects to handle deployments to EV3 brick

[![Circle CI](https://circleci.com/gh/mindstorms-cop/lejos-ev3-gradle-plugin/tree/master.svg?style=shield)](https://circleci.com/gh/mindstorms-cop/lejos-ev3-gradle-plugin/tree/master)

## Publish the packages to the Maven local repository 

```shell
./gradlew install
```

## Including the plugin in a Gradle project

We use [Jitpack](https://jitpack.io/#mindstorms-cop/lejos-ev3-gradle-plugin) for publishing the plugin.

Use the following in your Gradle configuration to include the plugin:

```
buildscript {
    repositories {
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.github.mindstorms-cop:lejos-ev3-gradle-plugin:0.1.0'
    }
}

plugins {
    id 'application' // application plugin is needed for leJOS EV3 plugin
    id 'com.github.johnrengelman.shadow' version '1.2.2' // shadow plugin is needed for leJOS EV3 plugin
}

apply plugin: 'org.mindstormscop.lejosev3'

ev3 {
    host = '10.0.3.31'
    port = 22
    user = 'root'
    discoverBrick = true
}
```
