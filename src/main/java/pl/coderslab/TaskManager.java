package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
                    saveTasksToFile("tasks2.csv");
                    System.out.println(ConsoleColors.RED + "... bye ...");
                    System.exit(0);
                }
                default -> System.out.println(" - unknown command - ");
            }
            showCommands();
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

    private static void removeTask(String taskNumStr, Scanner scanner) {
        while (!NumberUtils.isParsable(taskNumStr) ||
                Integer.parseInt(taskNumStr) > tasks.length ||
                Integer.parseInt(taskNumStr) < 1) {
                System.out.println("Provide correct format. Choose number from 1 to " + tasks.length);
            taskNumStr = scanner.nextLine();
        }
        int taskNum = Integer.parseInt(taskNumStr);
        tasks = ArrayUtils.remove(tasks, taskNum - 1);
        System.out.println("... great, task " + taskNum + " has been deleted ...");
    }

    private static void addTask(Scanner scanner) {
        System.out.println("Provide task description:");
        String taskDescription = scanner.nextLine();
        System.out.println("Provide task due date:");
        String taskDueDate = scanner.nextLine();
        System.out.println("Is this task important: true/false");
        String taskImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[]{taskDescription,taskDueDate, taskImportant};
        System.out.println("... new task has been added ...");
    }

    private static void saveTasksToFile(String fileName) {
        Path path = Paths.get(fileName);
        List<String> outList = convert2DTableToList();
        try {
            Files.write(path, outList);
            System.out.println("... tasks were saved to file " + fileName + " ...");
        } catch (IOException ex) {
            System.out.println("Nie można zapisać pliku.");
        }
    }

    private static List<String> convert2DTableToList() {
        List<String> tasksList = new ArrayList<>();
        for (int i = 0; i < tasks.length; i++) {
            String task = StringUtils.join(tasks[i], ", ");
            tasksList.add(task);
        }
        return tasksList;
    }
}