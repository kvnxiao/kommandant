<h1>Kommandant &nbsp;<sup><sup><sub><i>[origin: German] (noun): commander</i></sub></sup></sup></h1>

![](https://jitpack.io/v/kvnxiao/kommandant.svg)

Fast, modular, and flexible. Kommandant is a multipurpose command framework for the JVM, written in Kotlin and ready for easy integration into any project.

# Overview

Kommandant is a command framework fit for any project, whether it may be for a command-line app or for a complex chat system.

For more detailed instructions on how to use it, go here for [Kotlin]() usage, or here for [Java]() usage.

# Download

Grab it using your favourite dependency management tools! Replace `@VERSION` with the latest release tag or use a commit hash.

**PICK ONE OF:**

`kommandant-configurable` *(relies on the core dendency)* is an extension module that can easily enable/disable commands at will, as well as load or save command properties to a .json file.

`kommandant-core` comes with bare essentials to manange your commands.

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
