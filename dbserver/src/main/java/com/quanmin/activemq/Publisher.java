package com.quanmin.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher 
{
	private static Logger logger = LoggerFactory.getLogger(Publisher.class);
	
	public static void main(String[] args) 
	{
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageProducer producer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "failover:tcp://localhost:61616");
        try 
        {
            connection = connectionFactory.createConnection();
            connection.start();
            
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            //注意这里与ptp例子的区别，使用null作为destination
            producer = session.createProducer(null);
            
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            sendMessage(session, producer);
            
            session.commit();
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

	private static void sendMessage(Session session, MessageProducer producer) throws JMSException 
	{
		Destination destination = session.createTopic("pub/sub");
		Message message = session.createMapMessage();
		message.setStringProperty("company", "alibaba");
		message.setStringProperty("department", "b2b");
		logger.info("destination is {};message is ready to send", destination);
		
		producer.send(destination, message);
	}
}
