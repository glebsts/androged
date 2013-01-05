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
/***
 * search thread starter, result management
 * @author gleb
 *
 */
public class SearchProcessor implements Runnable {
	private String searchTerm;
	private SearchOptions searchOptions;
	private IDictionary dict;
	private Context ctx;
	static final String TAG = "ged.processor"; // tag for LogCat
	private List<SearchResult> results;
	private long runTime = 0;
	private double maxEditDist = -1;
	private int best = -1;
	private Handler threadHandler;

	public SearchProcessor(Context pCtx, Handler gedHandler, String pSearchTerm,
			SearchOptions pSearchOptions, IDictionary pDict, double pMaxEditDist, int pBest) {
		searchTerm = pSearchTerm;
		searchOptions = pSearchOptions;
		dict = pDict;
		ctx = pCtx;
		maxEditDist = pMaxEditDist;
		best = pBest;
		this.threadHandler = gedHandler;
		Log.d(TAG,
				"Created new SearchProcessor: " + searchTerm + " : "
						+ dict.getName());
	}

	public void run() {
		Log.d(TAG, "Run!");
		runTime = System.currentTimeMillis();
		// init search variables
		NativeGed nativeGed = new NativeGed();
		nativeGed.initializeStore();
		nativeGed.setSearchTermExt(searchTerm);
		nativeGed.setMaxEditDistExt(maxEditDist);
		nativeGed.setBestExt(best);
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
        // start search
		String[] searchres = nativeGed.process();
		// clean store
		nativeGed.finalizeStore();
		this.results = new ArrayList<SearchResult>(0);
		if (searchres != null) {
			this.results = new ArrayList<SearchResult>(searchres.length);
			SearchResult item;
			// parse results to convert from string array to SearchResult array
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

		// sort results
		Collections.sort(this.results);

		// notify activity about end of search
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
}
