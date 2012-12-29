package ee.ut.ta.dict;

import android.util.Log;

public class Letter {
	static final String TAG = "letter"; // tag for LogCat

	private String a = "";
	private String b = "";
	
	public Letter(String[] parts){
		if(parts.length!=2){
			Log.e(TAG, "Wrong parts, length: "+parts.length);
		}else {
			assignValues(parts);
		}
		
	}

	private void assignValues(String[] parts) {
		this.a=parts[0];
		this.b=parts[1];
	}
	
	public Letter(String row){
		String[] parts = row.split(":",	2);
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

}
