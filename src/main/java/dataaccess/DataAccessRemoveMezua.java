package dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Admin;
import domain.Apustua;
import domain.ArretaElkarrizketa;
import domain.ArretaMezua;
import domain.Bezeroa;
import domain.BezeroaContainer;
import domain.BezeroartekoMezua;
import domain.Errepikapena;
import domain.ErrepikatuakContainer;
import domain.Event;
import domain.Langilea;
import domain.Mezua;
import domain.Pertsona;
import domain.Pronostikoa;
import domain.PronostikoaContainer;
import domain.Question;
import domain.RegisterParameter;
import exceptions.EventAlreadyExist;
import exceptions.EventFinished;
import exceptions.PronosticAlreadyExist;
import exceptions.QuestionAlreadyExist;
import exceptions.UserAlreadyExist; 

import java.util.logging.Logger;
/**
 * It implements the data access to the objectDb database
 */
public class DataAccessRemoveMezua {
	
	protected EntityManager db;
	protected EntityManagerFactory emf;
	protected DataAccessInitializer defaultInitializer = new MyDefaultDAI();

	ConfigXML c = ConfigXML.getInstance();

	public DataAccessRemoveMezua(boolean initializeMode) {
		Logger.getLogger(this.getClass().getName()).info("Creating DataAccess instance => isDatabaseLocal: " + c.isDatabaseLocal()
				+ " getDatabBaseOpenMode: " + c.getDataBaseOpenMode());
		open(initializeMode);
	}

	public DataAccessRemoveMezua() {
		this(false);
	}

	public void open(boolean initializeMode) {

		System.out.println("Opening DataAccess instance => isDatabaseLocal: " + c.isDatabaseLocal()
				+ " getDatabBaseOpenMode: " + c.getDataBaseOpenMode());

		String fileName = c.getDbFilename();
		if (initializeMode) {
			fileName = fileName + ";drop";
			System.out.println("Deleting the DataBase");
		}

		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);

