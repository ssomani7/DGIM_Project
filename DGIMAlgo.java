import java.util.ArrayList;
import java.util.HashMap;


public class DGIMAlgo {
	HashMap<Integer,MapElementVo> map = new HashMap<>();
	ArrayList<ListElementVo> list= new ArrayList<>();
	
	public ArrayList<ListElementVo> getList() {
		return list;
	}

	public void displayMap() {
		int cnt =0;
		for (Integer key:map.keySet()) {
			MapElementVo rec = map.get(key);
			System.out.println("#"+ cnt++ +"[Key="+key+"]Cnt="+rec.cnt +", LatestIndex="+ rec.latestIndex);
		}
	}
	
	public void displayList() {
		int cnt=0;
		for (ListElementVo listPair : list) {
			System.out.println("#"+ cnt++ +"[TimeStamp="+listPair.pos+", Size="+listPair.key+"]");			
		}
	}
	
	public int ln(int value) {
		return (int) (Math.log(value)/Math.log(2));
	}
	
	public void addBit(int pos){
		int key = 1;
		list.add(new ListElementVo(pos,key ));
		int index=list.size()-1;
		updateMap(key, index);
	}

	private void updateMap(int key, int index) {
		if(!map.containsKey(key)) {
			map.put(key, new MapElementVo(1,index));
		}else{
			MapElementVo mapElem = map.get(key);
			mapElem.cnt+=1;
			mapElem.latestIndex=index;
				if(mapElem.cnt==4) {
					mapElem.cnt=2;
				
				int newKey=(int)Math.pow(2, ln(key)+1);
				int newIndex = mapElem.latestIndex-3;
				ListElementVo listElem = list.get(newIndex);
				listElem.key=newKey;
				listElem.pos=list.get(newIndex+1).pos;
				list.remove(newIndex+1);
				
				updateMap(newKey,newIndex);
				mapElem.latestIndex-=1;
			}
		}
	}	
}
