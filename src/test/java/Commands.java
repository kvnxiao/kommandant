import com.github.alphahelix00.ordinator.commands.MainCommand;
import com.github.alphahelix00.ordinator.commands.SubCommand;

import java.util.List;

/**
 * Created on:   6/24/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
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
            subCommands = "sub1"
    )
    public void mainCommand(List<String> args) {
        System.out.println("main command test!");
    }

    @SubCommand(
            name = "sub1",
            alias = "sub1",
            description = "sub command of main",
            subCommands = "sub2"
    )
    public void subCommandOne(List<String> args) {
        System.out.println("sub1 command test!");
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
