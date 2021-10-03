package test.dataaccess;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Admin;
import domain.Apustua;
import domain.ArretaElkarrizketa;
import domain.ArretaMezua;
import domain.Bezeroa;
import domain.BezeroartekoMezua;
import domain.Event;
import domain.Langilea;
import domain.Mezua;
import domain.Pertsona;
import domain.Question;
import exceptions.UserAlreadyExist;
import java.util.Collection;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("Creating TestDataAccess instance");

		open();
		
	}

	
	public void open(){
		
		System.out.println("Opening TestDataAccess instance ");

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		
	}
	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}

	public boolean removeEvent(Event ev) {
		System.out.println(">> DataAccessTest: removeEvent");
		Event e = db.find(Event.class, ev.getEventNumber());
		if (e!=null) {
			db.getTransaction().begin();
			db.remove(e);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
		
	public Event addEventWithQuestion(String desc, Date d, String question, float qty) {
		System.out.println(">> DataAccessTest: addEvent");
		Event ev=null;
			db.getTransaction().begin();
			try {
			    ev=new Event(desc,d);
			    ev.addQuestion(question, qty);
				db.persist(ev);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return ev;
    }
	
	public boolean existQuestion(Event ev,Question q) {
		System.out.println(">> DataAccessTest: existQuestion");
		Event e = db.find(Event.class, ev.getEventNumber());
		if (e!=null) {
			return e.DoesQuestionExists(q.getQuestion());
		} else 
		return false;
		
	}
	
	public Pertsona register(String izena, String abizena1, String abizena2, String erabiltzaileIzena, String pasahitza, String telefonoZbkia, String emaila, Date jaiotzeData, String mota) throws UserAlreadyExist{
		TypedQuery<Pertsona> query = db.createQuery("SELECT p FROM Pertsona p WHERE p.erabiltzaileIzena=?1", Pertsona.class);
		query.setParameter(1, erabiltzaileIzena);
		List<Pertsona> pertsona = query.getResultList();
		if(!pertsona.isEmpty()) {
			throw new UserAlreadyExist();
		}else {
			Pertsona berria = null;
			if(mota.equals("admin")) {
				berria = new Admin(izena, abizena1, abizena2, erabiltzaileIzena, pasahitza, telefonoZbkia, emaila, jaiotzeData);
			}else if (mota.equals("langilea")) {
				berria = new Langilea(izena, abizena1, abizena2, erabiltzaileIzena, pasahitza, telefonoZbkia, emaila, jaiotzeData);
			}else if (mota.equals("bezeroa")) {
				berria = new Bezeroa(izena, abizena1, abizena2, erabiltzaileIzena, pasahitza, telefonoZbkia, emaila, jaiotzeData);
			}
			db.getTransaction().begin();
			if (berria != null) db.persist(berria);
			db.getTransaction().commit();
			return berria;
		}
	}
	
	public List<Apustua> getApustuak(String erabiltzaileIzena) {
		List<Apustua> apustuak = new ArrayList<>();
		TypedQuery<Apustua> query = db.createQuery("SELECT a FROM Apustua a WHERE a.erabiltzaileIzena=?1", Apustua.class);
		query.setParameter(1, erabiltzaileIzena);
		apustuak = query.getResultList();
		
		return apustuak;
	
	}
	
	public Pertsona removeUser(String erabiltzaileIzena) {
		TypedQuery<Pertsona> query = db.createQuery("SELECT p FROM Pertsona p WHERE p.erabiltzaileIzena=?1", Pertsona.class);
		query.setParameter(1, erabiltzaileIzena);
		List<Pertsona> pertsona = query.getResultList();
		
		if (!pertsona.isEmpty()) {
			db.getTransaction().begin();
			db.remove(pertsona.get(0));
			db.getTransaction().commit();
			
			return pertsona.get(0);
		}
		return null;
	}
	
	
	public BezeroartekoMezua bidaliMezua(Bezeroa nork, Bezeroa nori, String mezua, String gaia, String mota, double zenbatApostatu, double hilabeteanZenbat, double zenbatErrepikatuarentzat) {
		Bezeroa igorlea = db.find(Bezeroa.class, nork.getErabiltzaileIzena());
		Bezeroa hartzailea = db.find(Bezeroa.class, nori.getErabiltzaileIzena());
		db.getTransaction().begin();
		BezeroartekoMezua mezuBerria = igorlea.addBidalitakoBezeroMezua(nori, mezua, gaia, mota, zenbatApostatu, hilabeteanZenbat, zenbatErrepikatuarentzat);
		hartzailea.addJasotakoBezeroMezua(mezuBerria);
		db.persist(mezuBerria);
		db.getTransaction().commit();
		return mezuBerria;
    }
	
	public Mezua mezuaJaso(Mezua mezua) {
		if(mezua == null) return null;
		if(mezua instanceof BezeroartekoMezua) {
			BezeroartekoMezua m = db.find(BezeroartekoMezua.class, mezua.getIdentifikadorea());
			return m;
		}else if (mezua instanceof ArretaMezua) {
			ArretaMezua m = db.find(ArretaMezua.class, mezua.getIdentifikadorea());
			return m;
		}else {
			return null;
		}
	}
	
	public void removeArretaElkarhizketaGuztiz(ArretaElkarrizketa arretaElk) {
		if(arretaElk!=null) {
			ArretaElkarrizketa ae = db.find(ArretaElkarrizketa.class, arretaElk);
			if(ae != null) {
				db.getTransaction().begin();
				
				for(ArretaMezua amB: ae.getBezeroakBidalitakoak()) {
					ArretaMezua a = db.find(ArretaMezua.class, amB.getIdentifikadorea());
					if(a != null) {
						db.remove(a);
					}
					
				}
				ae.getBezeroakBidalitakoak().clear();
				
				for(ArretaMezua amL: ae.getLangileakBidalitakoak()) {
					ArretaMezua a = db.find(ArretaMezua.class, amL.getIdentifikadorea());
					if(a != null) {
						db.remove(a);
					}
					
				}
				ae.getLangileakBidalitakoak().clear();
				Langilea lang = ae.getLangilea();
				if(lang != null) {
					lang.removeElkarrizketa(ae);
				}
				db.remove(ae);
				db.getTransaction().commit();
			}
		}
		
	}
	public Vector<ArretaMezua> getArretaMezuakDatubaseanBadaude(ArretaElkarrizketa ae){
		ArretaElkarrizketa emaitza = db.find(ArretaElkarrizketa.class, ae.getIdentifikadorea());
		Stream<ArretaMezua> strAmB, strAmL;
		strAmB = emaitza.getBezeroakBidalitakoak().stream()
				.filter(am -> this.mezuaJaso(am) != null);
		strAmL = emaitza.getLangileakBidalitakoak().stream()
				.filter(am -> this.mezuaJaso(am) != null);
		return Stream.concat(strAmB, strAmL).collect(Collectors.toCollection(Vector::new));
	}
}

