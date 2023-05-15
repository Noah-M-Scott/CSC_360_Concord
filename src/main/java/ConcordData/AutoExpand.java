package ConcordData;

public class AutoExpand implements TextCheck {

	@Override
	public String CheckString(long userId, String in) {
		return AutoExpandAbbreviation(in);
	}

	public String name = "AutoExpand";
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String[] getAbbr() {
		return Abbr;
	}

	@Override
	public void setAbbr(String[] abbr) {
		Abbr = abbr;
	}

	@Override
	public String[] getFull() {
		return Full;
	}

	@Override
	public void setFull(String[] full) {
		Full = full;
	}

	@Override
	public String[] getWordList() {
		return null;
	}

	@Override
	public void setWordList(String[] wordList) {
		return;
	}
	
	public String[] Abbr;
	public String[] Full;
	
	public AutoExpand() {
		super();
	}
	
	public AutoExpand(String[] ina, String[] inf) {
		super();
		Abbr = ina;
		Full = inf;
	}
	
	private String AutoExpandAbbreviation(String in) {
		String builtMessage = "";
		String[] partialWord = in.split("\\s+");
		
		boolean marked = false;
		for(int i = 0; i < partialWord.length; i++) {
			for(int j = 0; j < Abbr.length; j++) {
				if(partialWord[i].equalsIgnoreCase(Abbr[j])) {
					if(i != 0)
						builtMessage += " " + Full[j];
					else
						builtMessage += Full[j];
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
