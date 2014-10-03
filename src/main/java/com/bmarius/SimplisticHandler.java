package com.bmarius;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mbd on 03.10.2014.
 * Custom handler for messages received in server
 */
public class SimplisticHandler extends UntypedActor {

    final LoggingAdapter log = Logging
            .getLogger(getContext().system(), getSelf());

    /**
     * first sender messages
     */
    private ActorRef sender;

    /**
     * On first {@link akka.io.Tcp.Received} message retain the sender for further messages
     * This actor will be the Tcp Server
     * @param msg
     * @throws Exception
     */
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Tcp.Received) {
            final ByteString data = ((Tcp.Received) msg).data();

            if(sender == null)
                sender = getSender();
            /**
             * Receive message and transform it from ByteString to byte[] and then deserialize into Message object
             */
            byte[] dataByte = data.toArray();
            Message message = null;
            try {
                message = (Message) this.deserialize(dataByte);
            }catch (EOFException e){
                /**
                 * this is important sometimes we receive EOFException
                 * Is a weird thing, still diggin'
                 * For the moment just put in dumped messages and process later
                 */
                log.error(e.getMessage());
                StaticValues.dumped.add(data);
            }
            log.info("Message " + message.getMessage());

            Thread.sleep(1000);

            /**
             * Ack message that need to be send to the client
             */
            ACK ack = ACK.newInstace();
            ack.setMessage("OK!");

            /**
             * if writing is suspended then resume it
             */
            sender.tell(TcpMessage.resumeWriting(), getSelf());

            sender.tell(TcpMessage.write(ByteString.fromArray(this.serialize(ack))), getSelf());

            /**
             * this method {@link akka.io.TcpMessage confirmedClose()} is important because we need to tell to the manager that need to flush the output Inbox
             * and partial close the connection
             */
            sender.tell(TcpMessage.confirmedClose(), getSelf());

            this.performDumped();
        } else if (msg instanceof Tcp.ConnectionClosed) {
            getContext().stop(getSelf());
        }
    }

    /**
     * Simple deserialize method
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    /**
     * Simple serialize method
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }


    public void performDumped(){
        if(StaticValues.dumped.size() == 0)
            return;
        log.info("Processing dumped chunk of size: "+StaticValues.dumped.size());
        for(ByteString data : StaticValues.dumped){
            byte[] dataByte = data.toArray();
            ACK ack = null;
            try {
                ack = (ACK) this.deserialize(dataByte);
            }catch (EOFException e){
                log.error(e.getMessage());
                StaticValues.dumped.add(data);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info(ack.getMessage());
            sender.tell(TcpMessage.resumeReading(), getSelf());
            sender.tell(TcpMessage.write(ByteString.fromArray(dataByte)), getSelf());
            sender.tell(TcpMessage.confirmedClose(), getSelf());
        }
    }
}