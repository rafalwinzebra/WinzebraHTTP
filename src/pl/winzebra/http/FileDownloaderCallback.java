package pl.winzebra.http;

public interface FileDownloaderCallback {
	void onFileDownloaderCompletedSuccess();
	void onFileDownloaderCompletedFailed();
	void onFileDownloaderProgress(String percentage);
}
