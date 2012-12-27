package ee.ut.ta.dict;

abstract class AbstractDictionary implements IDictionary  {
	private int id;
	private String name;
	private String description;
	private String fileName;
	private String letterFileName;
	private int transformationFileId;
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setLetterFileName(String letterFileName) {
		this.letterFileName = letterFileName;
	}
	public String getLetterFileName() {
		return letterFileName;
	}
	public void setTransformationFileId(int transformationFileId) {
		this.transformationFileId = transformationFileId;
	}
	public int getTransformationFileId() {
		return transformationFileId;
	}
}
