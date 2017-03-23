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
package testcommands;

import com.github.kvnxiao.kommandant.command.CommandAnn;
import com.github.kvnxiao.kommandant.command.CommandContext;

/** Created on: 2017-03-07 Author: Kevin Xiao (github.com/alphahelix00) */
public class MainAndSubCommands {

  @CommandAnn(uniqueName = "parent", aliases = "parent")
  public String parentCommand(CommandContext context, Object... opt) {
    System.out.println("Parent test");
    return "I am the parent command.";
  }

  @CommandAnn(uniqueName = "child", aliases = "child", parentName = "parent")
  public String childCommand(CommandContext context, Object... opt) {
    System.out.println("Child test");
    return "I am the child command.";
  }
}
