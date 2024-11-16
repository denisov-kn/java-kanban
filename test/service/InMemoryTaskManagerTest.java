package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static utils.Equals.assertEqualsTask;

@DisplayName("Менеджер задач")
class InMemoryTaskManagerTest {

    private static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void BeforeEach(){
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
    @DisplayName("должен удалять задачу")
    public void shouldRemoveTask (){
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        inMemoryTaskManager.create(task);
        int taskId = task.getId();

        inMemoryTaskManager.removeTask(taskId);

        Assertions.assertNull(inMemoryTaskManager.getTask(taskId), "Задача должна быть удалена");
    }

    @Test
    @DisplayName("должен удалять эпик и его сабтаски")
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
    @DisplayName("должен удалять сабтаску")
    public void shouldRemoveSubTask(){
        Epic epic = new Epic("Эпик 1", "Описание 2");
        inMemoryTaskManager.create(epic);
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic.getId());
        inMemoryTaskManager.create(subTask);

        inMemoryTaskManager.removeSubTask(subTask.getId());

        SubTask currentSubTask = inMemoryTaskManager.getSubTask(subTask.getId());

        Assertions.assertNull(currentSubTask, "Сабтаска должна быть удалена");
    }

    @Test
    @DisplayName("должен удалять все задачи")
    public void shouldRemoveAllTask(){
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);

        inMemoryTaskManager.removeAllTask();

