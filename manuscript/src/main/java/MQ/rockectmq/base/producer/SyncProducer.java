package MQ.rockectmq.base.producer;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * Created by lei on 2021/5/18.
 */
public class SyncProducer {

    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        //指定nameserve地址
        defaultMQProducer.setNamesrvAddr("192.168.1.249:9876;192.168.1.248:9876");
        try {
            //3、启动producer
            defaultMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            //4、创建消息对象，指定主题topic、tag和消息体
            /**
             * 参数一：消息主题topic
             * 参数二：消息tag
             * 参数三：消息内容
             */
            Message msg = new Message("base","Tag1",("Hello World"+i).getBytes());
            /**
             * 发送异步消息
             */

            /**
             * 发送单向消息
             */
            try {
                defaultMQProducer.sendOneway(msg);
            } catch (org.apache.rocketmq.remoting.exception.RemotingException e) {
                e.printStackTrace();
            }
            /**
             * 发送顺序消息
             */
            String orderId = "123";
            try {
                /**
                 * 参数一：消息对象
                 * 参数二：消息队列选择器
                 * 参数三：选择队列的业务标识（订单id）
                 */
                try {
                    defaultMQProducer.send(msg, new MessageQueueSelector() {
                        /**
                         *
                         * @param list  当前队列集合
                         * @param message   消息对象
                         * @param o      业务标识的参数
                         * @return
                         */
                        @Override
                        public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                            //不同的策略去选择队列
                            return list.get(Integer.parseInt(orderId)%list.size());
                        }
                    },orderId);
                } catch (RemotingException e) {
                    e.printStackTrace();
                }
            } catch (MQBrokerException e) {
                e.printStackTrace();
            }
            /**
             * 发送延时消息
             */
            try{
                //设置延时等级3，这个消息将在10s之后发送（现在只支持固定的几个时间，详看delayTImeLevel）
                msg.setDelayTimeLevel(3);
                defaultMQProducer.send(msg);
            }catch(MQBrokerException e) {
                e.printStackTrace();
            }
            //发送延时消息end
            /**
             * 事务消息
             * 先将消息（half message）写入 mq临时队列
             * 执行本地事务 知道事务最后 发送commit 或者rollback 通知mq本地事务状态
             * commit 将消息转至消息队列 rollback 删除临时消息
             *
             *
             */
            TransactionMQProducer transactionMQProducer = new TransactionMQProducer();
            transactionMQProducer.setTransactionListener(new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                      LocalTransactionState localTransactionState = null;
                      if(message.getKeys().equals("msg_1")){
                          localTransactionState = LocalTransactionState.COMMIT_MESSAGE;
                      }else if(message.getKeys().equals("msg_2")){
                          localTransactionState = LocalTransactionState.ROLLBACK_MESSAGE;
                      }else{
                          localTransactionState = LocalTransactionState.UNKNOW;
                      }
                      return localTransactionState;
                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                      if(null!= messageExt.getKeys()){
                        switch ((messageExt.getKeys())){
                            case "msg-3":
                                System.out.println("msg-3 unknow");
                                return LocalTransactionState.UNKNOW;
                            case "msg-4":
                                System.out.println("msg-4 commit_message");
                                return LocalTransactionState.COMMIT_MESSAGE;
                            case "msg-5":
                                System.out.println("msg-4 ROLLBACK_MESSAGE");
                                return LocalTransactionState.ROLLBACK_MESSAGE;
                        }
                    }
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            });
            //事务消息end
            defaultMQProducer.start();
            defaultMQProducer.shutdown();
        }
    }
}
