<h1>Kommandant &nbsp;<sup><sup><sub><i>[origin: German] (noun): commander</i></sub></sup></sup></h1>

[![CircleCI](https://circleci.com/gh/kvnxiao/kommandant/tree/dev.svg?style=shield)](https://circleci.com/gh/kvnxiao/kommandant/tree/master)
[![codecov](https://codecov.io/gh/kvnxiao/kommandant/branch/dev/graph/badge.svg)](https://codecov.io/gh/kvnxiao/kommandant)
[![Release](https://jitpack.io/v/kvnxiao/kommandant.svg)](https://jitpack.io/#kvnxiao/kommandant)
[![Apache License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Fast, modular, and flexible. Kommandant is a multipurpose command framework for the JVM, ready for easy integration into any project.

[GitHub Wiki - How this works and how to use](https://github.com/kvnxiao/kommandant/wiki)

## Overview

Kommandant is a simple command framework designed to be fit for any project, ranging from command-line applications to games or even back-end services.

For detailed instructions on how to use Kommandant, please visit the [github wiki](https://github.com/kvnxiao/kommandant/wiki).

You can grab Kommandant using your favourite dependency management tools. Replace `@VERSION@` with the latest release tag or use a commit hash.

### Gradle

`build.gradle`

```gradle
allprojects {
  repositories {
    // ...
    maven { url 'https://jitpack.io' }
  }
}
```

`kommandant-core`
```gradle
dependencies {
    compile 'com.github.kvnxiao.kommandant:kommandant-core:@VERSION@'
}
```

### Maven

`pom.xml`

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

`kommandant-core`
```xml
	<dependency>
	    <groupId>com.github.kvnxiao.kommandant</groupId>
	    <artifactId>kommandant-core</artifactId>
	    <version>@VERSION@</version>
	</dependency>
```

## Development

Kommandant is written in Kotlin and provides full Java interoperability. It is recommended to use IntelliJ IDEA if you wish to edit the source code.

Feel free to submit an issue or pull request if you have ideas for improvement.
