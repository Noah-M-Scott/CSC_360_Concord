package ConcordData;
import java.sql.Timestamp;

public class MsgData {
	public long MsgIndex;
	public long QuoteIndex;
	public String Text;
	public long UserId;
	public Timestamp MsgTimeStamp;
	public boolean deleted;
	
	public long getMsgIndex() {
		return MsgIndex;
	}
	public void setMsgIndex(long msgIndex) {
		MsgIndex = msgIndex;
	}
	public long getQuoteIndex() {
		return QuoteIndex;
	}
	public void setQuoteIndex(long quoteIndex) {
		QuoteIndex = quoteIndex;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public long getUserId() {
		return UserId;
	}
	public void setUserId(long userId) {
		UserId = userId;
	}
	public Timestamp getMsgTimeStamp() {
		return MsgTimeStamp;
	}
	public void setMsgTimeStamp(Timestamp msgTimeStamp) {
		MsgTimeStamp = msgTimeStamp;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
}
