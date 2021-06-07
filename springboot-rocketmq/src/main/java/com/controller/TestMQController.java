package com.controller;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lei on 2021/6/7.
 */
@RestController
@RequestMapping("/mqProducer")
public class TestMQController {
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @GetMapping("/send")
    public SendResult send(String msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if (msg ==null || "".equals(msg)) {
            return new SendResult();
        }
//        LOGGER.info("发送MQ消息内容：" + msg);
        Message sendMsg = new Message("TestTopic", "TestTag", msg.getBytes());
        // 默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
//        LOGGER.info("消息发送响应：" + sendResult.toString());
        return sendResult;
    }
}
