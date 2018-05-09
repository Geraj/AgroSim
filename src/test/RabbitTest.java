
package test;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import event.Event;
import util.EventSerializationUtil;
/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class RabbitTest {
  
  private final static String EXCHANGE_NAME = "event_exchange";

  public static void main(String[] args) throws java.io.IOException,
  java.lang.InterruptedException, TimeoutException{
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");
    
    
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body)
          throws IOException {        
        Event o = EventSerializationUtil.createEventFromByteArray(body);
        System.out.println(" [x] Received '" + o + "'");                  
      }
    };
    channel.basicConsume(queueName, true, consumer);
    
  }
}
