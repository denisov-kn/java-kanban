package service.server;

public enum HttpCode {

    OK(200),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    CREATE(201),
    NOT_ACCEPTABLE(406),
    BAD_REQUEST(400);

    public final Integer code;

    HttpCode(Integer code) {
        this.code = code;
    }
}
