package client_components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

public class Client {
	String mailServer;
    InetAddress mailHost;
    InetAddress localHost;
    //Socket smtpSocket;
    BufferedReader infromServer;
    BufferedReader infromUser;
    PrintWriter pr;
    Socket smtp;
    String state;
    //String msg;
    
    public Client()
    {
    	state = "Closed";
    	System.out.println("Present State: "+state);
    	infromUser = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public void Begin() throws IOException
    {
    	//mailServer = new String("webmail.buet.ac.bd");
    	mailServer = new String("smtp.sendgrid.net");
    	mailHost = InetAddress.getByName(mailServer);
    	localHost = InetAddress.getLocalHost();
    	//smtp = new Socket(mailHost,25);
    	smtp = new Socket(mailHost,587);
		infromServer =  new BufferedReader(new InputStreamReader(smtp.getInputStream()));
        pr = new PrintWriter(smtp.getOutputStream(),true);
        //System.out.println(infromServer.readLine());
    }
    
    public String updateState(String s,String t) throws IOException
    {
    	if(s.equalsIgnoreCase("221"))
    	{
    		state = "FINISHED";
    	}
    	else if(state.equalsIgnoreCase("Closed"))
    	{
    		if(s.equalsIgnoreCase("500"))
    		{
    			state = "Closed";
    		}
    		else if(s.equalsIgnoreCase("220"))
    		{
    			state = "Begin";
    		}
    	}
    	else if(state.equalsIgnoreCase("Begin"))
    	{
    		if(s.equalsIgnoreCase("250") && t.equalsIgnoreCase("HELO"))
    		{
    			 state = "Wait";
    			 pr.println("AUTH LOGIN") ;
    		     System.out.println(infromServer.readLine()) ;
    		     pr.println(new String(Base64.getEncoder().encode("tanjimdipon83".getBytes())));
    		     System.out.println(infromServer.readLine()) ;
    		     pr.println(new String(Base64.getEncoder().encode("mun106473ir".getBytes())));
    		     System.out.println(infromServer.readLine()) ;
    		     
    		}
    		else
    		{
    			state = "Begin";
    		}
    	}
    	else if(state.equalsIgnoreCase("Wait"))
    	{
    		if(s.equalsIgnoreCase("250") && t.equalsIgnoreCase("Mail"))
    		{
    			state = "envelope created, no recipients";
    		}
    		else
    		{
    			state = "Wait";
    		}
    	}
    	else if(state.equalsIgnoreCase("envelope created, no recipients"))
    	{
    		if(s.equalsIgnoreCase("250") && t.equalsIgnoreCase("RCPT"))
    		{
    			state = "recipients set";
    		}
    		else if(s.equalsIgnoreCase("250") && t.equalsIgnoreCase("RSET"))
    		{
    			state = "Wait";
    		}
    		else
    		{
    			state = "envelope created, no recipients";
    		}
    	}
    	else if(state.equalsIgnoreCase("recipients set"))
    	{
    		if(s.equalsIgnoreCase("354") && t.equalsIgnoreCase("DATA"))
    		{
    			state = "writing mail";
    		}
    		else if(s.equalsIgnoreCase("250") && t.equalsIgnoreCase("RSET"))
    		{
    			state = "Wait";
    		}
    		else
    		{
    			state = "recipients set";
    		}
    	}
    	else if(state.equals("writing mail"))
    	{
    		if(s.equals("500"))
    		{
    			state = "writing mail";
    		}
    		else if(s.equals("250"))
    		{
    			state = "ready to deliver";
    		}
    	}
    	else if(state.equals("ready to deliver"))
    	{
    		if(s.equals("Ok"))
    		{
    			state = "Wait";
    		}
    		else if(s.equals("Error"))
    		{
    			state = "attempt to deliver";
    		}
    	}
    	
    	return state;
    	
    }
    
    

}
