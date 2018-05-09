
package rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import event.Event;
import util.EventSerializationUtil;

/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class EventConsumer extends DefaultConsumer{

  /**
   * Constructs a new instance.
   * @param channel
   */
  public EventConsumer(Channel channel) {
    super(channel);
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
    // TODO process event
  }

}
