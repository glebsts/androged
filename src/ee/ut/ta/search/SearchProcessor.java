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
		
	/*	Trie trie = new Trie();
		List<String> words =dict.getWords(); 
		for(int i=0;i<words.size();i++){
			trie.insertString(words.get(i));
			if(i % 1000 == 0){
				Log.d(TAG, Integer.toString(i) + ": "+ Double.toString(android.os.Debug.getNativeHeapAllocatedSize()/1024/1024));

			}
		}
		*/
		
	/*	jniStoreWords(dict.getWords().toArray(new String[dict.getWords().size()])); */
		
		NativeGed nativeGed = new NativeGed();
		nativeGed.initializeStore();
		nativeGed.setSearchTerm(searchTerm);
		nativeGed.setSearchOptions(new boolean[]{searchOptions.getExactMatches(), searchOptions.getBeginningMatch(),
				searchOptions.getMiddleMatch(), searchOptions.getEndingMatch(), searchOptions.getCaseSensitive()});
		
	//	nativeGed.setDictionaryContent(dict.getWords().toArray(new String[dict.getWords().size()]));
		String[] transstrings = new String[dict.getTransformations().size()];
		for(int i=0;i<transstrings.length;i++){
			transstrings[i]=dict.getTransformations().get(i).toString();
			
		}
	//	nativeGed.setTransformationContent(transstrings);
		nativeGed.createTrie();
		nativeGed.finalizeStore();
		
		dict.unload();
	}
	
 /*	public static native void jniStoreWords(String[] words);

	static {
		System.loadLibrary("ged"); // 
		}
	
	 */

}
