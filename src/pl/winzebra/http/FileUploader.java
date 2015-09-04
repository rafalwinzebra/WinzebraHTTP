package pl.winzebra.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class FileUploader extends AsyncTask<String, String, String>{

	private Context context;
	private String progressPercentage;
	private Boolean didFailed = false;
	private FileUploaderCallback listener;
	
	private static String FILE_UPLOAD_SUCCESS = "FILE_UPLOAD_SUCCESS";
	private static String FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED";

	public FileUploader(FileUploaderCallback listener){
		didFailed = true;
        this.listener=listener;
    }

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... aurl) {

	    try {

	        HttpClient httpClient = new DefaultHttpClient();

	        HttpPost httpPost = new HttpPost(aurl[1]);

	        File file = new File(aurl[0]);
	        FileBody fileBody = new FileBody(file);

	        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	        reqEntity.addPart("fileToUpload", fileBody);

	        httpPost.setEntity(reqEntity);

	        HttpResponse response = httpClient.execute(httpPost);
	        HttpEntity resEntity = response.getEntity();

	        if (resEntity != null) {

	            String responseStr = EntityUtils.toString(resEntity).trim();

	            if(responseStr.equalsIgnoreCase(FILE_UPLOAD_SUCCESS))
	            	didFailed = false;
	            else if(responseStr.equalsIgnoreCase(FILE_UPLOAD_FAILED))
	            	didFailed = true;
	        }
	        return "";
	    } catch (Exception e) {
	        Log.d("Uploader", e.getMessage());
	        didFailed = true;
	        
	    }

	    return null;

	}

	protected void onProgressUpdate(String... progress) {
	     this.progressPercentage = progress[0];
	     listener.onFileUploaderProgress(this.progressPercentage);
	}

	@Override
	protected void onPostExecute(String unused) {
		if(!didFailed)
			listener.onFileUploaderCompletedSuccess();
		else
			listener.onFileUploaderCompletedFailed();
	}
	
	public String getProgressPercentage()
	{
		return this.progressPercentage;
	}



}
