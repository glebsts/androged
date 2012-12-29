package ee.ut.ta.dict;

import java.util.List;

import android.content.Context;
import android.util.Log;
import ee.ut.ta.R;
import ee.ut.ta.dict.transf.AssetTransformationStorage;


public class AssetDictionaryStorage extends AbstractDictionaryStorage{
	static final String TAG = "ged.assetdictionarystorage"; // tag for LogCat
	private Context context;
	public AssetDictionaryStorage(Context pContext){
		this.context = pContext;
		this.transformationStorage = new AssetTransformationStorage(this.context);
	}
	
	@Override
	List<IDictionary> loadDictionaries() {
		Log.d(TAG, "Loading.. ");
		DictionaryListFileXmlParser parser = new DictionaryListFileXmlParser();
		
		return parser.parseFile(this.context.getResources().openRawResource(R.raw.dictionaries), this);
	}
	
	
}
