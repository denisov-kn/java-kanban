package server;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.Managers;
import service.TaskManager;
import service.server.HttpTaskServer;

import java.net.http.HttpClient;

abstract class BaseHttpTaskServerTest {

    static HttpTaskServer taskServer;
    static HttpClient client;
    static TaskManager taskManager;
    static Gson gson;


    @BeforeEach
    void setUp() {
        gson = Managers.getGson();
        taskManager =  Managers.getDefaultManager();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        client = HttpClient.newHttpClient();
    }


    @AfterEach
    void afterAll() {
        taskServer.stop();
    }

}