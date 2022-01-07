package application;

import application.log.LogInterceptor;
import extension_patterns.InterceptorRegistry;
import middleware.Autumn;

/**
 * Main class of middleware,
 */

public class Main {
    public static void main (String[] args){
    	//Instance of the class
        Calculator calc = new Calculator();

        LogInterceptor logger = new LogInterceptor("logger", new String[]{"before"});
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        interceptorRegistry.addRemoteObject(calc);
        interceptorRegistry.assignInterceptorToRemoteObject(calc, logger);

    	//Instance of the middleware
        Autumn server = new Autumn();
        server.setInterceptorRegistry(interceptorRegistry);

        //Add method annotations and save in hashmaps
        server.addMethods(calc);
        server.addMethodsWithInterceptor(calc, logger);

        //Start middleware in parameter port
        server.start(7080);
    }
}
