package automation.library.mq.core.rabbitmq;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Helper class with wrapper methods to leverage the RabbitMQ Java client library to allows Java applications to
 * interface with RabbitMQ.
 */
public class RabbitMQ {

    private Connection connection = null;
    private Channel channel = null;

    private String mqSever;
    private int port=0;
    private String username;
    private String password;
    private String exchangeName;
    private String exchangeType;
    private String queueName;
    private String bindingKey;

    private static final Logger LOGGER = LogManager.getLogger(RabbitMQ.class);

    public RabbitMQ(String mqSever, int port, String username, String password, String exchangeName, String exchangeType, String queueName, String bindingKey) {
        this.mqSever = mqSever;
        this.port = port;
        this.username = username;
        this.password = password;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.queueName = queueName;
        this.bindingKey = bindingKey;
    }

    public void createConnection() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.mqSever);

        if(this.port != 0){
            factory.setPort(this.port);
        }
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            LOGGER.error("Exception occurred during creating connection to RabbitMQ Server " + this.mqSever, e);
            e.printStackTrace();
        }
    }

    public void createExchange() {

        try {
            switch (this.exchangeType.toLowerCase()) {
                case "topic":
                    this.channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC);
                    break;
                case "headers":
                    this.channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.HEADERS);
                    break;
                case "fanout":
                    this.channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.FANOUT);
                    break;
                default:
                    this.channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.DIRECT, false);
                    break;
            }
        } catch (IOException e) {
            LOGGER.error("Exception occurred during creating exchange at RabbitMQ Server " + this.mqSever, e);
            e.printStackTrace();
        }
    }

    public void createQueue() {
        try {
            if (this.queueName == null) {
                this.queueName = this.channel.queueDeclare().getQueue();
            } else {
                this.channel.queueDeclare(this.queueName, false, false, false, null);
            }
        } catch (IOException e) {
            LOGGER.error("Exception occurred during declaring queue on exchange " + this.exchangeName, e);
            e.printStackTrace();
        }
    }

    public void bindQueue(){

        if(this.bindingKey!=null){
            try {
                channel.queueBind(this.queueName, this.exchangeName, this.bindingKey);
            } catch (IOException e) {
                LOGGER.error("Exception occurred during queue binding to exchange " + this.exchangeName, e);
                e.printStackTrace();
            }
        }
    }

    public void putMessage(String message){
        try {
            this.channel.basicPublish(this.exchangeName, "", null, message.getBytes("UTF-8"));
        } catch (IOException e) {
            LOGGER.error("Exception occurred during putting message into exchange " + this.exchangeName,e);
            e.printStackTrace();
        }
    }

    public void putMessage(String message, String routingKey){
        try {
            this.channel.basicPublish(this.exchangeName, routingKey, null, message.getBytes("UTF-8"));
        } catch (IOException e) {
            LOGGER.error("Exception occurred during putting message into exchange " + this.exchangeName,e);
            e.printStackTrace();
        }
    }

    public void putMessage(String message, String routingKey, AMQP.BasicProperties properties){
        try {
            this.channel.basicPublish(this.exchangeName, routingKey, properties, message.getBytes("UTF-8"));
        } catch (IOException e) {
            LOGGER.error("Exception occurred during putting message into exchange " + this.exchangeName,e);
            e.printStackTrace();
        }
    }

    public String getMessage(){

        final CountDownLatch countDown = new CountDownLatch(1);
        final AtomicReference<String> result = new AtomicReference<String>();

        try {
            this.channel.basicConsume(this.queueName, false, new DefaultConsumer(this.channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    result.set(new String(body, "UTF-8"));
                }
            });
            countDown.await(5, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result.get();
    }

    public void purgeQueue() throws IOException {
        try {
            this.channel.queuePurge(queueName);
        } catch (Exception e) {
            System.out.println("Could not purge " + queueName + e);
        }
    }

    public void deleteQueue() throws IOException {
        try {
            this.channel.queueDelete(queueName);
        } catch (Exception e) {
            System.out.println("Could not purge " + queueName + e);
        }
    }

    public void closeConnection(){
        try {
            this.channel.close();
            this.connection.close();
        }catch (IOException | TimeoutException e) {
            LOGGER.error("Exception occurred during closing RabbitMq connections/channel " + channel,e);
            e.printStackTrace();
        }
    }

    public Channel getChannel(){
        return this.channel;
    }

    public int getQueueDepth(String queueName){
        // returns the number of messages in Ready state in the queue
        AMQP.Queue.DeclareOk response = null;
        try {
            response = this.channel.queueDeclarePassive(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getMessageCount();
    }
}
