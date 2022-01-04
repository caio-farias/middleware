package extension_patterns;

import middleware.communication.message.InternMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class InvocationInterceptor {
    private InvocationContext invocationContext;
    private final String name;
    private final String[] hookTypesConsumer;
    private final ArrayList<String> methods = new ArrayList<>();


    public InvocationInterceptor(String name, String[] hookTypesConsumer) {
        this.name = name;
        this.hookTypesConsumer = hookTypesConsumer;
    }


    public void run(InternMessage internMessage) throws IOException {
        generateInvocationContext(internMessage);
    }

    private void generateInvocationContext(InternMessage internMessage){
        invocationContext = new InvocationContext(internMessage);
    }

    public InvocationContext getInvocationContext(){
        return invocationContext;
    }

    public boolean isHookConsumer(String hookType){
        if(hookTypesConsumer.length < 1)
            return false;
        return Arrays.stream(hookTypesConsumer).toList().contains(hookType);
    }

    public String getName(){
        return name;
    }

    public ArrayList<String> getMethods() {
        return methods;
    }

    public void setMethods(String method) {
        methods.add(method);
    }
}
