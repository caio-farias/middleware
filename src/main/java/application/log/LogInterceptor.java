package application.log;

import extension_patterns.InvocationContext;
import extension_patterns.InvocationInterceptor;
import middleware.communication.message.InternMessage;

import java.io.*;
import java.util.Date;

public class LogInterceptor extends InvocationInterceptor {
    private final String logPath ="src/main/java/application/log/log.txt";

    public LogInterceptor(String name, String[] hookTypesConsumer) {
        super(name, hookTypesConsumer);
    }

    @Override
    public void run(InternMessage internMessage) throws IOException {
        super.run(internMessage);
        writeLog();
    }

    private String getNewLogContent(){
        InvocationContext invocationContext = getInvocationContext();
        Date date = new Date();
        String newLogLine =  date  + " - " + invocationContext.getType() + "/" +  invocationContext.getMethodType() + " - " + "Interceptor: "  + getName() ;
        return newLogLine;
    }

    public String readLog() throws IOException {
        String content = "";
        BufferedReader reader = new BufferedReader(new FileReader(logPath));
        String currentLine = reader.readLine();
        while(currentLine != null ){
            content += currentLine + "\n";
            currentLine = reader.readLine();
        }
        reader.close();
        return content;
    }

    public void writeLog() throws IOException {
        String newLogLine = getNewLogContent();
        String oldContent = readLog();
        BufferedWriter writer = new BufferedWriter(new FileWriter(logPath));
        writer.write(oldContent + newLogLine +  "\n");
        writer.close();
    }

}
