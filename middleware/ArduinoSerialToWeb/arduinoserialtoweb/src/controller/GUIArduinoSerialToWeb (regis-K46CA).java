package controller;

import javax.swing.*;

import conexao.Serial;
import conexao.SerialDataListener;
import conexao.URLHTTP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GUIArduinoSerialToWeb extends JFrame implements SerialDataListener, ActionListener {

	// Componentes de Tela
	JTextField textPorta;
	JLabel labelPorta;
	TextArea areaTexto;
	JButton botaoLimpar;
	JToggleButton botaoConectar;
	JButton botaoDesconectar;
	JButton botaoEnviar;
	JTextField textDados;

	JLabel labelURL;
	JTextField texturl;

	Timer timer;

	// Instancia serial
	Serial serial;

	private static String SERVER_URL = "http://localhost/meteorologia-api/v1/index.php/submitreading";
	private static String auth = "12345678901234567890";
	private static String portName = "COM7";

	// Classe que enviará os dados para o arduino
	private URLHTTP objURLHTTP;

	// Cria a Interface (Tudo manual)
	public GUIArduinoSerialToWeb() {

		Container JANELA = getContentPane();
		JANELA.setLayout(null);
		textPorta = new JTextField(portName, 10);
		JANELA.add(textPorta);
		textPorta.setBounds(194, 36, 121, 21);
		botaoConectar = new JToggleButton("Conectar");
		JANELA.add(botaoConectar);
		botaoConectar.setBounds(350, 36, 121, 21);
		botaoDesconectar = new JButton("Desconectar");
		JANELA.add(botaoDesconectar);
		botaoDesconectar.setBounds(480, 36, 121, 21);
		labelPorta = new JLabel("Porta Serial");
		JANELA.add(labelPorta);
		labelPorta.setBounds(54, 42, 97, 13);

		areaTexto = new TextArea("");
		JANELA.add(areaTexto);
		areaTexto.setBounds(75, 216, 547, 220);

		labelURL = new JLabel("URL ");
		JANELA.add(labelURL);
		labelURL.setBounds(15, 450, 97, 13);

		texturl = new JTextField("");
		JANELA.add(texturl);
		texturl.setBounds(80, 445, 650, 21);

		botaoLimpar = new JButton("Limpar");
		JANELA.add(botaoLimpar);
		botaoLimpar.setBounds(642, 410, 75, 25);
		botaoEnviar = new JButton("Enviar Dados");
		JANELA.add(botaoEnviar);
		botaoEnviar.setBounds(530, 131, 120, 25);
		textDados = new JTextField("", 10);
		JANELA.add(textDados);
		textDados.setBounds(77, 131, 440, 21);

		// adiciona o actionListenner para os botões
		botaoConectar.addActionListener(this);
		botaoDesconectar.addActionListener(this);
		botaoEnviar.addActionListener(this);
		botaoLimpar.addActionListener(this);

		// seta o Console para não ser editado
		areaTexto.setEditable(false);

		// Nome da App
		this.setTitle("Serial Monitor");
		// tamanho
		setSize(800, 533);
		// visivel
		setVisible(true);
		// não redimensionado
		setResizable(false);
		// envia para o centro da tela
		setLocationRelativeTo(null);

		texturl.setText(SERVER_URL);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Acao do botao conectar
		if (e.getSource() == botaoConectar) {

			try {
				//
				String portName = textPorta.getText();
				serial = new Serial(this, portName);
				SERVER_URL = texturl.getText();
				textPorta.setEnabled(false);
				texturl.setEnabled(false);
				botaoConectar.setEnabled(false);

			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Não foi possivel conectar, Erro:" + ex.toString());
				textPorta.setEnabled(true);
				texturl.setEnabled(true);
				botaoConectar.setEnabled(true);
			}
		}

		if (e.getSource() == botaoDesconectar) {

			try {
				if (serial != null)
					serial.close();
				botaoConectar.setEnabled(true);
				botaoConectar.setText("Conectar");
				textPorta.setEnabled(true);
				texturl.setEnabled(true);
			} catch (Exception ex) {

				JOptionPane.showMessageDialog(null, ex.toString());
			}
		}

		if (e.getSource() == botaoLimpar) {
			areaTexto.setText("");

		}

		// Acao do botão enviar
		if (e.getSource() == botaoEnviar) {

			try {
				if (serial != null)
					serial.send(textDados.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// limpa a caixa
		textDados.setText("");
	}

	@Override
	public void serialDataReceived(String inputLine) {
		if (!areaTexto.getText().equals("") && !areaTexto.getText().equals(null)) {
			areaTexto.setText(areaTexto.getText() + "\n" + inputLine);
		} else {
			areaTexto.setText(inputLine);
		}
		try {
			if (objURLHTTP == null)
				objURLHTTP = new URLHTTP(SERVER_URL, auth);
			objURLHTTP.dadosEnviarServidor(inputLine);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
	}

}
