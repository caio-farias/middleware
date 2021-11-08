package core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import util.message.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * the SERVER REQUEST HANDLER receive messages from the network,
 * combine the message fragments to complete messages, and dispatch the messages to the
 * correct INVOKER for further processing. The SERVER REQUEST
 * HANDLER will manage all the required resources, such as connections
 * and threads.
 */
@Slf4j
public class ServerRequestHandler {
    private static final int SERVER_PORT = 7080;

    /**
     * Main function from Server Request Handler, wait for connections
     * and instantiates new thread for each connection
     */
    public void run() {
        try {
            log.info("Server Request Handler starting on port " + SERVER_PORT);
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true){
                log.info("Waiting for client requests...");
                Socket remote = serverSocket.accept();
                log.info("Connection made");

                // As it implements Runnable, create Thread
                new Thread(new ServerHandler(remote)).start();
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
            var response =  handleRequest(this.socket);
            log.info("\n ServerHandler terminated for" + this.socket + "\n");

            try{
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.write(marshaller.marshalToSocket(response));
                socket.close();
            } catch (Exception e){
                log.error("Error to response client");
            }

        }

        /**
         * Recover and executes the commands received from client
         * @param socket the data received from client
         */
        private Message handleRequest(Socket socket){
            try {
                Message msg = marshaller.unmarshalFromSocket(socket.getInputStream());
                var invoker = new Invoker();
                return invoker.invokeRemoteObject(msg);
            } catch (IOException e) {
                log.error("Error in recover data from received package");
                return new Message();
            }
        }

    }



}
