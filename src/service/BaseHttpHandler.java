package service;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected void sendText (HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendWithoutText(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201,-1);
        exchange.close();
    }

}
