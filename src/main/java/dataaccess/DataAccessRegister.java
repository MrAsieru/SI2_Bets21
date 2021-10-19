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
public class DataAccessRegister {
	
	protected EntityManager db;
	protected EntityManagerFactory emf;
	protected DataAccessInitializer defaultInitializer = new MyDefaultDAI();

	ConfigXML c = ConfigXML.getInstance();

	public DataAccessRegister(boolean initializeMode) {
		Logger.getLogger(this.getClass().getName()).info("Creating DataAccess instance => isDatabaseLocal: " + c.isDatabaseLocal()
				+ " getDatabBaseOpenMode: " + c.getDataBaseOpenMode());
		open(initializeMode);
	}

	public DataAccessRegister() {
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

	public void close() {
		db.close();
		System.out.println("DataBase closed");
	}
}
