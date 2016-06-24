import com.github.alphahelix00.ordinator.Ordinator;
import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.handler.CommandHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class ConsoleTest {

    private CommandRegistry commandRegistry = Ordinator.getCommandRegistry();
    private CommandHandler commandHandler = commandRegistry.getCommandHandler();

    @Before
    public void setup() {
        commandRegistry.addCommand(Command.of(true, "-", "one", Collections.singletonList("one"), "command one", new ArrayList<>()));
        commandRegistry.addCommand(Command.of(true, "-", "two", Collections.singletonList("two"), "command two", new ArrayList<>()));
    }

    @Test
    public void testUniqueKeysCommands() {
        commandRegistry.addCommand(Command.of(true, "-", "one", Collections.singletonList("one"), "command one", new ArrayList<>()));
        commandRegistry.addCommand(Command.of(true, "-", "two", Collections.singletonList("two"), "command two", new ArrayList<>()));
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

}