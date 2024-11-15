package server;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
@DisplayName("Обработка запросов к задачам /tasks")
class  TaskHttpTaskServerTest extends BaseHttpTaskServerTest {


    @Test
    @DisplayName("должен получать список задач")
    public void shouldGetTasksList() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getTaskList());
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - список задач");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }


    @Test
    @DisplayName("должен получать задачу")
    public void shouldGetTask() throws IOException, InterruptedException {


        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        taskManager.create(task1);



        Integer taskId = taskManager.getTaskList().getFirst().getId();
        URI uri = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getTask(taskId));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - задача с id: " + taskId);
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");

    }

    @Test
    @DisplayName("должен создавать задачу")
    public void shouldCreateTask() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask = LocalDateTime.of(2024,1, 1, 3,0,0);
        Task task4 = new Task("Задача 4", "Описание 4", Status.NEW,60L, dateTimeTask);

        URI uri = URI.create("http://localhost:8080/tasks");
        String jsonTask = gson.toJson(task4);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getTask(0));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при создании задачи");
        assertEquals (201, response.statusCode(), "Код ответа должен быть 201");
    }


    @Test
    @DisplayName("должен обновлять задачу")
    public void shouldUpdateTask() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask = LocalDateTime.of(2024,1, 1, 1,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask);


        taskManager.create(task1);
        task2.setId(task1.getId());

        URI uri = URI.create("http://localhost:8080/tasks");
        String jsonTask = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getTask(task1.getId()));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера после обновления задачи");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }


    @Test
    @DisplayName("должен удалять задачу")
    public void shouldDeleteTask() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);


        taskManager.create(task1);
        taskManager.create(task2);
        Integer task2Id = task2.getId();

        URI uri = URI.create("http://localhost:8080/tasks/" + task2Id);
        HttpRequest request = HttpRequest.newBuilder() //
                .DELETE()
                .uri(uri)
                .build();
        String jsonFromManager = gson.toJson(taskManager.getTask(task2Id));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при удалении задачи");
        assertNull(taskManager.getTask(task2Id), "Не должно быть id по задаче");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }

    @Test
    @DisplayName("должен выдавать 400 ошибку")
    public void shouldGet400() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        taskManager.create(task1);


        URI uri = URI.create("http://localhost:8080/tasks/u" );
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (400, response.statusCode(), "Код ответа должен быть 400");
    }

    @Test
    @DisplayName("должен выдавать 404 ошибку")
    public void shouldGet404() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        taskManager.create(task1);


        URI uri = URI.create("http://localhost:8080/tasks/22" );
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (404, response.statusCode(), "Код ответа должен быть 404");
    }


    @Test
    @DisplayName("должен выдавать 406 ошибку")
    public void shouldGet406() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask1);
        taskManager.create(task1);

        URI uri = URI.create("http://localhost:8080/tasks" );
        String jsonTask = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (406, response.statusCode(), "Код ответа должен быть 406");
    }

}