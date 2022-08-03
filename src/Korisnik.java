import java.net.Socket;

public class Korisnik {
	public String ime;
	public Socket socket;

	public Korisnik(String ime, Socket soc) {
		this.ime = ime;
		this.socket = soc;
	}
}
