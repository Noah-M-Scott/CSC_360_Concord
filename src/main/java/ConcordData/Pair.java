package ConcordData;

//Machine Generated
public class Pair<T1, T2> {
	private T1 key = null;
	private T2 value = null;

	public Pair(){
		
	}
	
    public Pair(T1 key, T2 value) {
        this.key = key;
        this.value = value;
    }
    
	public T1 getKey() {
		return key;
	}

	public T2 getValue() {
		return value;
	}

	public void setValue(T2 value) {
		this.value = value;
	}

	public void setKey(T1 key) {
		this.key = key;
	}
    
}
