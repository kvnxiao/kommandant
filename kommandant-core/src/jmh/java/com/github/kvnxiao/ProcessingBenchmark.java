package com.github.kvnxiao;

import com.github.kvnxiao.kommandant.Kommandant;
import com.github.kvnxiao.kommandant.command.CommandContext;
import com.github.kvnxiao.kommandant.command.CommandDefaults;
import com.github.kvnxiao.kommandant.command.CommandResult;
import com.github.kvnxiao.kommandant.command.ICommand;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Created on:   2017-03-04
 * Author:       Kevin Xiao (github.com/alphahelix00)
 */
@State(Scope.Thread)
public class ProcessingBenchmark {

    private Kommandant kommandant;

    @Setup
    public void setup() {
        kommandant = new Kommandant();
        kommandant.addCommand(new ICommand<String>("/", "testCommand", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, "asdf", "test") {
            @Override
            public String execute(@NotNull CommandContext context, @NotNull Object... opt) {
                return "a";
            }
        });
    }

    @Benchmark
    public void testMethod() {
        CommandResult<String> result = kommandant.process("/test");
        String str = result.getResult();
    }

}
