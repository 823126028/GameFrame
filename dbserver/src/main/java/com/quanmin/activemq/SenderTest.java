package com.quanmin.activemq;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SenderTest {  
  
    static int size = 200000;  
    static Session session;  
    static MessageProducer producer;  
    static Queue queue;  
    static Connection connection;  
    static String str = "[{'flag':'1','value':'8854c92e92404b188e63c4031db0eac9','label':'交换机(虚机)'},{'flag':'1','value':'3f367296c2174b7981342dc6fcb39d64','label':'防火墙'},{'flag':'1','value':'8a3e05eeedf54f8cbed37c6fb38c6385','label':'负载均衡'},{'flag':'1','value':'4f0ebc601dfc40ed854e08953f0cdce8','label':'其他设备'},{'flag':'1','value':'6','label':'路由器'},{'flag':'1','value':'4','label':'交换机'},{'flag':'1','value':'b216ca1af7ec49e6965bac19aadf66da','label':'服务器'},{'flag':'1','value':'7','label':'安全设备'},{'flag':'1','value':'cd8b768a300a4ce4811f5deff91ef700','label':'DWDM\\SDH'},{'flag':'1','value':'5','label':'防火墙(模块)'},{'flag':'1','value':'01748963956649e589a11c644d6c09b5','label':'机箱'}]";  
  
    public static void init_connection() throws Exception {  
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.useAsyncSend=true");  
        connection = factory.createConnection();  
        connection.start();  
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
        queue = session.createQueue("java.activemq.tps");  
        producer = session.createProducer(queue);  
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);  
    }  
  
    public static void sendMessage(String msg) {  
        TextMessage message;  
        try {  
            message = session.createTextMessage();  
            message.setText(str);  
            producer.send(message);  
        } catch (JMSException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void close() throws Exception {  
        connection.close();  
    }  
  
    public static void main(String[] arg) throws Exception {  
    	System.out.println(str.getBytes().length);
//        long start = System.currentTimeMillis();  
//        ExecutorService es = Executors.newFixedThreadPool(10);  
//        final CountDownLatch cdl = new CountDownLatch(size);  
//        init_connection();  
//        for (int a = 0; a < size; a++) {  
//            es.execute(new Runnable() {  
//                @Override  
//                public void run() {  
//                    sendMessage(str);  
//                    cdl.countDown();  
//                }  
//            });  
//        }  
//        cdl.await();  
//        es.shutdown();  
//        long time = System.currentTimeMillis() - start;  
//        System.out.println("插入" + size + "条JSON，共消耗：" + (double)time / 1000 + " s");  
//        System.out.println("平均：" + size / ((double)time/1000) + " 条/秒");  
//        close();  
    }  
}  