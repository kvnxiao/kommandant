package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.configurable.CommandBankConfigurable

/**
 * Created by kxiao on 3/8/17.
 */
class KommandantConfigurable : Kommandant(cmdBank = CommandBankConfigurable()) {

    fun enableCommand(command: ICommand<*>?) {
        if (command !== null) command.props.isDisabled = false
    }

    fun disableCommand(command: ICommand<*>?) {
        if (command !== null) command.props.isDisabled = true
    }

}