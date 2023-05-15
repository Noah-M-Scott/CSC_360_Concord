package ConcordData;

public interface TextCheck {

	public String getName();
	
	public void setName(String name);
	
	public String CheckString(long userId, String in);
	
	public String[] getWordList();

	public void setWordList(String[] wordList);
	
	public String[] getAbbr();

	public void setAbbr(String[] abbr);

	public String[] getFull();

	public void setFull(String[] full);
	
}
