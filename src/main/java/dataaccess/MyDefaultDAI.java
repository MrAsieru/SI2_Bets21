package dataaccess;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import configuration.UtilDate;
import domain.Admin;
import domain.Apustua;
import domain.Bezeroa;
import domain.Errepikapena;
import domain.Event;
import domain.Langilea;
import domain.Mugimendua;
import domain.Pertsona;
import domain.Pronostikoa;
import domain.Question;

public class MyDefaultDAI implements DataAccessInitializer {
	
	private Map<String, Event> eventDictionary = new HashMap<>();
	private Map<String, Question> questionDictionary = new HashMap<>();
	private Map<String, Pertsona> pertsonaDictionary = new HashMap<>();
	private Map<String, Pronostikoa> pronostikoDictionary = new HashMap<>();
	private Map<String, Mugimendua> mugimenduaDictionary = new HashMap<>();
	
	private int[] getTodayDateOnArrayYM() {
		int[] dateym = new int[2];
		try {
			Calendar today = Calendar.getInstance();
	
			int month = today.get(Calendar.MONTH);
			month += 1;
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 0;
				year += 1;
			}
			dateym[0] = year;
			dateym[1] = month;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateym;
	}
	
	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 * @param entityManagerDB TODO
	 */
	@Override
	public void initializeDB(EntityManager entityManagerDB) {
		entityManagerDB.getTransaction().begin();
		createEvents();
		createQuestionsAndSetRelationsWithEvents();
		createUsers();
		createPronostikoakAndCreateRelationsWithQuestions();
		createErrepikapenakAndCreateRelationsWithBezeroa();
		createApustuakAndCreateRalationsWithPronostikoakBezeroak();
		createMugimenduakAndCreateRelationsWithBezeroak();
		persistAllObjectsInsideDefinedDictionaries(entityManagerDB);
		entityManagerDB.getTransaction().commit();
		System.out.println("Db initialized");
	
	}
	
	private void createMugimenduakAndCreateRelationsWithBezeroak() {
	
		Bezeroa b2 = (Bezeroa) pertsonaDictionary.get("b2");
		
		mugimenduaDictionary.put("m1", b2.addMugimendua("Bankuko diru-sarrera", 52, "bankua", UtilDate.newDate(2021, 2, 15)));
		mugimenduaDictionary.put("m2", b2.addMugimendua("Apustua egin", -2, "jokatu", UtilDate.newDate(2021, 2, 16)));
		mugimenduaDictionary.put("m3", b2.addMugimendua("Bankuko diru-sarrera", 30, "bankua", UtilDate.newDate(2021, 2, 15)));
		mugimenduaDictionary.put("m4", ((Bezeroa) pertsonaDictionary.get("b5")).addMugimendua("Apustu errepikatua egin ("+b2+")", -4, "jokatu", UtilDate.newDate(2021, 2, 16)));
		
	}

	private void createApustuakAndCreateRalationsWithPronostikoakBezeroak() {
		Bezeroa b2 = (Bezeroa) pertsonaDictionary.get("b2");
		Bezeroa b5 = (Bezeroa) pertsonaDictionary.get("b5");
		Pronostikoa pronos2 = pronostikoDictionary.get("pronos2");
		Pronostikoa pronos4 = pronostikoDictionary.get("pronos5");

		
		ArrayList<Pronostikoa> pronostikoLista = new ArrayList<Pronostikoa>();
		pronostikoLista.add(pronos2);
		pronostikoLista.add(pronos4);
		
		Apustua apustua1 = b2.addApustua(pronostikoLista, 2, null);
		Apustua apustu2=b5.addApustua(pronostikoLista, 4, b2);
		
		pronos2.addApustua(apustua1);
		pronos2.addApustua(apustu2);
		pronos4.addApustua(apustu2);
		pronos4.addApustua(apustua1);
		
	}

