package application.auth;

import extension_patterns.InvocationInterceptor;
import middleware.communication.message.InternMessage;

import java.io.IOException;

public class AuthInteceptor extends InvocationInterceptor {
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
        return false;
    }

}
