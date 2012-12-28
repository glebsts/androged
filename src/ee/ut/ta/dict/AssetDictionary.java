package ee.ut.ta.dict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;



public class AssetDictionary extends AbstractDictionary {
	static final String TAG = "ged.assetdictionary"; // tag for LogCat
	@Override
	List<String> loadWords() {
		try {
			if(words==null){
				words = new ArrayList<String>();
			}
			BufferedReader br = new BufferedReader(new FileReader("file:///android_asset/dict/"+this.getFileName()));
			String line;
		    while ((line = br.readLine()) != null) {
		        words.add(line);
		    }

		} catch (Exception e) {
			Log.e(TAG, "Error loading words: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}


}
