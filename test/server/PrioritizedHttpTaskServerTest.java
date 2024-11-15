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


@DisplayName("Обработка запросов к приоритизированному по времени списку задач /prioritized")
class PrioritizedHttpTaskServerTest extends BaseHttpTaskServerTest {

    @Test
    @DisplayName("должен получать приоритизированный по времени список задач")
    public void shouldGetPrioritizedTasks() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 2,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 3,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonFromManager = gson.toJson(taskManager.getPrioritizedTasks());
        assertEquals(jsonFromManager, response.body(),
                "Должен совпадать Json от сервера и менеджера - приоритизированный список задач");
        assertEquals (200, response.statusCode(), "Код ответа должен быть 200");
    }

    @Test
    @DisplayName("не должен получать приоритизированный по времени список задач")
    public void shouldNotGetPrioritizedTasks() throws IOException, InterruptedException {

        LocalDateTime dateTimeTask1 = LocalDateTime.of(2024,1, 1, 2,0,0);
        LocalDateTime dateTimeTask2 = LocalDateTime.of(2024,1, 1, 3,0,0);
        LocalDateTime dateTimeTask3 = LocalDateTime.of(2024,1, 1, 1,0,0);
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW,60L, dateTimeTask1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW,60L, dateTimeTask2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW,60L, dateTimeTask3);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        URI uri = URI.create("http://localhost:8080/prioritized/ut");
        HttpRequest request = HttpRequest.newBuilder() //
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals (400, response.statusCode(), "Код ответа должен быть 400");
    }

}