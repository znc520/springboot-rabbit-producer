package com.znc.rabbitmq.springbootrabbitproducer.component;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author zhunc
 * @Date 2022/5/14 17:34
 */
@Component
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 这里就是确认消息的回调监听接口，用于确认消息是否被broker所收到
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         *
         * @param correlationData 作为唯一的标识
         * @param ack 是否落盘成功
         * @param cause 失败的一些一次消息
         */
         @Override
         public void confirm(CorrelationData correlationData, boolean ack, String cause) {
             System.err.println("消息结果："+ ack + "  correlationData:" + correlationData.getId() +" cause:"+ cause);
         }
     };

    /**
     * 对外发送消息的方法
     * @param message  具体消息内容
     * @param properties 额外的附带属性
     */
    public void send(Object message, Map<String, Object> properties) throws  Exception{

        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        //指定业务唯一id
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                System.out.println("post to do:" + message);
                return message;
            }


        };
        rabbitTemplate.convertAndSend("exchange-1",
                 "springboot.rabbit", message, messagePostProcessor, correlationData);

    }
}
