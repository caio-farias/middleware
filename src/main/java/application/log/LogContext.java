package application.log;

import extension_patterns.InvocationContext;
import middleware.communication.message.InternMessage;

public class LogContext extends InvocationContext {

    public LogContext(InternMessage internMessage) {
        super(internMessage);
    }
}
