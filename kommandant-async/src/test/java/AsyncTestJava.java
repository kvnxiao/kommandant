import com.github.kvnxiao.kommandant.KommandantAsync;
import com.github.kvnxiao.kommandant.command.CommandBuilder;
import org.junit.After;
import org.junit.Test;

/**
 * Created by kxiao on 3/9/17.
 */
public class AsyncTestJava {

    private static final KommandantAsync kommandant = new KommandantAsync();

    @After
    public void after() {
        kommandant.clearAll();
    }

    @Test
    public void testAsync() {
        kommandant.addCommand(new CommandBuilder<Void>("test").withAliases("test").build(((context, opt) -> {
            try {
            Thread.sleep(1000);
            } catch (Exception ignored) {}
            System.out.println("Hello world!");
            return null;
        })));

        kommandant.processAsync("/test");
        kommandant.processAsync("/test");

        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {}
    }

}
