package client_components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Client_Controller {
	
	volatile static int x;
	public static void main(String[] args) throws IOException {
		Client s = new Client();
		String m,n;
	    int flag=0;
		int f1=0,f2=0,f3=0;
		int tstart = 0;
		String []y;
		FileReader fr=new FileReader("F:\\eclipse-workspace\\CSE322_Offline1\\src\\client_components\\Input.txt");    
        BufferedReader br=new BufferedReader(fr);
        //long x,z;
		
		while(true)
		{
			Timer tm = new Timer(s);
			Thread tm1 = new Thread(tm);
			System.out.print("Client: ");
			tm1.start();
			x = 0;
			m = s.infromUser.readLine();
			x = 1;
			//m = br.readLine();
			if(s.state.equals("writing mail"))
			{
				y = m.split(":");
				if(y[0].equals("Subject"))
				{
					f1=1;
					tstart++;
				}
				else if(y[0].equals("From"))
				{
					f2=1;
					tstart++;
				}
				else if(y[0].equals("To"))
				{
					f3 = 1;
					tstart++;
				}
			}
	
			if(m.equalsIgnoreCase("begin")  && flag==0)
			{
				s.Begin();
				flag = 1;
			}
			else if(m.equals("HELO") && flag==1)
			{
				s.pr.println("HELO "+s.localHost.getHostName());
			}
			else
			{
				if(s.state.equalsIgnoreCase("Closed"))
				{
					System.out.println("Establish a connection first");
				}
				else
				{
					if((tstart == 2 && f3 == 0) || (tstart == 3 && f3 == 1))
					{
						s.pr.println();
						tstart = 0;
					}
					s.pr.println(m);
					System.out.println(m);
				}
			}
			
			if((flag == 1 && s.state.equals("writing mail") && !(m.equals("."))) || flag == 0)
			{
				n = "500";
			}
			else
			{
				n = s.infromServer.readLine();
				System.out.println(n);
			}
			
			//System.out.println("HELLOA");
			//System.out.println(m.substring(0, 4));
			if(m.length()>=4)
			{
				System.out.println("Present state: "+s.updateState(n.substring(0, 3),m.substring(0, 4)));
			}
			else
			{
				System.out.println("Present state: "+s.updateState(n.substring(0, 3),"null"));
			}
			
			if(s.state.equals("ready to deliver"))
			{
				if(f1==1 && f2==1)
				{
					System.out.println("Present state: "+s.updateState("Ok","null"));
				}
				else
				{
					System.out.println("Present state: "+s.updateState("Error","null"));
				}
				f1 = 0;
				f2 = 0;
				f3 = 0;
			}
			
			if(s.state.equals("FINISHED"))
			{
				s.pr.close();
				s.infromServer.close();
				s.infromUser.close();
				break;
			}
			
		}
		System.out.println("Came out");

	}

}

class Timer implements Runnable
{
	Client k;
	
	public Timer(Client t)
	{
		k = t;
	}
	
	@Override
	public void run() {
		long j = System.currentTimeMillis();
		while(true)
		{
			if((System.currentTimeMillis()-j)>=20000 || (Client_Controller.x == 1))
			{
				break;
			}
		}
		
		if(Client_Controller.x != 1)
		{
			System.out.println("Request timed out, Run the code again");
			System.exit(0);
		}
		
	}

}
