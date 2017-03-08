import com.github.kvnxiao.kommandant.Kommandant;
import com.github.kvnxiao.kommandant.command.*;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Test;
import testcommands.KotlinCommand;
import testcommands.MainAndSubCommands;
import testcommands.SimpleCommand;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Created by kvnxiao on 3/3/17.
 */
public class JavaTest {

    private static final Kommandant kommandant = new Kommandant();

    @After
    public void after() {
        kommandant.clearAll();
    }

    @Test
    public void testAddCommand() {
        // Add command through constructor
        kommandant.addCommand(new ICommand<String>(CommandDefaults.PREFIX, "testAddCommand1", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "testAddCommand1", "t1") {
            @Override
            public String execute(@NotNull CommandContext context, @NotNull Object... opt) throws InvocationTargetException, IllegalAccessException {
                return "testAddCommand1";
            }
        });

        // Add command through builder
        kommandant.addCommand(new CommandBuilder<Integer>("testAddCommand2").withAliases("testAddCommand2", "t2").build(((context, opt) -> 2)));

        // Process first command
        CommandResult<String> commandResult1 = kommandant.process("/testAddCommand1");
        assertTrue(commandResult1.getSuccess());
        assertEquals("testAddCommand1", commandResult1.getResult());
        // Process first command with alternate alias
        commandResult1 = kommandant.process("/t1");
        assertTrue(commandResult1.getSuccess());
        assertEquals("testAddCommand1", commandResult1.getResult());

        // Process second command
        CommandResult<Integer> commandResult2 = kommandant.process("/testAddCommand2");
        assertTrue(commandResult2.getSuccess());
        assertEquals(new Integer(2), commandResult2.getResult());
        // Process second command with alternate alias
        commandResult2 = kommandant.process("/testAddCommand2");
        assertTrue(commandResult2.getSuccess());
        assertEquals(new Integer(2), commandResult2.getResult());
    }

    @Test
    public void testAddAnnotatedCommands() {
        kommandant.addAnnotatedCommands(SimpleCommand.class);
        kommandant.addAnnotatedCommands(KotlinCommand.class);
        kommandant.addAnnotatedCommands(MainAndSubCommands.class);

        // Process simple command
        assertTrue(kommandant.process("/simple").getSuccess());

        // Process main-sub (parent/child) commands
        CommandResult<String> parent = kommandant.process("/parent");
        assertTrue(parent.getSuccess());
        assertEquals("I am the parent command.", parent.getResult());
        CommandResult<String> child = kommandant.process("/parent child");
        assertTrue(child.getSuccess());
        assertEquals("I am the child command.", child.getResult());

        // Process kotlin based command
        kommandant.process("/kotlin");
    }

    @Test
    public void testRenameCommand() {
        kommandant.addCommand(new CommandBuilder<Integer>("prefixchange").withAliases("prefixchange").build((context, opt) -> {
            System.out.println("This is a command that will have its prefix changed");
            return 0;
        }));

        // Check for command bank size and registered prefixes
        assertEquals(1, kommandant.getPrefixes().size());
        assertTrue(kommandant.getPrefixes().contains("/"));
        assertFalse(kommandant.getPrefixes().contains("!"));
        assertTrue(kommandant.process("/prefixchange").getSuccess());
        assertEquals(0, kommandant.process("/prefixchange").getResult());

        // Get command and change its prefix
        ICommand command = kommandant.getCommand("/prefixchange");
        kommandant.changePrefix(command, "!");      // change prefix from default '/' to '!'
        assertEquals(1, kommandant.getPrefixes().size());
        assertTrue(kommandant.getPrefixes().contains("!"));
        assertFalse(kommandant.getPrefixes().contains("/"));
        assertFalse(kommandant.process("/prefixchange").getSuccess());
        assertTrue(kommandant.process("!prefixchange").getSuccess());
    }

    @Test
    public void testAddCommandDifferentPrefixClash() {
        kommandant.addCommand(new CommandBuilder<Integer>("diffPrefixClash").withAliases("diffPrefixClash").withPrefix("!!").build((context, opt) -> 1));
        kommandant.addCommand(new CommandBuilder<Integer>("!diffPrefixClash").withAliases("!diffPrefixClash").withPrefix("!").build((context, opt) -> 2));
        assertEquals(1, kommandant.process("!!diffPrefixClash").getResult());
    }

}
