package middleware;

import java.lang.reflect.InvocationTargetException;

import extension_patterns.InterceptorRegistry;
import lombok.SneakyThrows;
import middleware.communication.message.InternMessage;
import middleware.communication.message.ResponseMessage;


/**
 * Provide an INVOKER that accepts client invocations from REQUESTORS.
 * REQUESTORS send requests across the network, containing the ID of
 * the remote object, operation name, operation parameters, as well as
 * additional contextual information. The INVOKER reads the request
 * and demarshals it to obtain the OBJECT ID and the name of the operation.
 * It then dispatches the invocation with demarshaled invocation
 * parameters to the targeted remote object. That is, it looks up the
 * correct local object and its operation implementation, as described by
 * the remote invocation, and invokes it.
 */
public class Invoker {
        private InterceptorRegistry interceptorRegistry;
		// Method that invokes a remote object, receiving an InternMessage and returning a ResponseMessage
        public ResponseMessage invokeRemoteObject (InternMessage msg) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
            // Separates the method type and concatenates with the path to form the hashmap key
            var invokerKey = msg.getMethodType().toLowerCase();
            invokerKey = invokerKey + msg.getRoute();

            try{
                String remoteObjectClass = RemoteObject.getRemoteObjectClass(msg);
                beforeInvocationHook(remoteObjectClass, interceptorRegistry, msg);
                // Calls the invoke method passing the JSON key and parameters.
                ResponseMessage respMsg = RemoteObject.findMethod(invokerKey, msg.getBody());
                afterInvocationHook(remoteObjectClass, interceptorRegistry, msg);
                return respMsg;
            } catch(Exception e){
                e.printStackTrace();
                return new ResponseMessage(e.getMessage(), "Sem Autorização", "");
            }
        }

        public void setInterceptorRegistry(InterceptorRegistry interceptorRegistry){
            this.interceptorRegistry = interceptorRegistry;;
        }

        @SneakyThrows
        public void beforeInvocationHook(String remoteObjName, InterceptorRegistry interceptorRegistry, InternMessage internMessage){
            try {
                interceptorRegistry.runRemoteObjectInterceptors(remoteObjName, internMessage, "before");
            }catch (Exception e){
                throw e;
            }
        }

        public void afterInvocationHook(String remoteObjName, InterceptorRegistry interceptorRegistry, InternMessage internMessage){
            try {
                interceptorRegistry.runRemoteObjectInterceptors(remoteObjName, internMessage, "after");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
}
