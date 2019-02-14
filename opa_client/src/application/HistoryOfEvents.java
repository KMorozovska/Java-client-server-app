package application;

public class HistoryOfEvents {

	private String fileName;
	private String filePath;
	private String fileDate;
	

	public HistoryOfEvents(String fileName, String filePath, String fileDate) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileDate = fileDate;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	
	
	
	
}
