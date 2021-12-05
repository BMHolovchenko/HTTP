package command;

import command.interfaces.Output;
import model.ApiResponse;
import model.Order;
import service.DataCheck;
import util.HttpUtil;
import console.Command;

import java.io.IOException;
import java.time.LocalDate;

public class OrderCommand implements Output {
    private final Command command;
    private final DataCheck check;
    private final HttpUtil httpUtil;

    public OrderCommand(Command command, DataCheck check, HttpUtil httpUtil) {
        this.command = command;
        this.check = check;
        this.httpUtil = httpUtil;
    }

    @Override
    public void execute() {
        boolean isNotExit = true;
        while (isNotExit) {
            command.print("Commands menu:\n"
                    + "Write '1' to create Order;\n"
                    + "Write '2' to delete Order by id;\n"
                    + "Write '3' to get pet inventory by status;\n"
                    + "Write '4' to get Order by id;\n"
                    + "Write '5' to leave Order menu");
            int number = 0;
            try {
                number = Integer.parseInt(command.read());
            } catch (Exception e) {
                command.print("Command not found");
            }
            switch (number) {
                case 1 -> {
                    try {
                        Order orderCreate = new Order();
                        orderCreate.setId(check.isLong("Enter id"));
                        orderCreate.setPetId(check.isLong("Enter pet id"));
                        orderCreate.setShipDate(LocalDate.now().toString());
                        orderCreate.setStatus(check.isOrderStatus("Enter status: placed, approved, delivered"));
                        orderCreate.setQuantity(check.isInt("Enter quantity"));
                        orderCreate.setComplete(check.trueOrFalse("Enter completion: true or false"));
                        command.print(httpUtil.createOrder(orderCreate).toString());
                    } catch (Exception e) {
                        command.print("Error while creating Order");
                    }
                }
                case 2 -> {
                    try {
                        ApiResponse apiResponse = httpUtil.deleteOrderById(check.isLong("Enter id"));
                        check.getResponse(apiResponse, "Order was deleted", "Order not found");
                    } catch (Exception e) {
                        command.print("Error while deleting Order");
                    }
                }
                case 3 -> {
                    try {
                        command.print(httpUtil.mapOfStatusCodesToQuantities().toString());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        Order order = httpUtil.getOrderById(check.isIdForOder("Enter id from 1 to 10"));
                        if (order.getId() != 0) {
                            command.print(order.toString());
                        } else command.print("Order not found");
                    } catch (Exception e) {
                        command.print("Order not found");
                    }
                }
                case 5 -> isNotExit = false;
                default -> command.print("Select another command");
            }
        }
    }

    @Override
    public String getCommandName() {
        return "store";
    }
}