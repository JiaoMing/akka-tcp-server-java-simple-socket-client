package com.bmarius.server;

import akka.actor.DeadLetter;
import akka.actor.UntypedActor;

/**
 * Created by mbd on 03.10.2014.
 * DeadLetterActor that intercept dead letters
 */
public class DeadLetterActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof DeadLetter){
            DeadLetter letter = (DeadLetter) message;
            System.out.println("Message from dead letter " + letter.message());
        }
    }
}
