package service.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import model.Task;
import service.TaskManager;
import service.exception.BadRequestException;
import service.exception.InteractionException;
import service.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {


    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
            switch (endpoint) {
                case POST_SUB_TASK:
                    handlePostSubTask(exchange);
                    break;
                case GET_SUB_TASK:
                    handleGetSubTask(exchange);
                    break;
                case GET_SUB_TASKS:
                    handleGetSubTasks(exchange);
                    break;
                case DELETE_SUB_TASK:
                    handleDeleteSubTask(exchange);
                    break;
                default:
                    throw new BadRequestException("Неизвестная команда " +
                            exchange.getRequestMethod() +
                            " : " + exchange.getRequestURI().getPath());
            }
        } catch (InteractionException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.NOT_ACCEPTABLE.code, gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (NotFoundException e) {
            e.printStackTrace();
            sendText(exchange,HttpCode.NOT_FOUND.code, gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (BadRequestException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.BAD_REQUEST.code, gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (IOException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.INTERNAL_SERVER_ERROR.code,
                    gson.toJson(new ErrorResponse("Внутренняя ошибка сервера"))
            );
        }

        finally {
            exchange.close();
        }

    }


    private void handleGetSubTask(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        int SubTaskId = parseId(path.split("/")[2]);
        if (SubTaskId == -1)
            throw new BadRequestException("Ошибка в id сабтаска, ожидается целое число. В запросе:" + path);
        if (taskManager.getSubTask(SubTaskId) == null)
            throw new NotFoundException("Сабтаск с id " + SubTaskId + " не найдена");
        String subTaskSerialized = gson.toJson(taskManager.getSubTask(SubTaskId));
        sendText(exchange, HttpCode.OK.code, subTaskSerialized);
    }


    private void handleGetSubTasks(HttpExchange exchange) throws IOException {

        String tasksSerialized = gson.toJson(taskManager.getSubTaskList());
        sendText(exchange, HttpCode.OK.code, tasksSerialized);

    }

    private  void handlePostSubTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body =  new String(requestBody.readAllBytes());

        SubTask subTask = gson.fromJson(body, SubTask.class);
        if(subTask.getId() >= 0) {
            subTask = taskManager.updateSubTask(subTask);
            sendText(exchange, HttpCode.OK.code, gson.toJson(subTask));
        }
        else {
            subTask =  taskManager.create(subTask);
            sendText(exchange, HttpCode.CREATE.code, gson.toJson(subTask));
        }

    }

    private void handleDeleteSubTask(HttpExchange exchange) throws IOException{
        String path = exchange.getRequestURI().getPath();
        int subTaskId = parseId(path.split("/")[2]);
        if (subTaskId == -1) {
            throw new BadRequestException("Ошибка в id сабтаски, ожидается целое число. В запросе:" + path);
        }

        String subTaskSerialized = null;
        try {
            subTaskSerialized = gson.toJson(taskManager.getSubTask(subTaskId));
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Сабтаска с id " + subTaskId + " не найдена");
        }
        Task task = taskManager.removeSubTask(subTaskId);
        subTaskSerialized = gson.toJson(task);
        sendText(exchange,HttpCode.OK.code, subTaskSerialized);
    }


    private Endpoint getEndpoint (String requestPath, String requestMethod) {

        if (requestMethod.equals("POST")) return  Endpoint.POST_SUB_TASK;
        else if (requestMethod.equals("DELETE")) return  Endpoint.DELETE_SUB_TASK;
        else if (requestMethod.equals("GET") && Pattern.matches("^/subtasks$", requestPath)) return Endpoint.GET_SUB_TASKS;
        else if (requestMethod.equals("GET") && Pattern.matches("^/subtasks/\\d+$", requestPath))
            return Endpoint.GET_SUB_TASK;
        return Endpoint.UNKNOWN;
    }

    enum  Endpoint {
        GET_SUB_TASKS,
        GET_SUB_TASK,
        POST_SUB_TASK,
        DELETE_SUB_TASK,
        UNKNOWN
    }


}
