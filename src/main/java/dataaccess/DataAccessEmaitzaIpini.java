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
import exceptions.EventAlreadyExist;
import exceptions.EventFinished;
import exceptions.PronosticAlreadyExist;
import exceptions.QuestionAlreadyExist;
import exceptions.UserAlreadyExist; 

import java.util.logging.Logger;
/**
 * It implements the data access to the objectDb database
 */
public class DataAccessEmaitzaIpini {
	
	protected EntityManager db;
	protected EntityManagerFactory emf;
	protected DataAccessInitializer defaultInitializer = new MyDefaultDAI();

	ConfigXML c = ConfigXML.getInstance();

	public DataAccessEmaitzaIpini(boolean initializeMode) {
		Logger.getLogger(this.getClass().getName()).info("Creating DataAccess instance => isDatabaseLocal: " + c.isDatabaseLocal()
				+ " getDatabBaseOpenMode: " + c.getDataBaseOpenMode());
		open(initializeMode);
	}

	public DataAccessEmaitzaIpini() {
		this(false);
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
		defaultInitializer.initializeDB(db);
	}

	/**
	 * This method creates a question for an event, with a question text and the
	 * minimum bet
	 * 
	 * @param event      to which question is added
	 * @param question   text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws QuestionAlreadyExist if the same question already exists for the
	 *                              event
	 */
	public Question createQuestion(Event event, String question, double betMinimum) throws QuestionAlreadyExist {
		System.out.println(">> DataAccess: createQuestion=> event= " + event + " question= " + question + " betMinimum="
				+ betMinimum);

		Event ev = db.find(Event.class, event.getEventNumber());

		if (ev.DoesQuestionExists(question))
			throw new QuestionAlreadyExist(ResourceBundle.getBundle("Etiquetas").getString("ErrorQueryAlreadyExist"));

		db.getTransaction().begin();
		Question q = ev.addQuestion(question, betMinimum);
		// db.persist(q);
		db.persist(ev); // db.persist(q) not required when CascadeType.PERSIST is added in questions
						// property of Event class
						// @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
		db.getTransaction().commit();
		return q;

	}

