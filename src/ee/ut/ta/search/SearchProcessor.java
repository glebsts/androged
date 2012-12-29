package ee.ut.ta.search;

import android.content.Context;
import android.util.Log;
import ee.ut.ta.dict.IDictionary;

public class SearchProcessor implements Runnable {
	String searchTerm;
	SearchOptions searchOptions;
	IDictionary dict;
	Context ctx;
	static final String TAG = "ged.processor"; // tag for LogCat
	
	public SearchProcessor(Context pCtx, String pSearchTerm, SearchOptions pSearchOptions,
			IDictionary pDict) {
		searchTerm = pSearchTerm;
		searchOptions = pSearchOptions;
		dict = pDict;
		ctx = pCtx;
		Log.d(TAG, "Created new SearchProcessor: "+searchTerm+" : "+dict.getName());
	}
	
	public void run(){
		Log.d(TAG, "Run!");
		dict.getWords(ctx);
		
		dict.unload();
	}
	

}
