import javax.swing.JFrame;

import controller.ArduinoSerialToWeb;
import controller.GUIArduinoSerialToWeb;

public class Main {

	public static void main(String[] args) {
		boolean console = false;
		String auth = null;
		String url = null;
		String port = null;
		
		if (args.length >= 1){
			for(int i = 0; i < args.length; i++) {
	            System.out.println(args[i]);
				String arg = args[i].toLowerCase();
				if (arg.equals("-c"))
					console = true;
				else if (args.length >= i+2){
					if (arg.equals("-p"))
						port = args[i+1];
					else if (arg.equals("-u"))
						url = args[i+1];
					else if (arg.equals("-a"))
						auth = args[i+1];
				}
	        }
			if (console == true && url == null)
				System.out.println("\nErro: Par�metro obrigat�rio n�o especificado: -u");
		}else{
			System.out.println("Como utilizar:\njava -jar app.jar [-c] [-p] -u \"URL\" [-a \"CHAVE DE AUTORIZA��O\"]");
			System.out.println("\n-c : Executar em modo console(sem interface gr�fica)");
			System.out.println("-p : Porta COM. Ex: COM7. Caso n�o especificado, sistema tentar� localizar automaticamente.");
			System.out.println("-u : Url do servi�o. Ex: http://localhost/meteorologia-api/v1/index.php/submitreading.");
			System.out.println("-a : C�digo de autoriza��o. Ser� enviado no cabe�alho, com nome \"Authorization\".");
			System.out.println("Obs: Par�metros entre colchetes [] s�o opcionais");
		}

		if (console){
			if (url != null)
				new ArduinoSerialToWeb(port, url, auth);
		}
		else{
			GUIArduinoSerialToWeb app = new GUIArduinoSerialToWeb();
			app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

}
