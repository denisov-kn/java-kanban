import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");



        InMemoryTaskManager inMemoryTaskManager = Managers.getDefaultManager();


        // Добавляем первоначальный набор задач

        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Описание 3");
        Epic epic2 = new Epic("Эпик 2", "Описание 6");

        inMemoryTaskManager.create(task);
        inMemoryTaskManager.create(task2);
        inMemoryTaskManager.create(epic1);
        inMemoryTaskManager.create(epic2);

        SubTask subTask1 = new SubTask("Сабтаск1", "Описание 4", Status.NEW,epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск2", "Описание 5", Status.NEW,epic1.getId());
        SubTask subTask3 = new SubTask("Сабтаск3", "Описание 7", Status.NEW,epic2.getId());



        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);
        inMemoryTaskManager.create(subTask3);

        print(inMemoryTaskManager.getTaskList());
        print(inMemoryTaskManager.getSubTask());
        print(inMemoryTaskManager.getEpicList());
        System.out.println(" ");
        System.out.println("История просмотра: ");

        System.out.println(" ");
        System.out.println("Проверяем  обновление задач: замену задач, обновление статусов у сабтасков и эпика ");



        Task task3 = inMemoryTaskManager.getTask(task.getId());
        task3.setStatus(Status.IN_PROGRESS);

        SubTask subTask4 = inMemoryTaskManager.getSubTask(subTask1.getId());
        subTask4.setSummary("Сабтаск5");
        subTask4.setDescription("Описание p9");
        subTask4.setStatus(Status.IN_PROGRESS);


        SubTask subTask5 = inMemoryTaskManager.getSubTask(subTask2.getId());
        subTask5.setSummary("Сабтаск56");
        subTask5.setDescription("Описание uu");
        subTask5.setStatus(Status.DONE);

        SubTask subTask6 = inMemoryTaskManager.getSubTask(subTask3.getId());
        subTask6.setSummary("Сабтаск7879");
        subTask6.setDescription("Описание ii");
        subTask6.setStatus(Status.DONE);

/*
        inMemoryTaskManager.updateTask(task3);
        inMemoryTaskManager.updateSubTask(subTask4);
        inMemoryTaskManager.updateSubTask(subTask5);
        inMemoryTaskManager.updateSubTask(subTask6);
*/

        print(inMemoryTaskManager.getTaskList());
        print(inMemoryTaskManager.getSubTask());
        print(inMemoryTaskManager.getEpicList());
        System.out.println(" ");
        System.out.println("История просмотра: ");
        print(inMemoryTaskManager.getHistoryManager().getHistory());

        System.out.println(" ");
        System.out.println("Проверяем удаление задач: эпик, сабтаски");

        inMemoryTaskManager.removeEpic(epic1.getId());
        inMemoryTaskManager.removeSubTask(subTask6.getId());

        print(inMemoryTaskManager.getTaskList());
        print(inMemoryTaskManager.getSubTask());
        print(inMemoryTaskManager.getEpicList());
        System.out.println(" ");
        System.out.println("История просмотра: ");
        print(inMemoryTaskManager.getHistoryManager().getHistory());


    }
     // Печать задач
    static void print (ArrayList arrayList) {

        for (Object obj : arrayList) {
            System.out.println(obj);
        }
    }

}
