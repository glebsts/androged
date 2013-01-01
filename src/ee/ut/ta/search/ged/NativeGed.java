package ee.ut.ta.search.ged;

import ee.ut.ta.search.SearchResult;
import android.util.Log;

public class NativeGed {
	static final String TAG = "ged.nativeged"; // tag for LogCat
static{
	System.loadLibrary("ged");
	Log.d(TAG, "Library 'ged' loaded..");
} 

public native void initializeStore();
public native void finalizeStore();
public native void setSearchTerm(String searchTerm);
public native void setSearchOptions(boolean[] searchOptions);
public native void setDictionaryContent(String[] array);
public native void setTransformationContent(String[] array);
public native String[] process();

}
