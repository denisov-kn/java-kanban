package service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected TaskManager taskManager;
    protected Gson gson;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = Managers.getGson();
    }

    protected void sendText (HttpExchange exchange, Integer code, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected Integer parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch(NumberFormatException exception) {
           return -1;
        }
    }



}
