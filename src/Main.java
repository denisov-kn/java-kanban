import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;
import java.util.ArrayList;
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
        Epic epic2 = new Epic("Эпик 2", "Описание 6");

        taskManager.create(task);
        taskManager.create(task2);
        taskManager.create(epic1);
        taskManager.create(epic2);

        SubTask subTask1 = new SubTask("Сабтаск1", "Описание 4", Status.NEW,epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск2", "Описание 5", Status.NEW,epic1.getId());
        SubTask subTask3 = new SubTask("Сабтаск3", "Описание 7", Status.NEW,epic2.getId());



        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.create(subTask3);

        print(taskManager.getTaskList());
        print(taskManager.getSubTask());
        print(taskManager.getEpicList());

        System.out.println(" ");
        System.out.println("Проверяем  обновление задач: замену задач, обновление статусов у сабтасков и эпика ");



        Task task3 = taskManager.getTask(task.getId());
        task3.setStatus(Status.IN_PROGRESS);



        SubTask subTask4 = taskManager.getSubTask(subTask1.getId());
        subTask4.setSummary("Сабтаск5");
        subTask4.setDescription("Описание p9");
        subTask4.setStatus(Status.IN_PROGRESS);


        SubTask subTask5 = taskManager.getSubTask(subTask2.getId());
        subTask5.setSummary("Сабтаск56");
        subTask5.setDescription("Описание uu");
        subTask5.setStatus(Status.DONE);

        SubTask subTask6 = taskManager.getSubTask(subTask3.getId());
        subTask6.setSummary("Сабтаск7879");
        subTask6.setDescription("Описание ii");
        subTask6.setStatus(Status.DONE);


        taskManager.updateTask(task3);
        taskManager.updateSubTask(subTask4);
        taskManager.updateSubTask(subTask5);
        taskManager.updateSubTask(subTask6);


        print(taskManager.getTaskList());
        print(taskManager.getSubTask());
        print(taskManager.getEpicList());
        System.out.println(" ");
        System.out.println("Проверяем удаление задач: эпик, сабтаски");

        taskManager.removeEpic(epic1.getId());
        taskManager.removeSubTask(subTask6.getId());

        print(taskManager.getTaskList());
        print(taskManager.getSubTask());
        print(taskManager.getEpicList());

    }
     // Печать задач
    static void print (ArrayList arrayList) {

        for (Object obj : arrayList) {
            System.out.println(obj);
        }
    }

}
