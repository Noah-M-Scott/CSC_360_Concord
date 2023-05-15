package ConcordData;

import java.util.ArrayList;

public class ChatListing {
	public long ChatId;
	public ArrayList<MsgData> Chat = new ArrayList<MsgData>();
	public String ChatName = "New Chat";
	public ArrayList<TextCheck>	Check = new ArrayList<TextCheck>();
	
	public ArrayList<TextCheck> getCheck() {
		return Check;
	}

	public void setCheck(ArrayList<TextCheck> check) {
		Check = check;
	}

	public long getChatId() {
		return ChatId;
	}

	public void setChatId(long chatId) {
		ChatId = chatId;
	}

	public ArrayList<MsgData> getChat() {
		return Chat;
	}

	public void setChat(ArrayList<MsgData> chat) {
		Chat = chat;
	}

	public String getChatName() {
		return ChatName;
	}

	public void setChatName(String chatName) {
		ChatName = chatName;
	}

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
		for(int i = 0; i < Check.size(); i++)
			in.Text = Check.get(i).CheckString(in.UserId, in.Text);
		
		Chat.add(in);
		Chat.get(Chat.size() - 1).MsgIndex = Chat.size() - 1;
		return Chat.size() - 1;
	}
	
	public MsgData findMsgById(long index) {
		return Chat.get((int) index);
	}	
}
