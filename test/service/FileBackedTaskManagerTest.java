package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Менеджер задач с бекапом в файл")
class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    File file;
    FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    void beforeEach() {
        try {
            file = File.createTempFile("tempFile", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @DisplayName("менеджер должен загружать состояние из нормального файла")
    public void shouldLoadFromNormalFile() {

        try (Writer fileWriter = new FileWriter(file.getAbsolutePath())) {

            fileWriter.write("id,type,name,status,description,epic\n");
            fileWriter.write("1,TASK,Task1,NEW,Description task1\n");
            fileWriter.write("2,EPIC,Epic2,DONE,Description epic2,\n");
            fileWriter.write("3,SUBTASK,Sub Task2,DONE,Description sub task3,2\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("Description task1", fileBackedTaskManager.getTask(1).getDescription(),
                "Должно быть правильное описание Task ");
        Assertions.assertEquals(Status.NEW, fileBackedTaskManager.getTask(1).getStatus(),
                "Должно быть правильный статус  Task ");
        Assertions.assertEquals("Task1", fileBackedTaskManager.getTask(1).getSummary(),
                "Должно быть правильное наименование Task ");

        Assertions.assertEquals("Description epic2", fileBackedTaskManager.getEpic(2).getDescription(),
                "Должно быть правильное описание Epic2");
        Assertions.assertEquals("Epic2", fileBackedTaskManager.getEpic(2).getSummary(),
                "Должно быть правильное наименование Epic2");
        Assertions.assertEquals(fileBackedTaskManager.getSubTask(3),
                fileBackedTaskManager.getEpic(2).getSubTaskList().get(0),
                "Должна быть правильная сабтаска у Epic2");

        Assertions.assertEquals("Description sub task3", fileBackedTaskManager.getSubTask(3).getDescription(),
                "Должно быть правильное описание SubTask");
        Assertions.assertEquals(Status.DONE, fileBackedTaskManager.getSubTask(3).getStatus(),
                "Должно быть правильный статус  SubTask ");
        Assertions.assertEquals("Sub Task2", fileBackedTaskManager.getSubTask(3).getSummary(),
                "Должно быть правильное наименование SubTask ");
        Assertions.assertEquals(fileBackedTaskManager.getEpic(2).getId(),
                fileBackedTaskManager.getSubTask(3).getParentId(),
                "Должна быть правильный эпик у SubTask");

        Assertions.assertEquals(3, fileBackedTaskManager.getId(),
                "Должен быть правильный текущий id менеджера");
    }

    @Test
    @DisplayName("менеджер Не должен загружать состояние из пустого файла")
    public void shouldNotLoadFromEmptyFile() {
        try {
            fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(0, file.length());

        fileBackedTaskManager = new FileBackedTaskManager(file);
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        task.setId(0);

        fileBackedTaskManager.create(task);

        List<String> backup = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            while (fileReader.ready()) {
                backup.add(fileReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] header = backup.get(0).split(",");
        Assertions.assertEquals("id", header[0], "В заголовке должен быть id");
        Assertions.assertEquals("type", header[1], "В заголовке должен быть type");
        Assertions.assertEquals("name", header[2], "В заголовке должен быть name");
        Assertions.assertEquals("status", header[3], "В заголовке должен быть status");
        Assertions.assertEquals("description", header[4], "В заголовке должен быть description");
        Assertions.assertEquals("epic", header[5], "В заголовке должен быть epic");
        Assertions.assertEquals(6, header.length,
                "Заголовок должен быть верным по количеству элементов");

        String[] taskStr = backup.get(1).split(",");
        Assertions.assertEquals("0", taskStr[0], "У задачи должен быть верный id");
        Assertions.assertEquals("TASK", taskStr[1], "У задачи должен быть верный type");
        Assertions.assertEquals("Задача 1", taskStr[2], "У задачи должен быть верный summary");
        Assertions.assertEquals("NEW", taskStr[3], "У задачи должен быть верный status");
        Assertions.assertEquals("Описание 1", taskStr[4], "У задачи должен быть верный description");
        Assertions.assertEquals(5, taskStr.length,
                "Поля задачи должны быть верными по общему количеству элементов");
    }

    @Test
    @DisplayName("менеджер должен сохранять бекап")
    public void shouldSave() {
        fileBackedTaskManager = new FileBackedTaskManager(file);
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        task.setId(0);
        Epic epic = new Epic("Эпик 1", "Описание 2");
        epic.setId(1);
        SubTask subTask = new SubTask("Сабтаск 1", "Описание 3", Status.DONE, 1);
        subTask.setId(2);

        fileBackedTaskManager.create(task);
        fileBackedTaskManager.create(epic);
        fileBackedTaskManager.create(subTask);
        fileBackedTaskManager.getSubTask(2);

        List<String> backup = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            while (fileReader.ready()) {
                backup.add(fileReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] header = backup.get(0).split(",");
        Assertions.assertEquals("id", header[0], "В заголовке должен быть id");
        Assertions.assertEquals("type", header[1], "В заголовке должен быть type");
        Assertions.assertEquals("name", header[2], "В заголовке должен быть name");
        Assertions.assertEquals("status", header[3], "В заголовке должен быть status");
        Assertions.assertEquals("description", header[4], "В заголовке должен быть description");
        Assertions.assertEquals("epic", header[5], "В заголовке должен быть epic");
        Assertions.assertEquals(6, header.length,
                "Заголовок должен быть верным по количеству элементов");

        String[] taskStr = backup.get(1).split(",");
        Assertions.assertEquals("0", taskStr[0], "У задачи должен быть верный id");
        Assertions.assertEquals("TASK", taskStr[1], "У задачи должен быть верный type");
        Assertions.assertEquals("Задача 1", taskStr[2], "У задачи должен быть верный summary");
        Assertions.assertEquals("NEW", taskStr[3], "У задачи должен быть верный status");
        Assertions.assertEquals("Описание 1", taskStr[4], "У задачи должен быть верный description");
        Assertions.assertEquals(5, taskStr.length,
                "Поля задачи должны быть верными по общему количеству элементов");

        String[] epicStr = backup.get(2).split(",");
        Assertions.assertEquals("1", epicStr[0], "У эпика должен быть верный id");
        Assertions.assertEquals("EPIC", epicStr[1], "У эпика должен быть верный type");
        Assertions.assertEquals("Эпик 1", epicStr[2], "У эпика должен быть верный summary");
        Assertions.assertEquals("DONE", epicStr[3], "У эпика должен быть верный status");
        Assertions.assertEquals("Описание 2", epicStr[4], "У эпика должен быть верный description");
        Assertions.assertEquals(5, epicStr.length,
                "Поля эпика должны быть верными по общему количеству элементов");

        String[] subTaskStr = backup.get(3).split(",");
        Assertions.assertEquals("2", subTaskStr[0], "У сабтаска должен быть верный id");
        Assertions.assertEquals("SUBTASK", subTaskStr[1], "У сабтаска должен быть верный type");
        Assertions.assertEquals("Сабтаск 1", subTaskStr[2], "У сабтаска должен быть верный summary");
        Assertions.assertEquals("DONE", subTaskStr[3], "У сабтаска должен быть верный status");
        Assertions.assertEquals("Описание 3", subTaskStr[4], "У сабтаска должен быть верный description");
        Assertions.assertEquals("1", subTaskStr[5], "У сабтаска должен быть верный epicId");
        Assertions.assertEquals(6, subTaskStr.length,
                "Поля сабтаска должны быть верными по общему количеству элементов");
    }
}