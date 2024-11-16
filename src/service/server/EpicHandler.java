package service.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.SubTask;
import service.TaskManager;
import service.exception.BadRequestException;
import service.exception.InteractionException;
import service.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {


    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
            switch (endpoint) {
                case POST_EPIC:
                    handlePostEpic(exchange);
                    break;
                case GET_EPIC:
                    handleGetEpic(exchange);
                    break;
                case GET_EPICS:
                    handleGetEpics(exchange);
                    break;
                case DELETE_EPIC:
                    handleDeleteEpic(exchange);
                    break;
                case GET_SUBTASK_OF_EPIC:
                    handleGetSubTasksOfEpic(exchange);
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
            sendText(exchange, HttpCode.INTERNAL_SERVER_ERROR.code, gson.toJson(new ErrorResponse("Внутренняя ошибка сервера")));
        } finally {
            exchange.close();
        }

    }


    private void handleGetEpic(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        int epicId = parseId(path.split("/")[2]);
        if (epicId == -1)
            throw new BadRequestException("Ошибка в id эпика, ожидается целое число. В запросе:" + path);
        if (taskManager.getEpic(epicId) == null)
            throw new NotFoundException("Эпик с id " + epicId + " не найден");
        String epicSerialized = gson.toJson(taskManager.getEpic(epicId));
        sendText(exchange, HttpCode.OK.code, epicSerialized);

    }


    private void handleGetEpics(HttpExchange exchange) throws IOException {

        List<Epic> epicList = taskManager.getEpicList();
        String epicsSerialized = gson.toJson(taskManager.getEpicList());
        sendText(exchange, HttpCode.OK.code, epicsSerialized);

    }

    private  void handlePostEpic(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body =  new String(requestBody.readAllBytes());

        Epic epic = gson.fromJson(body, Epic.class);
        if (epic.getId() >= 0) {
            epic = taskManager.updateEpic(epic);
            sendText(exchange, HttpCode.OK.code, gson.toJson(epic));
        } else {
            epic =  taskManager.create(epic);
            sendText(exchange, HttpCode.CREATE.code, gson.toJson(epic));
        }

    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int epicId = parseId(path.split("/")[2]);
        if (epicId == -1)
            throw new BadRequestException("Ошибка в id эпика, ожидается целое число. В запросе:" + path);

        String epicSerialized = null;
        try {
            epicSerialized = gson.toJson(taskManager.getEpic(epicId));
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Эпик с id " + epicId + " не найден");
        }
        Epic epic = taskManager.removeEpic(epicId);
        epicSerialized = gson.toJson(epic);
        sendText(exchange,HttpCode.OK.code, epicSerialized);
    }

    private void handleGetSubTasksOfEpic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int epicId = parseId(path.split("/")[2]);
        if (epicId == -1) {
            throw new BadRequestException("Ошибка в id эпика, ожидается целое число. В запросе:" + path);
        }

        String epicSerialized = null;
        try {
            epicSerialized = gson.toJson(taskManager.getEpic(epicId));
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Эпик с id " + epicId + " не найден");
        }

        List<SubTask> subTaskList = taskManager.getSubTaskList();
        String subTaskListSerialized = gson.toJson(subTaskList);
        sendText(exchange, HttpCode.OK.code, subTaskListSerialized);
    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {

        if(requestMethod.equals("POST")) return Endpoint.POST_EPIC;
        else if(requestMethod.equals("DELETE")) return Endpoint.DELETE_EPIC;
        else if(requestMethod.equals("GET") && Pattern.matches("^/epics$", requestPath))
            return Endpoint.GET_EPICS;
        else if(requestMethod.equals("GET") && Pattern.matches("^/epics/\\d+$", requestPath))
            return Endpoint.GET_EPIC;
        else if(requestMethod.equals("GET") && Pattern.matches("^/epics/\\d+/subtasks$", requestPath)) return Endpoint.GET_SUBTASK_OF_EPIC;

        return Endpoint.UNKNOWN;
    }

    enum  Endpoint {
        GET_EPICS,
        GET_EPIC,
        GET_SUBTASK_OF_EPIC,
        POST_EPIC,
        DELETE_EPIC,
        UNKNOWN
    }
}
