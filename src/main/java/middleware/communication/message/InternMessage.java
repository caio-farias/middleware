package middleware.communication.message;

import lombok.Data;
import org.json.JSONObject;

import java.util.Map;

@Data
public class InternMessage {
    private String route;
    private String methodType;
    private JSONObject body;
    private Map<String, String> headers;
    private MessageType type;

    public String getEndpoint(){
        return getMethodType().toLowerCase() + getRoute();
    }
}
