package com.quanmin.activemq;
import javax.jms.JMSException;  
import javax.jms.MapMessage;  
import javax.jms.Message;  
import javax.jms.MessageListener;  
  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
  
public class MyListener implements MessageListener   
{  
    private static Logger logger = LoggerFactory.getLogger(MyListener.class);  
      
    public void onMessage(Message message)   
    {  
        MapMessage mapMessage = (MapMessage) message;  
        try   
        {  
            String company = mapMessage.getStringProperty("company");  
            String department = mapMessage.getStringProperty("department");  
             
            System.out.println("company is {} ; department is {}" + company + "" + department);   
        } catch (JMSException e)   
        {  
            e.printStackTrace();  
        }  
    }  
  
}  
