package com.bmarius;

import java.io.Serializable;

/**
 * Created by mbd on 03.10.2014.
 * ACK class. Send ack after a message is received
 */
public class ACK implements Serializable{

    private static final long serialVersionUID = 4L;

    private ACK(){};

    public static ACK newInstace(){
        return new ACK();
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

        ACK ack = (ACK) o;

        if (message != null ? !message.equals(ack.message) : ack.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() + 64 : 0;
    }

    @Override
    public String toString() {
        return "ACK{" +
                "message='" + message + '\'' +
                '}';
    }
}
