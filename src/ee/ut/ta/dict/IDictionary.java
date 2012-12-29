package ee.ut.ta.dict;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

public interface IDictionary {
	public int getId();
	public void setId(int id);
	public String getName();
	public void setName(String name);
	public String getDescription();
	public void setDescription(String description);
	public String getFileName();
	public void setFileName(String fileName);
	public String getLetterFileName();
	public void setLetterFileName(String letterFileName);
	public int getTransformationFileId();
	public void setTransformationFileId(int transformationFileId);
	public List<String> getWords(Context context);
	public void unload();
	
}
