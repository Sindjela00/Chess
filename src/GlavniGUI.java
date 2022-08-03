import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class GlavniGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Socket socket;
	public static List<String> listaKorisnika;
	public JList korisnici;
	public Dugme kliknut = null;
	public Dugme[][] matrica = new Dugme[8][8];
	Boolean cekanje = false;
	Boolean boja = false;
	JTextPane jp;
	private Dugme Kralj;

	public GlavniGUI() {
		setTitle("Sah");
		setBounds(300, 300, 1440, 900);
		setMinimumSize(new Dimension(300, 300));
		Panel tabla = new Panel(new GridLayout(8, 8));
		tabla.setPreferredSize(new Dimension(800, 800));
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				Dugme button = new Dugme(i, j);
				button.setPreferredSize(new Dimension(20, 20));
				tabla.add(button);
				matrica[i][j] = button;
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (cekanje == true) {
							if (kliknut == null) {
								if (button.figura != null && button.figura.boja == boja) {
									kliknut = button;
									update();
									return;
								}

							} else {
								if (kliknut.i == button.i && kliknut.j == button.j) {
									kliknut = null;
									update();
									return;
								}
								try {
									if (button.figura != null) {
										if (button.figura.boja == boja)
											return;
									}
									if (kliknut.figura.figura.equals("Pijun")) {
										if (pijun(kliknut, button, matrica)) {
											Dugme[][] dugmici = deepCopy(matrica);
											dugmici[button.i][button.j].figura = dugmici[kliknut.i][kliknut.j].figura;
											dugmici[kliknut.i][kliknut.j].figura = null;
											if (sah(Kralj, dugmici)) {
												System.out.println("Idalje pod sahom");
												return;
											}
										} else {
											return;
										}
									} else if (kliknut.figura.figura.equals("Lovac")) {
										if (lovac(kliknut, button, matrica)) {
											Dugme[][] dugmici = deepCopy(matrica);
											dugmici[button.i][button.j].figura = dugmici[kliknut.i][kliknut.j].figura;
											dugmici[kliknut.i][kliknut.j].figura = null;
											if (sah(Kralj, dugmici))
												return;
										} else {
											return;
										}
									} else if (kliknut.figura.figura.equals("Top")) {
										if (top(kliknut, button, matrica)) {
											Dugme[][] dugmici = deepCopy(matrica);
											dugmici[button.i][button.j].figura = dugmici[kliknut.i][kliknut.j].figura;
											dugmici[kliknut.i][kliknut.j].figura = null;
											if (sah(Kralj, dugmici))
												return;
										} else {
											return;
										}
									} else if (kliknut.figura.figura.equals("Konj")) {
										if (konj(kliknut, button, matrica)) {
											Dugme[][] dugmici = deepCopy(matrica);
											dugmici[button.i][button.j].figura = dugmici[kliknut.i][kliknut.j].figura;
											dugmici[kliknut.i][kliknut.j].figura = null;
											if (sah(Kralj, dugmici))
												return;
										} else {
											return;
										}
									} else if (kliknut.figura.figura.equals("Kraljica")) {
										if (kraljica(kliknut, button, matrica)) {
											Dugme[][] dugmici = deepCopy(matrica);
											dugmici[button.i][button.j].figura = dugmici[kliknut.i][kliknut.j].figura;
											dugmici[kliknut.i][kliknut.j].figura = null;
											if (sah(Kralj, dugmici))
												return;
										} else {
											return;
										}
									} else if (kliknut.figura.figura.equals("Kralj")) {
										if (kralj(kliknut, button, matrica)) {
											Dugme[][] dugmici = deepCopy(matrica);
											dugmici[button.i][button.j].figura = dugmici[kliknut.i][kliknut.j].figura;
											dugmici[kliknut.i][kliknut.j].figura = null;
											if (sah(dugmici[button.i][button.j], dugmici))
												return;

										} else {
											return;
										}
									} else {
										return;
									}
									Poruka2 pr = new Poruka2("Game",
											String.valueOf(kliknut.i) + String.valueOf(kliknut.j)
													+ String.valueOf(button.i) + String.valueOf(button.j));

									matrica[button.i][button.j].figura = kliknut.figura;
									matrica[kliknut.i][kliknut.j].figura = null;
									if (matrica[button.i][button.j].figura.figura == "Kralj")
										Kralj = matrica[button.i][button.j];
									BufferedWriter bw;
									bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
									cekanje = false;
									bw.write(pr.toString());
									bw.newLine();
									bw.flush();
									kliknut = null;
									update();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				});
			}
		PopuniTablu();
		update();
		getContentPane().add(tabla, BorderLayout.CENTER);
		JPanel cet = new JPanel();
		// JPanel cet = new JPanel(new GridBagLayout());
		cet.setLayout(new BoxLayout(cet, BoxLayout.Y_AXIS));
		cet.setPreferredSize(new Dimension(500, 600));
		JPanel gornji = new JPanel(new GridLayout(1, 2));
		gornji.setPreferredSize(new Dimension(300, 400));
		korisnici = new JList();
		Thread lista = new Thread() {
			@Override
			public void run() {
				try {
					InetAddress address = InetAddress.getByName("225.1.1.1");
					MulticastSocket soc = new MulticastSocket(6787);
					soc.joinGroup(address);
					byte[] data = new byte[4048];
					while (true) {
						DatagramPacket dp = new DatagramPacket(data, data.length, address, 6787);
						soc.receive(dp);
						String poruka = new String(dp.getData());
						listaKorisnika = stringToList(poruka);
						int selected = korisnici.getSelectedIndex();
						korisnici.setListData(listaKorisnika.toArray());
						korisnici.setSelectedIndex(selected);
						// korisnici = new JList(listaKorisnika.toArray());
						// korisnici.setPreferredSize(new Dimension(200,200));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		lista.start();
		JScrollPane jk = new JScrollPane(korisnici);
		jk.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		gornji.add(jk);
		// cet.add(korisnici);
		jp = new JTextPane();
		jp.setEditable(false);
		jp.setPreferredSize(new Dimension(200, 500));
		jp.setText("Welcome!");
		JScrollPane jscrol = new JScrollPane(jp);
		jscrol.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// cet.add(jp);
		gornji.add(jscrol);
		cet.add(gornji);
		Thread stizanjePoruka = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String poruka;

						poruka = br.readLine();
						XMLDecoder xmlD = new XMLDecoder(new ByteArrayInputStream(poruka.getBytes()));
						Poruka3 por1 = (Poruka3) xmlD.readObject();
						if (por1.getString1().equals("Private")) {
							String por = "Private message from " + por1.getString2() + ": " + por1.getString3();
							jp.setText(jp.getText() + "\n" + por);
						} else if (por1.getString1().equals("Global")) {
							String por = por1.getString2() + ": " + por1.getString3();
							jp.setText(jp.getText() + "\n" + por);
						} else if (por1.getString1().equals("Challenged")) {
							System.out.println("Challenged " + por1.getString2() + " by " + por1.getString3());
							int result = JOptionPane.showConfirmDialog(new JFrame(),
									por1.getString3() + " challanged you.", "New game.", JOptionPane.YES_NO_OPTION);
							if (result == 0) {
								Poruka2 pr = new Poruka2("Accept", por1.getString3());
								BufferedWriter bw = new BufferedWriter(
										new OutputStreamWriter(socket.getOutputStream()));
								bw.write(pr.toString());
								bw.newLine();
								bw.flush();
								cekanje = false;
								boja = false;
								PopuniTablu();
							}
						} else if (por1.getString1().equals("Accepted")) {
							System.out.println("Challenge accepted");
							Poruka2 pr = new Poruka2("Accepted", por1.getString3());
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							bw.write(pr.toString());
							bw.newLine();
							bw.flush();
							cekanje = true;
							boja = true;
							PopuniTablu();
						} else if (por1.getString1().equals("Game")) {
							System.out.println(por1.getString1() + " " + por1.getString2() + " " + por1.getString3());
							int x = Integer.parseInt(por1.getString3().substring(0, 1));
							int y = Integer.parseInt(por1.getString3().substring(1, 2));
							int z = Integer.parseInt(por1.getString3().substring(2, 3));
							int c = Integer.parseInt(por1.getString3().substring(3, 4));
							matrica[z][c].figura = matrica[x][y].figura;
							matrica[x][y].figura = null;
							update();
							if (sah(Kralj, matrica)) {
								System.out.println("Sah");
								if (sahmat(Kralj, matrica)) {
									System.out.println("SahMat");
									if (!blokira(Kralj)) {
										Poruka2 pr = new Poruka2("Izgubio", "Sahmat");
										BufferedWriter bw = new BufferedWriter(
												new OutputStreamWriter(socket.getOutputStream()));
										bw.write(pr.toString());
										bw.newLine();
										bw.flush();
										cekanje = false;
									}
								}
							}
							cekanje = true;
						} else if (por1.getString1().equals("Nereseno")) {
							if (por1.getString2().equals("Izazvao")) {
								int result = JOptionPane.showConfirmDialog(new JFrame(),
										por1.getString3() + " offered a draw", "Draw game.", JOptionPane.YES_NO_OPTION);
								if (result == 0) {
									Poruka2 pr = new Poruka2("Nereseno", "Prihvatio");
									BufferedWriter bw = new BufferedWriter(
											new OutputStreamWriter(socket.getOutputStream()));
									bw.write(pr.toString());
									bw.newLine();
									bw.flush();
									cekanje = false;
									JOptionPane.showMessageDialog(new JFrame(), "Nereseno", "Nereseno",
											JOptionPane.OK_OPTION);
								}
							} else if (por1.getString2().equals("Prihvation")) {
								Poruka2 pr = new Poruka2("Nereseno", "Prihvation");
								BufferedWriter bw = new BufferedWriter(
										new OutputStreamWriter(socket.getOutputStream()));
								bw.write(pr.toString());
								bw.newLine();
								bw.flush();
								cekanje = false;
								JOptionPane.showMessageDialog(new JFrame(), "Nereseno", "Nereseno",
										JOptionPane.OK_OPTION);
							}
						} else if (por1.getString1().equals("Pobedio")) {
							Poruka2 pr = new Poruka2("Pobedio", "Prihvation");
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							bw.write(pr.toString());
							bw.newLine();
							bw.flush();
							cekanje = false;
							JOptionPane.showMessageDialog(new JFrame(), "Pobedio si", "Pobeda", JOptionPane.OK_OPTION);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		stizanjePoruka.start();

		JPanel donji = new JPanel(new GridLayout(1, 2));
		JTextField message = new JTextField("Input message");
		message.setPreferredSize(new Dimension(100, 100));
		donji.add(message);
		// cet.add(message);

		JButton button = new JButton("Submit");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (korisnici.getSelectedValue() != null) {

						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						Poruka2 por = new Poruka2(korisnici.getSelectedValue().toString(), message.getText());
						if (korisnici.getSelectedIndex() > 0) {
							jp.setText(jp.getText() + "\n" + "To " + korisnici.getSelectedValue() + ": "
									+ message.getText());
						}
						bw.write(por.toString());
						bw.newLine();
						bw.flush();
					} else {
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						Poruka2 por = new Poruka2("Global", message.getText());
						bw.write(por.toString());
						bw.newLine();
						bw.flush();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		button.setPreferredSize(new Dimension(100, 100));
		JButton challange = new JButton("Challenge");
		challange.setPreferredSize(new Dimension(100, 100));
		challange.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (korisnici.getSelectedValue() != null) {
					BufferedWriter bw;
					try {
						bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						Poruka2 por = new Poruka2("Challenge", korisnici.getSelectedValue().toString());
						if (korisnici.getSelectedIndex() > 0) {
							jp.setText(jp.getText() + "\n" + "Challenged " + korisnici.getSelectedValue());
							bw.write(por.toString());
							bw.newLine();
							bw.flush();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		JPanel sc = new JPanel(new GridLayout(1, 2));
		sc.setPreferredSize(new Dimension(200, 100));
		sc.add(button);
		sc.add(challange);
		// cet.add(button);
		donji.add(sc);
		cet.add(donji);
		getContentPane().add(cet, BorderLayout.EAST);
		JButton disconnect = new JButton("Disconnect.");
		disconnect.setPreferredSize(new Dimension(30, 30));
		disconnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					Poruka2 por = new Poruka2("Disconnect", korisnici.getSelectedValue().toString());
					bw.write(por.toString());
					bw.newLine();
					bw.flush();
					stizanjePoruka.stop();
					socket.close();
					System.exit(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		JPanel predajInereseno = new JPanel(new GridLayout(2, 1));
		predajInereseno.setPreferredSize(new Dimension(100, 200));
		JButton predaj = new JButton("Predaj");
		predaj.setPreferredSize(new Dimension(30, 10));
		predaj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Poruka2 pr = new Poruka2("Izgubio", "Izazvao");
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bw.write(pr.toString());
					bw.newLine();
					bw.flush();
					cekanje = false;
					JOptionPane.showMessageDialog(new JFrame(), "Izgubio si", "Poraz", JOptionPane.OK_OPTION);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		predajInereseno.add(predaj);
		JButton nereseno = new JButton("Nereseno");
		nereseno.setPreferredSize(new Dimension(30, 10));
		nereseno.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Poruka2 pr = new Poruka2("Nereseno", "Izazvao");
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bw.write(pr.toString());
					bw.newLine();
					bw.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		predajInereseno.add(nereseno);
		getContentPane().add(predajInereseno, BorderLayout.WEST);

		getContentPane().add(disconnect, BorderLayout.SOUTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	public List<String> stringToList(String niz) {
		List<String> lista = new ArrayList<String>();
		lista.add("Global");
		for (String i : niz.split(";"))
			lista.add(i);
		return lista;
	}

	public void PopuniTablu() {
		matrica[0][0].figura = new Figura("Top", false);
		matrica[0][1].figura = new Figura("Konj", false);
		matrica[0][2].figura = new Figura("Lovac", false);
		matrica[0][3].figura = new Figura("Kraljica", false);
		matrica[0][4].figura = new Figura("Kralj", false);
		matrica[0][5].figura = new Figura("Lovac", false);
		matrica[0][6].figura = new Figura("Konj", false);
		matrica[0][7].figura = new Figura("Top", false);
		matrica[1][0].figura = new Figura("Pijun", false);
		matrica[1][1].figura = new Figura("Pijun", false);
		matrica[1][2].figura = new Figura("Pijun", false);
		matrica[1][3].figura = new Figura("Pijun", false);
		matrica[1][4].figura = new Figura("Pijun", false);
		matrica[1][5].figura = new Figura("Pijun", false);
		matrica[1][6].figura = new Figura("Pijun", false);
		matrica[1][7].figura = new Figura("Pijun", false);
		matrica[6][0].figura = new Figura("Pijun", true);
		matrica[6][1].figura = new Figura("Pijun", true);
		matrica[6][2].figura = new Figura("Pijun", true);
		matrica[6][3].figura = new Figura("Pijun", true);
		matrica[6][4].figura = new Figura("Pijun", true);
		matrica[6][5].figura = new Figura("Pijun", true);
		matrica[6][6].figura = new Figura("Pijun", true);
		matrica[6][7].figura = new Figura("Pijun", true);
		matrica[7][0].figura = new Figura("Top", true);
		matrica[7][1].figura = new Figura("Konj", true);
		matrica[7][2].figura = new Figura("Lovac", true);
		matrica[7][3].figura = new Figura("Kraljica", true);
		matrica[7][4].figura = new Figura("Kralj", true);
		matrica[7][5].figura = new Figura("Lovac", true);
		matrica[7][6].figura = new Figura("Konj", true);
		matrica[7][7].figura = new Figura("Top", true);

		for (int i = 2; i < 6; i++)
			for (int j = 0; j < 8; j++) {
				matrica[i][j].figura = null;
			}

		if (boja == false)
			Kralj = matrica[0][4];
		else
			Kralj = matrica[7][4];

		update();
	}

	public void update() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (matrica[i][j].figura != null) {
					matrica[i][j].setText(matrica[i][j].figura.figura);
					if (matrica[i][j].figura.boja == false)
						matrica[i][j].setBackground(new Color(0, 255, 0));
					else
						matrica[i][j].setBackground(new Color(255, 0, 0));
				} else {
					matrica[i][j].setText("");
					matrica[i][j].setBackground(new Color(255, 255, 255));
				}
			}
		}
		if (kliknut != null) {
			kliknut.setBackground(new Color(0, 0, 255));
		}
	}

	public boolean pijun(Dugme pocetna, Dugme zavrsna, Dugme[][] matrica) {
		if (pocetna.i + 1 == zavrsna.i && pocetna.j == zavrsna.j && pocetna.figura.boja == false
				&& zavrsna.figura == null) {
			return true;
		} else if (matrica[pocetna.i + 1][pocetna.j].figura == null && pocetna.i == 1 && pocetna.i + 2 == zavrsna.i
				&& pocetna.j == zavrsna.j && pocetna.figura.boja == false && zavrsna.figura == null) {
			return true;
		} else if (pocetna.i + 1 == zavrsna.i && (pocetna.j == zavrsna.j - 1 || pocetna.j == zavrsna.j + 1)
				&& pocetna.figura.boja == false && zavrsna.figura != null
				&& zavrsna.figura.boja != pocetna.figura.boja) {
			return true;
		} else if (pocetna.i - 1 == zavrsna.i && pocetna.j == zavrsna.j && pocetna.figura.boja == true
				&& zavrsna.figura == null) {
			return true;
		} else if (matrica[pocetna.i - 1][pocetna.j].figura == null && pocetna.i == 6 && pocetna.i - 2 == zavrsna.i
				&& pocetna.j == zavrsna.j && pocetna.figura.boja == true && zavrsna.figura == null) {
			return true;
		} else if (pocetna.i - 1 == zavrsna.i && (pocetna.j == zavrsna.j - 1 || pocetna.j == zavrsna.j + 1)
				&& pocetna.figura.boja == true && zavrsna.figura != null
				&& zavrsna.figura.boja != pocetna.figura.boja) {
			return true;
		}
		return false;
	}

	public boolean lovac(Dugme pocetna, Dugme zavrsna, Dugme[][] matrica) {
		if (zavrsna.figura != null)
			if (pocetna.figura.boja == zavrsna.figura.boja)
				return false;

		if (pocetna.i < zavrsna.i) {
			if (pocetna.j < zavrsna.j) {
				for (int i = 1; i < 8; i++) {
					if (pocetna.i + i > 7 || pocetna.j + i > 7)
						return false;
					if (pocetna.i + i == zavrsna.i && pocetna.j + i == zavrsna.j) {
						return true;
					}
					if (matrica[pocetna.i + i][pocetna.j + i].figura != null) {
						return false;
					}
				}
				return false;
			} else {
				for (int i = 1; i < 8; i++) {
					if (pocetna.i + i > 7 || pocetna.j - i < 0)
						return false;
					if (pocetna.i + i == zavrsna.i && pocetna.j - i == zavrsna.j) {
						return true;
					}
					if (matrica[pocetna.i + i][pocetna.j - i].figura != null) {
						return false;
					}
				}
				return false;
			}
		} else {
			if (pocetna.j < zavrsna.j) {
				for (int i = 1; i < 8; i++) {
					if (pocetna.i - i < 0 || pocetna.j + i > 7) {
						return false;
					}
					if (pocetna.i - i == zavrsna.i && pocetna.j + i == zavrsna.j) {
						return true;
					}
					if (matrica[pocetna.i - i][pocetna.j + i].figura != null) {
						return false;
					}
				}
				return false;
			} else {
				for (int i = 1; i < 8; i++) {
					if (pocetna.i - i < 0 || pocetna.j - i < 0)
						return false;
					if (pocetna.i - i == zavrsna.i && pocetna.j - i == zavrsna.j) {
						return true;
					}
					if (matrica[pocetna.i - i][pocetna.j - i].figura != null) {
						return false;
					}
				}
				return false;
			}
		}
	}

	public boolean top(Dugme pocetna, Dugme zavrsna, Dugme[][] matrica) {
		if (zavrsna.figura != null)
			if (pocetna.figura.boja == zavrsna.figura.boja)
				return false;
		if (pocetna.i < zavrsna.i && pocetna.j == zavrsna.j) {
			for (int i = 1; i < 8; i++) {
				if (pocetna.i + i > 7)
					return false;
				if (pocetna.i + i == zavrsna.i) {
					return true;
				}
				if (matrica[pocetna.i + i][pocetna.j].figura != null) {
					return false;
				}
			}
			return false;
		} else if (pocetna.i > zavrsna.i && pocetna.j == zavrsna.j) {
			for (int i = 1; i < 8; i++) {
				if (pocetna.i - i < 0)
					return false;
				if (pocetna.i - i == zavrsna.i) {
					return true;
				}
				if (matrica[pocetna.i - i][pocetna.j].figura != null) {
					return false;
				}
			}
			return false;
		} else if (pocetna.j < zavrsna.j && pocetna.i == zavrsna.i) {
			for (int i = 1; i < 8; i++) {
				if (pocetna.j + i > 7)
					return false;
				if (pocetna.j + i == zavrsna.j) {
					return true;
				}
				if (matrica[pocetna.i][pocetna.j + i].figura != null) {
					return false;
				}
			}
			return false;
		} else if (pocetna.j > zavrsna.j && pocetna.i == zavrsna.i) {
			for (int i = 1; i < 8; i++) {
				if (pocetna.j - i < 0)
					return false;
				if (pocetna.j - i == zavrsna.j) {
					return true;
				}
				if (matrica[pocetna.i][pocetna.j - i].figura != null) {
					return false;
				}
			}
			return false;
		}
		return false;
	}

	public boolean konj(Dugme pocetna, Dugme zavrsna, Dugme[][] matrica) {
		if (zavrsna.figura != null)
			if (pocetna.figura.boja == zavrsna.figura.boja)
				return false;
		if (pocetna.i + 2 == zavrsna.i) {
			if (pocetna.j + 1 == zavrsna.j || pocetna.j - 1 == zavrsna.j)
				return true;
		} else if (pocetna.i - 2 == zavrsna.i) {
			if (pocetna.j + 1 == zavrsna.j || pocetna.j - 1 == zavrsna.j)
				return true;
		} else if (pocetna.i + 1 == zavrsna.i) {
			if (pocetna.j + 2 == zavrsna.j || pocetna.j - 2 == zavrsna.j)
				return true;
		} else if (pocetna.i - 1 == zavrsna.i) {
			if (pocetna.j + 2 == zavrsna.j || pocetna.j - 2 == zavrsna.j)
				return true;
		}
		return false;
	}

	public boolean kraljica(Dugme pocetna, Dugme zavrsna, Dugme[][] matrica) {
		if (top(pocetna, zavrsna, matrica) || lovac(pocetna, zavrsna, matrica))
			return true;
		return false;
	}

	public boolean kralj(Dugme pocetna, Dugme zavrsna, Dugme[][] matrica) {
		if (zavrsna.figura != null)
			if (boja == zavrsna.figura.boja)
				return false;
		if (sah(zavrsna, matrica))
			return false;
		if (pocetna.i == zavrsna.i) {
			if (pocetna.j + 1 == zavrsna.j || pocetna.j - 1 == zavrsna.j)
				return true;
			return false;
		} else if (pocetna.i + 1 == zavrsna.i) {
			if (pocetna.j + 1 == zavrsna.j || pocetna.j - 1 == zavrsna.j || pocetna.j == zavrsna.j)
				return true;
			return false;
		} else if (pocetna.i - 1 == zavrsna.i) {
			if (pocetna.j + 1 == zavrsna.j || pocetna.j - 1 == zavrsna.j || pocetna.j == zavrsna.j)
				return true;
			return false;
		}
		return false;
	}

	public boolean sah(Dugme Kralj, Dugme[][] matricaa) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (matricaa[i][j].figura != null) {

					if (matricaa[i][j].figura.boja != boja) {
						if (matricaa[i][j].figura.figura == "Kraljica") {
							if (kraljica(matricaa[i][j], Kralj, matricaa))
								return true;
						} else if (matricaa[i][j].figura.figura == "Konj") {
							if (konj(matricaa[i][j], Kralj, matricaa))
								return true;
						} else if (matricaa[i][j].figura.figura == "Lovac") {
							if (lovac(matricaa[i][j], Kralj, matricaa))
								return true;
						} else if (matricaa[i][j].figura.figura == "Pijun" && matricaa[i][j].figura.boja != boja) {
							if (matricaa[i][j].figura.boja == false) {
								if (matricaa[i][j].i + 1 == Kralj.i
										&& (matricaa[i][j].j == Kralj.j - 1 || matricaa[i][j].j == Kralj.j + 1)
										&& boja != matricaa[i][j].figura.boja)
									return true;
							}
							if (matricaa[i][j].figura.boja == true)
								if (matricaa[i][j].i - 1 == Kralj.i
										&& (matricaa[i][j].j == Kralj.j - 1 || matricaa[i][j].j == Kralj.j + 1)
										&& boja != matricaa[i][j].figura.boja) {
									return true;
								}
						} else if (matricaa[i][j].figura.figura == "Top") {
							if (top(matricaa[i][j], Kralj, matricaa))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean sahmat(Dugme Kralj, Dugme[][] matricaa) {
		if (Kralj.i + 1 < 7)
			if ((matricaa[Kralj.i + 1][Kralj.j].figura != null
					&& matricaa[Kralj.i + 1][Kralj.j].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i + 1][Kralj.j].figura == null)
				if (kralj(Kralj, matricaa[Kralj.i + 1][Kralj.j], matricaa))
					if (sah(matricaa[Kralj.i + 1][Kralj.j], matricaa))
						return false;

		if (Kralj.i + 1 < 7 && Kralj.j - 1 > -1)
			if ((matricaa[Kralj.i + 1][Kralj.j - 1].figura != null
					&& matricaa[Kralj.i + 1][Kralj.j - 1].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i + 1][Kralj.j - 1].figura == null)
				if (kralj(Kralj, matricaa[Kralj.i + 1][Kralj.j - 1], matricaa))
					if (sah(matricaa[Kralj.i + 1][Kralj.j - 1], matricaa))
						return false;

		if (Kralj.i + 1 < 7 && Kralj.j + 1 < 7)
			if ((matricaa[Kralj.i + 1][Kralj.j + 1].figura != null
					&& matricaa[Kralj.i + 1][Kralj.j + 1].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i + 1][Kralj.j + 1].figura == null)
				if (kralj(Kralj, matricaa[Kralj.i + 1][Kralj.j + 1], matricaa))
					if (sah(matricaa[Kralj.i + 1][Kralj.j + 1], matricaa))
						return false;

		if (kralj(Kralj, matricaa[Kralj.i][Kralj.j], matricaa))
			if (sah(matricaa[Kralj.i][Kralj.j], matricaa))
				return false;

		if (Kralj.j - 1 > -1)
			if ((matricaa[Kralj.i][Kralj.j - 1].figura != null
					&& matricaa[Kralj.i][Kralj.j - 1].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i][Kralj.j - 1].figura == null)
				if (kralj(Kralj, matricaa[Kralj.i][Kralj.j - 1], matricaa))
					if (sah(matricaa[Kralj.i][Kralj.j - 1], matricaa))
						return false;

		if (Kralj.j + 1 < 7)
			if ((matricaa[Kralj.i][Kralj.j + 1].figura != null
					&& matricaa[Kralj.i][Kralj.j + 1].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i][Kralj.j + 1].figura != null)
				if (kralj(Kralj, matricaa[Kralj.i][Kralj.j + 1], matricaa))
					if (sah(matricaa[Kralj.i][Kralj.j + 1], matricaa))
						return false;

		if (Kralj.i - 1 > -1)
			if ((matricaa[Kralj.i - 1][Kralj.j].figura != null
					&& matricaa[Kralj.i - 1][Kralj.j].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i - 1][Kralj.j].figura != null)
				if (kralj(Kralj, matricaa[Kralj.i - 1][Kralj.j], matricaa))
					if (sah(matricaa[Kralj.i - 1][Kralj.j], matricaa))
						return false;

		if (Kralj.i - 1 > -1 && Kralj.j - 1 > -1)
			if ((matricaa[Kralj.i - 1][Kralj.j - 1].figura != null
					&& matricaa[Kralj.i - 1][Kralj.j - 1].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i - 1][Kralj.j - 1].figura != null)
				if (kralj(Kralj, matricaa[Kralj.i - 1][Kralj.j - 1], matricaa))
					if (sah(matricaa[Kralj.i - 1][Kralj.j - 1], matricaa))
						return false;

		if (Kralj.i - 1 > -1 && Kralj.j + 1 < 7)
			if ((matricaa[Kralj.i - 1][Kralj.j + 1].figura != null
					&& matricaa[Kralj.i - 1][Kralj.j + 1].figura.boja != Kralj.figura.boja)
					|| matricaa[Kralj.i - 1][Kralj.j + 1].figura == null)
				if (kralj(Kralj, matricaa[Kralj.i - 1][Kralj.j + 1], matricaa))
					if (sah(matricaa[Kralj.i - 1][Kralj.j + 1], matricaa))
						return false;

		return true;
	}

	public boolean blokira(Dugme Kralj) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (matrica[i][j].figura != null) {
					if (matrica[i][j].figura.boja == boja) {
						for (int x = 0; x < 8; x++) {
							for (int y = 0; y < 8; y++) {
								if (matrica[i][j].figura.figura.equals("Pijun")) {
									if (pijun(matrica[i][j], matrica[x][y], matrica)) {
										System.out.println(i + " " + j + " " + x + " " + y);
										Dugme[][] dugmici = deepCopy(matrica);
										dugmici[x][y].figura = dugmici[i][j].figura;
										dugmici[i][j].figura = null;
										if (!sah(Kralj, dugmici))
											return true;
									}
								} else if (matrica[i][j].figura.figura.equals("Lovac")) {
									if (lovac(matrica[i][j], matrica[x][y], matrica)) {
										Dugme[][] dugmici = deepCopy(matrica);
										dugmici[x][y].figura = dugmici[i][j].figura;
										dugmici[i][j].figura = null;
										if (!sah(Kralj, dugmici))
											return true;
									}
								} else if (matrica[i][j].figura.figura.equals("Top")) {
									if (top(matrica[i][j], matrica[x][y], matrica)) {
										Dugme[][] dugmici = deepCopy(matrica);
										dugmici[x][y].figura = dugmici[i][j].figura;
										dugmici[i][j].figura = null;
										if (!sah(Kralj, dugmici))
											return true;
									}
								} else if (matrica[i][j].figura.figura.equals("Konj")) {
									if (konj(matrica[i][j], matrica[x][y], matrica)) {
										Dugme[][] dugmici = deepCopy(matrica);
										dugmici[x][y].figura = dugmici[i][j].figura;
										dugmici[i][j].figura = null;
										if (!sah(Kralj, dugmici))
											return true;
									}
								} else if (matrica[i][j].figura.figura.equals("Kraljica")) {
									if (kraljica(matrica[i][j], matrica[x][y], matrica)) {
										Dugme[][] dugmici = deepCopy(matrica);
										dugmici[x][y].figura = dugmici[i][j].figura;
										dugmici[i][j].figura = null;
										if (!sah(Kralj, dugmici))
											return true;
									}
								} else if (matrica[i][j].figura.figura.equals("Kralj")) {
									if (kralj(matrica[i][j], matrica[x][y], matrica)) {
										Dugme[][] dugmici = deepCopy(matrica);
										dugmici[x][y].figura = dugmici[i][j].figura;
										dugmici[i][j].figura = null;
										if (!sah(dugmici[x][y], dugmici)) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public Dugme[][] deepCopy(Dugme[][] mat) {
		Dugme[][] mat1 = new Dugme[8][8];
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				mat1[i][j] = new Dugme(mat[i][j].i, mat[i][j].j);
				mat1[i][j].setPreferredSize(new Dimension(20, 20));
				if (mat[i][j].figura != null)
					mat1[i][j].figura = new Figura(mat[i][j].figura.figura, mat[i][j].figura.boja);
				else
					mat1[i][j].figura = null;
			}
		return mat1;
	}
}
