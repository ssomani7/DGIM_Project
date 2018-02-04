import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class SocketThread extends Thread{  
	static DGIMAlgo obj = new DGIMAlgo();
	static boolean onHold = false;
	private List<Integer> windowSizeList = new LinkedList<Integer>();
	static int windowSizeListCnt=0;
	private int windowSize1;
	private int portNumber1;
	private String hname ;
	private Socket client;

	public SocketThread(int portNumber2, int windowSize2,String hostnameString) { 
		portNumber1 = portNumber2;
		windowSize1 = windowSize2;
		hname = hostnameString;	
	}

	public void run(){  
		try {
			InetAddress ipAddress = InetAddress.getByName(hname); 
			client = new Socket(ipAddress, portNumber1);
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			Scanner in = new Scanner(new InputStreamReader(client.getInputStream()));
			int timeStamp = 1;
			try {
				P2.lock.acquire();
			} catch (InterruptedException e1) {
				System.out.println(e1);
			}
			P2.lock.release();
			//checks whether the input stream has next element
			while(in.hasNext()) {	  										  					
				String s1 = in.next();	
				try {
					P2.lock.acquire();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				System.out.print(s1);
				P2.lock.release();
				if(windowSizeList.size()>=windowSize1) {
					if(windowSizeList.get(0).intValue()==1)
						windowSizeListCnt--;
					windowSizeList.remove(0);
				}
				windowSizeList.add(Integer.parseInt(s1));
				int temp1 = Integer.parseInt(s1);								
				if(temp1 == 1) {
					while(onHold);
					obj.addBit(timeStamp);	//entry in DGIMAlgo code						
					timeStamp +=1;
					windowSizeListCnt++;
				}
				else {
					timeStamp +=1;
				}		   	
			}
			if(null!=in) {
				in.close();
			}
			client.close();
		}catch (UnknownHostException e) {
			System.out.println(e);
		}
		catch (IOException e) {			
			System.out.println(e);
		}
	}  
} 