import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Poruka3 implements Serializable {
	private static final long serialVersionUID = 1L;
	public String string1;
	public String string2;
	public String string3;
	public Poruka3() {}
	public Poruka3(String st,String st1) {
		this.string1 = st;
		this.string2 = st1;
	}

	
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getString2() {
		return string2;
	}
	public void setString2(String string2) {
		this.string2 = string2;
	}
	public String getString3() {
		return string3;
	}
	public void setString3(String string3) {
		this.string3 = string3;
	}
	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder xmlE = new XMLEncoder(baos);
		xmlE.writeObject(this);
		xmlE.close();
		String xml = new String(baos.toByteArray());
		xml = xml.replace("\n", " ");
		return xml;
	}
	
}
