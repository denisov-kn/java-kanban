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

@DisplayName("Обработка запросов к эпикам /epics")
class EpicHttpTaskServerTest extends BaseHttpTaskServerTest {


    @Test
    @DisplayName("должен получать список 'эпиков'")
    public void shouldGetEpicsList() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Задача 1", "Описание 1");
        Epic epic2 = new Epic("Задача 2", "Описание 2");
        Epic epic3 = new Epic("Задача 3", "Описание 3");

        taskManager.create(epic1);
        taskManager.create(epic2);
        taskManager.create(epic3);

        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getEpicList());
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - список эпиков");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }


    @Test
    @DisplayName("должен получать эпик")
    public void shouldGetEpic() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Задача 1", "Описание 1");
        taskManager.create(epic1);

        Integer epicId = taskManager.getEpicList().getFirst().getId();
        URI uri = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getEpic(epicId));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - эпик с id: " + epicId);
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");

    }

    @Test
    @DisplayName("должен создавать эпик")
    public void shouldCreateEpic() throws IOException, InterruptedException {

        Epic epic = new Epic("Задача 1", "Описание 1");

        URI uri = URI.create("http://localhost:8080/epics");
        String jsonEpic = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getEpic(0));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при создании эпика");
        assertEquals (201, response.statusCode(), "Код ответа должен быть 201");
    }


    @Test
    @DisplayName("должен обновлять эпик")
    public void shouldUpdateEpic() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Задача 1", "Описание 1");
        Epic epic2 = new Epic("Задача 2", "Описание 2");
        taskManager.create(epic1);
        epic2.setId(epic1.getId());

        URI uri = URI.create("http://localhost:8080/epics");
        String jsonEpic = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder() //
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonFromManager = gson.toJson(taskManager.getEpic(epic1.getId()));
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера после обновления эпика");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }


    @Test
    @DisplayName("должен удалять эпик")
    public void shouldDeleteEpic() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Задача 1", "Описание 1");
        Epic epic2 = new Epic("Задача 2", "Описание 2");

        taskManager.create(epic1);
        taskManager.create(epic2);
        Integer epic2Id = epic2.getId();

        URI uri = URI.create("http://localhost:8080/epics/" + epic2Id);
        HttpRequest request = HttpRequest.newBuilder() //
                .DELETE()
                .uri(uri)
                .build();
        String jsonFromManager = gson.toJson(taskManager.getEpic(epic2Id));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при удалении эпика");
        assertNull(taskManager.getTask(epic2Id), "Не должно быть эпика по id");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }

    @Test
    @DisplayName("должен выдавать 400 ошибку")
    public void shouldGet400() throws IOException, InterruptedException {

        Epic epic = new Epic("Задача 1", "Описание 1");
        taskManager.create(epic);

        URI uri = URI.create("http://localhost:8080/epics/u" );
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
        Epic epic = new Epic("Задача 1", "Описание 1");
        taskManager.create(epic);

        URI uri = URI.create("http://localhost:8080/epics/22" );
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (404, response.statusCode(), "Код ответа должен быть 404");
    }


    @Test
    @DisplayName("должен возвращать список сабтасков")
    public void shouldGetSubTasksListOfEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Задача 1", "Описание 1");
        taskManager.create(epic);
        Integer epicId = epic.getId();
        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        SubTask subTask1 = new SubTask("Задача 1", "Описание 1", Status.NEW, epicId, 60L, dateTimeTask1);
        SubTask subTask2 = new SubTask("Задача 2", "Описание 2", Status.NEW, epicId, 60L, dateTimeTask2);
        SubTask subTask3 = new SubTask("Задача 3", "Описание 3", Status.NEW, epicId, 60L, dateTimeTask3);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.create(subTask3);

        URI uri = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        String jsonFromManager = gson.toJson(taskManager.getSubTasksByEpic(epicId));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера при запросе списка сабтасков");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }

}