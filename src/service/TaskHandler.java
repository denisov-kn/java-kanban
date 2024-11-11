package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TaskHandler extends  BaseHttpHandler implements HttpHandler {

    TaskManager taskManager = Managers.getDefaultManager();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());


        switch (endpoint) {
            case POST_TASK:
                handlePostTask(exchange);
                break;
            case GET_TASK:
                handleGetTask(exchange);
                break;
            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case DELETE_TASK:
                handleDeleteTask(exchange);
                break;
            default:
                sendNotFound(exchange, "Not Found");

        }

    }


    private void handleGetTask(HttpExchange exchange) {

        String path = exchange.getRequestURI().getPath();
        int taskId = Integer.parseInt(path.split("/")[1]);
        Gson gson = new GsonBuilder()
                .create();
        String taskSerialized = gson.toJson(taskManager.getTask(taskId));
        super.sendText(exchange, taskSerialized);

    }


    private void handleGetTasks(HttpExchange exchange) {

        Gson gson = new GsonBuilder()
                .create();

        String taskSerialized = gson.toJson(taskManager.getTaskList());
        sendText(exchange, taskSerialized);

    }

    private  void handlePostTask(HttpExchange exchange) {
        InputStream requestBody = exchange.getRequestBody();
        Gson gson = new GsonBuilder()
                .create();
        Task task = gson.fromJson(new InputStreamReader(requestBody), Task.class);
        if(task.getId() > 0) taskManager.updateTask(task);
        else taskManager.create(task);
        sendWithoutText(exchange);
    }

    private void handleDeleteTask(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        int taskId = Integer.parseInt(path.split("/")[1]);
        Gson gson = new GsonBuilder()
                .create();
        String taskSerialized = gson.toJson(taskManager.getTask(taskId));
        taskManager.removeTask(taskId);
        super.sendText(exchange, taskSerialized);
    }




    private Endpoint getEndpoint (String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if(requestMethod.equals("POST"))  return  Endpoint.POST_TASK;
        else if(requestMethod.equals("DELETE")) return  Endpoint.DELETE_TASK;
        else if(requestMethod.equals("GET")) {
            if(pathParts.length == 2) return Endpoint.GET_TASK;
            else if (pathParts.length == 1)  return Endpoint.GET_TASKS;
        }
        return Endpoint.UNKNOWN;

    }

    enum  Endpoint {
        GET_TASKS,
        GET_TASK,
        POST_TASK,
        DELETE_TASK,
        UNKNOWN

    }


}
