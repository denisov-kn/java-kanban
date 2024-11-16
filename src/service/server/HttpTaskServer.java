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


    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {

        HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefaultManager());
        taskServer.start();
        taskServer.stop();

    }

    public void start() {
        System.out.println("Сервер запущен на порту: " + PORT);
        httpServer.start();

    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен на порту: " + PORT);
    }
}

