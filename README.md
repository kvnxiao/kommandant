<h1>Kommandant &nbsp;<sup><sup><sub><i>[origin: German] (noun): commander</i></sub></sup></sup></h1>

[![CircleCI](https://circleci.com/gh/kvnxiao/kommandant/tree/master.svg?style=shield)](https://circleci.com/gh/kvnxiao/kommandant/tree/master)
[![Release](https://jitpack.io/v/kvnxiao/kommandant.svg)](https://jitpack.io/#kvnxiao/kommandant)
[![Apache License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Fast, modular, and flexible. Kommandant is a multipurpose command framework for the JVM, ready for easy integration into any project.

## Overview

Kommandant is a command framework fit for any project, whether it may be for a command-line app or for a complex chat system.

For more detailed instructions on how to use it, go here for [Kotlin]() usage, or here for [Java]() usage.

You can grab Kommandant using your favourite dependency management tools! Just replace `@VERSION` with the latest release tag or use a commit hash.

### **PICK ONE OF:**

`kommandant-configurable` *(relies on the core dendency)* is an extension module that can easily enable/disable commands at will, as well as load or save command properties to a `.json` file.

`kommandant-core` comes with bare essentials to manange your commands.

### **Additional Modules**

`kommandant-async` is an extension module with an asynchronous command processing method addon that relies on Kotlin [coroutines](https://github.com/Kotlin/kotlin-coroutines/blob/master/kotlin-coroutines-informal.md). Please keep in mind that as of Kotlin 1.10, coroutines are still _experimental_ and subject to change.


### Gradle

build.gradle
```gradle
allprojects {
  repositories {
    // ...
    maven { url 'https://jitpack.io' }
  }
}
```

For `kommandant-core`
```gradle
dependencies {
    compile 'com.github.kvnxiao.kommandant:kommandant-core:@VERSION@'
}
```

For `kommandant-configurable`
```gradle
dependencies {
    compile 'com.github.kvnxiao.kommandant:kommandant-configurable:@VERSION@'
}
```

### Maven

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

For `kommandant-core`
```xml
	<dependency>
	    <groupId>com.github.kvnxiao.kommandant</groupId>
	    <artifactId>kommandant-core</artifactId>
	    <version>@VERSION@</version>
	</dependency>
```

For `kommandant-configurable`
```xml
	<dependency>
	    <groupId>com.github.kvnxiao.kommandant</groupId>
	    <artifactId>kommandant-configurable</artifactId>
	    <version>@VERSION@</version>
	</dependency>
```

## Development

Kommandant is written in Kotlin and provides full Java interoperability. It is recommended to use IntelliJ IDEA if you wish to edit the source code.

Have suggestions? See problems? Got new ideas or improvements? Feel free to submit an issue or pull request!
