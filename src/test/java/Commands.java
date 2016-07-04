import com.github.alphahelix00.ordinator.commands.MainCommand;
import com.github.alphahelix00.ordinator.commands.SubCommand;

import java.util.List;

/**
 * A bunch of annotated commands defined for testing purposes
 *
 * <p>Created on:   6/24/2016</p>
 * <p>Author:       Kevin Xiao (github.com/alphahelix00)</p>
 */
public class Commands {

    @MainCommand(
            name = "single",
            alias = "single",
            description = "single command test"
    )
    public void singleCommandTest(List<String> args) {
        System.out.println("single command test!");
    }

    @MainCommand(
            name = "main",
            alias = "main",
            description = "sub command test",
            subCommands = {"Sub1", "Sub1.1"}
    )
    public void mainCommand(List<String> args) {
        System.out.println("main command test!");
    }

    @SubCommand(
            name = "Sub1",
            alias = "sub1",
            description = "sub command of main",
            subCommands = "sub2"
    )
    public void subCommandOne(List<String> args) {
        System.out.println("sub1 command test!");
    }

    @SubCommand(
            name = "Sub1.1",
            alias = "sub1.1",
            description = "sub command of main",
            subCommands = "sub2"
    )
    public void subCommandOne2(List<String> args) {
        System.out.println("sub1.1 command test!");
    }

    @SubCommand(
            name = "sub2",
            alias = {"sub2", "subtwo"},
            description = "sub command of main"
    )
    public void subCommandTwo(List<String> args) {
        System.out.println("sub2 command test!");
    }
}
