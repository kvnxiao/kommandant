import com.github.alphahelix00.ordinator.Ordinator;
import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.handler.CommandHandler;
import org.jooq.lambda.tuple.Tuple3;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class ConsoleTest {

    private CommandRegistry commandRegistry = Ordinator.getCommandRegistry();
    private CommandHandler commandHandler = commandRegistry.getCommandHandler();
    private static boolean setUpDone = false;

    @Before
    public void setup() {
        if (!setUpDone) {
            Command sub = Command.builder("subcommand", "command one's sub command", "sub").prefix("-").addSubCommand(
                    Command.builder("tertiary", "command one's second sub command", "sub").build((executor) -> System.out.println("command 1 tertiary command"))
            ).build((executor) -> System.out.println("command 1 sub command."));
            Command main = Command.builder("one", "command one", "one").prefix("-").addSubCommand(sub).build((executor) -> System.out.println("command 1."));
            commandRegistry.addCommand(main);
            commandRegistry.addCommand(Command.builder("two", "command two", "two").prefix("-").build((executor) -> System.out.println("command 2.")));
            setUpDone = true;
        }
    }

    @Test
    public void testUniqueKeysCommands() {
        commandRegistry.addCommand(Command.builder("one", "command one", "one").prefix("-").build((executor) -> System.out.println("command 1.")));
        commandRegistry.addCommand(Command.builder("two", "command two", "two").prefix("-").build((executor) -> System.out.println("command 2.")));
    }

    @Test
    public void testRegistryAddRetrieve() {
        commandRegistry.getCommandByAlias("-", "one").ifPresent(System.out::println);
        commandRegistry.getCommandByAlias("-", "asdf").ifPresent(System.out::println);
        commandRegistry.getCommandByAlias("-", "two").ifPresent(System.out::println);
        commandRegistry.getCommandByAlias("-", "asdf testing 123").ifPresent(System.out::println);
        commandRegistry.getCommandByAlias("!", "asdf testing 123").ifPresent(System.out::println);
        commandRegistry.getCommandByAlias("!", "one").ifPresent(System.out::println);
        commandRegistry.getCommandByAlias("!", "two").ifPresent(System.out::println);
        commandRegistry.getCommandMap("!").ifPresent(System.out::println);
        commandRegistry.getCommandMap("-").ifPresent(System.out::println);
    }

    @Test
    public void testValidation() {
        System.out.println(commandHandler.validateMessage("-one"));
        System.out.println(commandHandler.validateMessage("!one"));
    }

    @Test
    public void TestExecution() {
        commandHandler.validateParse("-one");
        commandHandler.validateParse("-!one");
        commandHandler.validateParse("-one sub");
        commandHandler.validateParse("-one sub sub");
    }

}