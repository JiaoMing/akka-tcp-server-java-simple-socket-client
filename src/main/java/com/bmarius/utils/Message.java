package com.bmarius.utils;

import java.io.Serializable;

/**
 * Created by mbd on 26.09.2014.
 * Message class used to transfer message form server to client and vice versa
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 999003161432860L;

    private Message(){}

    public static Message newInstance(){
        return new Message();
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (message != null ? !message.equals(message1.message) : message1.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() + 454 : 0;
    }

    @Override
    public String toString() {
        return "com.bmarius.Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
