## Cucumber Library

#### 1. Purpose and Contents

The 'cucumber' library builds on the API Library and Selenium Library to enable browser/mobile app/api testing using Cucumber BDD.

The helper classes are split into 3 packages 'core', 'api' and 'selenium'.

| Package       | Class             | Purpose                                                      |
| ------------- | ------------------| ------------------------------------------------------------ |
| core          | Constants         | List of directory references used by the library classes relative to the test project |
| core.rabbitmq | RabbitMQ          | methods to allows Java applications to interface with RabbitMQ. |
| core.rabbitmq | RabbitMQBuilder   | Helper class to set the queue connection details.|


#### 2. Usage
#### 2.1 Core 
##### _Constants_

Holds the reference to the list of directories used by library classes relative to test project

- BASEPATH : src/test/resources/ folder path

##### _RabbitMQ_

Helper class with wrapper methods to leverage the RabbitMQ Java client library to allows Java applications to interface with RabbitMQ.

##### _RabbitMQBuilder_
Helper class to set the queue connection details. - server, port, username, password, exchangeName, exchangeType, queueName, bindingKey
```java
    public RabbitMQBuilder setMqSever(String mqSever)
    public RabbitMQBuilder setPort(int port) 
    public RabbitMQBuilder setUsername(String username)
    public RabbitMQBuilder setPassword(String password)
    public RabbitMQBuilder setExchangeName(String exchangeName)
    public RabbitMQBuilder setExchangeType(String exchangeType)
    public RabbitMQBuilder setQueueName(String queueName)
    public RabbitMQBuilder setBindingKey(String bindingKey)
    public RabbitMQ getRabbitMQ()
```