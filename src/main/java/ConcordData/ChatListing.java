package ConcordData;

import java.util.ArrayList;

public class ChatListing {
	public long ChatId;
	public ArrayList<MsgData> Chat = new ArrayList<MsgData>();
	public String ChatName = "New Chat";
	
	
	public boolean deleteMsg(long index) {
		
		if( Chat.size() - 1 < index )
			return false;
		
		MsgData hold = Chat.get((int) index);  
		
		hold.deleted = true;
		hold.Text = "deleted";
		hold.QuoteIndex = hold.MsgIndex;
		return true;
	}
	
	public long addMsg(MsgData in){
		Chat.add(in);
		Chat.get(Chat.size() - 1).MsgIndex = Chat.size() - 1;
		return Chat.size() - 1;
	}
	
	public MsgData findMsgById(long index) {
		return Chat.get((int) index);
	}	
}
