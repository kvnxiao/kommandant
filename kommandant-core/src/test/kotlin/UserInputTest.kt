import com.github.kvnxiao.kommandant.Kommandant
import com.github.kvnxiao.kommandant.command.CommandBuilder
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.execute

fun main(args: Array<String>) {
    val kommandant = Kommandant()

    kommandant.addCommand(CommandBuilder<Unit>("hello_world")
            .withPrefix("-")
            .withAliases("hello", "print")
            .build(execute {
        context, opt ->
        println("Hello world!")
    }))

    while (true) {
        println("Please enter your input:")
        kommandant.process<Any?>(readLine()!!)
    }

}