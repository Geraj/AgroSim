
package calculations.simulation.event;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

/**
 * Connecting to rabbit messaging queue exchange
 * 
 * @author Geraj
 */
public class RabbitExchangeConnecter {

  /** Default event exchange */
  private final static String EXCHANGE_NAME = "event_exchange";

  /** Exchange */
  private String exchange;

  /** host */
  private String host;
  
  /** connection */
  private Connection connection;

  /**
   * 
   * Constructs a new instance.
   */
  public RabbitExchangeConnecter() {
    exchange = EXCHANGE_NAME;
    host = "localhost";
  }
  

  /**
   * 
   * Constructs a new instance.
   * 
   * @param exchangeName
   * @param host
   */
  public RabbitExchangeConnecter(String exchangeName, String host) {
    this.exchange = exchangeName;
    this.host = host;
  }

  /**
   * 
   * Connect to exchange and start to consume
   * @param processor - event processor to use for handling message in the event
   */
  public void connect(EventProcessor processor) {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(host);
    try {
      connection = factory.newConnection();
      Channel channel = connection.createChannel();
      channel.exchangeDeclare(exchange, "fanout");
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, exchange, "");

      System.out.println(" [*] Waiting for messages.");
      Consumer consumer = new RabbitMessageConsumer(channel, processor);
      channel.basicConsume(queueName, true, consumer);
    } catch (IOException | TimeoutException e) {
      // TODO Add your own exception handling here, consider logging
      e.printStackTrace();
    }

  }
  
  public void disconnect() {
    try {
      connection.close();
    } catch (IOException e) {
      // TODO Add your own exception handling here, consider logging
      e.printStackTrace();
    }
  }
}
