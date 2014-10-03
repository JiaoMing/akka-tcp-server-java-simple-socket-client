package com.bmarius;

import akka.util.ByteString;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mbd on 26.09.2014.
 */
public class StaticValues {

    public static boolean isConnected = false;
    public static LinkedBlockingQueue<ByteString> dumped = new LinkedBlockingQueue<>();
    public static final int port = 7654;
    public static final String server = "localhost";

}
