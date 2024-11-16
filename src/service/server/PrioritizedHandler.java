package service.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {


    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try (exchange) {

            String requestPath = exchange.getRequestURI().getPath();
            if (Pattern.matches("^/prioritized$", requestPath) && exchange.getRequestMethod().equals("GET")) {
                String prioritizedSerialized = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, HttpCode.OK.code, prioritizedSerialized);
            } else {
                sendText(exchange, HttpCode.BAD_REQUEST.code, gson.toJson(new ErrorResponse(
                        "Неизвестная команда " +
                        exchange.getRequestMethod() +
                        " : " + exchange.getRequestURI().getPath()))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendText(exchange, HttpCode.INTERNAL_SERVER_ERROR.code,
                    gson.toJson(new ErrorResponse("Внутрення ошибка сервера"))
            );

        }

    }

}