			db = emf.createEntityManager();
		}

	}

	public void close() {
		db.close();
		System.out.println("DataBase closed");
	}
	
	public void removeMezua(Mezua mezua) {
		if(mezua instanceof BezeroartekoMezua) {
			BezeroartekoMezua m = db.find(BezeroartekoMezua.class, mezua.getIdentifikadorea());
			Bezeroa nork = m.getIgorlea();
			Bezeroa nori = m.getHartzailea();
			db.getTransaction().begin();
			nork.ezabatuBidalitakoBezeroMezua(m);
			nori.ezabatuJasotakoBezeroMezua(m);
			db.remove(m);
			db.getTransaction().commit();
		}else {
			ArretaMezua m = db.find(ArretaMezua.class, mezua.getIdentifikadorea());
			ArretaElkarrizketa elkarrizketa = m.getElkarrizketa();
			db.getTransaction().begin();
			if(elkarrizketa.isAmaituta()) {
				elkarrizketa.removeMezua(m);
				db.remove(m);
				if(elkarrizketa.mezurikEz()) {
					db.remove(elkarrizketa);
				}
			}else {
				m.setIkusgaiBezeroarentzat(false);
			}
			db.getTransaction().commit();
		}
    }

	public Bezeroa getBezeroa(String ErabiltzaileIzena) {
		Bezeroa erabiltzaile = db.find(Bezeroa.class, ErabiltzaileIzena);
		return erabiltzaile;
	}
	
	public ArretaElkarrizketa arretaElkarrizketaSortu(Bezeroa bezeroa, String gaia, String mezua) {
		Bezeroa erabiltzailea = db.find(Bezeroa.class, bezeroa.getErabiltzaileIzena());
		db.getTransaction().begin();
		ArretaElkarrizketa elkarrizketa = erabiltzailea.addElkarrizketa(gaia);
		elkarrizketa.addMezua(mezua, true);
		db.persist(elkarrizketa);
		db.getTransaction().commit();
		return elkarrizketa;
	}
	
	public ArretaElkarrizketa arretaMezuaBidali(ArretaElkarrizketa elkarrizketa, String mezua, boolean langileari) {
		ArretaElkarrizketa elkar = db.find(ArretaElkarrizketa.class, elkarrizketa.getIdentifikadorea());
		db.getTransaction().begin();
		elkar.addMezua(mezua, langileari);
		db.getTransaction().commit();
		return elkar;
	}
	
	public ArretaElkarrizketa bezeroaEsleitu(Langilea langilea) {
		TypedQuery<ArretaElkarrizketa> query = db.createQuery("SELECT ae FROM ArretaElkarrizketa ae WHERE ae.langilea IS NULL AND ae.amaituta=false", ArretaElkarrizketa.class);
		List<ArretaElkarrizketa> elkarrizketak = query.getResultList();
		if(elkarrizketak.isEmpty()) {
			return null;
		}
		Langilea l = db.find(Langilea.class, langilea.getErabiltzaileIzena());
		db.getTransaction().begin();
	    ArretaElkarrizketa elkarrizketa = elkarrizketak.get(0);
	    elkarrizketa.setLangilea(l);
		l.addElkarrizketa(elkarrizketa);
		db.getTransaction().commit();
		return elkarrizketa;
	}
	
	public void amaituElkarrizketa(ArretaElkarrizketa ae) {
		ArretaElkarrizketa elkar = db.find(ArretaElkarrizketa.class, ae.getIdentifikadorea());
		db.getTransaction().begin();
		for(ArretaMezua am : elkar.getBezeroakBidalitakoak()) {
			db.remove(am);
		}
		elkar.getLangilea().removeElkarrizketa(elkar);
		for(ArretaMezua am : elkar.getLangileakBidalitakoak()) {
			if(!am.isIkusgaiBezeroarentzat()) {
				db.remove(am);
			}else {
				am.setIrakurrita(true);
			}
		}
		ArretaMezua m = elkar.addMezua("", false);
		m.setAzkena(true);
		elkar.setAmaituta(true);
		db.getTransaction().commit();
	}
	
	public ArretaElkarrizketa getArretaElkarrizketa(ArretaElkarrizketa ae) {
		ArretaElkarrizketa emaitza = db.find(ArretaElkarrizketa.class, ae.getIdentifikadorea());
		return emaitza;
	}
	
	public Pertsona register(RegisterParameter parameterObject) throws UserAlreadyExist{
		TypedQuery<Pertsona> query = db.createQuery("SELECT p FROM Pertsona p WHERE p.erabiltzaileIzena=?1", Pertsona.class);
		query.setParameter(1, parameterObject.erabiltzaileIzena);
		List<Pertsona> pertsona = query.getResultList();
		if(!pertsona.isEmpty()) {
			throw new UserAlreadyExist();
		}else {
			Pertsona berria = null;
			if(parameterObject.mota.equals("admin")) {
				berria = new Admin(parameterObject.izena, parameterObject.abizena1, parameterObject.abizena2, parameterObject.erabiltzaileIzena, parameterObject.pasahitza, parameterObject.telefonoZbkia, parameterObject.emaila, parameterObject.jaiotzeData);
			}else if (parameterObject.mota.equals("langilea")) {
				berria = new Langilea(parameterObject.izena, parameterObject.abizena1, parameterObject.abizena2, parameterObject.erabiltzaileIzena, parameterObject.pasahitza, parameterObject.telefonoZbkia, parameterObject.emaila, parameterObject.jaiotzeData);
			}else if (parameterObject.mota.equals("bezeroa")) {
				berria = new Bezeroa(parameterObject.izena, parameterObject.abizena1, parameterObject.abizena2, parameterObject.erabiltzaileIzena, parameterObject.pasahitza, parameterObject.telefonoZbkia, parameterObject.emaila, parameterObject.jaiotzeData);
			}
			db.getTransaction().begin();
			db.persist(berria);
			db.getTransaction().commit();	
			return berria;
		}
	}
}
