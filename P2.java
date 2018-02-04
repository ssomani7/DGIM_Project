import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


//main class
public class P2 { 
	public static QueryThread queryThread ;
	public static Semaphore lock = new Semaphore(1);
	public static SocketThread socketThread;

	public static void main(String[] args) throws IOException {
		System.out.println("Enter Window Size=");
		Scanner in = new Scanner(System.in);
		int windowSize = in.nextInt();	
		System.out.println("Enter localhost:portnumber =");
		String ipAddressAndPort = in.next();

		String[] s = ipAddressAndPort.split(":");
		String s1 = s[0];  
		int portnumber = Integer.parseInt(s[1]);
		socketThread  = new SocketThread(portnumber,windowSize,s1); //thread-1
		socketThread.start();
		queryThread = new QueryThread(windowSize); //thread-2
		queryThread.start();
		while(!socketThread.isAlive()) {
			queryThread.interrupt();
			if(null!=in)
				in.close();
		}
	}
}
