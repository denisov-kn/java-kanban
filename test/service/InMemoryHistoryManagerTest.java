package service;


import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.history.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;

import static utils.Equals.assertEqualsTask;

@DisplayName("Менеджер истории")
class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager inMemoryHistoryManager;


    @BeforeEach
    public void BeforeEach() {

        inMemoryHistoryManager = new InMemoryHistoryManager();


    }

    @Test
    @DisplayName("должен принимать все типы задач")
    public void shouldAcceptAnyTypeOfTasks(){
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        task.setId(0);
        Epic epic = new Epic("Эпик 1", "Описание 2");
        epic.setId(1);
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, 1);
        subTask.setId(2);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subTask);

        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(3, history.size(), "В историю должны добавляться все задачи");

    }

    @Test
    @DisplayName("должен корректно работать при более чем 20-ти задачах")
    public void shouldAddMoreThen20Task(){


        for (int i = 0; i < 21; i++) {
            Task task = new Task("Задача" + i, "Описание 1", Status.NEW);
            task.setId(i);
            inMemoryHistoryManager.add(task);
        }

        List<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(21,history.size(), "В истории должно быть 21 задача");

    }

    @Test
    @DisplayName("не должен перезаписывать предыдущие задачи")
    public void shouldNotReplacePreviousTask(){
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 1", Status.NEW);
        task1.setId(0);
        task2.setId(1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);

        List<Task> history = inMemoryHistoryManager.getHistory();

        assertEqualsTask(task1, history.get(0),"Не должна перезаписываться предыдущая задача");
        assertEqualsTask(task2, history.get(1), "Не должна перезаписываться предыдущая задача");
        Assertions.assertEquals(2,history.size(), "Должны добавить две задачи");

    }


    @Test
    @DisplayName("должен удалять задачу из истории, даже если она была первая при просмотре")
    public void shouldRemoveFirstTaskFromHistory() {

        List <Task> taskList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Task task = new Task("Задача" + i, "Описание 1", Status.NEW);
            task.setId(i);
            inMemoryHistoryManager.add(task);
            taskList.add(task);
        }

        inMemoryHistoryManager.add(taskList.get(0));

        List<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(5, history.size(), "В истории должно быть только 5 задач");
        Assertions.assertEquals(taskList.get(1), history.get(0),"В начале истории должно быть задача 1");
        Assertions.assertEquals(taskList.get(0), history.get(4),"В конце истории должно быть задача 0");
    }


    @Test
    @DisplayName("должен удалять задачу из истории, даже если она была в середине при просмотре")
    public void shouldRemoveMiddleTaskFromHistory() {

        List <Task> taskList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Task task = new Task("Задача" + i, "Описание 1", Status.NEW);
            task.setId(i);
            inMemoryHistoryManager.add(task);
            taskList.add(task);
        }

        inMemoryHistoryManager.add(taskList.get(2));

        List<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(5, history.size(), "В истории должно быть только 5 задач");
        Assertions.assertEquals(taskList.get(3), history.get(2),"В середине истории должно быть задача 3");
        Assertions.assertEquals(taskList.get(2), history.get(4),"В конце истории должно быть задача 2");
    }

    @Test
    @DisplayName("должен удалять задачу из истории, даже если она была в конце при просмотре")
    public void shouldRemoveLastTaskFromHistory() {

        List <Task> taskList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Task task = new Task("Задача" + i, "Описание 1", Status.NEW);
            task.setId(i);
            inMemoryHistoryManager.add(task);
            taskList.add(task);
        }

        inMemoryHistoryManager.add(taskList.get(4));

        List<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(5, history.size(), "В истории должно быть только 5 задач");
        Assertions.assertEquals(taskList.get(4), history.get(4),"В конце истории должно быть задача 4");
    }


    @Test
    @DisplayName("должен удалять дубликаты, даже если в истории только одна задача")
    public void shouldRemoveIfHistoryContainsOneTask(){
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task1.setId(0);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);

        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(1,history.size(), "В истории должна быть одна задача");

    }

    @Test
    @DisplayName("должен удалять дубликаты, даже если в истории только две задачи")
    public void shouldRemoveIfHistoryContainsTwoTask(){
        Task task0 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task1 = new Task("Задача 2", "Описание 1", Status.NEW);
        task0.setId(0);
        task1.setId(1);
        inMemoryHistoryManager.add(task0);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task0);

        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(2,history.size(), "В истории должна быть две задачи");
        Assertions.assertEquals(task1, history.get(0),"В начале истории должно быть задача 1");
        Assertions.assertEquals(task0, history.get(1),"В конце истории должно быть задача 0");

    }
}