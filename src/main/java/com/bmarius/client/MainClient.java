package com.bmarius.client; /**
 * Created by mbd on 12.09.2014.
 */
import java.io.IOException;

/**
 * Created by mbd on 15.09.2014.
 * main class
 */
public class MainClient {

    public static void main(String[] args) throws IOException {

            //Create threads with clients.
            for(int i = 0;i < 200; i++){
                ClientThread cl = new ClientThread();
                Thread th = new Thread(cl);
                th.start();

            }

        Client client = new Client();
        client.connectToServer();
        Client client2 = new Client();
        client2.connectToServer();
    }
}
