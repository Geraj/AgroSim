
package calculations.simulation.event;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import event.Event;
import util.EventSerializationUtil;

/**
 * Consumes events from rabbitMQ
 * 
 * @author Geraj
 */
public class RabbitMessageConsumer extends DefaultConsumer{

  /** Action to perform for event*/
  private EventProcessor processor;
  /**
   * Constructs a new instance.
   * @param channel
   */
  public RabbitMessageConsumer(Channel channel, EventProcessor processor) {
    super(channel);
    this.processor = processor;
  }
  
  /**
   * 
   * @see com.rabbitmq.client.DefaultConsumer#handleDelivery(java.lang.String, com.rabbitmq.client.Envelope, com.rabbitmq.client.AMQP.BasicProperties, byte[])
   */
  @Override
  public void handleDelivery(String consumerTag, Envelope envelope,
                             AMQP.BasicProperties properties, byte[] body)
      throws IOException {        
    Event o = EventSerializationUtil.createEventFromByteArray(body);
    System.out.println(" [x] Received '" + o + "'");
    processor.handleSimulationEvent(o);
  }

}