        Assertions.assertEquals(0, inMemoryTaskManager.getTaskList().size(),
                "Все задачи должны быть удалены");
    }


    @Test
    @DisplayName("должен удалять все эпики")
    public void shouldRemoveAllEpic(){
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
        Assertions.assertEquals(0, inMemoryTaskManager.getSubTaskList().size(),
                "Все сабтаски должны быть удалены");
    }

    @Test
    @DisplayName("должен удалять все сабтаски")
    public void shouldRemoveAllSubTask(){
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic1);
        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.DONE, epic1.getId());
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.IN_PROGRESS, epic1.getId());
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);

        inMemoryTaskManager.removeAllSubTask();

        Assertions.assertEquals(0, inMemoryTaskManager.getSubTaskList().size(),
                "Все сабтаски должны быть удалены");

    }

    @Test
    @DisplayName("должен возвращать все сабтаски у эпика")
    public void shouldGetSubTaskByEpicId(){
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
    @Test
    @DisplayName("Должен добавлять задачи если время задач разнесено")
    public void shouldCreateIfTimePeriodsOfTasksAreNotIntersect(){
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);

        Assertions.assertDoesNotThrow(() -> {
            inMemoryTaskManager.create(task1);
            inMemoryTaskManager.create(task2);
            inMemoryTaskManager.create(task3);
        },"Все задачи должны добавляться менеджером");

    }

    @Test
    @DisplayName("Не должен добавлять задачи если есть пересечение по времени у задач")
    public void shouldNotCreateIfTimePeriodsOfTasksAreIntersect(){
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 3,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 4,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,61L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);


        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);

        Assertions.assertThrows(ValidationException.class,() -> {
            inMemoryTaskManager.create(task3);
        },"Задача 3 не должна добавляться, т.к имеет пересечение по левому краю");


        dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        task3.setDuration(61L);
        task3.setStartTime(dateTimeTask3);
        Assertions.assertThrows(ValidationException.class,() -> {
            inMemoryTaskManager.create(task3);
        },"Задача 3 не должна добавляться, т.к имеет пересечение по правому краю");

    }

    @Test
    @DisplayName("менеджер должен возвращать задачи и сабтаски правильно приоритезированы " +
            "по времени выполнения при создании новых и при их удалении")
    public void shouldGetPrioritizedTasksIfCreateNew(){

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 5,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 4,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,10L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,5L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,15L, dateTimeTask3);

        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic1);
        Integer epic1Id = epic1.getId();

        LocalDateTime dateTimeSubTask1 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeSubTask2 = LocalDateTime.of(2024,1, 1, 3,0,0);
        LocalDateTime dateTimeSubTask3 = LocalDateTime.of(2024,1, 1, 6,0,0);
        SubTask subTask1 = new SubTask("Сабтаск 1",
                "Описание 4",
                Status.NEW,
                epic1Id,
                20L,
                dateTimeSubTask1);
        SubTask subTask2 = new SubTask("Сабтаск 2",
                "Описание 5",
                Status.NEW,
                epic1Id,
                30L,
                dateTimeSubTask2);
        SubTask subTask3 = new SubTask("Сабтаск 3",
                "Описание 6",
                Status.NEW,
                epic1Id,
                45L,
                dateTimeSubTask3);

        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);
        inMemoryTaskManager.create(task3);
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);
        inMemoryTaskManager.create(subTask3);
        List<Task> prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();

        Assertions.assertEquals(subTask1,prioritizedTasks.get(0), "Сабтаск1 должен быть на 1 месте");
        Assertions.assertEquals(task3, prioritizedTasks.get(1), "Задача3 должна быть на 2 месте");
        Assertions.assertEquals(subTask2, prioritizedTasks.get(2), "Сабтаск2 должен быть на 3 месте");
        Assertions.assertEquals(task2, prioritizedTasks.get(3), "Задача2 должна быть на 4 месте");
        Assertions.assertEquals(task1, prioritizedTasks.get(4), "Задача1 должна быть на 5 месте");
        Assertions.assertEquals(subTask3, prioritizedTasks.get(5), "Сабтаск3 должен быть на 6 месте");


        inMemoryTaskManager.removeTask(task1.getId());
        inMemoryTaskManager.removeSubTask(subTask1.getId());
        prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();

        Assertions.assertEquals(task3, prioritizedTasks.get(0), "Задача3 должна быть на 1 месте");
        Assertions.assertEquals(subTask2, prioritizedTasks.get(1), "Сабтаск2 должен быть на 2 месте");
        Assertions.assertEquals(task2, prioritizedTasks.get(2), "Задача2 должна быть на 3 месте");
        Assertions.assertEquals(subTask3, prioritizedTasks.get(3), "Сабтаск3 должен быть на 4 месте");
        Assertions.assertEquals(4, prioritizedTasks.size(), "В списке задач должно остаться 4 элемента");

        inMemoryTaskManager.removeEpic(epic1.getId());
        prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();

        Assertions.assertEquals(task3, prioritizedTasks.get(0), "Задача3 должна быть на 1 месте");
        Assertions.assertEquals(task2, prioritizedTasks.get(1), "Задача2 должна быть на 2 месте");
        Assertions.assertEquals(2, prioritizedTasks.size(), "В списке задач должно остаться 2 элемента");

        inMemoryTaskManager.removeAllTask();
        prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();
        Assertions.assertEquals(0, prioritizedTasks.size(), "В списке не должно быть задач");
    }


    @Test
    @DisplayName("менеджер должен возвращать задачи и сабтаски правильно приоритезированы при их обновлении")
    public void shouldGetPrioritizedTasksIfUpdate(){

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 5,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,10L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,5L, dateTimeTask2);

        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        inMemoryTaskManager.create(epic1);
        Integer epic1Id = epic1.getId();

        LocalDateTime dateTimeSubTask1 = LocalDateTime.of(2024,1, 1,4 ,0,0);
        LocalDateTime dateTimeSubTask2 = LocalDateTime.of(2024,1, 1, 3,0,0);
        SubTask subTask1 = new SubTask("Сабтаск 1",
                "Описание 4",
                Status.NEW,
                epic1Id,
                20L,
                dateTimeSubTask1);
        SubTask subTask2 = new SubTask("Сабтаск 2",
                "Описание 5",
                Status.NEW,
                epic1Id,
                30L,
                dateTimeSubTask2);

        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);
        inMemoryTaskManager.create(subTask1);
        inMemoryTaskManager.create(subTask2);

        dateTimeTask2 = LocalDateTime.of(2024,1, 1, 4,0,0);
        dateTimeSubTask1 = LocalDateTime.of(2024,1, 1, 2,0,0);

        SubTask subTask1New = new SubTask("Сабтаск 1 - new",
                "Описание 4 new",
                Status.NEW,
                epic1Id,
                20L,
                dateTimeSubTask1);
        subTask1New.setId(subTask1.getId());
        inMemoryTaskManager.updateSubTask(subTask1New);

        Task task2New = new Task("Задача 2 - new",
                "Описание 2 new",
                Status.NEW,
                10L,
                dateTimeTask2);
        task2New.setId(task2.getId());
        inMemoryTaskManager.updateTask(task2New);

        List<Task> prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();

        Assertions.assertEquals(subTask1,prioritizedTasks.get(0), "Сабтаск1 должен быть на 1 месте");
        Assertions.assertEquals(subTask2, prioritizedTasks.get(1), "Сабтаск2 должен быть на 2 месте");
        Assertions.assertEquals(task2, prioritizedTasks.get(2), "Задача2 должна быть на 3 месте");
        Assertions.assertEquals(task1, prioritizedTasks.get(3), "Задача1 должна быть на 4 месте");

    }

}