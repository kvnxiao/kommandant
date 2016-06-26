import com.github.alphahelix00.ordinator.Ordinator;
import com.github.alphahelix00.ordinator.commands.Command;
import com.github.alphahelix00.ordinator.commands.CommandRegistry;
import com.github.alphahelix00.ordinator.commands.essential.EssentialCommands;
import com.github.alphahelix00.ordinator.commands.handler.AbstractCommandHandler;
import com.github.alphahelix00.ordinator.commands.handler.CommandHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created on:   6/23/2016
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
public class ConsoleTest {

    private CommandRegistry commandRegistry = Ordinator.getCommandRegistry();
    private AbstractCommandHandler commandHandler = commandRegistry.setCommandHandler(new CommandHandler(commandRegistry));
    private static boolean setUpDone = false;

    @Before
    public void setup() {
        if (!setUpDone) {
            Command sub = Command.builder("sub", "command one's sub command").prefix("-").isMain(false).addSubCommand(
                    Command.builder("tertiary", "command one's second sub command").build((executor) -> {
                        System.out.println("command 1 tertiary command");
                        return null;
                    })
            ).build((executor) -> {
                System.out.println("command 1 sub command.");
                return null;
            });
            Command main = Command.builder("one", "command one").prefix("-").addSubCommand(sub).build((executor) -> {
                System.out.println("command 1.");
                return null;
            });
            commandHandler.registerCommand(main);
            commandHandler.registerCommand(Command.builder("two", "command two").prefix("-").build((executor) -> {
                System.out.println("command 2.");
                return null;
            }));
            commandHandler.registerAnnotatedCommands(new Commands());
            commandHandler.registerCommand(new EssentialCommands.Enable());
            commandHandler.registerCommand(new EssentialCommands.Disable());
            commandHandler.registerCommand(new EssentialCommands.Help());
            setUpDone = true;
        }
    }

    @Test
    public void testUniqueKeysCommands() {
        assertFalse(commandHandler.registerCommand(Command.builder("one", "command one").prefix("-").build((executor) -> {
            System.out.println("test command 1.");
            return Optional.empty();
        })));
        assertFalse(commandHandler.registerCommand(Command.builder("two", "command two").prefix("-").build((executor) -> {
            System.out.println("test command 2.");
            return Optional.empty();
        })));
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
        assertTrue(commandHandler.validatePrefix("-one").v1);
        assertTrue(commandHandler.validatePrefix("!one").v1);
    }

    @Test
    public void TestExecution() {
        assertTrue(commandHandler.validateParse("-one"));
        assertFalse(commandHandler.validateParse("-!one"));
        assertTrue(commandHandler.validateParse("-one sub"));
        assertTrue(commandHandler.validateParse("-one sub sub"));
    }

    @Test
    public void TestAnnotatedCommands() {
        assertTrue(commandHandler.validateParse("-single"));
        assertTrue(commandHandler.validateParse("-main"));
        assertTrue(commandHandler.validateParse("-main sub1"));
        assertTrue(commandHandler.validateParse("-main sub1 sub2"));
        assertTrue(commandHandler.validateParse("-main sub1 test blah"));
    }

    @Test
    public void TestEssentialCommands() {
        assertTrue(commandHandler.validateParse("!enable"));
        assertTrue(commandHandler.validateParse("!disable"));
        assertTrue(commandHandler.validateParse("!disable -main sub1 sub2"));
        assertTrue(commandHandler.validateParse("!enable -main sub1 sub2"));
        assertTrue(commandHandler.validateParse("!disable !enable"));
//        assertTrue(commandHandler.validateParse("!help"));
//        assertTrue(commandHandler.validateParse("!help !enable"));
    }

}