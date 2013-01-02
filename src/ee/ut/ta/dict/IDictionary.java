package ee.ut.ta.dict;

import java.util.List;

import android.content.Context;
import ee.ut.ta.dict.transf.Transformation;

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
	public List<String> getWords();
	public List<Transformation> getTransformations(Context context);
	public List<Transformation> getTransformations();
	public List<Letter> getLetters(Context context);
	public void unload();
	public IDictionaryStorage getStorage();
	public void setStorage(IDictionaryStorage storage);
	public String getTransformationFileName();
	public void setTransformationFileName(String transformationFileName);
	
}
