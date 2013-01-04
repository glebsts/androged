package ee.ut.ta.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import ee.ut.ta.dict.IDictionary;
import ee.ut.ta.search.ged.NativeGed;

public class SearchProcessor implements Runnable {
	private String searchTerm;
	private SearchOptions searchOptions;
	private IDictionary dict;
	private Context ctx;
	static final String TAG = "ged.processor"; // tag for LogCat
	private List<SearchResult> results;
	private long runTime = 0;
	private double maxEditDist = 0.5;
	private Handler threadHandler;

	public SearchProcessor(Context pCtx, Handler gedHandler, String pSearchTerm,
			SearchOptions pSearchOptions, IDictionary pDict, double pMaxEditDist) {
		searchTerm = pSearchTerm;
		searchOptions = pSearchOptions;
		dict = pDict;
		ctx = pCtx;
		maxEditDist = pMaxEditDist;
		this.threadHandler = gedHandler;
		Log.d(TAG,
				"Created new SearchProcessor: " + searchTerm + " : "
						+ dict.getName());
	}

	public void run() {
 

		Log.d(TAG, "Run!");
		// dict.getWords(ctx);
		// dict.getLetters(ctx);
		// dict.getTransformations(ctx);
		// case-sensitive?

		/*
		 * jniStoreWords(dict.getWords().toArray(new
		 * String[dict.getWords().size()]));
		 */

		runTime = System.currentTimeMillis();

		NativeGed nativeGed = new NativeGed();
		nativeGed.initializeStore();
		nativeGed.setSearchTerm2(searchTerm);
		nativeGed.setMaxEditDist2(maxEditDist);
		nativeGed.setSearchOptions(new boolean[] {
				searchOptions.getExactMatches(),
				searchOptions.getBeginningMatch(),
				searchOptions.getMiddleMatch(), searchOptions.getEndingMatch(),
				searchOptions.getCaseSensitive() });

		nativeGed
				.setDictionaryFileName("/mnt/sdcard/ged/" + dict.getFileName());
		nativeGed.setTransformationFileName("/mnt/sdcard/ged/"
				+ dict.getTransformationFileName());
		nativeGed.setLetterFileName("/mnt/sdcard/ged/"
				+ dict.getLetterFileName());

		// nativeGed.setDictionaryContent(dict.getWords().toArray(new
		// String[dict.getWords().size()]));
		// String[] transstrings = new String[dict.getTransformations().size()];
		// for(int i=0;i<transstrings.length;i++){
		// transstrings[i]=dict.getTransformations().get(i).toString();

		// }
		// nativeGed.setTransformationContent(transstrings);
		String[] searchres = nativeGed.process();
		nativeGed.finalizeStore();
		this.results = new ArrayList<SearchResult>(0);
		if (searchres != null) {
			this.results = new ArrayList<SearchResult>(searchres.length);
			SearchResult item;
			for (String res : searchres) {
				// Log.d(TAG, res);
				try {
					item = new SearchResult(res);
					this.results.add(item);
				} catch (NumberFormatException exc) {
					Log.d(TAG, String.format("Line %s is not parseable.", res));
				} catch (ArrayIndexOutOfBoundsException exc) {
					Log.d(TAG, String.format("Line %s is not parseable.", res));
				}

			}

		} else {
			Log.d(TAG, "Result is empty");
		}
		runTime =  System.currentTimeMillis() - runTime;

		dict.unload();

		Collections.sort(this.results);
		/*for (SearchResult sr : this.results) {
			Log.d(TAG, sr.toString());
		}*/
		Message msg = Message.obtain();
		msg.obj = runTime;
		this.threadHandler.sendMessage(msg);
		 


	}

	private void setResults(List<SearchResult> results) {
		this.results = results;
	}

	public List<SearchResult> getResults() {
		return results;
	}

	public long getRunTime() {
		return runTime;
	}

	/*
	 * public static native void jniStoreWords(String[] words);
	 * 
	 * static { System.loadLibrary("ged"); // }
	 */

}
