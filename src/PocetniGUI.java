import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PocetniGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PocetniGUI() {

		setBounds(300, 300, 400, 500);
		setMinimumSize(new Dimension(300, 300));
		Panel panelZaLogin = new Panel(new GridLayout(3, 1));
		JTextField input1 = new JTextField("Username");
		panelZaLogin.add(input1);
		JTextField input2 = new JTextField("Password");
		panelZaLogin.add(input2);
		Panel dugmici = new Panel(new GridLayout(1, 2));
		JButton button1 = new JButton("Login");
		button1.setPreferredSize(new Dimension(30, 30));
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Socket client;
				try {
					client = new Socket(InetAddress.getByName("localhost"), 6789);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
					Poruka2 por = new Poruka2(input1.getText(), input2.getText());
					bw.write(por.toString());
					bw.newLine();
					bw.flush();
					String poruka = br.readLine();
					XMLDecoder xmlD = new XMLDecoder(new ByteArrayInputStream(poruka.getBytes()));
					Poruka2 por1 = (Poruka2) xmlD.readObject();
					System.out.println(por1.getString1() + ": " + por1.getString2());
					if (por1.getString2().equals("1")) {
						System.out.println(por.getString2());
						dispose();
						GlavniGUI.socket = client;
						GlavniGUI gg = new GlavniGUI();
					}
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		dugmici.add(button1);
		JButton button2 = new JButton("Register");
		button2.setPreferredSize(new Dimension(30, 30));
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Socket client;
				try {
					client = new Socket(InetAddress.getByName("localhost"), 6788);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
					Poruka2 por = new Poruka2(input1.getText(), input2.getText());
					bw.write(por.toString());
					bw.newLine();
					bw.flush();
					String poruka = br.readLine();
					XMLDecoder xmlD = new XMLDecoder(new ByteArrayInputStream(poruka.getBytes()));
					Poruka2 por1 = (Poruka2) xmlD.readObject();
					System.out.println(por1.getString1() + ": " + por1.getString2());
					if (por1.getString2().equals("1")) {
						GlavniGUI.socket = client;
						GlavniGUI gg = new GlavniGUI();
					}
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		dugmici.add(button2);
		panelZaLogin.add(dugmici);
		getContentPane().add(panelZaLogin, BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
}
