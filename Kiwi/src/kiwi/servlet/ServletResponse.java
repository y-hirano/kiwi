package kiwi.servlet;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class ServletResponse {
    enum Status {
        NO_ERROR("ok"),
        INVALID_PARAMETER("invalid parameter"),
        DATASTORE_ERROR("datastore error"),
        LOGIN_ERROR("login error"),
        UNKNOWN_ERROR("unknown error");
        
        private Status(String value) {
            this.value = value;
        }
        private final String value;
        public String toString() {
            return value;
        }
        static JsonSerializer<ServletResponse.Status> gsonAdapter = new JsonSerializer<ServletResponse.Status>() {
            public JsonElement serialize(ServletResponse.Status src, Type type, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        };

    }
    ServletResponse.Status status;
    Object body;
    ServletResponse(ServletResponse.Status status, Object body) {
        this.status = status;
        this.body = body;
    }
    ServletResponse(ServletResponse.Status status) {
        this.status = status;
        this.body = null;
    }
}