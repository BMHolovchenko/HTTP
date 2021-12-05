package controller;

import command.*;
import command.interfaces.Output;
import service.DataCheck;
import util.HttpUtil;
import console.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    private final Command command;
    private final List<Output> commands;

    public Controller(Command command) {
        DataCheck dataCheck = new DataCheck(command);
        HttpUtil httpUtil = new HttpUtil();
        this.command = command;
        this.commands = new ArrayList<>(Arrays.asList(
                new Help(command),
                new PetCommand(command, dataCheck, httpUtil),
                new OrderCommand(command, dataCheck, httpUtil),
                new UserCommand(command, dataCheck, httpUtil)
        ));
    }

    public void run() {
        command.print("Welcome to Pet store application");
        execute();
    }

    private void execute() {
        boolean isNotExit = true;
        while (isNotExit) {
            command.print("Write command or 'help' to get list of commands");
            String inputCommand = command.read();
            for (Output command : commands) {
                if (command.canExecute(inputCommand)) {
                    command.execute();
                    break;
                } else if (inputCommand.equalsIgnoreCase("exit")) {
                    this.command.print("Application is closed.");
                    isNotExit = false;
                    break;
                }
            }
        }
    }
}