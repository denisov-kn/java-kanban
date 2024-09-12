package service;


import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

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
        Epic epic = new Epic("Эпик 1", "Описание 2");
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, 1);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subTask);

        ArrayList<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(3, history.size(), "В историю должны добавляться все задачи");

    }

    @Test
    @DisplayName("должен корректно смещать элементы при более чем 10-ти задач")
    public void shouldRemoveFirstTaskIfWeAdd11(){
        Task firstTask = new Task("Задача 1", "Описание 1", Status.NEW);
        firstTask.setId(0);
        inMemoryHistoryManager.add(firstTask);
        for (int i = 1; i <15; i++) {
            Task task = new Task("Задача" + i, "Описание 1", Status.NEW);
            task.setId(i);
            inMemoryHistoryManager.add(task);
        }
        Task secondTask = inMemoryHistoryManager.getHistory().get(1);
        Task lastTask = new Epic("Эпик 10", "Описание 2");
        inMemoryHistoryManager.add(lastTask);
        lastTask.setId(10);

        ArrayList<Task> history = inMemoryHistoryManager.getHistory();

        assertEqualsTask(secondTask, history.getFirst(), "История должна сохранять только 10 последних задач");
        assertEqualsTask(lastTask, history.getLast(), "История должна сохранять только 10 последних задач");
        Assertions.assertEquals(10,history.size());

    }

    @Test
    @DisplayName("не должен перезаписывать предыдущие задачи ")
    public void shouldNotReplacePreviousTask(){
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 1", Status.NEW);
        task1.setId(0);
        task2.setId(1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);

        ArrayList<Task> history = inMemoryHistoryManager.getHistory();

        assertEqualsTask(task1, history.get(0),"Не должна перезаписываться предыдущая задача");
        assertEqualsTask(task2, history.get(1), "Не должна перезаписываться предыдущая задача");
        Assertions.assertEquals(2,history.size(), "Должны добавить две задачи");

    }


}