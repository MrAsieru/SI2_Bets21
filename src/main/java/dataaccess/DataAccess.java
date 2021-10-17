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
public class DataAccess {
	
	protected EntityManager db;
	protected EntityManagerFactory emf;
	protected DataAccessInitializer defaultInitializer = new MyDefaultDAI();

	ConfigXML c = ConfigXML.getInstance();

	public DataAccess(boolean initializeMode) {
		Logger.getLogger(this.getClass().getName()).info("Creating DataAccess instance => isDatabaseLocal: " + c.isDatabaseLocal()
				+ " getDatabBaseOpenMode: " + c.getDataBaseOpenMode());
		open(initializeMode);
	}

	public DataAccess() {
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
	
	public Vector<Question> getQuestions(Event event) {
		System.out.println(">> DataAccess: getQuestions");
		Vector<Question> res = new Vector<Question>();
		TypedQuery<Question> query = db.createQuery("SELECT q FROM Question q WHERE q.event=?1", Question.class);
		query.setParameter(1, event);
		List<Question> events = query.getResultList();
		for (Question q : events) {
			System.out.println(q.toString());
			res.add(q);
		}
		return res;
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

	private void gehituApustua(ArrayList<Pronostikoa> pronostikoak, ArrayList<Pronostikoa> pronostikoSorta) {
		Pronostikoa pronos;
		for(Pronostikoa p : pronostikoak) {
			pronos = db.find(Pronostikoa.class, p.getIdentifikadorea());
			pronostikoSorta.add(pronos);
		}
	}
	
	public Bezeroa deleteApustua(Apustua apustua) throws EventFinished{
		db.getTransaction().begin();
		Apustua a=db.find(Apustua.class, apustua.getIdentifikadorea());
		ArrayList<Pronostikoa> pronostikoak = a.getPronostikoak();
		Date today = new Date();
		for(Pronostikoa p : pronostikoak) {
			Date eventDate = p.getQuestion().getEvent().getEventDate();
			if(!eventDate.after(today)) {
				throw new EventFinished();
			}
		}
		Bezeroa bezeroa = a.getBezeroa();
		bezeroa.removeApustua(a);
		if(a.getErrepikatua()!=null) {
			Errepikapena errepikapen=bezeroa.getErrepikapena(a.getErrepikatua());
			errepikapen.eguneratuHilHonetanGeratzenDena(a.getKopurua());
		}
		bezeroa.addMugimendua("Apustua ezabatu ("+a.getIdentifikadorea()+")",a.getKopurua(),"bueltatu");
		for(Pronostikoa p : pronostikoak) {
			p.removeApustua(a);
		}
		Vector<Errepikapena> errepikatzaileak= bezeroa.getErrepikatzaileak();
		for(Errepikapena er : errepikatzaileak) {
			Bezeroa bez = er.getNork();
			Apustua apusErr = bez.baduApustua(a);
			if(apusErr!=null) {
				bez.removeApustua(apusErr);
				bez.addMugimendua("Apustu errepikatua ezabatu ("+bezeroa+")", apusErr.getKopurua(), "bueltatu");
				for (Pronostikoa p: apusErr.getPronostikoak()) {
					p.removeApustua(apusErr);
				}
				er.eguneratuHilHonetanGeratzenDena(apusErr.getKopurua());
				db.remove(apusErr);
			}
		}
		db.remove(a);
		db.getTransaction().commit();
		return bezeroa;
	}
	
	public Bezeroa diruaSartu(double u, Bezeroa bezeroa) {
		db.getTransaction().begin();
		Bezeroa erabiltzaile = db.find(Bezeroa.class, bezeroa.getErabiltzaileIzena());
		erabiltzaile.addMugimendua("Bankuko diru-sarrera", u, "bankua");
		db.persist(erabiltzaile);
		db.getTransaction().commit();
		return erabiltzaile;
	}
	
	public void ezabatuGertaera(Event event) {
		Bezeroa bezeroa;
		int x;
		Event e = db.find(Event.class, event.getEventNumber());
		db.getTransaction().begin();
		for (Question question : e.getQuestions()) {
			for (Pronostikoa pronostic : question.getPronostics()) {
				for (Apustua bet : pronostic.getApustuak()) {
					x = bet.removePronostikoa(pronostic);
					if(x==0) {
						bezeroa=bet.getBezeroa();
						if (bet.getErrepikatua()!=null) {
							Bezeroa erre=bet.getErrepikatua();
							Errepikapena er=bezeroa.getErrepikapena(erre);
							er.eguneratuHilHonetanGeratzenDena(bet.getKopurua());
						}
						bezeroa.addMugimendua("Apustuaren dirua itzuli ("+bet.getIdentifikadorea()+")", bet.getKopurua(),"bueltatu");
						bezeroa.removeApustua(bet);
						db.remove(bet);
					}else if(bet.getAsmatutakoKop().equals(bet.getPronostikoKop())) {
						double komisioa = 0;
						if (bet.getErrepikatua()!=null) {
							Bezeroa bez = bet.getErrepikatua();
							bezeroa = bet.getBezeroa();
							Errepikapena errepikapen=bezeroa.getErrepikapena(bez);
							komisioa=(bet.getKopurua()*bet.getKuotaTotala()-bet.getKopurua())*errepikapen.getKomisioa();
							bez.addMugimendua("Apustu errepikatuaren komisioa ("+bezeroa+")", komisioa,"irabazi");
						}
						bezeroa=bet.getBezeroa();
						double irabazia=bet.getKopurua()*bet.getKuotaTotala()-komisioa;
						bezeroa.addMugimendua("Apustua irabazi ("+bet.getIdentifikadorea()+")", irabazia, "irabazi");
					}
					System.out.println(bet.getPronostikoak()+"  berrize");
				}
			}
		}
		db.remove(e);
		db.getTransaction().commit();
		Apustua a = db.find(Apustua.class, 53);
		System.out.println(a);
	}
	
	public Bezeroa getBezeroa(String ErabiltzaileIzena) {
		Bezeroa erabiltzaile = db.find(Bezeroa.class, ErabiltzaileIzena);
		return erabiltzaile;
	}
	
	public Langilea getLangilea(String ErabiltzaileIzena) {
		Langilea erabiltzaile = db.find(Langilea.class, ErabiltzaileIzena);
		return erabiltzaile;
	}
	
	public Vector<Bezeroa> getBezeroak(String username, Bezeroa bezeroa){
		Bezeroa erabiltzaile = db.find(Bezeroa.class, bezeroa.getErabiltzaileIzena());
		Vector<Bezeroa> res = new Vector<Bezeroa>();
		TypedQuery<Bezeroa> query = db.createQuery("SELECT b FROM Bezeroa b", Bezeroa.class);
		List<Bezeroa> bezeroak = query.getResultList();
		for (Bezeroa b : bezeroak) {
			if(b.isPublikoa() && !b.getErabiltzaileIzena().equals(erabiltzaile.getErabiltzaileIzena()) && b.getErabiltzaileIzena().contains(username) && !erabiltzaile.baduMezua(b) && !erabiltzaile.errepikapenErlazioaDu(b)) {
				res.add(b);
			}
		}
		return res;
	}
	
	public Bezeroa bidaliMezua(Bezeroa nork, Bezeroa nori, String mezua, String gaia, String mota, double zenbatApostatu, double hilabeteanZenbat, double zenbatErrepikatuarentzat) {
		Bezeroa igorlea = db.find(Bezeroa.class, nork.getErabiltzaileIzena());
		Bezeroa hartzailea = db.find(Bezeroa.class, nori.getErabiltzaileIzena());
		db.getTransaction().begin();
		BezeroartekoMezua mezuBerria = igorlea.addBidalitakoBezeroMezua(nori, mezua, gaia, mota, zenbatApostatu, hilabeteanZenbat, zenbatErrepikatuarentzat);
		hartzailea.addJasotakoBezeroMezua(mezuBerria);
		db.persist(mezuBerria);
		db.getTransaction().commit();
		return igorlea;
    }
	
	public void errepikatu(Bezeroa nork, Bezeroa nori, double apustatukoDena, double hilabetekoMax, double komisioa){
		Bezeroa errepikatzailea = db.find(Bezeroa.class, nork.getErabiltzaileIzena());
		Bezeroa errepikatua = db.find(Bezeroa.class, nori.getErabiltzaileIzena());
		db.getTransaction().begin();
		Errepikapena errepikapenBerria = errepikatua.addErrepikatzailea(nork, apustatukoDena, hilabetekoMax, komisioa);
		errepikatzailea.addErrepikatua(errepikapenBerria);
		db.persist(errepikapenBerria);
		db.getTransaction().commit();
	}
	
	public Vector<Mezua> getMezuak(Bezeroa bezeroa){
		Bezeroa erabiltzailea = db.find(Bezeroa.class, bezeroa.getErabiltzaileIzena());
		return erabiltzailea.getMezuak();
	}
	
	public void mezuaIrakurri(Mezua mezua) {
		Mezua m = db.find(Mezua.class, mezua.getIdentifikadorea());
		db.getTransaction().begin();
		m.setIrakurrita(true);
		db.getTransaction().commit();
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
	
	public Bezeroa eguneratuEzarpenak(Bezeroa b, double komisioa, boolean publikoa) {
		Bezeroa erabiltzailea = db.find(Bezeroa.class, b.getErabiltzaileIzena());
		db.getTransaction().begin();
		erabiltzailea.eguneratuEzarpenak(publikoa, komisioa);
		db.getTransaction().commit();
		return erabiltzailea;
	}
	
	public Vector<PronostikoaContainer> getPronostikoak(Apustua a){
		Apustua ap = db.find(Apustua.class, a.getIdentifikadorea());
		ArrayList<Pronostikoa> pronostikoak = ap.getPronostikoak();
		Vector<PronostikoaContainer> emaitza = new Vector<PronostikoaContainer>();
		for(Pronostikoa p : pronostikoak) {
			emaitza.add(new PronostikoaContainer(p));
		}
		return emaitza;
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
	
	public BezeroaContainer getBezeroaContainer(Bezeroa b){
		Bezeroa bezeroa = db.find(Bezeroa.class, b.getErabiltzaileIzena());
		return new BezeroaContainer(bezeroa);
	}
	
	public void geldituElkarrizketa(ArretaElkarrizketa ae) {
		ArretaElkarrizketa elkar = db.find(ArretaElkarrizketa.class, ae.getIdentifikadorea());
		db.getTransaction().begin();
		elkar.gelditu();
		db.getTransaction().commit();
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
	
	public void gehituPuntuazioa(ArretaElkarrizketa l, Integer x) {
		ArretaElkarrizketa arretaElkarrizketa = db.find(ArretaElkarrizketa.class, l.getIdentifikadorea());
		Langilea langilea = arretaElkarrizketa.getLangilea();
		db.getTransaction().begin();
		langilea.addBalorazioa(x);
		db.getTransaction().commit();
	}
	
	public void eguneratuErrepikapenak() {
		TypedQuery<Errepikapena> query = db.createQuery("SELECT e FROM Errepikapena e", Errepikapena.class);
		List<Errepikapena> lista=query.getResultList();
		db.getTransaction().begin();
		for (Errepikapena i: lista) {
				i.setHilHonetanGeratzenDena(i.getHilabetekoMax());
		}
		db.getTransaction().commit();
	}
	
	public Vector<Langilea> getLangileak() {
		Vector<Langilea> langileak = new Vector<Langilea>();
		TypedQuery<Langilea> query = db.createQuery("SELECT l FROM Langilea l", Langilea.class);
		List<Langilea> list = query.getResultList();
		for (Langilea l : list) {
			langileak.add(l);
		}
		return langileak;
	}
	
	public ArrayList<ErrepikatuakContainer> getErrepikatzaileak(Bezeroa bezeroa) {
		ArrayList<ErrepikatuakContainer> emaitza = new ArrayList<ErrepikatuakContainer>();
		Bezeroa erabiltzailea = db.find(Bezeroa.class, bezeroa.getErabiltzaileIzena());
		Vector<Errepikapena> mezuak = erabiltzailea.getErrepikatzaileak();
		ErrepikatuakContainer x;

		for (Errepikapena m : mezuak) {
			x = new ErrepikatuakContainer(m);
			emaitza.add(x);
		}
		return emaitza;
	}

	public ArrayList<ErrepikatuakContainer> getErrepikapenak(Bezeroa bezeroa) {
		ArrayList<ErrepikatuakContainer> emaitza = new ArrayList<ErrepikatuakContainer>();
		Bezeroa erabiltzailea = db.find(Bezeroa.class, bezeroa.getErabiltzaileIzena());
		Vector<Errepikapena> mezuak = erabiltzailea.getErrepikatuak();
		ErrepikatuakContainer x;

		for (Errepikapena m : mezuak) {
			x = new ErrepikatuakContainer(m);
			emaitza.add(x);
		}
		return emaitza;
	}

	public void jarraitzeariUtzi(Errepikapena errepikapena) {
		Errepikapena e = db.find(Errepikapena.class, errepikapena.getIdentifikadorea());
		Bezeroa nork = e.getNork();
		Bezeroa nori = e.getNori();
		db.getTransaction().begin();
		nork.ezabatuErrepikatua(e);
		nori.ezabatuErrepikatzailea(e);
		db.remove(e);
		db.getTransaction().commit();
	}
	
	public ArretaElkarrizketa getArretaElkarrizketa(ArretaElkarrizketa ae) {
		ArretaElkarrizketa emaitza = db.find(ArretaElkarrizketa.class, ae.getIdentifikadorea());
		return emaitza;
	}
}
