package ee.ut.ta.search;

public class SearchResult {
	
	private int word;
	private double distance;
	public void setWord(int word) {
		this.word = word;
	}
	public int getWord() {
		return word;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDistance() {
		return distance;
	}

}
