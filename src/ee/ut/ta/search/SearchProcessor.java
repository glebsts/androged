package ee.ut.ta.search;

import android.content.Context;
import android.util.Log;
import ee.ut.ta.dict.IDictionary;
import ee.ut.ta.search.ged.NativeGed;

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
		dict.getLetters(ctx);
		dict.getTransformations(ctx);
		// case-sensitive?
		
	/*	jniStoreWords(dict.getWords().toArray(new String[dict.getWords().size()])); */
		
		NativeGed nativeGed = new NativeGed();
		nativeGed.initializeStore();
		nativeGed.setSearchTerm2(searchTerm);
		nativeGed.setSearchOptions(new boolean[]{searchOptions.getExactMatches(), searchOptions.getBeginningMatch(),
				searchOptions.getMiddleMatch(), searchOptions.getEndingMatch(), searchOptions.getCaseSensitive()});
		
		
		
		
		nativeGed.setDictionaryFileName("/mnt/sdcard/ged/"+dict.getFileName());
		nativeGed.setTransformationFileName("/mnt/sdcard/ged/"+dict.getTransformationFileName());
		nativeGed.setLetterFileName("/mnt/sdcard/ged/"+dict.getLetterFileName());
		
	//	nativeGed.setDictionaryContent(dict.getWords().toArray(new String[dict.getWords().size()]));
//		String[] transstrings = new String[dict.getTransformations().size()];
//		for(int i=0;i<transstrings.length;i++){
	//		transstrings[i]=dict.getTransformations().get(i).toString();
			
//		}
	//	nativeGed.setTransformationContent(transstrings);
		String[] searchres =  nativeGed.process();
		nativeGed.finalizeStore();
		
		for (String res : searchres) {
			Log.d(TAG, res);
		}
		dict.unload();
	}
	
 /*	public static native void jniStoreWords(String[] words);

	static {
		System.loadLibrary("ged"); // 
		}
	
	 */

}
