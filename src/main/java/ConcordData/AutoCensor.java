package ConcordData;

public class AutoCensor implements TextCheck {

	@Override
	public String CheckString(long userId, String in) {
		// TODO Auto-generated method stub
		return ClearBannedWord(in);
	}
	
	public String name = "AutoCensor";
	
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
		return null;
	}

	@Override
	public void setAbbr(String[] abbr) {
		return;
	}

	@Override
	public String[] getFull() {
		return null;
	}

	@Override
	public void setFull(String[] full) {
		return;
	}


	public String[] WordList;
	
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
		
		boolean marked = false;
		for(int i = 0; i < partialWord.length; i++) {
			for(int j = 0; j < WordList.length; j++) {
				if(partialWord[i].equalsIgnoreCase(WordList[j])) {
					if(i != 0)
						builtMessage += " [redacted]";
					else
						builtMessage += "[redacted]";
					marked = true;
					break;
				}	
			}
			if(!marked)
				if(i != 0)
					builtMessage += " " + partialWord[i];
				else
					builtMessage += partialWord[i];
			marked = false;
		}
		
		return builtMessage;
	}

}
