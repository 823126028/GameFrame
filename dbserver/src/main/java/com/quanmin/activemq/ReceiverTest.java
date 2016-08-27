package com.quanmin.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiverTest {
	 public static void main(String[] args)   {  
	        ConnectionFactory connectionFactory;  
	        Connection connection = null;  
	        Session session;  
	        Destination destination;  
	        MessageConsumer consumer;  
	        int i = 0;
	        connectionFactory = new ActiveMQConnectionFactory(  
	                ActiveMQConnection.DEFAULT_USER,  
	                ActiveMQConnection.DEFAULT_PASSWORD,  
	                "tcp://localhost:61616");  
	        try  {  
	            connection = connectionFactory.createConnection();  
	            connection.start();  
	            session = connection.createSession(Boolean.FALSE,  
	                    Session.AUTO_ACKNOWLEDGE);  
	              
	            destination = session.createQueue("java.activemq.tps");  
	            consumer = session.createConsumer(destination);
	            long beforeTime = System.currentTimeMillis();
	            while ( true) {
                    //设置接收者接收消息的时间，为了便于测试，这里设定为100s
                    TextMessage message = (TextMessage) consumer.receive(5 * 1000);
                    if ( null != message) {
                    	  i++;
                    	  if(i == 1){
                    		  beforeTime = System.currentTimeMillis();
                    	  }
                    	  if(i >= 200000)
                    		  System. out.println("线程收到消息:time" + (System.currentTimeMillis()- beforeTime ) / 1000+ ";" + message.getText());
                    } else {
                          continue;
                    }
               }
         } catch (Exception e) {
               e.printStackTrace();
         } finally {
               try {
                    if ( null != connection)
                          connection.close();
               } catch (Throwable ignore) {

               }
         }

    }
}
