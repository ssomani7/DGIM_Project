import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class QueryThread extends Thread{   
	private int windowSize3;

	QueryThread(int windowSize4){
		windowSize3 =windowSize4;  
	}

	public void run(){  

		String pattern = "what is the number of ones for last ([0-9]*) data[?]";
		Scanner in = new Scanner(new InputStreamReader(System.in));
		int k;
		String str;
		while((str = in.nextLine())!=null){
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);

			//matches the format of query
			if(m.matches()) { 
				k=Integer.parseInt(m.group(1));

				if(k<=windowSize3){	   		
					try {
						P2.lock.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("The number of ones of last "+k+" data is exact "+SocketThread.windowSizeListCnt);
					P2.lock.release();
				}
				else {
					ArrayList<ListElementVo> list = SocketThread.obj.list;				   
					SocketThread.onHold = true;

					int i= list.size()-1;
					int limit=(int) (list.get(i).pos - k);
					int sum=0;
					ListElementVo listElement;
					do {
						listElement = list.get(i);
						if(limit>=listElement.pos||i==0)
							break;
						sum+=listElement.key;
						i--;
					}while(true);
					System.out.println("The number of ones of last "+k+" data is estimated "+sum);
					SocketThread.onHold = false;
				}
			}		   
		}   
		if(null!=in) {
			in.close();
		}
	}  
} 