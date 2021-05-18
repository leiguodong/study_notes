package MQ.rockectmq.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**消息接受者
 * Created by lei on 2021/5/18.
 */
public class Consumer {
    public static void main(String[] args) {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("group1");
        defaultMQPushConsumer.setNamesrvAddr("192.168.1.249:9876;192.168.1.248:9876");
        //设置消息消费模式  CLUSTERING 负载均衡(默认)  BROADCASTING 广播模式
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        try {
            defaultMQPushConsumer.subscribe("base","Tag1");
            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                //接受消息内容
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //接受顺序消息
            defaultMQPushConsumer.registerMessageListener(new MessageListenerOrderly() {

                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                    return null;
                }
            });
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}
