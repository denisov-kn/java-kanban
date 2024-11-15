package service.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;
import service.exception.BadRequestException;
import service.exception.InteractionException;
import service.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {


    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try (exchange) {
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
                    throw new BadRequestException("Неизвестная команда " +
                            exchange.getRequestMethod() +
                            " : " + exchange.getRequestURI().getPath());
            }
        } catch (InteractionException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.INTERNAL_SERVER_ERROR.code, gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (NotFoundException e) {
            e.printStackTrace();
            sendText(exchange,HttpCode.NOT_FOUND.code, gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (BadRequestException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.BAD_REQUEST.code, gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (IOException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.BAD_REQUEST.code, gson.toJson(new ErrorResponse("Внутренняя ошибка сервера")));
        }

    }


    private void handleGetTask(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        int taskId = parseId(path.split("/")[1]);

        if (taskId == -1)
            throw new BadRequestException("Ошибка в id задачи, ожидается целое число. В запросе:" + path);
        String taskSerialized ="";
        try {
            taskSerialized = gson.toJson(taskManager.getTask(taskId));
        } catch (IndexOutOfBoundsException exception) {
            throw new NotFoundException("Задача с id " + taskId + " не найдена");
        }

        sendText(exchange, HttpCode.OK.code, taskSerialized);

    }


    private void handleGetTasks(HttpExchange exchange) throws IOException {

        String tasksSerialized = gson.toJson(taskManager.getTaskList());
        sendText(exchange, HttpCode.OK.code, tasksSerialized);

    }

    private  void handlePostTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        Task task = gson.fromJson(new InputStreamReader(requestBody), Task.class);
        if(task.getId() > 0) {
            task = taskManager.updateTask(task);
            sendText(exchange, HttpCode.OK.code, gson.toJson(task));
        }
        else {
            task =  taskManager.create(task);
            sendText(exchange, HttpCode.CREATE.code, gson.toJson(task));
        }

    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException{
        String path = exchange.getRequestURI().getPath();
        int taskId = parseId(path.split("/")[1]);
        if (taskId == -1) {
            throw new BadRequestException("Ошибка в id задачи, ожидается целое число. В запросе:" + path);
        }

        String taskSerialized = null;
        try {
            taskSerialized = gson.toJson(taskManager.getTask(taskId));
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Задача с id " + taskId + " не найдена");
        }
        Task task = taskManager.removeTask(taskId);
        taskSerialized = gson.toJson(task);
        sendText(exchange,HttpCode.OK.code, taskSerialized);
    }


    private Endpoint getEndpoint (String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if(requestMethod.equals("POST"))  return  Endpoint.POST_TASK;
        else if(requestMethod.equals("DELETE")) return  Endpoint.DELETE_TASK;
        else if(requestMethod.equals("GET") && pathParts.length == 1)  return Endpoint.GET_TASKS;
        else if(requestMethod.equals("GET") && Pattern.matches("^/tasks/\\d+$", requestPath))
            return Endpoint.GET_TASK;
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
