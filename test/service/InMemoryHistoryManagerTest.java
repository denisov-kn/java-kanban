package service;


import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;
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
    @DisplayName("не должен перезаписывать предыдущие задачи ")
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
    @DisplayName("не должно быть дублей при просмотре задач")
    public void shouldRemoveDoubleViewTask() {

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 1", Status.NEW);
        task1.setId(0);
        task2.setId(1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task2);

        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(2, history.size(), "В истории должно быть только две задачи");
    }

    @Test
    @DisplayName("должен удалять задачи из истории при их удалении")
    public void shouRemoveNotReplacePreviousTask() {

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 1", Status.NEW);
        task1.setId(0);
        task2.setId(1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task2);

        List<Task> history = inMemoryHistoryManager.getHistory();

        Assertions.assertEquals(2, history.size(), "В истории должно быть только две задачи");
    }




}