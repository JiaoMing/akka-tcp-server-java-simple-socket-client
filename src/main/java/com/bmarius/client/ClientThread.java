package com.bmarius.client; /**
 * Created by mbd on 12.09.2014.
 */
import java.io.IOException;

/**
 * Created by mbd on 15.09.2014.
 *
 * Client thread
 */
public class ClientThread implements Runnable{

    @Override
    public void run() {
        Client client = new Client();
        try {
            client.connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
