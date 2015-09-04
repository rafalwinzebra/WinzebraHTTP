package pl.winzebra.http;

public interface FileUploaderCallback {
	void onFileUploaderCompletedSuccess();
	void onFileUploaderCompletedFailed();
	void onFileUploaderProgress(String percentage);
}
