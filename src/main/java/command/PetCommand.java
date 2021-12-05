package command;

import command.interfaces.Output;
import service.DataCheck;
import util.HttpUtil;
import console.Command;

import java.io.IOException;

public class PetCommand implements Output {
    private final Command command;
    private final DataCheck check;
    private final HttpUtil httpUtil;

    public PetCommand(Command command, DataCheck check, HttpUtil httpUtil) {
        this.command = command;
        this.check = check;
        this.httpUtil = httpUtil;
    }

    @Override
    public void execute() {
        boolean isNotExit = true;
        while (isNotExit) {
            command.print("Commands menu:\n"
                            + "Write '1' to create Pet;\n"
                            + "Write '2' to update Pet;\n"
                            + "Write '3' to find Pet by status;\n"
                            + "Write '4' to find Pet by id;\n"
                            + "Write '5' to update pet by id;\n"
                            + "Write '6' delete to pet by id;\n"
                            + "Write '7' to upload Pet image by id;\n"
                            + "Write '8' to leave Pet menu;\n");

            int number = 0;
            try {
                number = Integer.parseInt(command.read());
            } catch (Exception e) {
                command.print("Command not found");
            }
            switch (number) {
                case 1 -> {
                    model.Pet petCreate = check.getPetEntity();
                    try {
                        command.print(httpUtil.createPet(petCreate).toString());
                    } catch (Exception e) {
                        command.print("Error while creating Pet");
                    }
                }
                case 2 -> {
                    model.Pet petUpdate = check.getPetEntity();
                    try {
                        model.Pet pet = httpUtil.updatePet(petUpdate);
                        if (pet.getId() != 0) {
                            command.print(pet.toString());
                        } else command.print("Pet not found");
                    } catch (Exception e) {
                        command.print("Error while updating User");
                    }
                }
                case 3 -> {
                    try {
                        command.print(httpUtil.findByStatus(check.isPetStatus(
                                "Enter status: available, pending, sold")).toString());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        model.Pet pet = httpUtil.getPetById(check.isLong("Enter id to search Pet"));
                        if (pet.getId() != 0) {
                            command.print(pet.toString());
                        } else command.print("Pet not found");
                    } catch (Exception e) {
                        command.print("Pet not found");
                    }
                }
                case 5 -> {
                    try {
                        long id = check.isLong("Enter pet id to update Pet");
                        command.print(httpUtil.updatePetById(id,
                                        check.isEmpty("Enter Pet name"),
                                        check.isPetStatus("Enter Pet status"))
                                .toString());
                    } catch (Exception e) {
                        command.print("Pet not found");
                    }
                }
                case 6 -> {
                    try {
                        command.print(httpUtil.deleteById(check.isLong("Enter id for delete Pet"))
                                .toString());
                    } catch (Exception e) {
                        command.print("Error while deleting Pet");
                    }
                }
                case 7 -> {
                    try {
                        command.print(httpUtil.uploadImage(check.isLong("Enter Pet id"),
                                check.isEmpty("Enter meta data"),
                                check.isImage("Enter image url")).toString());
                    } catch (Exception e) {
                        command.print("File not found");
                    }
                }
                case 8 -> isNotExit = false;
                default -> command.print("Select another command");
            }
        }
    }

    @Override
    public String getCommandName() {
        return "pet";
    }
}