	private void createUsers() {
		
		createUsersAdmin();
		createUsersLangilea();
		createUsersBezeroa();
		
	}
	private void createUsersAdmin() {
		pertsonaDictionary.put("a1", new Admin("Ramon", "Rodriguez", "Soto", "Admin", "aaaaaaaa", "666666666","ramonAdmindb.@gmail.com", UtilDate.newDate(2001,2,12)));
	}
	private void createUsersLangilea() {
		pertsonaDictionary.put("l1", new Langilea("Oier", "Elola", "Urkizu", "Elola", "aaaaaaaa", "987654321", "oierurkizu@gmail.com", UtilDate.newDate(2001,7,23)));
		pertsonaDictionary.put("l2", new Langilea("Unax", "Lazkanotegi", "Bengoetxea", "UnaxLazka", "aaaaaaaa", "384625395","UnaxLazka@gmail.com", UtilDate.newDate(2001,7,23)));
	}
	private void createUsersBezeroa() {
		pertsonaDictionary.put("b1", 	new Bezeroa("Tarek", 	"Chamkhi",	 "Ermina", 	"Tarek12301", "aaaaaaaa", "123456789", "Tarek12301@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b2", 	new Bezeroa("Josu", 	"Loidi",	 "Gorostidi","Josulo", "aaaaaaaa", "123456789", "josulo@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b3", 	new Bezeroa("Jose", 	"Garc�a",	 "Perez", 		"JoseRamon", "aaaaaaaa", "123456789", "JoseRamon@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b4", 	new Bezeroa("Josu", 	"Perez",	 "Galdos", 		"Josueeee", "aaaaaaaa", "123456789", "Josueeee@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b5", 	new Bezeroa("Saioa", 	"Goikoetxea", "Ugarte", 	"Saioo99", "b", "123456789", "Saioo99@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b6", 	new Bezeroa("Mikel", 	"Artola",	 "Peraz", 		"Gamer75", "aaaaaaaa", "123456789", "Gamer75@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b7", 	new Bezeroa("Pello", 	"Garcia",	 "Lorca", 		"PelloJoxepe", "aaaaaaaa", "123456789", "PelloJoxepe@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b8", 	new Bezeroa("Karmele", 	"Loidi",	 "Gorostidi", 	"Katuu19", "aaaaaaaa", "123456789", "Katuu19@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b9", 	new Bezeroa("Eneko", 	"Sagastume", "Ontsalo", 	"Ontsalo", "aaaaaaaa", "123456789", "Ontsalo@gmail.com",UtilDate.newDate(2001,8,9)));
		pertsonaDictionary.put("b10",  	new Bezeroa("Naiara", 	"Agirre",	 "Urriza", 		"Na1ara", "aaaaaaaa", "123456789", "Na1ara@gmail.com",UtilDate.newDate(2001,8,9)));
		((Bezeroa) pertsonaDictionary.get("b2")).setPublikoa(false);
	}

	private void createEvents() {
		int[] tdy = getTodayDateOnArrayYM();
		int y = tdy[0];
		int m = tdy[1];
		eventDictionary.put("ev1" , new Event(1,"Atl�tico-Athletic", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev2" , new Event(2, "Eibar-Barcelona", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev3" , new Event(3, "Getafe-Celta", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev4" , new Event(4, "Alav�s-Deportivo", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev5" , new Event(5, "Espa�ol-Villareal", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev6" , new Event(6, "Las Palmas-Sevilla", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev7" , new Event(7, "Malaga-Valencia", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev8" , new Event(8, "Girona-Legan�s", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev9" , new Event(9, "Real Sociedad-Levante", UtilDate.newDate(y, m, 17)));
		eventDictionary.put("ev10", new Event(10, "Betis-Real Madrid", UtilDate.newDate(y, m, 17)));
        
		eventDictionary.put("ev11", new Event(11, "Atletico-Athletic", UtilDate.newDate(y, m, 1)));
		eventDictionary.put("ev12", new Event(12, "Eibar-Barcelona", UtilDate.newDate(y, m, 1)));
		eventDictionary.put("ev13", new Event(13, "Getafe-Celta", UtilDate.newDate(y, m, 1)));
		eventDictionary.put("ev14", new Event(14, "Alav�s-Deportivo", UtilDate.newDate(y, m, 1)));
		eventDictionary.put("ev15", new Event(15, "Espa�ol-Villareal", UtilDate.newDate(y, m, 1)));
		eventDictionary.put("ev16", new Event(16, "Las Palmas-Sevilla", UtilDate.newDate(y, m, 1)));
	
		eventDictionary.put("ev17", new Event(17, "M�laga-Valencia", UtilDate.newDate(y, m + 1, 28)));
		eventDictionary.put("ev18", new Event(18, "Girona-Legan�s", UtilDate.newDate(y, m + 1, 28)));
		eventDictionary.put("ev19", new Event(19, "Real Sociedad-Levante", UtilDate.newDate(y, m + 1, 28)));
		eventDictionary.put("ev20", new Event(20, "Betis-Real Madrid", UtilDate.newDate(y, m + 1, 28)));
		
		eventDictionary.put("event1", new Event(21,"Eibar-Celta", UtilDate.newDate(2021, 2, 17)));
		eventDictionary.put("event2", new Event(22,"Granada-Athletic", UtilDate.newDate(2021, 2, 17)));
	}
	
	private void createQuestionsAndSetRelationsWithEvents() {
		String qs1, qs2, qs3, qs4;

		if (Locale.getDefault().equals(new Locale("es"))) {
			qs1 = "¿Quién ganará el partido?";
			qs2 = "¿Quién meterá el primer gol?";
			qs3 = "¿Cuántos goles se marcarán?";
			qs4 = "¿Habrá goles en la primera parte?";
			
		} else if (Locale.getDefault().equals(new Locale("en"))) {
			qs1 = "Who will win the match?";
			qs2 = "Who will score first?";
			qs3 = "How many goals will be scored in the match?";
			qs4 = "Will there be goals in the first half?";
			
		} else {
			qs1 = "Zeinek irabaziko du partidua?";
			qs2 = "Zeinek sartuko du lehenengo gola?";
			qs3 =  "Zenbat gol sartuko dira?";
			qs4 =  "Golak sartuko dira lehenengo zatian?";
		}
		
		questionDictionary.put("q1", eventDictionary.get("ev1")	.addQuestion(qs1, 1));
		questionDictionary.put("q2", eventDictionary.get("ev1")	.addQuestion(qs2, 2));
		questionDictionary.put("q3", eventDictionary.get("ev11").addQuestion(qs1, 1));
		questionDictionary.put("q4", eventDictionary.get("ev11").addQuestion(qs3, 2));
		questionDictionary.put("q5", eventDictionary.get("ev17").addQuestion(qs1, 1));
		questionDictionary.put("q6", eventDictionary.get("ev17").addQuestion(qs4, 2));
		
		
		questionDictionary.put("ques1", eventDictionary.get("event1").addQuestion("Zeinek irabaziko du partidua?", 1));
		questionDictionary.put("ques2", eventDictionary.get("event1").addQuestion("Zeinek sartuko du lehenengo gola?", 1));
		questionDictionary.put("ques3", eventDictionary.get("event2").addQuestion("Zeinek irabaziko du partidua?", 1));
		questionDictionary.put("ques4", eventDictionary.get("event2").addQuestion("Golik sartuko al da lehen zatian?", 1));
		
	}
	
	private void createPronostikoakAndCreateRelationsWithQuestions() {
		pronostikoDictionary.put("pronos1", 	questionDictionary.get("ques1").addPronostic("1", (double)1.2));
		pronostikoDictionary.put("pronos2", 	questionDictionary.get("ques1").addPronostic("X", (double)1.5));//
		pronostikoDictionary.put("pronos3", 	questionDictionary.get("ques1").addPronostic("2", (double)1.8));
		pronostikoDictionary.put("pronos4", 	questionDictionary.get("ques2").addPronostic("1", (double)1.2));//
		pronostikoDictionary.put("pronos5", 	questionDictionary.get("ques2").addPronostic("2", (double)1.6));
		pronostikoDictionary.put("pronos6", 	questionDictionary.get("ques2").addPronostic("Golik ez", (double)1.8));
		pronostikoDictionary.put("pronos7", 	questionDictionary.get("ques3").addPronostic("1", (double)2.2));//
		pronostikoDictionary.put("pronos8", 	questionDictionary.get("ques3").addPronostic("X", (double)1.4));
		pronostikoDictionary.put("pronos9", 	questionDictionary.get("ques3").addPronostic("2", (double)1.2));
		pronostikoDictionary.put("pronos10", 	questionDictionary.get("ques4").addPronostic("Bai", (double)1.3));
		pronostikoDictionary.put("pronos11", 	questionDictionary.get("ques4").addPronostic("Ez", (double)2.5));//

		pronostikoDictionary.put("pronos12", 	questionDictionary.get("q1").addPronostic("1", (double)1.2));
		pronostikoDictionary.put("pronos13", 	questionDictionary.get("q1").addPronostic("X", (double)1.5));//
		pronostikoDictionary.put("pronos14", 	questionDictionary.get("q1").addPronostic("2", (double)1.8));
		pronostikoDictionary.put("pronos15", 	questionDictionary.get("q2").addPronostic("1", (double)1.2));//
		pronostikoDictionary.put("pronos16", 	questionDictionary.get("q2").addPronostic("2", (double)1.6));
		pronostikoDictionary.put("pronos17", 	questionDictionary.get("q2").addPronostic("Golik ez", (double)1.8));
		pronostikoDictionary.put("pronos12", 	questionDictionary.get("q3").addPronostic("1", (double)1.2));
		pronostikoDictionary.put("pronos13", 	questionDictionary.get("q3").addPronostic("X", (double)1.5));//
		pronostikoDictionary.put("pronos14", 	questionDictionary.get("q3").addPronostic("2", (double)1.8));
		pronostikoDictionary.put("pronos15", 	questionDictionary.get("q4").addPronostic("<2", (double)1.2));//
		pronostikoDictionary.put("pronos16", 	questionDictionary.get("q4").addPronostic("3", (double)1.6));
		pronostikoDictionary.put("pronos17", 	questionDictionary.get("q4").addPronostic(">3", (double)1.8));
	}
	
	private void createErrepikapenakAndCreateRelationsWithBezeroa() {
		Bezeroa b2 = (Bezeroa) pertsonaDictionary.get("b2");
		Bezeroa b5Errepikatzailea = (Bezeroa) pertsonaDictionary.get("b5");
		
		Errepikapena errepikapenBerria = b2.addErrepikatzailea(b5Errepikatzailea, 2, 10, 0.2);
		b5Errepikatzailea.addErrepikatua(errepikapenBerria);
	}
	
	
	private void persistAllObjectsInsideDefinedDictionaries(EntityManager pEntManagerDB) {
		persistAllObjectsInsideEventDictionary(pEntManagerDB);
		persistAllObjectsInsideQuestionDictionary(pEntManagerDB);
		persistAllObjectsInsidePertsonaDictionary(pEntManagerDB);
		persistAllObjectsInsidePronostikoDictionary(pEntManagerDB);
		persistAllObjectsInsideMugimenduaDictionary(pEntManagerDB);
	}

	private void persistAllObjectsInsideMugimenduaDictionary(EntityManager pEntManagerDB) {
		for(Object objectToPersist: this.mugimenduaDictionary.values()) {
			pEntManagerDB.persist(objectToPersist);
		}
	}

	private void persistAllObjectsInsidePronostikoDictionary(EntityManager pEntManagerDB) {
		for(Object objectToPersist: this.pronostikoDictionary.values()) {
			pEntManagerDB.persist(objectToPersist);
		}
	}

	private void persistAllObjectsInsidePertsonaDictionary(EntityManager pEntManagerDB) {
		for(Object objectToPersist: this.pertsonaDictionary.values()) {
			pEntManagerDB.persist(objectToPersist);
		}
	}

	private void persistAllObjectsInsideQuestionDictionary(EntityManager pEntManagerDB) {
		for(Object objectToPersist: this.questionDictionary.values()) {
			pEntManagerDB.persist(objectToPersist);
		}
	}

	private void persistAllObjectsInsideEventDictionary(EntityManager pEntManagerDB) {
		for(Object objectToPersist: this.eventDictionary.values()) {
			pEntManagerDB.persist(objectToPersist);
		}
	}
	

}
