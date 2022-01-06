package application.auth;

import extension_patterns.InvocationContext;
import middleware.communication.message.InternMessage;

public class AuthContext extends InvocationContext {
    public AuthContext(InternMessage internMessage) { super(internMessage); }
}
