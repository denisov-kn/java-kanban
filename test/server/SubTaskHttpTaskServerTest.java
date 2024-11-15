package server;

import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
@DisplayName("Обработка запросов к сабтаскам /subtasks")
class SubTaskHttpTaskServerTest extends BaseHttpTaskServerTest {


    @Test
    @DisplayName("должен получать список сабтасков")
    public void shouldGetTasksList() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        SubTask subTask2 = new SubTask("Задача 2", "Описание 2", Status.NEW, epicId, 60L, dateTimeTask2);
        SubTask subTask3 = new SubTask("Задача 3", "Описание 3", Status.NEW, epicId, 60L, dateTimeTask3);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.create(subTask3);

        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getSubTaskList());
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - список сабтасков");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }


    @Test
    @DisplayName("должен получать сабтаск")
    public void shouldGetTask() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        taskManager.create(subTask1);

        Integer subTaskId = taskManager.getSubTaskList().getFirst().getId();
        URI uri = URI.create("http://localhost:8080/subtasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getSubTask(subTaskId));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - сабтаск с id: " + subTaskId);
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");

    }

    @Test
    @DisplayName("должен создавать сабтаск")
    public void shouldCreateTask() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);

        URI uri = URI.create("http://localhost:8080/subtasks");
        String jsonSubTask = gson.toJson(subTask1);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getSubTask(1));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при создании сабтаска");
        assertEquals (201, response.statusCode(), "Код ответа должен быть 201");
    }


    @Test
    @DisplayName("должен обновлять сабтаск")
    public void shouldUpdateTask() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        SubTask subTask2 = new SubTask("Задача 2", "Описание 2", Status.NEW, epicId, 60L, dateTimeTask2);
        taskManager.create(subTask1);


        taskManager.create(subTask1);
        subTask2.setId(subTask1.getId());

        URI uri = URI.create("http://localhost:8080/subtasks");
        String jsonSubTask = gson.toJson(subTask2);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getSubTask(subTask1.getId()));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера после обновления сабтаска");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }


    @Test
    @DisplayName("должен удалять сабтаск")
    public void shouldDeleteTask() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        SubTask subTask2 = new SubTask("Задача 2", "Описание 2", Status.NEW, epicId, 60L, dateTimeTask2);
        taskManager.create(subTask1);
        taskManager.create(subTask2);

        Integer subTask2Id = subTask2.getId();

        URI uri = URI.create("http://localhost:8080/subtasks/" + subTask2Id);
        HttpRequest request = HttpRequest.newBuilder() //
                .DELETE()
                .uri(uri)
                .build();
        String jsonFromManager = gson.toJson(taskManager.getSubTask(subTask2Id));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при удалении сабтаска");
        assertNull(taskManager.getTask(subTask2Id), "Не должно быть id по сабтаску");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }

    @Test
    @DisplayName("должен выдавать 400 ошибку")
    public void shouldGet400() throws IOException, InterruptedException {
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        taskManager.create(subTask1);


        URI uri = URI.create("http://localhost:8080/subtasks/u" );
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
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        taskManager.create(subTask1);


        URI uri = URI.create("http://localhost:8080/subtasks/22" );
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

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Epic epic = new Epic("Эпик 1", "Описание Эпика");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        SubTask subTask2 = new SubTask("Задача 2", "Описание 2", Status.NEW, epicId, 60L, dateTimeTask2);
        taskManager.create(subTask1);

        URI uri = URI.create("http://localhost:8080/subtasks" );
        String jsonTask = gson.toJson(subTask2);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (406, response.statusCode(), "Код ответа должен быть 406");
    }
}