package application.auth;

import extension_patterns.InvocationInterceptor;
import middleware.communication.message.InternMessage;

import java.io.IOException;

public class AuthInteceptor extends InvocationInterceptor {
    static String xApiKey = "L996OSjd241VrBk1cRxky7c9XwtB3VxYdK2rY5n6";

    public AuthInteceptor(String name, String[] hookTypesConsumer) {
        super(name, hookTypesConsumer);
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

        //Falta verificar se o accessToken esta no banco

        return false;
    }

}
