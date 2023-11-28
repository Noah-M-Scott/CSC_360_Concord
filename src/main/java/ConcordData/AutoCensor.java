package ConcordData;

public class AutoCensor implements TextCheck {

	@Override
	public String CheckString(long userId, String in) {
		return ClearBannedWord(in);
	}
	
	private String name = "AutoCensor";

	private String[] WordList;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String[] getWordList() {
		return WordList;
	}

	@Override
	public void setWordList(String[] wordList) {
		WordList = wordList;
	}
	
	@Override
	public String[] getAbbr() {
		return new String[0];
	}

	@Override
	public void setAbbr(String[] abbr) {
		return;
	}

	@Override
	public String[] getFull() {
		return new String[0];
	}

	@Override
	public void setFull(String[] full) {
		return;
	}
	
	public AutoCensor(){
		super();
	}
	
	public AutoCensor(String[] inList){
		super();
		
		WordList = inList;
	}
	
	private String ClearBannedWord(String in) {
		
		String builtMessage = "";
		String[] partialWord = in.split("\\s+");
		
		for(int i = 0; i < partialWord.length; i++) {
		
			if(i != 0)
				builtMessage += " ";

			
			if(!matchBanWord(partialWord[i]))		
					builtMessage += partialWord[i];
			else 
					builtMessage += "[redacted]";
			
		}
		
		return builtMessage;
	}

	private boolean matchBanWord(String a) {
		for(int j = 0; j < WordList.length; j++) {
			if(a.equalsIgnoreCase(WordList[j])) {
				return true;
			}	
		}
		return false;
	}
	
}
