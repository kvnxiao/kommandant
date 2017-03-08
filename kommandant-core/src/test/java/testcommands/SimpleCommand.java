package testcommands;

import com.github.kvnxiao.kommandant.command.CommandAnn;
import com.github.kvnxiao.kommandant.command.CommandContext;

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class SimpleCommand {

    @CommandAnn(
            uniqueName = "simple",
            aliases = "simple"
    )
    public void simpleCommand(CommandContext context, Object... opt) {
        System.out.println("this is a simple command!");
    }

    @CommandAnn(
            uniqueName = "parent",
            aliases = "parent"
    )
    public String parentCommand(CommandContext context, Object... opt) {
        System.out.println("Parent test");
        return "I am the parent command.";
    }

    @CommandAnn(
            uniqueName = "child",
            aliases = "child",
            parentName = "parent"
    )
    public String childCommand(CommandContext context, Object... opt) {
        System.out.println("Child test");
        return "I am the child command.";
    }

}
