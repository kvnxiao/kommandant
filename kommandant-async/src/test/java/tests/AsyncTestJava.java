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
package tests;

import com.github.kvnxiao.kommandant.KommandantAsync;
import com.github.kvnxiao.kommandant.command.CommandBuilder;
import org.junit.After;
import org.junit.Test;

/** Created by kxiao on 3/9/17. */
public class AsyncTestJava {

  private static final KommandantAsync kommandant = new KommandantAsync();

  @After
  public void after() {
    kommandant.clearAll();
  }

  @Test
  public void testAsync() {
    kommandant.addCommand(
        new CommandBuilder<Void>("test")
            .withAliases("test")
            .build(
                ((context, opt) -> {
                  try {
                    Thread.sleep(1000);
                  } catch (Exception ignored) {
                  }
                  System.out.println("Hello world!");
                  return null;
                })));

    kommandant.processAsync("/test");
    kommandant.processAsync("/test");

    try {
      Thread.sleep(2000);
    } catch (Exception ignored) {
    }
  }
}
