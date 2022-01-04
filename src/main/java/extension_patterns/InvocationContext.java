package extension_patterns;

import middleware.communication.message.InternMessage;
import org.json.JSONObject;

public class InvocationContext {
    private final InternMessage internMessage;

    public InvocationContext(InternMessage internMessage) {
        this.internMessage = internMessage;
    }

    public void setBody(JSONObject newJsonObj){
        internMessage.setBody(newJsonObj);
    }

    public String getMethodType(){
        return internMessage.getMethodType();
    }

    public String getRoute(){
        return internMessage.getRoute();
    }

    public JSONObject getBody(){
        return getBody();
    }

    public  String getType(){
        return internMessage.getType().name();
    }
}
