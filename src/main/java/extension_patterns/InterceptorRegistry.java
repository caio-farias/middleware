package extension_patterns;

import middleware.communication.message.InternMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class InterceptorRegistry {
    private final HashMap<String, ArrayList<InvocationInterceptor>> registries;

    public InterceptorRegistry() {
        registries = new HashMap<>();
    }

    public HashMap<String, ArrayList<InvocationInterceptor>> getRegistries() {
        return registries;
    }

    private String getClassName(Object obj){
        return obj.getClass().getName();
    }

    private boolean isValidRemoteObj(String remoteObjName){
        return registries.containsKey(remoteObjName);
    }

    private boolean isValidInterceptor(String remoteObjName, String interceptorName){
        return findInterceptor(remoteObjName, interceptorName) != null;
    }

    public void addRemoteObject(Object remoteObj){
        registries.put(getClassName(remoteObj), new ArrayList<>());
    }

    public void assignInterceptorToRemoteObject(Object remoteObj, InvocationInterceptor newInvocationInterceptor){
        String remoteObjName = getClassName(remoteObj);
        if(!isValidRemoteObj(remoteObjName))
            return;
        getInterceptors(remoteObjName).add(newInvocationInterceptor);
    }

    public void registerMethodInInterceptor(Object remoteObj, String interceptorName, String method){
        String remoteObjName = getClassName(remoteObj);

        if(!isValidInterceptor(remoteObjName, interceptorName))
            return;
        InvocationInterceptor invocationInterceptor = findInterceptor(remoteObjName, interceptorName);
        invocationInterceptor.setMethods(method);
    }

    public ArrayList<InvocationInterceptor> getInterceptors(String remoteObjName){
        return registries.get(remoteObjName);
    }

    private InvocationInterceptor findInterceptor(String remoteObjName, String interceptorName){
        if(registries.get(remoteObjName) == null)
            return  null;
        for(InvocationInterceptor invInterceptor: registries.get(remoteObjName)){
            if(invInterceptor.getName().equals(interceptorName))
                return invInterceptor;
        }
        return null;
    }

    public void runRemoteObjectInterceptors(String remoteObjName, InternMessage internMessage, String hookType) throws IOException {
        if(registries.get(remoteObjName) == null)
            return;
        String endpoint = internMessage.getEndpoint();
        for(InvocationInterceptor invInterceptor: registries.get(remoteObjName)){
            if(invInterceptor.getMethods().contains(endpoint) && invInterceptor.isHookConsumer(hookType))
                invInterceptor.run(internMessage);
        }
    }
}

