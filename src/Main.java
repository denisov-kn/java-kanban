import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager inMemoryTaskManager = Managers.getDefaultManager();
        HistoryManager inMemoryHistoryManager = inMemoryTaskManager.getHistoryManager();


        // Добавляем первоначальный набор задач

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Описание 3");
        Epic epic2 = new Epic("Эпик 2", "Описание 6");

        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);
        inMemoryTaskManager.create(epic1);
        inMemoryTaskManager.create(epic2);

        SubTask subTask1 = new SubTask("Сабтаск1", "Описание 4", Status.NEW,epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск2", "Описание 5", Status.NEW,epic1.getId());
        SubTask subTask3 = new SubTask("Сабтаск3", "Описание 7", Status.NEW,epic1.getId());

        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);
        inMemoryTaskManager.create(subTask3);

        print(inMemoryTaskManager.getTaskList());
        print(inMemoryTaskManager.getSubTask());
        print(inMemoryTaskManager.getEpicList());

        System.out.println(" ");
        System.out.println("История просмотра: ");

        print(inMemoryHistoryManager.getHistory());

        System.out.println(" ");
        System.out.println("Проверяем  запрос задач ");

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getSubTask(subTask1.getId());
        inMemoryTaskManager.getSubTask(subTask1.getId());
        inMemoryTaskManager.getSubTask(subTask2.getId());
        inMemoryTaskManager.getSubTask(subTask3.getId());
        inMemoryTaskManager.getSubTask(subTask2.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getEpic(epic2.getId());
        inMemoryTaskManager.getEpic(epic1.getId());
        inMemoryTaskManager.getEpic(epic1.getId());
        inMemoryTaskManager.getEpic(epic2.getId());

        System.out.println(" ");
        System.out.println("История просмотра");
        print(inMemoryHistoryManager.getHistory());

        System.out.println(" ");
        System.out.println("Удаляем задачу 1");

        inMemoryTaskManager.removeTask(task1.getId());

        System.out.println(" ");
        System.out.println("История просмотра");
        print(inMemoryHistoryManager.getHistory());


        System.out.println(" ");
        System.out.println("Удаляем эпик вместе с сабтасками");

        inMemoryTaskManager.removeEpic(epic1.getId());

        System.out.println(" ");
        System.out.println("История просмотра");
        print(inMemoryHistoryManager.getHistory());


    }
     // Печать задач
    static void print (List arrayList) {

        for (Object obj : arrayList) {
            System.out.println(obj);
        }
    }

}
