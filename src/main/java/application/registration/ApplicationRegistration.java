package application.registration;

import application.registration.controller.ApplicationUserController;
import application.registration.controller.AuthController;
import middleware.Autumn;

/**
 * Main class of middleware,
 */

public class ApplicationRegistration {
    public static void main (String[] args){
    	//Instance of the class
        AuthController authController = new AuthController();
        ApplicationUserController applicationUserController = new ApplicationUserController();
        //Instance of the middleware
        Autumn server = new Autumn();

    	//Add method annotations and save in hashmaps
        server.addMethods(authController);
        server.addMethods(applicationUserController);

    	//Start middleware in parameter port
        server.start(7080);
    }
}
