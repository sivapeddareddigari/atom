package automation.library.mq.core.rabbitmq;

/**
 * Helper class to set the queue connection details. - server, port, username, password, exchangeName, exchangeType, queueName, bindingKey
 */
public class RabbitMQBuilder {

    private String mqSever;
    private int port;
    private String username;
    private String password;
    private String exchangeName;
    private String exchangeType;
    private String queueName;
    private String bindingKey;

    public RabbitMQBuilder setMqSever(String mqSever) {
        this.mqSever = mqSever;
        return this;
    }

    public RabbitMQBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public RabbitMQBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public RabbitMQBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public RabbitMQBuilder setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
        return this;
    }

    public RabbitMQBuilder setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
        return this;
    }

    public RabbitMQBuilder setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public RabbitMQBuilder setBindingKey(String bindingKey) {
        this.bindingKey = bindingKey;
        return this;
    }
    public RabbitMQ getRabbitMQ(){
        return new RabbitMQ(mqSever, port, username, password, exchangeName, exchangeType, queueName, bindingKey);
    }
}
