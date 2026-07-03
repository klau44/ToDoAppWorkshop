package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    private static final String FILE_NAME = "tasks.csv";
    private static final String[] COMMANDS = {"add", "remove", "list", "exit"};
    private static String[][] tasks;

    public static void main(String[] args) {
        showCommands();
        try {
            tasks = getDataFromFile(FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error with reading data from a file");
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String userCommand = scanner.nextLine();
            switch (userCommand) {
                case "add" -> System.out.println("... adding task ...");
                case "remove" -> {
                    System.out.println("... deleting task ...");
                    System.out.println("Please select number to remove");
                    String taskNum = scanner.nextLine();
                    removeTask(Integer.parseInt(taskNum));
                    showCommands();
                }
                case "list" -> {
                    System.out.println("... listing tasks ...");
                    listTasks();
                    showCommands();
                }
                case "exit" -> {
                    System.out.println(ConsoleColors.RED + "... bye ...");
                    // todo: save tasks from array to file
                    System.exit(0);
                }
                default -> System.out.println(" - unknown command - ");
            }
        }
    }

    private static void showCommands() {
        System.out.println(ConsoleColors.BLUE + " Please select an option: ");
        for (String command : COMMANDS) {
            System.out.println(ConsoleColors.RESET + command);
        }
    }

    private static String[][] getDataFromFile(String fileName) throws IOException {
        Path path = Paths.get("tasks.csv");
        List<String> tasksList = Files.readAllLines(path);
        String[][] tasks = new String[tasksList.size()][3];

        for (int i = 0; i < tasksList.size(); i++) {
            String[] partsOfTasks = tasksList.get(i).split(", ");
            tasks[i][0] = partsOfTasks[0];
            tasks[i][1] = partsOfTasks[1];
            tasks[i][2] = partsOfTasks[2];
        }
        return tasks;
    }

    private static void listTasks() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + 1 + ": ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void removeTask(int taskNum) {
        tasks = ArrayUtils.remove(tasks, taskNum - 1);
    }
}