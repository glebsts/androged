package ee.ut.ta.search;

public class SearchResult implements Comparable<SearchResult> {
	
	private String word;
	private double distance;
	private int type;
	
	public SearchResult(String pWord, double pDist, int pType){
		word  = pWord;
		distance = pDist;
		type = pType;
	}
	
	public SearchResult(String str){
		
		String[] parts = str.split("\\|", 4);
		this.word = parts[1];
		this.distance = Double.parseDouble(parts[2]);
		this.type = Integer.parseInt(parts[3]);
		
		
	}
	
	
	public void setWord(String word) {
		this.word = word;
	}
	public String getWord() {
		return word;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDistance() {
		return distance;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}

	public int compareTo(SearchResult i2) {
		int result = 0;
		if(this.type<i2.type){
			result = -1;
			return result;
		}
		if(this.type>i2.type){
			result = 1;
			return result;
		}
		
		if(this.distance < i2.distance){
			result = -1;
			return result;
		}
		if(this.distance > i2.distance){
			result = 1;
			return result;
		}
		
		result = this.word.compareTo(i2.word);
		
		return result;
	}

	@Override
	public String toString() {
		return String.format("%d %1.2f %s", type, distance, word);
	}
}
