#Ordinator
[![Build Status](https://drone.io/github.com/alphahelix00/Ordinator/status.png)](https://drone.io/github.com/alphahelix00/Ordinator/latest)
[![](https://jitpack.io/v/alphahelix00/Ordinator.svg?style=flat-square)](https://jitpack.io/#alphahelix00/Ordinator)

An easily adoptable command framework made for Java 8+

## About

Ordinator is a simple, modularized command framework created with the purpose of handling the processing and execution of user defined commands.
It was created with the goal of easy adaptation into any other project.

##Functionality
The command framework within Ordinator is roughly based on the Command Design Pattern by abstracting executable commands from the client.
The two important classes within the framework are:
- `CommandRegistry` *(the command bank)*
  - Storage of all commands
- `CommandHandler` *(the ordinator)*
  - Validate and parse Strings for command calls
  - Calls upon the registry to register and execute commands (annotated methods / CommandBuilder / Command class extension)

### Commands
Commands are split into _**main**_ and _**sub**_ commands. Main commands must have unique names and are registered into the command registry. Sub commands are entirely optional, and are tied to their parent commands by their unique names. All commands are called by their _**alias**_ but defined by their _**name**_ For a sub command to properly function, it is crucial that the name of the sub commands is set within the parent command. Take the following as an example:

#### Example
Supposed you have defined a main command `(name = "Greet Command", alias = "greet", subCommands = {"Hi Command", "Bye Command"})` that will print out `Greetings.`.

And suppose you have also defined two sub commands: `(name = "Hi Command", alias = "hi")` that will print out `Hi.`, and `(name = "Bye Command", alias = "bye")` that will print out `Bye.`

When a string containing `greet` is parsed, `Greetings.` will be printed. When a string containing `greet hi` is parsed, `Greetings. Hi.` will be printed, and likewise if `greet bye` is parsed, `Greetings. Bye.` will be printed.

All commands can have a list of arguments passed into it on execution. For example, if `greet bye args1 args2` is called, the main command will execute a method to print `Greetings. Bye`, but will also have access to an argument token list, `{args1, args2}`.
Note that one can choose to have a main command do nothing and have only its sub commands do something. So in the above example, we can choose to not print out `"Greetings."`, so that only `"Hi."` is displayed when `greet hi` is parsed.

#### Command chaining
There is no limit to the number of sub commands, provided that all the names are unique. It is also possible for a sub command to be the sub command of another sub command. As long as the defining hierarchy is followed, there should be no problem having any number of sub command links.

##### Repeatable (Self-chaining) Commands
A command can also be defined as self-chaining or repeatable if the command name is included within the subCommands: `(name = "repeat", alias = "repeat", subCommands = "repeat")`
Calling `repeat` will do something once, and calling `repeat repeat` will do the same thing twice, as if it were a sub command of itself.

## Creating / Registering / Parsing Commands

All commands within the command registry can be parsed for by their alias.
There are several ways of defining a command:

---

##### Annotations
A class containing methods defined with `@MainCommand` (and for sub commands, `@SubCommand`) can have these methods registered as a command in the CommandRegistry.
Call `#registerAnnotatedCommands` on the object containing the annotations from the `AbstractCommandHandler` implementation to register the commands.

By default, the prefix is not a required field within the annotation (defaults to `-`), and the method name can be declared as anything.

---

##### CommandBuilder
Using the CommandBuilder, supply the proper information to create a new command and add it to the registry.
Within the .build() method call, the use of Java 8 lambda expressions is recommended  as it takes in a functional interface CommandExecutor object.

---

##### User-defined commands extending Command class
One can also define commands by extending the `Command` class.

## Usage
To import into your own project, download the jar files from [release](https://github.com/alphahelix00/Ordinator/releases) and add as external dependencies within your IDE, or see below for Gradle or Maven import through jitpack.io. 

### Gradle

###### build.gradle

```
allprojects {
     repositories {
         ...
         maven { url "https://jitpack.io" }
     }
}
```

```
dependencies {
        compile 'com.github.alphahelix00:Ordinator:@VERSION_NUM'
}
```

### Maven

###### pom.xml
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.alphahelix00</groupId>
    <artifactId>Ordinator</artifactId>
    <version>@VERSION_NUM@</version>
</dependency>
```

Replace `@VERSION_NUM@` with the version you want to use. To use the latest development snapshot, use `dev-SNAPSHOT`.

##Contribution

For those of you who are interested seeing this project improve, all contributions are appreciated! Any suggestions for changes or improvements are also welcome.