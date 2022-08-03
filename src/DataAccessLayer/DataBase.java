package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase implements IKorisnikAccess {

	Connection con;

	public DataBase() {
		String conString = "jdbc:mysql://localhost:3306/sah?user=root&password=";
		try {
			con = DriverManager.getConnection(conString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int register(String korisnickoIme, String sifra) {
		String query = "Insert into Korisnik ( `KorisnickoIme`,`Sifra`,`BrojPobeda`,`BrojPoraza`,`BrojNeresenih`) Values (?,?,0,0,0)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, korisnickoIme);
			ps.setString(2, sifra);
			if (ps.executeUpdate() > 0)
				return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int login(String korisnickoIme, String sifra) {
		if (korisnickoIme.equals("admin") && sifra.equals("admin"))
			return 2;
		String query = "Select * from Korisnik where KorisnickoIme=? and Sifra=?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, korisnickoIme);
			ps.setString(2, sifra);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int pobedjenMec(String id, String id1) {
		String query = "Update Korisnik set brojPobeda = brojPobeda+1 where korisnickoime =?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, id);
			ps.executeUpdate();
			query = "Update Korisnik set brojPoraza = brojPoraza+1 where korisnickoime =?";
			PreparedStatement ps1 = con.prepareStatement(query);
			ps1.setString(1, id1);
			ps1.executeUpdate();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public int Izgubljenmec(String id, String id1) {
		try {
			System.out.println("Izgubljenmec");
			System.out.println(id + id1);
			String query = "Update Korisnik set brojPoraza = brojPoraza+1 where korisnickoime =?";
			PreparedStatement ps1 = con.prepareStatement(query);
			ps1.setString(1, id1);
			System.out.println(ps1.executeUpdate());
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int neresenMec(String id, String id1) {
		String query = "Update Korisnik set brojNeresenih = brojNeresenih+1 where korisnickoime =? or korisnickoime=?";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, id1);
			if (ps.executeUpdate() > 0)
				return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
