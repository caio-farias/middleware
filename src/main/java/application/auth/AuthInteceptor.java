package application.auth;

import application.registration.repository.AuthRepository;
import extension_patterns.InvocationInterceptor;
import middleware.communication.message.InternMessage;

import java.io.IOException;
import java.sql.SQLException;

public class AuthInteceptor extends InvocationInterceptor {

    private final AuthRepository authRepository;
    static String xApiKey = "L996OSjd241VrBk1cRxky7c9XwtB3VxYdK2rY5n6";

    public AuthInteceptor(String name, String[] hookTypesConsumer) {
        super(name, hookTypesConsumer);
        authRepository = new AuthRepository();
    }

    @Override
    public void run(InternMessage internMessage) throws IOException {
        super.run(internMessage);

        if (noAuth(internMessage))
            throw new IOException("Sem autorização!");
    }


    public boolean noAuth(InternMessage internMessage){
        if(!internMessage.getHeaders().get("x-api-key").equals(xApiKey))
            return true;

        String authorization = internMessage.getHeaders().get("Authorization");
        if (authorization == null)
            return true;

        try{
            if(authRepository.existToken(Integer.parseInt(authorization.replace("bearer ", ""))))
                return true;

        }catch (NumberFormatException e){
            return true;
        }catch (SQLException sqlException) {
            return true;
        }

        //Falta verificar se o accessToken esta no banco

        return false;
    }

}
