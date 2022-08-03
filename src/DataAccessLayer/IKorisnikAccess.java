package DataAccessLayer;

public interface IKorisnikAccess {
	public int register(String korisnickoIme, String sifra);

	public int login(String korisnickoIme, String sifra);

	public int pobedjenMec(String id, String id1);

	public int neresenMec(String id, String id1);
}
