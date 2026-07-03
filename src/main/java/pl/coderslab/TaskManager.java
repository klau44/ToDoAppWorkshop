package pl.coderslab;

import java.util.Scanner;

public class TaskManager {

    private static final String FILE_NAME = "tasks.csv";
    private static final String[] COMMANDS = {"add", "remove", "list", "exit"};
    private static String[][] tasks;

    public static void main(String[] args) {
        System.out.println(ConsoleColors.BLUE + " Please select an option: ");
        for (String command : COMMANDS) {
            System.out.println(ConsoleColors.RESET + command);
        }

        Scanner scanner = new Scanner(System.in);
        String userCommand = scanner.nextLine();

        switch (userCommand) {
            case "add" -> System.out.println("... adding task ...");
            case "remove" -> System.out.println("... deleting task ...");
            case "list" -> System.out.println("... listing tasks ...");
            case "exit" -> System.out.println("... bye ...");
            default -> System.out.println(" - unknown command - ");
        }
    }
}
