package pl.winzebra.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class FileDownloader extends AsyncTask<String, String, String>{

	private Context context;
	private String progressPercentage;
	private Boolean didFailed = false;
	private FileDownloaderCallback listener;

	public FileDownloader(FileDownloaderCallback listener){
		didFailed = false;
        this.listener=listener;
    }

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... aurl) {

	    try {

	        URL u = new URL(aurl[0]);
	        HttpURLConnection  c = (HttpURLConnection ) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setDoOutput(true);
	        c.connect();

	        int lenghtOfFile = c.getContentLength();

	        FileOutputStream f = new FileOutputStream(new File(aurl[1]));

	        InputStream in = c.getInputStream();

	        byte[] buffer = new byte[1024];
	        int len1 = 0;
	        long total = 0;

	        while ((len1 = in.read(buffer)) > 0) {
	            total += len1; //total = total + len1
	            publishProgress("" + (int)((total*100)/lenghtOfFile));
	            f.write(buffer, 0, len1);
	        }
	        f.close();
	    } catch (Exception e) {
	        Log.d("Downloader", e.getMessage());
	        didFailed = true;
	        
	    }

	    return null;

	}

	protected void onProgressUpdate(String... progress) {
	     this.progressPercentage = progress[0];
	     listener.onFileDownloaderProgress(this.progressPercentage);
	}

	@Override
	protected void onPostExecute(String unused) {
		if(!didFailed)
			listener.onFileDownloaderCompletedSuccess();
		else
			listener.onFileDownloaderCompletedFailed();
	}
	
	public String getProgressPercentage()
	{
		return this.progressPercentage;
	}



}
