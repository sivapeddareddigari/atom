package automation.library.mq.core.demo;

import automation.library.mq.core.rabbitmq.RabbitMQ;
import automation.library.mq.core.rabbitmq.RabbitMQBuilder;
import com.rabbitmq.client.AMQP;
import org.testng.annotations.Test;

import javax.jms.JMSException;
import java.io.IOException;

public class QueueBuilderDemo {

	@Test
	public void testRabbitMQ() throws JMSException, InterruptedException, IOException {
		RabbitMQ rabbit1 = new RabbitMQBuilder()
				.setMqSever("localhost")
				.setPort(5672)
				.setExchangeName("amq.topic")
				.setExchangeType("topic")
				.setBindingKey("#")
				.setUsername("guest")
				.setPassword("guest")
				.getRabbitMQ();
		rabbit1.createConnection();
		rabbit1.createQueue();
		rabbit1.bindQueue();
		System.out.println("Number of Messages on Queue before send="+rabbit1.getQueueDepth("MYQUEUE"));
		rabbit1.putMessage("This is a test message" +Math.random());
		rabbit1.putMessage("This is a test message" +Math.random());

		AMQP.BasicProperties prop = new AMQP.BasicProperties.Builder()
				.expiration("60000")
				.build();

		rabbit1.putMessage("This is another test message" +Math.random(), "", prop);
		Thread.sleep(3000);
		System.out.println("Number of Messages on Queue after send="+rabbit1.getQueueDepth("MYQUEUE"));
		String message = rabbit1.getMessage();
		System.out.println("First message="+message);
		System.out.println("Number of Messages on Queue after get="+rabbit1.getQueueDepth("MYQUEUE"));
		rabbit1.closeConnection();

		RabbitMQ rabbit2 = new RabbitMQBuilder()
				.setMqSever("localhost")
				.setPort(5672)
				.setQueueName("MYQUEUE")
				.setUsername("guest")
				.setPassword("guest")
				.getRabbitMQ();
		rabbit2.createConnection();
		rabbit2.createQueue();
		rabbit2.bindQueue();
		rabbit2.purgeQueue();
		System.out.println("Number of Messages on Queue after purge="+rabbit2.getQueueDepth("MYQUEUE"));

		rabbit2.closeConnection();
	}
}
