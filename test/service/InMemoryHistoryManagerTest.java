package service;


import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    private static InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeAll
    public static void BeforeAll() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAcceptAnyTypeOfTasks(){
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        Epic epic = new Epic("Эпик 1", "Описание 2");
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, 1);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subTask);
        ArrayList<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(3, history.size());

    }

    @Test
    public void shouldRemoveFirstTaskIfWeAdd11(){
        Epic firstEpic = new Epic("Эпик 0", "Описание 2");
        inMemoryHistoryManager.add(firstEpic);
        for (int i = 1; i <15; i++) {
            inMemoryHistoryManager.add(new Epic("Эпик " + i, "Описание"));
        }
        Epic secondEpic =(Epic) inMemoryHistoryManager.getHistory().get(1);
        Epic lastEpic = new Epic("Эпик 10", "Описание 2");
        inMemoryHistoryManager.add(lastEpic);
        ArrayList<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(secondEpic, history.getFirst());
        Assertions.assertEquals(lastEpic, history.getLast());
        Assertions.assertEquals(10,history.size());

    }

    @Test
    public void shouldSavePreviousTask(){
        Epic epic1 = new Epic("Эпик 0", "Описание 2");
        Epic epic2 = new Epic("Эпик 1", "Описание 3");
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(epic2);
        ArrayList<Task> history = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(epic1, history.get(0));
        Assertions.assertEquals(epic2, history.get(1));
        Assertions.assertEquals(2,history.size());

    }

}