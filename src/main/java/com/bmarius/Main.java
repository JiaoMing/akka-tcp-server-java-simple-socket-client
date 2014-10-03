package com.bmarius;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.io.Tcp;

/**
 * Created by mbd on 26.09.2014.
 * main class.
 * {@see http://doc.akka.io/docs/akka/snapshot/java/io-tcp.html} for more information
 */
public class Main {

    public static void main(String... args){
        /**
         * create the actor system
         */
        ActorSystem system = ActorSystem.create("server-tcp");
        /**
         * create dead letter actor with references at DeadLetterActor that handle the dead letters
         */
        final ActorRef deadLetterActor = system.actorOf(Props.create(DeadLetterActor.class));
        /**
         * subscribe to eventStream, more info {@see http://doc.akka.io/docs/akka/snapshot/java/event-bus.html}
         */
        system.eventStream().subscribe(deadLetterActor, DeadLetter.class);
        /**
         * Define server actor,  Tcp.get(system).getManager()) get Tcp manager actor
         */
        ActorRef server = system.actorOf(Props.create(Server.class, Tcp.get(system).getManager()), "server");

        /**
         * While is not connected show "Waiting" message
         */
        while(!StaticValues.isConnected){
            System.out.println("Waiting");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
