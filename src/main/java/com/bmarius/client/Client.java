package com.bmarius.client;

import com.bmarius.utils.ACK;
import com.bmarius.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by mbd on 15.09.2014.
 *
 * Client for akka Tcp Server
 * The client is a simple socket java client
 */
public class Client {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Connect to server method that makes multiple connections to the akka tcp server
     * @throws java.io.IOException
     */
    public void connectToServer() throws IOException {
        Socket socket = new Socket("localhost", 7654);
        out = new ObjectOutputStream(socket.getOutputStream());
        Message message = Message.newInstance();
        /**
         * send connected message
         */
        message.setMessage("Connected");
        out.writeObject(message);
        out.flush();

        /**
         * create threads with input channel
         */
        new Thread(()->{
            try {
                in = new ObjectInputStream(socket.getInputStream());
                ACK response = null;
                try {
                    response = (ACK) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Received ACK: " + response.getMessage());
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    if(in != null)
                        in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (NullPointerException e){
                System.out.println("Opppss NullPointerException.");
            }
        }).run();

        out.close();
        socket.close();
    }

}
