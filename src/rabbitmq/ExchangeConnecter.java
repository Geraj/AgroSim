
package rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class ExchangeConnecter {

  /** Default event exchange */
  private final static String EXCHANGE_NAME = "event_exchange";

  /** Exchange */
  private String exchange;

  /** host */
  private String host;

  /**
   * 
   * Constructs a new instance.
   */
  public ExchangeConnecter() {
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
  public ExchangeConnecter(String exchangeName, String host) {
    this.exchange = exchangeName;
    this.host = host;
  }

  /**
   * 
   * Connect to exchange and start to consume
   */
  public void connect() {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(host);
    Connection connection;
    try {
      connection = factory.newConnection();
      Channel channel = connection.createChannel();
      channel.exchangeDeclare(exchange, "fanout");
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, exchange, "");

      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
      Consumer consumer = new EventConsumer(channel);
      channel.basicConsume(queueName, true, consumer);
    } catch (IOException | TimeoutException e) {
      // TODO Add your own exception handling here, consider logging
      e.printStackTrace();
    }

  }
}
