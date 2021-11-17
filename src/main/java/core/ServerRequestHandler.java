package core;

import communication.message.InternMessage;
import communication.message.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import util.message.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * the SERVER REQUEST HANDLER receive messages from the network,
 * combine the message fragments to complete messages, and dispatch the messages to the
 * correct INVOKER for further processing. The SERVER REQUEST
 * HANDLER will manage all the required resources, such as connections
 * and threads.
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ServerRequestHandler {

    private final int MAX_THREAD_NUMBER = Runtime.getRuntime().availableProcessors() / 2;
    private int SERVER_PORT = 7080;
  
    /**
     * Main function from Server Request Handler, wait for connections
     * and instantiates new thread for each connection
     */    
    public void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD_NUMBER);
        try {
            log.info("Server Request Handler starting on port " + SERVER_PORT);
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true){
                log.info("Waiting for client requests...");
                Socket remote = serverSocket.accept();
                log.info("Connection made");
                executor.execute(new ServerHandler(remote));
            }
        } catch (IOException e) {
            log.error("[ERROR] problems to start the Server Request Handler");
        }
    }

    /**
     * This class implements a Thread-per-connection model to Server Request Handler.
     * For each connection received by server, a new thread is created to instantiate
     * an appropriate invoker to access the resource
     */
    @AllArgsConstructor
    private static class ServerHandler implements Runnable {
        private final Socket socket;
        private final Marshaller marshaller = new Marshaller();

        @Override
        public void run() {
            log.info("\n ServerHandler started for" + this.socket);
			try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var request = marshaller.unmarshall(in);
                var msg = handleRequest(request);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ResponseMessage responseMessage = new ResponseMessage("200", "OK", "{}");
                String httpResponse = marshaller.marshall(responseMessage);
                System.out.println(httpResponse);
                out.write(httpResponse);

                out.close();
                in.close();
                socket.close();

            } catch (Exception e1) {
				log.error("Error to receive data from handle requester");
                e1.printStackTrace();
			}

            log.info("\n ServerHandler terminated for" + this.socket + "\n");

        }

        /**
         * Recover and executes the commands received from client
         */
        private Message handleRequest(InternMessage internMessage){
            try {
                var invokerKey = internMessage.getMethodType().toLowerCase();
                invokerKey = invokerKey + "-" + internMessage.getRoute();
                Invoker inv = new Invoker();
                ArrayList<Object> params = new ArrayList<>();
                var keysParams = internMessage.getBody().keySet();
                for (var k : keysParams){
                    params.add(internMessage.getBody().get(k));
                }
                Message msg = new Message(true, 0, invokerKey, params);
                inv.invokeRemoteObject(msg);

                return new Message();
            } catch (Exception e) {
                log.error("Error in recover data from received package");
                return new Message();
            }
        }

    }



}
