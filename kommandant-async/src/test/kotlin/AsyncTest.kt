import com.github.kvnxiao.kommandant.KommandantAsync
import com.github.kvnxiao.kommandant.command.CommandBuilder
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.execute
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by kxiao on 3/9/17.
 */
class AsyncTest {


    companion object {
        private val kommandant: KommandantAsync = KommandantAsync()
    }

    @After
    fun after() {
        kommandant.clearAll()
    }

    @Test
    fun testAsync() {
        kommandant.addCommand(CommandBuilder<Unit>("test").withAliases("test").build(execute { context, opt ->
            Thread.sleep(1000)
            println("Hello world!")
        }))

        // Fire and forget
        kommandant.processAsync<Unit>("/test")
        kommandant.processAsync<Unit>("/test")
        runBlocking {
            assertTrue(kommandant.processAsync<Unit>("/test").await().success)
        }
    }

    @Test
    fun testSequential() {
        kommandant.addCommand(CommandBuilder<Unit>("test").withAliases("test").build(execute { context, opt ->
            Thread.sleep(1000)
            println("Hello world!")
        }))

        assertTrue(kommandant.process<Unit>("/test").success)
        assertTrue(kommandant.process<Unit>("/test").success)
    }

}