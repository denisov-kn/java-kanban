package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static utils.Equals.assertEqualsTask;

@DisplayName("Менеджер задач")
class InMemoryTaskManagerTest {

    private static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    public static void BeforeAll(){
        inMemoryTaskManager = new InMemoryTaskManager();
    }


    @Test
    @DisplayName("должен возвращать верную задачу по id")
    public void shouldTwoTaskEqualByID() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        inMemoryTaskManager.create(task);
        int id1 = task.getId();

        Task task2 = inMemoryTaskManager.getTask(id1);

        assertEqualsTask(task, task2, "Задачи должны быть одинаковы");
    }

    @Test
    @DisplayName("должен возвращать верный эпик по id")
    public void shouldTwoEpicEqualByID() {
        Epic epic = new Epic("Эпик 1", "Описание 2");
        inMemoryTaskManager.create(epic);

        int id1 = epic.getId();
        Epic epic2 = inMemoryTaskManager.getEpic(id1);

        assertEqualsTask(epic, epic2, "Эпики должны быть одинаковы");
    }

    @Test
    @DisplayName("должен возвращать верный сабтаск по id")
    public void shouldTwoSubTaskEqualByID() {
        Epic epic = new Epic("Эпик 1", "Описание 2");
        inMemoryTaskManager.create(epic);
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, epic.getId());
        inMemoryTaskManager.create(subTask);

        int id1 = subTask.getId();
        SubTask subTask2 = inMemoryTaskManager.getSubTask(id1);

        assertEqualsTask(subTask, subTask2, "Сабтаски должны быть одинаковы");
    }


    @Test
    @DisplayName("должен верно добавлять сабтаск")
    public void shouldAddSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание 2");
        inMemoryTaskManager.create(epic);
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, epic.getId());

        inMemoryTaskManager.create(subTask);

        assertEqualsTask(subTask, inMemoryTaskManager.getSubTask(subTask.getId()),
                "Сабтаск должен быть верно добавлен");
    }

    @Test
    @DisplayName("должен верно добавлять эпик")
    public void shouldAddEpic() {
        Epic epic = new Epic("Эпик 1", "Описание 2");

        inMemoryTaskManager.create(epic);

        assertEqualsTask(epic, inMemoryTaskManager.getEpic(epic.getId()),
                "Эпик должен быть верно добавлен");
    }


    @Test
    @DisplayName("должен верно обновлять задачу")
    public  void shouldTaskUpdate() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        inMemoryTaskManager.create(task1);
        int idTask1 = task1.getId();
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        task2.setId(idTask1);

        inMemoryTaskManager.updateTask(task2);
        Assertions.assertEquals(idTask1, task2.getId());
        Assertions.assertEquals("Задача 2",inMemoryTaskManager.getTask(idTask1).getSummary());
        Assertions.assertEquals("Описание 2",inMemoryTaskManager.getTask(idTask1).getDescription());
        Assertions.assertEquals(Status.NEW,inMemoryTaskManager.getTask(idTask1).getStatus());
    }

    @Test
    @DisplayName("должен верно обновлять эпик")
    public  void shouldEpicUpdate() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic1);
        int idEpic1 = epic1.getId();
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        epic2.setId(idEpic1);

        inMemoryTaskManager.updateEpic(epic2);

        Assertions.assertEquals(idEpic1, epic2.getId());
        Assertions.assertEquals("Эпик 2",inMemoryTaskManager.getEpic(idEpic1).getSummary());
        Assertions.assertEquals("Описание 2",inMemoryTaskManager.getEpic(idEpic1).getDescription());
        Assertions.assertEquals(Status.NEW,inMemoryTaskManager.getEpic(idEpic1).getStatus());
    }

    @Test
    @DisplayName("должен верно обновлять сабтаски")
    public  void shouldSubTaskUpdate() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, epic.getId());
        inMemoryTaskManager.create(subTask1);
        int idSubTask1 = subTask1.getId();
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.DONE, epic.getId());
        subTask2.setId(idSubTask1);

        inMemoryTaskManager.updateSubTask(subTask2);

        Assertions.assertEquals(idSubTask1, subTask2.getId());
        Assertions.assertEquals("Сабтаск 2",inMemoryTaskManager.getSubTask(idSubTask1).getSummary());
        Assertions.assertEquals("Описание 2",inMemoryTaskManager.getSubTask(idSubTask1).getDescription());
        Assertions.assertEquals(Status.DONE,inMemoryTaskManager.getSubTask(idSubTask1).getStatus());
    }

    @Test
    public void shouldRemoveTask (){
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        inMemoryTaskManager.create(task);
        int taskId = task.getId();

        inMemoryTaskManager.removeTask(taskId);

        Assertions.assertNull(inMemoryTaskManager.getTask(taskId), "Задача должна быть удалена");
    }

    @Test
    public void shouldRemoveEpicAndSubTasksOfEpic(){
        Epic epic = new Epic("Эпик 1", "Описание 2");
        inMemoryTaskManager.create(epic);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic.getId());
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.DONE, epic.getId());
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание 3", Status.DONE, epic.getId());
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);
        inMemoryTaskManager.create(subTask3);

        inMemoryTaskManager.removeEpic(epic.getId());

        Assertions.assertNull(inMemoryTaskManager.getEpic(epic.getId()), "Эпик должен быть удален");
        Assertions.assertNull(inMemoryTaskManager.getSubTask(subTask1.getId()), "Сабтаск должен быть удален");
        Assertions.assertNull(inMemoryTaskManager.getSubTask(subTask2.getId()), "Сабтаск должен быть удален");
        Assertions.assertNull(inMemoryTaskManager.getSubTask(subTask3.getId()), "Сабтаск должен быть удален");
    }

    @Test
    public void shouldRemoveSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание 2");
        inMemoryTaskManager.create(epic);
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic.getId());
        inMemoryTaskManager.create(subTask);

        inMemoryTaskManager.removeSubTask(subTask.getId());

        Assertions.assertNull(inMemoryTaskManager.getSubTask(subTask.getId()), "Сабтаска должна быть удалена");
    }

    @Test
    public void shouldRemoveAllTask() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);

        inMemoryTaskManager.removeAllTask();

        Assertions.assertEquals(0, inMemoryTaskManager.getTaskList().size(),
                "Все задачи должны быть удалены");
    }


    @Test
    public void shouldRemoveAllEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        inMemoryTaskManager.create(epic1);
        inMemoryTaskManager.create(epic2);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание 3", Status.NEW, epic2.getId());
        SubTask subTask4 = new SubTask("Сабтаск 4", "Описание 4", Status.DONE, epic2.getId());
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);
        inMemoryTaskManager.create(subTask3);
        inMemoryTaskManager.create(subTask4);

        inMemoryTaskManager.removeAllEpic();

        Assertions.assertEquals(0, inMemoryTaskManager.getEpicList().size(),
                "Все эпики должны быть удалены");
        Assertions.assertEquals(0, inMemoryTaskManager.getSubTask().size(),
                "Все сабтаски должны быть удалены");
    }

    @Test
    public void shouldRemoveAllSubTask() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic1);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.IN_PROGRESS, epic1.getId());
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);

        inMemoryTaskManager.removeAllSubTask();

        Assertions.assertEquals(0, inMemoryTaskManager.getSubTask().size(),
                "Все сабтаски должны быть удалены");

    }

    @Test
    public void shouldGetSubTaskByEpicId () {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic1);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.IN_PROGRESS, epic1.getId());
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание 3", Status.IN_PROGRESS, epic1.getId());
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);
        inMemoryTaskManager.create(subTask3);

        List<SubTask> subTaskList = inMemoryTaskManager.getSubTasksByEpic(epic1.getId());

        Assertions.assertEquals(3, subTaskList.size(),
                "Cписок сабтасков должен быть состоять из 3-ех элементов");
        assertEqualsTask(subTask1, subTaskList.get(0), "Должен возвращаться верный сабтаск");
        assertEqualsTask(subTask2, subTaskList.get(1), "Должен возвращаться верный сабтаск");
        assertEqualsTask(subTask3, subTaskList.get(2), "Должен возвращаться верный сабтаск");
    }

    private static class EmptyHistoryManager implements HistoryManager{
        @Override
        public <T extends Task> void add(T task) {
        }

        @Override
        public List<Task> getHistory() {
            return new ArrayList<>();
        }

        @Override
        public void remove(int id) {

        }
    }







}