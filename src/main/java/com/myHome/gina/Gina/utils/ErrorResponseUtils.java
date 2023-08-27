package com.myHome.gina.Gina.utils;

import java.util.Map;
import java.util.Objects;

public class ErrorResponseUtils {
    public static Map<String, Object> badRequest(String message) {
        if (Objects.isNull(message)) {
            message = "Bad Request";
        }
        return Map.of("success", false, "error", message);
    }

    public static Map<String, Object> unauthorized(String message) {
        if (Objects.isNull(message)) {
            message = "Unauthorized";
        }
        return Map.of("success", false, "error", message);
    }

    public static Map<String, Object> forbidden(String message) {
        if (Objects.isNull(message)) {
            message = "Forbidden";
        }
        return Map.of("success", false, "error", message);
    }
    public static Map<String,Object> internalServerError(String message) {
        if (Objects.isNull(message)) {
            message = "Internal Server Error";
        }
        return Map.of("success", false, "error", message);
    }
}
