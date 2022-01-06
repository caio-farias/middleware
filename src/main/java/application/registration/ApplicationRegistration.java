package application.registration;

import application.auth.AuthInteceptor;
import application.registration.controller.ApplicationUserController;
import application.registration.controller.AuthController;
import extension_patterns.InterceptorRegistry;
import middleware.Autumn;

/**
 * Main class of middleware,
 */

public class ApplicationRegistration {
    public static void main (String[] args){
    	//Instance of the class
        AuthController authController = new AuthController();
        ApplicationUserController applicationUserController = new ApplicationUserController();

        AuthInteceptor authentication = new AuthInteceptor("authentication", new String[]{"before"});
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        interceptorRegistry.addRemoteObject(applicationUserController);
        interceptorRegistry.assignInterceptorToRemoteObject(applicationUserController, authentication);

        //Instance of the middleware
        Autumn server = new Autumn();
        server.setInterceptorRegistry(interceptorRegistry);

        //Add method annotations and save in hashmaps
        server.addMethods(authController);
        server.addMethodsWithInterceptor(applicationUserController, authentication);

    	//Start middleware in parameter port
        server.start(7080);
    }
}
