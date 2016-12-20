package conexao;
//Biblioteca do Java
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

//biblioteca HTTP 
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;



public class ConexaoHTTPClient{ 
	public static final int HTTP_TIMEOUT = 30 * 1000;
	private static HttpClient httpclient;

	private static HttpClient getHttpClient() {
		if (httpclient == null) {
			httpclient = new DefaultHttpClient();
			final HttpParams httpparams = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpparams, HTTP_TIMEOUT);
			//ConnManagerParams.setTimeout(httpparams, HTTP_TIMEOUT);
		}
		return httpclient;
	}

	public String executaHTTPPost(String url,ArrayList<NameValuePair> parametrosPost, String apiKey) throws Exception {

		BufferedReader bufferedReader = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parametrosPost);
			httpPost.setEntity(formEntity);
			httpPost.setHeader("Authorization", apiKey);
			HttpResponse httpResponse = client.execute(httpPost); 
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String LS = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {

				stringBuffer.append(line + LS);
			}

			bufferedReader.close();
			String resultado = stringBuffer.toString();
			return resultado;

		} finally {

			if (bufferedReader != null) {

				try {
					bufferedReader.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		}
	}


	public String executaHTTPGet(String url) throws Exception {

		BufferedReader buffereReader = null;
		try {
			HttpClient client = getHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setURI(new URI(url));
			HttpResponse httpResponse = client.execute(httpGet); 
			buffereReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String LS = System.getProperty("line.separator");

			while ((line = buffereReader.readLine()) != null) {

				stringBuffer.append(line + LS);
			}

			buffereReader.close();
			String resultado = stringBuffer.toString();
			return resultado;

		} finally {

			if (buffereReader != null) {

				try {
					buffereReader.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		}
	}
}
