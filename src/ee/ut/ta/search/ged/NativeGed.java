package ee.ut.ta.search.ged;

import android.util.Log;

/***
 * wrapper for native component
 * @author gleb
 *
 */
public class NativeGed {
	static final String TAG = "ged.nativeged"; // tag for LogCat
static{
	System.loadLibrary("ged");
	Log.d(TAG, "Library 'ged' loaded..");
} 

public  void setSearchTermExt(String searchTerm){
	Log.d(TAG, "Setting term:"+searchTerm);
	setGedData(searchTerm, 1);
}
public  void setDictionaryFileName(String fileName){
	Log.d(TAG, "Setting dict:"+fileName);
	setGedData(fileName, 2);
}
public  void setTransformationFileName(String fileName){
	Log.d(TAG, "Setting trans:"+fileName);
	setGedData(fileName, 3);
}

public  void setLetterFileName(String fileName){
	Log.d(TAG, "Setting letters:"+fileName);
	setGedData(fileName, 4);
}
public void setMaxEditDistExt(double maxEditDist) {
	Log.d(TAG, String.format("Setting max dist to %1.2f:", maxEditDist));
	this.setMaxEditDist(maxEditDist);
	
}
public void setBestExt(int best) {
	Log.d(TAG, String.format("Setting best to %d:", best));
	this.setBest(best);
	
}

/* native functions wrappers */

public native void initializeStore();
public native void finalizeStore();
public native void setSearchOptions(boolean[] searchOptions);
public native String[] process();
public native void setGedData(String string, int type);
private native void setMaxEditDist(double maxEditDist);
private native void setBest(int best);

}
