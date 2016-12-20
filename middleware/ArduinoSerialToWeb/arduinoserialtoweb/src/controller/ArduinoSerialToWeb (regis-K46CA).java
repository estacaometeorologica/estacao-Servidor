package controller;

import conexao.Serial;
import conexao.SerialDataListener;
import conexao.URLHTTP;

public class ArduinoSerialToWeb implements SerialDataListener {

	// Instancia serialPort
	Serial serial;

	private static String url = "http://192.168.2.103/meteorologia-api/v1/index.php/submitreading";
	private static String auth;

	// String onde os dados serão coletados
	String inputLine = null;

	// Classe que enviará os dados para o arduino
	URLHTTP objURLHTTP;

	public ArduinoSerialToWeb(String port, String url, String auth) {
		if (url != null)
			ArduinoSerialToWeb.url = url;
		ArduinoSerialToWeb.auth = auth;
		try {
			serial = new Serial(this, port);

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			System.err.println("Não foi possivel conectar, Erro:" + ex.toString());
			serial.close();
		}
		//

	}

	public void serialDataReceived(String text) {
		// Envia a URL e parametro temperatura para a classe responsavel
		try {
			if (objURLHTTP == null)
				objURLHTTP = new URLHTTP(url, auth);
			objURLHTTP.dadosEnviarServidor(text);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}

	}
}