	/**
	 * This method retrieves from the database the events of a given date
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
	public Vector<Event> getEvents(Date date) {
		System.out.println(">> DataAccess: getEvents");
		Vector<Event> res = new Vector<Event>();
		TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.eventDate=?1", Event.class);
		query.setParameter(1, date);
		List<Event> events = query.getResultList();
		for (Event ev : events) {
			System.out.println(ev.toString());
			res.add(ev);
		}
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param date of the month for which days with events want to be retrieved
	 * @return collection of dates
	 */
	public Vector<Date> getEventsMonth(Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		Vector<Date> res = new Vector<Date>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT ev.eventDate FROM Event ev WHERE ev.eventDate BETWEEN ?1 and ?2", Date.class);
		query.setParameter(1, firstDayMonthDate);
		query.setParameter(2, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		for (Date d : dates) {
			System.out.println(d.toString());
			res.add(d);
		}
		return res;
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

	public boolean existQuestion(Event event, String question) {
		System.out.println(">> DataAccess: existQuestion=> event= " + event + " question= " + question);
		Event ev = db.find(Event.class, event.getEventNumber());
		return ev.DoesQuestionExists(question);

	}
	
	public Pertsona isLogin(String erabiltzaileIzena, String pasahitza) {
		TypedQuery<Pertsona> query = db.createQuery("SELECT p FROM Pertsona p WHERE p.erabiltzaileIzena=?1 AND p.pasahitza=?2", Pertsona.class);
		query.setParameter(1, erabiltzaileIzena);
		query.setParameter(2, pasahitza);
		List<Pertsona> pertsona = query.getResultList();
		if(pertsona.isEmpty()) {
			return null;
		}else {
			return pertsona.get(0);
		}
	}
	
	
	public void createEvent(String description, Date eventDate) throws EventAlreadyExist{
		TypedQuery<Event> query = db.createQuery("SELECT e FROM Event e WHERE e.description=?1 AND e.eventDate=?2", Event.class);
		query.setParameter(1, description);
		query.setParameter(2, eventDate);
		List<Event> gertaera = query.getResultList();
		if(!gertaera.isEmpty()) {
			throw new EventAlreadyExist();
		}else {
			Event berria = new Event(description, eventDate);
			db.getTransaction().begin();
			db.persist(berria);
			db.getTransaction().commit();
		}
	}

	public void close() {
		db.close();
		System.out.println("DataBase closed");
	}
	
	public void emaitzaIpini(Question question, Pronostikoa pronostikoa){
		Pronostikoa p = db.find(Pronostikoa.class, pronostikoa.getIdentifikadorea());
		Question q = db.find(Question.class, question.getQuestionNumber());
		db.getTransaction().begin();
		q.setResult(pronostikoa.getDeskripzioa());
		Vector<Apustua> apustuak = p.getApustuak();
		Bezeroa bezeroa;
		double irabazia;
		boolean irabazi;
		double komisioa;
		for(Apustua a : apustuak) {
			irabazi=a.eguneratuAsmatutakoKop();
			komisioa=0;
			if(irabazi) {
				if (a.getErrepikatua()!=null) {
					Bezeroa bez = a.getErrepikatua();
					bezeroa = a.getBezeroa();
					Errepikapena errepikapen=bezeroa.getErrepikapena(bez);
					komisioa=(a.getKopurua()*a.getKuotaTotala()-a.getKopurua())*errepikapen.getKomisioa();
					bez.addMugimendua("Apustu errepikatuaren komisioa ("+bezeroa+")", komisioa,"irabazi");
				}
				bezeroa=a.getBezeroa();
				irabazia=a.getKopurua()*a.getKuotaTotala()-komisioa;
				bezeroa.addMugimendua("Apustua irabazi ("+a.getIdentifikadorea()+")", irabazia, "irabazi");
			}
		}	
		db.getTransaction().commit();
	}
	
	public Pronostikoa createPronostic(Question question, String description, double kuota) throws PronosticAlreadyExist {

		Question q = db.find(Question.class, question.getQuestionNumber());
		
		if(q.DoesPronosticExists(description))
			throw new PronosticAlreadyExist();

		db.getTransaction().begin();
		Pronostikoa p = q.addPronostic(description, kuota);
		db.persist(q);
		db.getTransaction().commit();
		return p;

	}
	
	public Bezeroa apustuaEgin(ArrayList<Pronostikoa> pronostikoak, double a, Bezeroa bezero) {
		Bezeroa erabiltzaile = db.find(Bezeroa.class, bezero.getErabiltzaileIzena());
		Pronostikoa pronos;
		ArrayList<Pronostikoa> pronostikoSorta = new ArrayList<Pronostikoa>();
		gehituApustua(pronostikoak, pronostikoSorta);
		db.getTransaction().begin();
		Apustua apus = erabiltzaile.addApustua(pronostikoSorta, a, null);
		for(Pronostikoa p : pronostikoSorta) {
			p.addApustua(apus);
		}
		db.persist(apus);
		Vector<Errepikapena> jarraitzaile=erabiltzaile.getErrepikatzaileak();
		for(Errepikapena er: jarraitzaile) {
			double apustudiru=0;
			parametroarenAraberaProzesatu(a, erabiltzaile, pronostikoSorta, apus, er);
		}
		erabiltzaile.addMugimendua("Apustua egin ", -a, "jokatu");
		db.getTransaction().commit();
		return erabiltzaile;
	}

	private void gehituApustua(ArrayList<Pronostikoa> pronostikoak, ArrayList<Pronostikoa> pronostikoSorta) {
		Pronostikoa pronos;
		for(Pronostikoa p : pronostikoak) {
			pronos = db.find(Pronostikoa.class, p.getIdentifikadorea());
			pronostikoSorta.add(pronos);
		}
	}
	
	private void parametroarenAraberaProzesatu(double a, Bezeroa erabiltzaile, ArrayList<Pronostikoa> pronostikoSorta,
			Apustua apus, Errepikapena er) {
		double apustudiru;
		if (er.getHilabeteHonetanGeratzenDena()>0) {
			if (er.getHilabeteHonetanGeratzenDena()>=er.getApustatukoDena()*a) {
				apustudiru=er.getApustatukoDena()*a;
			} else {
				apustudiru=er.getHilabeteHonetanGeratzenDena();
			}
			if (er.getNork().getDirua() >= apustudiru) {
				Apustua apustu = er.getNork().addApustua(pronostikoSorta, apustudiru, erabiltzaile);
				for (Pronostikoa p : pronostikoSorta) {
					p.addApustua(apustu);
				}
				er.getNork().addMugimendua("Apustu errepikatua egin ("+erabiltzaile+")", -apustudiru, "jokatu");
				er.eguneratuHilHonetanGeratzenDena(-apustudiru);
				db.persist(apus);
			}
		}
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
			db.persist(berria);
			db.getTransaction().commit();	
			return berria;
		}
	}
	
}
