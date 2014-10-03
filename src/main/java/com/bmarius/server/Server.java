package com.bmarius.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import com.bmarius.utils.StaticValues;

import java.net.InetSocketAddress;

/**
 * Created by mbd on 29.09.2014.
 * Server Actor
 */
public class Server extends UntypedActor {

    /**
     * Manager actor, this actor is the default Tcp manager that handles the underlying level I/O resource
     * and instantiates workers for specific tasks
     */
    private final ActorRef manager;

    /**
     * handler is the actor that handle the messages received from clients
     */
    private ActorRef handler;


    /**
     * on create define the manager
     * @param manager
     */
    public Server(ActorRef manager) {
        this.manager = manager;
    }

    /**
     * on preStart send to manager actor a Bind message with server and port
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        manager.tell(TcpMessage.bind(getSelf(),
                new InetSocketAddress(StaticValues.server , StaticValues.port), 100), getSelf());
    }

    /**
     *  On receive message we have 3 types of messages:
     *  - {@link akka.io.Tcp.Bound}: server is ready to accept connection
     *  - {@link akka.io.Tcp.CommandFailed}: connection failed
     *  - {@link akka.io.Tcp.Connected}: connection successful
     * @param msg Received Message
     * @throws Exception
     */
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Tcp.Bound) {
            manager.tell(msg, getSelf());

        } else if (msg instanceof Tcp.CommandFailed) {
            getContext().stop(getSelf());

        } else if (msg instanceof Tcp.Connected) {
            final Tcp.Connected conn = (Tcp.Connected) msg;
            manager.tell(conn, getSelf());
            handler = getContext().actorOf(
                    Props.create(SimplisticHandler.class));
            getSender().tell(TcpMessage.register(handler, true, true), getSelf());
            StaticValues.isConnected = true;
        } else {
            /**
             * Unknown message
             */
            unhandled(msg);
        }
    }

}