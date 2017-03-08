import com.github.kvnxiao.kommandant.Kommandant;
import com.github.kvnxiao.kommandant.command.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import testcommands.KotlinBasedCommands;
import testcommands.SimpleCommand;

import static org.junit.Assert.*;

/**
 * Created by kvnxiao on 3/3/17.
 */
public class JavaTest {

    private static final Kommandant kommandant = new Kommandant();

    @Test
    public void test() {
        kommandant.addCommand(new ICommand<String>("/", "testCommand", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, "asdf", "test") {
            @Override
            public String execute(@NotNull CommandContext context, @NotNull Object... opt) {
                System.out.println("asdf");
                return "a";
            }
        });
        kommandant.addCommand(new CommandBuilder<Void>("lambda").withAliases("lambda").withPrefix("!").build((context, opt) -> {
            System.out.println("Test command builder with lambda");
            return null;
        }));
        kommandant.addAnnotatedCommands(SimpleCommand.class);
        kommandant.addAnnotatedCommands(KotlinBasedCommands.class);
        CommandResult<String> result = kommandant.process("/test");
        System.out.println(result.getResult());
        CommandResult<Void> result2 = kommandant.process("!lambda");
        System.out.println(result2.getResult());

        kommandant.process("/simple");

        System.out.println(kommandant.process("/parent child").getResult());

        kommandant.process("/kotlin");

        kommandant.getPrefixes().forEach((prefix) -> System.out.println(kommandant.getCommandsForPrefix(prefix)));

    }

    @Test
    public void rename() {
        kommandant.addCommand(new CommandBuilder<Void>("prefixchange").withAliases("prefixchange").build((context, opt) -> {
            System.out.println("This is a command that will have its prefix changed");
            return null;
        }));
        assertEquals(1, kommandant.getPrefixes().size());
        assertTrue(kommandant.getPrefixes().contains("/"));
        assertTrue(kommandant.process("/prefixchange").getSuccess());
        ICommand command = kommandant.getCommand("/prefixchange");
        kommandant.changePrefix(command, "!");
        System.out.println(kommandant.getPrefixes());
        assertEquals(1, kommandant.getPrefixes().size());
        assertTrue(kommandant.getPrefixes().contains("!"));
        assertFalse(kommandant.process("/prefixchange").getSuccess());
        assertTrue(kommandant.process("!prefixchange").getSuccess());
    }

    @Test
    public void addCommandDifferentPrefixClash() {
        kommandant.addCommand(new CommandBuilder<Integer>("diffPrefixClash").withAliases("diffPrefixClash").withPrefix("!!").build((context, opt) -> 1));
        kommandant.addCommand(new CommandBuilder<Integer>("!diffPrefixClash").withAliases("!diffPrefixClash").withPrefix("!").build((context, opt) -> 2));
        assertEquals(1, kommandant.process("!!diffPrefixClash").getResult());
    }

}
