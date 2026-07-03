package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    private static final String FILE_NAME = "tasks.csv";
    private static final String FILE_NAME_TO_SAVE = "tasks2.csv";
    private static final String[] COMMANDS = {"add", "remove", "list", "exit"};
    private static String[][] tasks;

    public static void main(String[] args) {
        loadData();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            showCommands();
            String userCommand = scanner.nextLine();
            switch (userCommand) {
                case "add" -> {
                    System.out.println("... adding task ...");
                    addTask(scanner);
                }
                case "remove" -> {
                    System.out.println("... deleting task ...");
                    System.out.println("Please select number to remove");
                    String taskNum = scanner.nextLine();
                    removeTask(taskNum, scanner);
                }
                case "list" -> {
                    System.out.println("... listing tasks ...");
                    listTasks();
                }
                case "exit" -> {
                    saveTasksToFile(FILE_NAME_TO_SAVE);
                    System.out.println(ConsoleColors.RED + "... bye ...");
                    System.exit(0);
                }
                default -> System.out.println(" - unknown command, please provide the correct one - ");
            }
        }
    }

    private static void loadData() {
        List<String> tasksList = new ArrayList<>();
        try {
            Path path = Paths.get(FILE_NAME);
            tasksList = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Cannot read the file.");
        }
        putDataToTable(tasksList);
    }

    private static void putDataToTable(List<String> tasksList) {
        tasks = new String[tasksList.size()][3];
        for (int i = 0; i < tasksList.size(); i++) {
            String[] taskData = tasksList.get(i).split(", ");
            tasks[i] = taskData;
        }
    }

    private static void showCommands() {
        System.out.println(ConsoleColors.BLUE + " Please select an option: ");
        for (String command : COMMANDS) {
            System.out.println(ConsoleColors.RESET + command);
        }
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

    private static void removeTask(String taskNum, Scanner scanner) {
        while (!NumberUtils.isParsable(taskNum) ||
                Integer.parseInt(taskNum) > tasks.length ||
                Integer.parseInt(taskNum) < 1) {
            System.out.println("Provide correct format. Choose number from 1 to " + tasks.length);
            taskNum = scanner.nextLine();
        }
        int index = Integer.parseInt(taskNum) - 1;
        tasks = ArrayUtils.remove(tasks, index);
        System.out.println("... great, task " + taskNum + " has been deleted ...");
    }

    private static void addTask(Scanner scanner) {
        String taskDescription = readTaskDescription(scanner);
        String taskDueDate = readTaskDueDate(scanner);
        String taskImportant = readTaskImportant(scanner);

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[]{taskDescription, taskDueDate, taskImportant};
        System.out.println("... new task has been added ...");
    }

    private static String readTaskDescription(Scanner scanner) {
        System.out.println("Provide task description (don't use delimiters):");
        String taskDescription = scanner.nextLine();
        while (taskDescription.contains(",")) {
            System.out.println("Don't use delimiter! Provide proper task description:");
            taskDescription = scanner.nextLine();
        }
        return taskDescription;
    }

    private static String readTaskDueDate(Scanner scanner) {
        System.out.println("Provide task due date. Required format: YYY-MM-DD");
        String taskDueDate = scanner.nextLine();
        while (!isDateValid(taskDueDate)) {
            System.out.println("Use correct date format: YYYY-MM-DD");
            taskDueDate = scanner.nextLine();
        }
        return taskDueDate;
    }

    private static String readTaskImportant(Scanner scanner) {
        System.out.println("Is this task important? Provide: true/false");
        String taskImportant = scanner.nextLine();
        while (!"true".equals(taskImportant) && !"false".equals(taskImportant)) {
            System.out.println("Provide 'true' or 'false' only");
            taskImportant = scanner.nextLine();
        }
        return taskImportant;
    }

    private static boolean isDateValid(String date) {
        return date != null && date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    private static void saveTasksToFile(String fileName) {
        Path path = Paths.get(fileName);
        try {
            Files.write(path, convert2DTableToList());
            System.out.println("... tasks were saved to file " + fileName + " ...");
        } catch (IOException ex) {
            System.out.println("Error with saving file: " + fileName);
        }
    }

    private static List<String> convert2DTableToList() {
        List<String> tasksList = new ArrayList<>();
        for (int i = 0; i < tasks.length; i++) {
            String task = String.join(", ", tasks[i]);
            tasksList.add(task);
        }
        return tasksList;
    }
}