package conexao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Serial implements SerialPortEventListener {
	// Instancia serialPort
	SerialPort serialPort;
	// o reader para a String que posteriormente receberemos do Arduino
	public static BufferedReader input;
	// O OutPut que enviaremos para o Arduino (caso necessario enviar algo)
	private OutputStream output;
	// tempo de espera
	private static final int TIME_OUT = 2000;
	// Frequencia padrao do arduino 9600 baud
	private static final int DATA_RATE = 9600;
	private static SerialDataListener controller;
	// As Portas que possivelmente iremos usar
	private static String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"/dev/ttyACM0", // Linux
			"COM7" // Windows
	};

	// metodo para comunicacao com a Porta Serial
	public Serial(SerialDataListener controller, String portName) throws Exception {
		Serial.controller = controller;
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// Primeiramente procura pela porta atraves de scanneamento
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (portName == null) {
				for (String port : PORT_NAMES) {
					if (currPortId.getName().equals(port)) {
						portId = currPortId;
						break;
					}
				}
			} else {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		// Se a Porta não for encontrada
		// Imprime uma msg de erro
		if (portId == null) {
			System.out.println("Porta COM não encontrada.");
			return;
		}

		// caso encontre inicia a conexão
		try {
			System.out.println("Conectando...");
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

			System.out.println("Conectado.");

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	// metodo que faz a desconexão da porta Serial
	public synchronized void close() {
		if (serialPort != null) {
			System.out.println("Desconectando...");

			serialPort.removeEventListener();
			serialPort.close();

			System.out.println("Desconectado.");
		}
	}

	public void send(String text) {
		try {
			if (output == null){
			// prepara o out para enviar dado para o arduino
				output = serialPort.getOutputStream();
			}
			new SerialWriter(output, text);
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	// metodo que recebe o dado (String) da Serial
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				if (input.ready()) {
					String inputLine = input.readLine();
					System.out.println(inputLine);
					controller.serialDataReceived(inputLine);
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	public class SerialWriter implements ActionListener {
		OutputStream out1;

		String dados;

		public SerialWriter(OutputStream out, String conteudo) {
			this.out1 = out;
			this.dados = conteudo;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {

				byte[] test = (dados).getBytes();// testando a String enviada
				System.out.println(test);
				this.out1.write(test);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
