package com.quanmin.activemq;

import javax.jms.Connection;  
import javax.jms.ConnectionFactory;  
import javax.jms.Destination;  
import javax.jms.MessageConsumer;  
import javax.jms.Session;  
  
import org.apache.activemq.ActiveMQConnection;  
import org.apache.activemq.ActiveMQConnectionFactory;  
  
  
public class Subscribe   
{  
    public static void main(String[] args)   
    {  
        ConnectionFactory connectionFactory;  
        Connection connection = null;  
        Session session;  
        Destination destination;  
        MessageConsumer consumer;  
          
        connectionFactory = new ActiveMQConnectionFactory(  
                ActiveMQConnection.DEFAULT_USER,  
                ActiveMQConnection.DEFAULT_PASSWORD,  
                "tcp://localhost:61616");  
        try   
        {  
            connection = connectionFactory.createConnection();  
            connection.start();  
            session = connection.createSession(Boolean.FALSE,  
                    Session.AUTO_ACKNOWLEDGE);  
              
            destination = session.createTopic("pub/sub");  
            consumer = session.createConsumer(destination);  
              
            //设置指定的监听器  
            consumer.setMessageListener(new MyListener());  
              
            Thread.sleep(100*1000);  
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        finally   
        {  
            try   
            {  
                if (null != connection)  
                    connection.close();  
            }  
            catch (Throwable ignore)   
            {  
            }  
        }  
      
    }  
}  