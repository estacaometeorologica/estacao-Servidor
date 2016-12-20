package conexao;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



public class URLHTTP {


	private static String url = null;
	private static String auth = null;
	
	public URLHTTP(String url, String auth){
		URLHTTP.url = url;
		URLHTTP.auth = auth;
	}


	public String dadosEnviarServidor(String linha){

		String respostaRetornada = null;
		String resposta = null;

		//você precisará colocar o IP do pc em que encontra seu servidor tomcat
		//static String url ="http://192.168.1.100:8084/JSONTeste/";
		//static String url ="http://192.168.0.183:8084/ArduinoWeb/insereTemperaturaUmidade.jsp?temperatura="+linha; 

		//lembrando que essa url eh composta da seguinte forma: http://<ip da maquina com servidor>/<nome do projeto>
		//Se você não configurou o Servlet como welcome no web.xml, vc precisa colocar este endereço assim:
		// http://<ip da maquina com servidor>/<nome do projeto>/<nome do Servlet>

		//Get-------
		try {

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("data", linha));

			ConexaoHTTPClient objConexaoHTTPClient = new ConexaoHTTPClient();
			respostaRetornada = objConexaoHTTPClient.executaHTTPPost(url, nameValuePair, auth);

			resposta = respostaRetornada.toString();

			System.out.println(resposta);
			return resposta;


		}catch (Exception e1) {	  
			return e1.toString();

		}
	}

	public String getUrl() {
		return url;
	}	

	public String getAuth() {
		return auth;
	}
}
