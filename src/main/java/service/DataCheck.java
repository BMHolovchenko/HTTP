package service;

import model.*;
import console.Command;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataCheck {

    private final Command command;

    public DataCheck(Command command) {
        this.command = command;
    }

    public Long isLong(String message) {
        boolean isNotNumber = true;
        long number = 0;
        while (isNotNumber) {
            try {
                command.print(message);
                number = Long.parseLong(command.read());
                if (number > 0) {
                    isNotNumber = false;
                }
            } catch (Exception e) {
                command.print("Error.");
            }
        }
        return number;
    }

    public int isInt(String message) {
        boolean isNotNumber = true;
        int number = 0;
        while (isNotNumber) {
            try {
                command.print(message);
                number = Integer.parseInt(command.read());
                if (number > 0) {
                    isNotNumber = false;
                }
            } catch (Exception e) {
                command.print("Error.");
            }
        }
        return number;
    }

    public String isEmpty(String message) {
        boolean isNotString = true;
        String result = "";
        while (isNotString) {
            try {
                command.print(message);
                result = command.read();
                if (!result.isEmpty()) {
                    isNotString = false;
                }
            } catch (Exception e) {
                command.print("Error.");
            }
        }
        return result;
    }

    public PetStatus isPetStatus(String message) {
        boolean isNotPetStatus = true;
        PetStatus petStatus = PetStatus.sold;
        while (isNotPetStatus) {
            command.print(message);
            try {
                petStatus = PetStatus.valueOf(command.read().toLowerCase());
                isNotPetStatus = false;
            } catch (Throwable throwable) {
                command.print("Error.");
            }
        }
        return petStatus;
    }

    public OrderStatus isOrderStatus(String message) {
        boolean isNotOrderStatus = true;
        OrderStatus orderStatus = OrderStatus.approved;
        while (isNotOrderStatus) {
            command.print(message);
            try {
                orderStatus = OrderStatus.valueOf(command.read().toLowerCase());
                isNotOrderStatus = false;
            } catch (Throwable throwable) {
                command.print("Error.");
            }
        }
        return orderStatus;
    }

    public Pet getPetEntity() {
        Pet pet = new Pet();
        pet.setId(isLong("Enter pet's id"));
        pet.setName(isEmpty("Enter pet's name"));
        pet.setCategory(createCategory("Enter pet's category"));
        pet.setTags(createTags("Enter tags"));
        pet.setPhotoUrl(createPhoto("Enter photo's url"));
        pet.setStatus(isPetStatus("Enter status: available, pending, sold"));
        return pet;
    }

    public List<Tag> createTags(String message) {
        List<Tag> tags = new ArrayList<>();
        long count = 1;
        boolean enterClose = true;
        command.print(message);
        while (enterClose) {
            String text = isEmpty("Enter 'yes' to add tag, enter 'no' to continue");
            if (text.equalsIgnoreCase("yes")) {
                Tag create = new Tag(count++, isEmpty("enter tags"));
                tags.add(create);
            } else if (text.equalsIgnoreCase("no")) {
                enterClose = false;
            } else {
                command.print("Unknown command");
            }
        }
        return tags;
    }

    public List<String> createPhoto(String message) {
        List<String> photo = new ArrayList<>();
        boolean enterClose = true;
        command.print(message);
        while (enterClose) {
            String text = isEmpty("Enter 'yes' to add photo, enter 'no' to continue");
            if (text.equalsIgnoreCase("yes")) {
                photo.add(isEmpty("Enter url"));
            } else if (text.equalsIgnoreCase("no")) {
                enterClose = false;
            } else {
                command.print("Unknown command");
            }
        }
        return photo;
    }

    public Category createCategory(String message) {
        boolean enterClose = true;
        long count = 1;
        Category category = null;
        while (enterClose) {
            category = new Category(count++, isEmpty(message));
            enterClose = false;
        }
        return category;
    }

    public File isImage(String message) {
        File file = null;
        boolean isEmpty = true;
        while (isEmpty) {
            try {
                file = new File(isEmpty(message));
                if (ImageIO.read(file) != null) {
                    isEmpty = false;
                }
            } catch (Exception e) {
                command.print("Error.");
            }
        }
        return file;
    }

    public boolean trueOrFalse(String message) {
        while (true) {
            String result = isEmpty(message).toLowerCase();
            if (result.equals("true")) {
                return true;
            } else if (result.equals("false")) {
                return false;
            }
            command.print("Error.");
        }
    }

    public long isIdForOder(String message) {
        long id = 0;
        boolean isGoodNumber = true;
        while (isGoodNumber) {
            id = isLong(message);
            if (id > 0 && id <= 10) {
                isGoodNumber = false;
            } else command.print("Error.");
        }
        return id;
    }

    public User getUserEntity() {
        User user = new User();
        user.setId(isLong("Enter id"));
        user.setUserName(isEmpty("Enter username"));
        user.setFirstName(isEmpty("Enter first name"));
        user.setLastName(isEmpty("Enter last name"));
        user.setEmail(isEmpty("Enter email"));
        user.setPhone(isEmpty("Enter phone"));
        user.setPassword(isEmpty("Enter password"));
        user.setUserStatus(isInt("Enter user status"));
        return user;
    }

    public List<User> createUserList(String message) {
        List<User> userList = new ArrayList<>();
        boolean enterClose = true;
        command.print(message);
        while (enterClose) {
            String text = isEmpty("Enter 'yes' to add User, enter 'no' to continue");
            if (text.equalsIgnoreCase("yes")) {
                User create = getUserEntity();
                userList.add(create);
            } else if (text.equalsIgnoreCase("no")) {
                enterClose = false;
            } else {
                command.print("Enter 'yes' or 'no'");
            }
        }
        if (userList.isEmpty()) {
            command.print("User list is empty.");
        }
        return userList;
    }

    public User getUpdateUser(String username, User user) {
        user.setId(isLong("Enter id"));
        user.setUserName(username);
        user.setFirstName(isEmpty("Enter first name"));
        user.setLastName(isEmpty("Enter last name"));
        user.setEmail(isEmpty("Enter email"));
        user.setPhone(isEmpty("Enter phone"));
        user.setPassword(isEmpty("Enter password"));
        user.setUserStatus(isInt("Enter user status"));
        return user;
    }

    public void getResponse(ApiResponse response, String yes, String no) {
        if (response.getCode() == 200) {
            command.print(response + "\n" + yes);
        } else command.print(no);
    }
}
