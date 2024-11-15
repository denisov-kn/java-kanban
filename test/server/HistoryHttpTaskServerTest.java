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

@DisplayName("Обработка запросов к истории просмотра задач /history")
class HistoryHttpTaskServerTest extends BaseHttpTaskServerTest {

    @Test
    @DisplayName("должен получать историю просмотра списка задач")
    public void shouldGetHistory() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());

        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonFromManager = gson.toJson(taskManager.getHistoryManager().getHistory());
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - история просмотра задач");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }

    @Test
    @DisplayName("не должен получать историю просмотра списка задач")
    public void shouldNotGetHistory() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 0,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 1,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 2,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());

        URI uri = URI.create("http://localhost:8080/history/u");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (400, response.statusCode(), "Код ответа должен быть 400");
    }



}