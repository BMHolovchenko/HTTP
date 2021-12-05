package command;

import command.interfaces.Output;
import model.ApiResponse;
import service.DataCheck;
import util.HttpUtil;
import console.Command;

public class UserCommand implements Output {

    private final Command command;
    private final DataCheck check;
    private final HttpUtil httpUtil;

    public UserCommand(Command command, DataCheck check, HttpUtil httpUtil) {
        this.command = command;
        this.check = check;
        this.httpUtil = httpUtil;
    }

    @Override
    public void execute() {
        boolean isNotExit = true;
        while (isNotExit) {
            command.print("Commands menu:\n"
                    + "Write '1' to create User;\n"
                    + "Write '2' to create User with list;\n"
                    + "Write '3' to get User by name;\n"
                    + "Write '4' to update User by name;\n"
                    + "Write '5' to delete User by name;\n"
                    + "Write '6' to leave User menu");
            int number = 0;
            try {
                number = Integer.parseInt(command.read());
            } catch (Exception e) {
                command.print("Command not found");
            }
            switch (number) {
                case 1 -> {
                    model.User user = check.getUserEntity();
                    try {
                        check.getResponse(httpUtil.createUser(user),
                                "User was created", "Error while creating User");
                    } catch (Exception e) {
                        command.print("Error while creating User");
                    }
                }
                case 2 -> {
                    try {
                        command.print(httpUtil.createWithListUsers(check.createUserList(
                                "Enter users to add to the list")).toString());
                    } catch (Exception e) {
                        command.print("Error while creating User");
                    }
                }
                case 3 -> {
                    try {
                        model.User user = httpUtil.getUserByName(check.isEmpty(
                                "Enter username").toLowerCase());
                        if (user.getId() != 0) {
                            command.print(user.toString());
                        } else command.print("User not found");
                    } catch (Exception e) {
                        command.print("User not found");
                    }
                }
                case 4 -> {
                    try {
                        String username = check.isEmpty("Enter username").toLowerCase();
                        model.User user = httpUtil.getUserByName(username);
                        user = check.getUpdateUser(username, user);
                        check.getResponse(httpUtil.updateUserByName(username, user),
                                "User was updated",
                                "Error while updating User");
                    } catch (Exception e) {
                        command.print("User not found");
                    }
                }
                case 5 -> {
                    try {
                        ApiResponse apiResponse = httpUtil.deleteUserByName(check.isEmpty(
                                "Enter username").toLowerCase());
                        check.getResponse(apiResponse, "User was deleted", "Error while deleting User");
                    } catch (Exception e) {
                        command.print("Error while deleting User");
                    }
                }
                case 6 -> isNotExit = false;
                default -> command.print("Select another command");
            }
        }
    }

    @Override
    public String getCommandName() {
        return "user";
    }
}