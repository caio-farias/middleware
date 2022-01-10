package application;

import application.auth.AuthInterceptor;
import application.log.LogInterceptor;
import application.registration.controller.ApplicationUserController;
import application.registration.controller.AuthController;
import extension_patterns.InterceptorRegistry;
import middleware.Autumn;

/**
 * Main class of middleware,
 */

public class Main {
    public static void main (String[] args){
    	//Instance of the class
        Calculator calc = new Calculator();
        AuthController authController = new AuthController();
        ApplicationUserController applicationUserController = new ApplicationUserController();

        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        LogInterceptor logger = new LogInterceptor("logger", new String[]{"before"});
        AuthInterceptor authentication = new AuthInterceptor("authentication", new String[]{"before"});

        interceptorRegistry.addRemoteObject(calc);
        interceptorRegistry.addRemoteObject(authController);
        interceptorRegistry.addRemoteObject(applicationUserController);

        // LogInterceptor - controllers
        interceptorRegistry.assignInterceptorToRemoteObject(calc, logger);
        interceptorRegistry.assignInterceptorToRemoteObject(authController, logger);
        interceptorRegistry.assignInterceptorToRemoteObject(applicationUserController, logger);

        // AuthInterceptor - controllers
        interceptorRegistry.assignInterceptorToRemoteObject(applicationUserController, authentication);

    	//Instance of the middleware
        Autumn server = new Autumn();
        server.setInterceptorRegistry(interceptorRegistry);
      
        //Add method annotations and save in hashmaps
        server.addMethodsWithInterceptor(calc, logger);
        server.addMethodsWithInterceptor(authController, logger);
        server.addMethodsWithInterceptor(applicationUserController, logger);
        server.addMethodsWithInterceptor(applicationUserController, authentication);

        //Start middleware in parameter port
        server.start(7080);
    }
}
