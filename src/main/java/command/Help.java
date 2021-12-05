package command;

import command.interfaces.Output;
import console.Command;

public class Help implements Output {

    private final Command command;

    public Help(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.print("Write 'Help' to get information about application commands.");
        command.print("Write 'Pet' to select pet command.");
        command.print("Write 'Store' to select store command.");
        command.print("Write 'User' to select user command.");
        command.print(("Write 'Exit' to close application."));
    }

    @Override
    public String getCommandName() {
        return "help";
    }
}