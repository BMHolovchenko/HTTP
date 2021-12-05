package console;

import java.util.Scanner;

public class Console implements Command {
    private final Scanner scanner;

    public Console() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String read() {
        return scanner.nextLine();
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}