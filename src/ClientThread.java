import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

import DataAccessLayer.DataBase;

public class ClientThread extends Thread {
	BufferedWriter igra;
	BufferedReader br;
	BufferedWriter bw;
	String ime;
	String protivnik;
	DataBase db;

	public ClientThread(Socket soc, String ime) {
		try {
			db = new DataBase();
			bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			this.ime = ime;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			String poruka;
			try {
				poruka = br.readLine();
				XMLDecoder xmlD = new XMLDecoder(new ByteArrayInputStream(poruka.getBytes()));
				Poruka2 por = (Poruka2) xmlD.readObject();

				synchronized (Server.korisnici) {
					Poruka3 por1 = new Poruka3();
					if (por.getString1().equals("Global")) {
						por1.setString1("Global");
						por1.setString2(ime);
						por1.setString3(por.getString2());
						for (Korisnik i : Server.korisnici) {
							try {
								BufferedWriter bwc = new BufferedWriter(
										new OutputStreamWriter(i.socket.getOutputStream()));
								bwc.write(por1.toString());
								bwc.newLine();
								bwc.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else if (por.getString1().equals("Challenge")) {

						if (por.getString2().trim().equals(ime)) {
							System.out.print("Cant challenge yourself");
						}

						else {
							for (Korisnik k : Server.korisnici) {
								if (k.ime.trim().equals(por.getString2().trim())) {
									System.out.print("Prosao");
									try {
										BufferedWriter bwc;
										bwc = new BufferedWriter(new OutputStreamWriter(k.socket.getOutputStream()));
										Poruka3 por2 = new Poruka3();
										por2.setString1("Challenged");
										por2.setString2(por.getString2());
										por2.setString3(ime);
										bwc.write(por2.toString());
										bwc.newLine();
										bwc.flush();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								System.out.println("Sledeci");
							}
							System.out.println("Zavrsio");
						}
					} else if (por.getString1().equals("Accept")) {
						System.out.print("Prihvatio");
						BufferedWriter bwc = null;
						for (Korisnik k : Server.korisnici) {
							if (k.ime.equals(por.getString2()))
								bwc = new BufferedWriter(new OutputStreamWriter(k.socket.getOutputStream()));
						}
						igra = bwc;
						por1.setString1("Accepted");
						por1.setString2(por.getString2());
						por1.setString3(ime);
						if (bwc == null)
							continue;
						protivnik = por.getString2();
						bwc.write(por1.toString());
						bwc.newLine();
						bwc.flush();
					} else if (por.getString1().equals("Accepted")) {
						BufferedWriter bwc = null;
						for (Korisnik k : Server.korisnici) {
							if (k.ime.equals(por.getString2()))
								bwc = new BufferedWriter(new OutputStreamWriter(k.socket.getOutputStream()));
						}
						if (bwc == null)
							continue;
						protivnik = por.getString2();
						igra = bwc;
					} else if (por.getString1().equals("Game")) {
						por1.setString1("Game");
						por1.setString2(ime);
						por1.setString3(por.getString2());
						igra.write(por1.toString());
						igra.newLine();
						igra.flush();
					} else if (por.getString1().equals("Disconnect")) {
						Korisnik k = null;
						for (Korisnik i : Server.korisnici) {
							if (ime == i.ime) {
								k = i;
								break;
							}
						}
						Server.korisnici.remove(k);
					} else if (por.getString1().equals("Izgubio") && igra != null) {
						db.pobedjenMec(protivnik, ime);
						db.Izgubljenmec(protivnik, ime);
						por1.setString1("Pobedio");
						por1.setString2(ime);
						por1.setString3(por.getString2());
						igra.write(por1.toString());
						igra.newLine();
						igra.flush();
						igra = null;
						protivnik = null;
					} else if (por.getString1().equals("Nereseno") && igra != null) {
						if (por.getString2().equals("Izazvao")) {
							por1.setString1("Nereseno");
							por1.setString2("Izazvao");
							por1.setString3(por.getString2());
							igra.write(por1.toString());
							igra.newLine();
							igra.flush();
						} else if (por.getString2().equals("Prihvatio")) {
							db.neresenMec(ime, protivnik);
							por1.setString1("Nereseno");
							por1.setString2("Prihvation");
							por1.setString3(por.getString2());
							igra.write(por1.toString());
							igra.newLine();
							igra.flush();
							igra = null;
							protivnik = null;
						} else if (por.getString2().equals("Prihvation")) {
							igra = null;
							protivnik = null;
						}
					} else if (por.getString1().equals("Pobedio") && igra != null) {
						igra = null;
						protivnik = null;
					} else {
						BufferedWriter bwc = null;
						for (Korisnik k : Server.korisnici) {
							System.out.println(k.ime + "-" + por.getString1());
							System.out.println(k.ime.compareTo(por.getString1()));
							if (k.ime.compareTo(por.getString1().trim()) == 0)
								bwc = new BufferedWriter(new OutputStreamWriter(k.socket.getOutputStream()));
						}
						if (bwc == null) {
							System.out.println("Prazno");
							continue;
						}
						por1 = new Poruka3();
						por1.setString1("Private");
						por1.setString2(ime);
						por1.setString3(por.getString2());
						bwc.write(por1.toString());
						bwc.newLine();
						bwc.flush();
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
