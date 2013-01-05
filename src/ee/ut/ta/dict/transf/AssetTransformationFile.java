package ee.ut.ta.dict.transf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
/***
 * asset-based transformation rules file
 * @author gleb
 *
 */
public class AssetTransformationFile extends AbstractTransformationFile {

	@Override
	void loadTransformations(Context ctx) {
		try {
			if (transformations == null) {
				transformations = new ArrayList<Transformation>();
			}

			AssetManager assetMan = ctx.getAssets();
			List<String> list = Arrays.asList(assetMan.list("transforms"));
			if (!list.contains(this.getFileName())) {
				Log.e(TAG, "Additional transformation file " + this.getFileName()
						+ " not found is assets");
				//throw new FileNotFoundException("Dictionary file "
				//		+ this.getFileName() + " not found is assets");
				return;
			}
			InputStream is = assetMan.open("transforms/" + this.getFileName(),
					Context.MODE_WORLD_READABLE);

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				transformations.add(new Transformation(line));
			}

			br.close();
			is.close();
			Log.d(TAG,"Transformations loaded, count = " + transformations.size());
		} catch (Exception e) {
			Log.e(TAG, "Error loading transformations: " + e.getMessage());
			e.printStackTrace();
		}
	}


}
