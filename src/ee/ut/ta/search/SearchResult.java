package ee.ut.ta.search;

public class SearchResult {
	
	private String word;
	private double distance;
	private int type;
	
	public SearchResult(String pWord, double pDist, int pType){
		word  = pWord;
		distance = pDist;
		type = pType;
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

}
