package ee.ut.ta.search.ged;

import android.util.Log;

public class NativeGed {
	static final String TAG = "ged.nativeged"; // tag for LogCat
static{
	System.loadLibrary("ged");
	Log.d(TAG, "Library 'ged' loaded..");
} 

public  void setSearchTerm2(String searchTerm){
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

public native void initializeStore();
public native void finalizeStore();
//public native void setSearchTerm(String searchTerm);
public native void setSearchOptions(boolean[] searchOptions);
public native void setDictionaryContent(String[] array);
public native void setTransformationContent(String[] array);
public native String[] process();
//public native void setDictionaryFileName(String string);
//public native void setTransformationFileName(String string);
public native void setGedData(String string, int type);
private native void setMaxEditDist(double maxEditDist);
public void setMaxEditDist2(double maxEditDist) {
	Log.d(TAG, String.format("Setting max dist to %1.2f:", maxEditDist));
	this.setMaxEditDist(maxEditDist);
	
}

}
