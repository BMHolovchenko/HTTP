import console.Console;
import controller.Controller;
import console.Command;

public class App {

    public static void main(String[] args) {
        Command command = new Console();
        Controller controller = new Controller(command);
        controller.run();
    }
}