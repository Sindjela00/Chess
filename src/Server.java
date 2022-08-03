import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import DataAccessLayer.DataBase;

public class Server {
	public static List<Korisnik> korisnici;

	public static void main(String[] args) {
		DataBase db = new DataBase();
		korisnici = new LinkedList<Korisnik>();
		Thread login = new Thread() {
			@Override
			public void run() {
				try {
					ServerSocket loginSocket = new ServerSocket(6789);
					while (true) {
						Socket client = loginSocket.accept();
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
						BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String poruka = br.readLine();
						XMLDecoder xmlD = new XMLDecoder(new ByteArrayInputStream(poruka.getBytes()));
						Poruka2 por = (Poruka2) xmlD.readObject();
						int val = db.login(por.getString1(), por.getString2());
						por.setString2(String.valueOf(val));
						if (val == 1) {
							synchronized (korisnici) {
								korisnici.add(new Korisnik(por.getString1(), client));
							}
							ClientThread ct = new ClientThread(client, por.getString1());
							ct.start();
						}
						bw.write(por.toString());
						bw.newLine();
						bw.flush();
					}
				} catch (IOException e) {
				}
			}
		};
		login.start();
		Thread register = new Thread() {
			@Override
			public void run() {
				try {
					ServerSocket registerSocket = new ServerSocket(6788);
					while (true) {
						Socket client = registerSocket.accept();
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
						BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String poruka = br.readLine();
						XMLDecoder xmlD = new XMLDecoder(new ByteArrayInputStream(poruka.getBytes()));
						Poruka2 por = (Poruka2) xmlD.readObject();
						int val = db.register(por.getString1(), por.getString2());
						por.setString2(String.valueOf(val));
						if (val == 1) {
							synchronized (korisnici) {
								korisnici.add(new Korisnik(por.getString1(), client));
							}
							ClientThread ct = new ClientThread(client, por.getString1());
							ct.start();
						}
						bw.write(por.toString());
						bw.newLine();
						bw.flush();
					}
				} catch (IOException e) {
				}

			}
		};
		register.start();
		Thread multicast = new Thread() {
			@Override
			public void run() {
				try {
					InetAddress address = InetAddress.getByName("225.1.1.1");
					MulticastSocket socket = new MulticastSocket(6787);
					socket.setTimeToLive(1);
					socket.joinGroup(address);
					while (true) {
						String poruka = "";
						for (Korisnik i : korisnici)
							poruka += i.ime + ";";
						// System.out.println(poruka);
						if (korisnici.size() > 0)
							poruka = poruka.substring(0, poruka.length() - 1);
						byte[] data = poruka.getBytes();
						DatagramPacket packet = new DatagramPacket(data, data.length, address, 6787);
						socket.send(packet);
						Thread.sleep(500);
					}
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		multicast.start();
		while (true)
			;
	}

}
