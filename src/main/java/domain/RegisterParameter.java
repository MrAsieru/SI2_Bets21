package domain;

import java.util.Date;

public class RegisterParameter {
	public String izena;
	public String abizena1;
	public String abizena2;
	public String erabiltzaileIzena;
	public String pasahitza;
	public String telefonoZbkia;
	public String emaila;
	public Date jaiotzeData;
	public String mota;

	public RegisterParameter(String izena, String abizena1, String abizena2, String erabiltzaileIzena, String pasahitza,
			String telefonoZbkia, String emaila, Date jaiotzeData, String mota) {
		this.izena = izena;
		this.abizena1 = abizena1;
		this.abizena2 = abizena2;
		this.erabiltzaileIzena = erabiltzaileIzena;
		this.pasahitza = pasahitza;
		this.telefonoZbkia = telefonoZbkia;
		this.emaila = emaila;
		this.jaiotzeData = jaiotzeData;
		this.mota = mota;
	}
}