package ee.ut.ta.dict.transf;

import android.util.Log;

public class Transformation {
	static final String TAG = "ged.transformation"; // tag for LogCat

	private String a = "";
	private String b = "";
	private double weight = 0.0;

	public Transformation(String[] parts){
		if(parts.length!=3){
			Log.e(TAG, "Wrong parts, length: "+parts.length);
		}else {
			assignValues(parts);
		}
		
	}

	private void assignValues(String[] parts) {
		this.a=parts[0];
		this.b=parts[1];
		this.weight=Double.parseDouble(parts[2]);
	}
	
	public Transformation(String row){
		String[] parts = row.split(":",	3);
		assignValues(parts);		
	}
	
	void setA(String a) {
		this.a = a;
	}

	String getA() {
		return a;
	}

	void setB(String b) {
		this.b = b;
	}

	String getB() {
		return b;
	}

	void setWeight(double weight) {
		this.weight = weight;
	}

	double getWeight() {
		return weight;
	}
}
