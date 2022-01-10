package application.auth;

import application.registration.repository.AuthRepository;
import extension_patterns.InvocationInterceptor;
import middleware.communication.message.InternMessage;

import java.io.IOException;
import java.sql.SQLException;

public class AuthInterceptor extends InvocationInterceptor {

    private final AuthRepository authRepository;
    static String xApiKey = "L996OSjd241VrBk1cRxky7c9XwtB3VxYdK2rY5n6";

    public AuthInterceptor(String name, String[] hookTypesConsumer) {
        super(name, hookTypesConsumer);
        authRepository = new AuthRepository();
    }

    @Override
    public void run(InternMessage internMessage) throws IOException {
        super.run(internMessage);

        String erroAuth = verifyError(internMessage);
        if (erroAuth != null)
            throw new IOException(erroAuth);
    }


    public String verifyError(InternMessage internMessage){
        if(!internMessage.getHeaders().get("x-api-key").equals(xApiKey))
            return "401";

        String authorization = internMessage.getHeaders().get("Authorization");
        if (authorization == null)
            return "401";

        try{
            if(!authRepository.existToken(Integer.parseInt(authorization.replace("bearer ", ""))))
                return "401";

        }catch (NumberFormatException | SQLException e){
            System.out.println("Erro interno");
            return "500";
        }

        return null;
    }

}
