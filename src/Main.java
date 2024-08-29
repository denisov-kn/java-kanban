import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        // Добавляем первоначальный набор задач

        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Описание 3");
        SubTask subTask1 = new SubTask("Сабтаск1", "Описание 4", Status.NEW);
        SubTask subTask2 = new SubTask("Сабтаск2", "Описание 5", Status.NEW);
        Epic epic2 = new Epic("Эпик 2", "Описание 6");
        SubTask subTask3 = new SubTask("Сабтаск3", "Описание 7", Status.NEW);

        taskManager.create(task);
        taskManager.create(task2);
        taskManager.create(epic1);
        taskManager.create(epic2);

        taskManager.create(subTask1, epic1.getId());
        taskManager.create(subTask2, epic1.getId());
        taskManager.create(subTask3, epic2.getId());

        print(taskManager.getTaskList());
        print(taskManager.getSubTask());
        print(taskManager.getEpicList());

        System.out.println("Проверяем  обновление задач: замену задач, обновление статусов у сабтасков и эпика ");


        Task task3 = new Task("Задача 1", "Описание 1", Status.IN_PROGRESS);
        SubTask subTask4 = new SubTask("Сабтаск5", "Описание p9", Status.IN_PROGRESS);
        SubTask subTask5 = new SubTask("Сабтаск56", "Описание uu", Status.DONE);
        SubTask subTask6 = new SubTask("Сабтаск7879", "Описание iii", Status.DONE);

        taskManager.updateTask(task.getId(),task3);
        taskManager.updateSubTask(subTask1.getId(),subTask4);
        taskManager.updateSubTask(subTask2.getId(),subTask5);
        taskManager.updateSubTask(subTask3.getId(),subTask6);


        print(taskManager.getTaskList());
        print(taskManager.getSubTask());
        print(taskManager.getEpicList());
        System.out.println("Проверяем удаление задач: эпик, сабтаски");

        taskManager.removeEpic(epic1.getId());
        taskManager.removeSubTask(subTask6.getId());

        print(taskManager.getTaskList());
        print(taskManager.getSubTask());
        print(taskManager.getEpicList());

    }
     // Печать задач
    static void print (HashMap hashMap) {
        for (Object obj : hashMap.entrySet()) {
            System.out.println(obj);
        }
    }

}
