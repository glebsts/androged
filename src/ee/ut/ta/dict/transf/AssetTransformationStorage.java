package ee.ut.ta.dict.transf;

import java.util.List;

import android.content.Context;
import android.util.Log;
import ee.ut.ta.R;

public class AssetTransformationStorage extends AbstractTransformationStorage
		implements ITransformationStorage {
	static final String TAG = "ged.assettransformationstorage"; // tag for LogCat
	private Context context;
	public AssetTransformationStorage(Context pContext){
		this.context = pContext;
	}
	
	@Override
	public
	List<ITransformationFile> loadTransformationFiles() {
		Log.d(TAG, "Loading.. ");
		TransformationListFileXmlParser parser = new TransformationListFileXmlParser();
		
		return parser.parseFile(this.context.getResources().openRawResource(R.raw.transformations));
	}
}
