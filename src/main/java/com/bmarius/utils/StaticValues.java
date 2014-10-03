package com.bmarius.utils;

import akka.util.ByteString;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mbd on 26.09.2014.
 * Static values
 */
public class StaticValues {

    public static boolean isConnected = false;
    public static LinkedBlockingQueue<ByteString> dumped = new LinkedBlockingQueue<ByteString>();
    public static final int port = 7654;
    public static final String server = "localhost";

}
