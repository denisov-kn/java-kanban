package service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    protected TaskManager taskManager;
    public static final int PORT = 8080;
    private HttpServer httpServer;
    private Gson gson;

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public HttpTaskServer() throws IOException {
        this(Managers.getDefaultManager());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {

        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
        taskServer.stop();

    }

    private void start() {
        System.out.println("Сервер запущен на порту: " + PORT);
        httpServer.start();

    }

    private void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен на порту: " + PORT);
    }
}

