package command.interfaces;

public interface Output {

    void execute();

    String getCommandName();

    default boolean canExecute(String command) {
        return getCommandName().equalsIgnoreCase(command);
    }
}