package ee.ut.ta.dict;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetDictionary extends AbstractDictionary {
	static final String TAG = "ged.assetdictionary"; // tag for LogCat

	@Override
	void loadWords(Context ctx) {
		try {
			if (words == null) {
				words = new ArrayList<String>();
			}
			AssetManager assetMan = ctx.getAssets();
			List<String> list = Arrays.asList(assetMan.list("dict"));
			if (!list.contains(this.getFileName())) {
				Log.e(TAG, "Dictionary file " + this.getFileName()
						+ " not found is assets");
				throw new FileNotFoundException("Dictionary file "
						+ this.getFileName() + " not found is assets");
			}
			InputStream is = assetMan.open("dict/" + this.getFileName(),
					Context.MODE_WORLD_READABLE);

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				words.add(line);
			}

			br.close();
			is.close();
		} catch (Exception e) {
			Log.e(TAG, "Error loading words: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
