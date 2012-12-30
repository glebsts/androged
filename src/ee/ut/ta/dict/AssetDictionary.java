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
import ee.ut.ta.dict.transf.ITransformationFile;
import ee.ut.ta.dict.transf.Transformation;

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
			Log.d(TAG,"Words loaded, count = " + words.size());
		} catch (Exception e) {
			Log.e(TAG, "Error loading words: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	void loadTransformations(Context ctx) {
		try {
			if (transformations == null) {
				transformations = new ArrayList<Transformation>();
			}
			ITransformationFile file = this.getStorage().getTransformationStorage().getTransformationFileById(this.getTransformationFileId());
			this.transformations = file.getTransformations(ctx);
		} catch (Exception e) {
			Log.e(TAG, "Error loading transformations: " + e.getMessage());
			e.printStackTrace();
		}
		
	}

	
	@Override
	void loadLetters(Context ctx) {
		try {
			if (letters == null) {
				letters = new ArrayList<Letter>();
			}
			
			AssetManager assetMan = ctx.getAssets();
			List<String> list = Arrays.asList(assetMan.list("letters"));
			if (!list.contains(this.getLetterFileName())) {
				Log.e(TAG, "Additional transformation file " + this.getLetterFileName()
						+ " not found is assets");
				//throw new FileNotFoundException("Dictionary file "
				//		+ this.getFileName() + " not found is assets");
				return;
			}
			InputStream is = assetMan.open("letters/" + this.getLetterFileName(),
					Context.MODE_WORLD_READABLE);

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				letters.add(new Letter(line));
			}

			br.close();
			is.close();
			Log.d(TAG,"Letters loaded, count = " + letters.size());
		} catch (Exception e) {
			Log.e(TAG, "Error loading letters: " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}

}
