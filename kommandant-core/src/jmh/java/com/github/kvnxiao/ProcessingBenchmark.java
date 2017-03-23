/*
 * Copyright 2017 Ze Hao Xiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/** Created on: 2017-03-04 Author: Kevin Xiao (github.com/alphahelix00) */
@State(Scope.Thread)
public class ProcessingBenchmark {

  private Kommandant kommandant;

  @Setup
  public void setup() {
    kommandant = new Kommandant();
    kommandant.addCommand(
        new ICommand<String>(
            "/",
            "testCommand",
            CommandDefaults.NO_DESCRIPTION,
            CommandDefaults.NO_USAGE,
            CommandDefaults.EXEC_WITH_SUBCOMMANDS,
            CommandDefaults.IS_DISABLED,
            "asdf",
            "test") {
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
