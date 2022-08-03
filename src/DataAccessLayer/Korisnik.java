package DataAccessLayer;

public class Korisnik {
	int id;
	String KorisnickoIme;
	String Sifra;
	int brojPobeda;
	int brojPoraza;
	int brojNeresenih;
	
	public Korisnik() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKorisnickoIme() {
		return KorisnickoIme;
	}
	public void setKorisnickoIme(String korisnickoIme) {
		KorisnickoIme = korisnickoIme;
	}
	public String getSifra() {
		return Sifra;
	}
	public void setSifra(String sifra) {
		Sifra = sifra;
	}

	public int getBrojPobeda() {
		return brojPobeda;
	}
	public void setBrojPobeda(int brojPobeda) {
		this.brojPobeda = brojPobeda;
	}
	public int getBrojPoraza() {
		return brojPoraza;
	}
	public void setBrojPoraza(int brojPoraza) {
		this.brojPoraza = brojPoraza;
	}
	public int getBrojNeresenih() {
		return brojNeresenih;
	}
	public void setBrojNeresenih(int brojNeresenih) {
		this.brojNeresenih = brojNeresenih;
	}
	
}
