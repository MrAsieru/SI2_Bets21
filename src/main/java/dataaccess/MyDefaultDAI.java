package dataaccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.persistence.EntityManager;

import configuration.UtilDate;
import domain.Admin;
import domain.Apustua;
import domain.Bezeroa;
import domain.Errepikapena;
import domain.Event;
import domain.Langilea;
import domain.Mugimendua;
import domain.Pronostikoa;
import domain.Question;

public class MyDefaultDAI implements DataAccessInitializer {

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
		try {
	
			Calendar today = Calendar.getInstance();
	
			int month = today.get(Calendar.MONTH);
			month += 1;
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 0;
				year += 1;
			}
	
			Event ev1 = new Event(1,"Atl�tico-Athletic", UtilDate.newDate(year, month, 17));
			Event ev2 = new Event(2, "Eibar-Barcelona", UtilDate.newDate(year, month, 17));
			Event ev3 = new Event(3, "Getafe-Celta", UtilDate.newDate(year, month, 17));
			Event ev4 = new Event(4, "Alav�s-Deportivo", UtilDate.newDate(year, month, 17));
			Event ev5 = new Event(5, "Espa�ol-Villareal", UtilDate.newDate(year, month, 17));
			Event ev6 = new Event(6, "Las Palmas-Sevilla", UtilDate.newDate(year, month, 17));
			Event ev7 = new Event(7, "Malaga-Valencia", UtilDate.newDate(year, month, 17));
			Event ev8 = new Event(8, "Girona-Legan�s", UtilDate.newDate(year, month, 17));
			Event ev9 = new Event(9, "Real Sociedad-Levante", UtilDate.newDate(year, month, 17));
			Event ev10 = new Event(10, "Betis-Real Madrid", UtilDate.newDate(year, month, 17));
	
			Event ev11 = new Event(11, "Atletico-Athletic", UtilDate.newDate(year, month, 1));
			Event ev12 = new Event(12, "Eibar-Barcelona", UtilDate.newDate(year, month, 1));
			Event ev13 = new Event(13, "Getafe-Celta", UtilDate.newDate(year, month, 1));
			Event ev14 = new Event(14, "Alav�s-Deportivo", UtilDate.newDate(year, month, 1));
			Event ev15 = new Event(15, "Espa�ol-Villareal", UtilDate.newDate(year, month, 1));
			Event ev16 = new Event(16, "Las Palmas-Sevilla", UtilDate.newDate(year, month, 1));
	
			Event ev17 = new Event(17, "M�laga-Valencia", UtilDate.newDate(year, month + 1, 28));
			Event ev18 = new Event(18, "Girona-Legan�s", UtilDate.newDate(year, month + 1, 28));
			Event ev19 = new Event(19, "Real Sociedad-Levante", UtilDate.newDate(year, month + 1, 28));
			Event ev20 = new Event(20, "Betis-Real Madrid", UtilDate.newDate(year, month + 1, 28));
	
			Question q1;
			Question q2;
			Question q3;
			Question q4;
			Question q5;
			Question q6;
	
			if (Locale.getDefault().equals(new Locale("es"))) {
				q1 = ev1.addQuestion("¿Quién ganará el partido?", 1);
				q2 = ev1.addQuestion("¿Quién meterá el primer gol?", 2);
				q3 = ev11.addQuestion("¿Quién ganará el partido?", 1);
				q4 = ev11.addQuestion("¿Cuántos goles se marcarán?", 2);
				q5 = ev17.addQuestion("¿Quién ganará el partido?", 1);
				q6 = ev17.addQuestion("¿Habrá goles en la primera parte?", 2);
			} else if (Locale.getDefault().equals(new Locale("en"))) {
				q1 = ev1.addQuestion("Who will win the match?", 1);
				q2 = ev1.addQuestion("Who will score first?", 2);
				q3 = ev11.addQuestion("Who will win the match?", 1);
				q4 = ev11.addQuestion("How many goals will be scored in the match?", 2);
				q5 = ev17.addQuestion("Who will win the match?", 1);
				q6 = ev17.addQuestion("Will there be goals in the first half?", 2);
			} else {
				q1 = ev1.addQuestion("Zeinek irabaziko du partidua?", 1);
				q2 = ev1.addQuestion("Zeinek sartuko du lehenengo gola?", 2);
				q3 = ev11.addQuestion("Zeinek irabaziko du partidua?", 1);
				q4 = ev11.addQuestion("Zenbat gol sartuko dira?", 2);
				q5 = ev17.addQuestion("Zeinek irabaziko du partidua?", 1);
				q6 = ev17.addQuestion("Golak sartuko dira lehenengo zatian?", 2);
			}
	
			Admin a1 = new Admin("Ramon", "Rodriguez", "Soto", "Admin", "aaaaaaaa", "666666666","ramonAdmindb.@gmail.com", UtilDate.newDate(2001,2,12));
			
			Langilea l1 = new Langilea("Oier", "Elola", "Urkizu", "Elola", "aaaaaaaa", "987654321", "oierurkizu@gmail.com", UtilDate.newDate(2001,7,23));
			Langilea l2 = new Langilea("Unax", "Lazkanotegi", "Bengoetxea", "UnaxLazka", "aaaaaaaa", "384625395","UnaxLazka@gmail.com", UtilDate.newDate(2001,7,23));
			
			Bezeroa b1 = new Bezeroa("Tarek", "Chamkhi", "Ermina", "Tarek12301", "aaaaaaaa", "123456789", "Tarek12301@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b2 = new Bezeroa("Josu", "Loidi", "Gorostidi", "Josulo", "aaaaaaaa", "123456789", "josulo@gmail.com",UtilDate.newDate(2001,8,9));
			b2.setPublikoa(false);
			Bezeroa b3 = new Bezeroa("Jose", "Garc�a", "Perez", "JoseRamon", "aaaaaaaa", "123456789", "JoseRamon@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b4 = new Bezeroa("Josu", "Perez", "Galdos", "Josueeee", "aaaaaaaa", "123456789", "Josueeee@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b5 = new Bezeroa("Saioa", "Goikoetxea", "Ugarte", "Saioo99", "b", "123456789", "Saioo99@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b6 = new Bezeroa("Mikel", "Artola", "Peraz", "Gamer75", "aaaaaaaa", "123456789", "Gamer75@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b7 = new Bezeroa("Pello", "Garcia", "Lorca", "PelloJoxepe", "aaaaaaaa", "123456789", "PelloJoxepe@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b8 = new Bezeroa("Karmele", "Loidi", "Gorostidi", "Katuu19", "aaaaaaaa", "123456789", "Katuu19@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b9 = new Bezeroa("Eneko", "Sagastume", "Ontsalo", "Ontsalo", "aaaaaaaa", "123456789", "Ontsalo@gmail.com",UtilDate.newDate(2001,8,9));
			Bezeroa b10 = new Bezeroa("Naiara", "Agirre", "Urriza", "Na1ara", "aaaaaaaa", "123456789", "Na1ara@gmail.com",UtilDate.newDate(2001,8,9));
			
			
			Event event1 = new Event(21,"Eibar-Celta", UtilDate.newDate(2021, 2, 17));
			Event event2 = new Event(22,"Granada-Athletic", UtilDate.newDate(2021, 2, 17));
			
			Question ques1 = event1.addQuestion("Zeinek irabaziko du partidua?", 1);
			Question ques2 = event1.addQuestion("Zeinek sartuko du lehenengo gola?", 1);
			Question ques3 = event2.addQuestion("Zeinek irabaziko du partidua?", 1);
			Question ques4 = event2.addQuestion("Golik sartuko al da lehen zatian?", 1);
			
			Pronostikoa pronos1, pronos2, pronos3, pronos4, pronos5, pronos6, pronos7, pronos8, pronos9, pronos10, pronos11, pronos12, pronos13, pronos14, pronos15, pronos16, pronos17;
			pronos1 = ques1.addPronostic("1", (double)1.2);
			pronos2 = ques1.addPronostic("X", (double)1.5);//
			pronos3 = ques1.addPronostic("2", (double)1.8);
			pronos4 = ques2.addPronostic("1", (double)1.2);//
			pronos5 = ques2.addPronostic("2", (double)1.6);
			pronos6 = ques2.addPronostic("Golik ez", (double)1.8);
			pronos7 = ques3.addPronostic("1", (double)2.2);//
			pronos8 = ques3.addPronostic("X", (double)1.4);
			pronos9 = ques3.addPronostic("2", (double)1.2);
			pronos10 = ques4.addPronostic("Bai", (double)1.3);
			pronos11 = ques4.addPronostic("Ez", (double)2.5);//
		
			pronos12 = q1.addPronostic("1", (double)1.2);
			pronos13 = q1.addPronostic("X", (double)1.5);//
			pronos14 = q1.addPronostic("2", (double)1.8);
			pronos15 = q2.addPronostic("1", (double)1.2);//
			pronos16 = q2.addPronostic("2", (double)1.6);
			pronos17 = q2.addPronostic("Golik ez", (double)1.8);
			pronos12 = q3.addPronostic("1", (double)1.2);
			pronos13 = q3.addPronostic("X", (double)1.5);//
			pronos14 = q3.addPronostic("2", (double)1.8);
			pronos15 = q4.addPronostic("<2", (double)1.2);//
			pronos16 = q4.addPronostic("3", (double)1.6);
			pronos17 = q4.addPronostic(">3", (double)1.8);
			
			
			Errepikapena errepikapenBerria = b2.addErrepikatzailea(b5, 2, 10, 0.2);
			b5.addErrepikatua(errepikapenBerria);
			
			ArrayList<Pronostikoa> p = new ArrayList<Pronostikoa>();
			p.add(pronos2);
			p.add(pronos4);
			Apustua apustua1 = b2.addApustua(p, 2, null);
			Apustua apustu2=b5.addApustua(p, 4, b2);
			
			pronos2.addApustua(apustua1);
			pronos2.addApustua(apustu2);
			pronos4.addApustua(apustu2);
			pronos4.addApustua(apustua1);
			
			entityManagerDB.persist(apustua1);
			entityManagerDB.persist(apustu2);
			
			Mugimendua m1,m2,m3,m4;
			m1 = b2.addMugimendua("Bankuko diru-sarrera", 52, "bankua", UtilDate.newDate(2021, 2, 15));
			m2 = b2.addMugimendua("Apustua egin", -2, "jokatu", UtilDate.newDate(2021, 2, 16));
			m3 = b2.addMugimendua("Bankuko diru-sarrera", 30, "bankua", UtilDate.newDate(2021, 2, 15));
			m4 = b5.addMugimendua("Apustu errepikatua egin ("+b2+")", -4, "jokatu", UtilDate.newDate(2021, 2, 16));
			
			entityManagerDB.persist(event1);
			entityManagerDB.persist(event2);
			
			entityManagerDB.persist(ques1);
			entityManagerDB.persist(ques2);
			entityManagerDB.persist(ques3);
			entityManagerDB.persist(ques4);
			
			entityManagerDB.persist(pronos1);
			entityManagerDB.persist(pronos2);
			entityManagerDB.persist(pronos3);
			entityManagerDB.persist(pronos4);
			entityManagerDB.persist(pronos5);
			entityManagerDB.persist(pronos6);
			entityManagerDB.persist(pronos7);
			entityManagerDB.persist(pronos8);
			entityManagerDB.persist(pronos9);
			entityManagerDB.persist(pronos10);
			entityManagerDB.persist(pronos11);
			
			entityManagerDB.persist(m1);
			entityManagerDB.persist(m2);
			entityManagerDB.persist(m3);
			entityManagerDB.persist(m4);			
			entityManagerDB.persist(a1);
			entityManagerDB.persist(l2);
			entityManagerDB.persist(l1);
			entityManagerDB.persist(b1);
			entityManagerDB.persist(b2);
			entityManagerDB.persist(b3);
			entityManagerDB.persist(b4);
			entityManagerDB.persist(b5);
			entityManagerDB.persist(b6);
			entityManagerDB.persist(b7);
			entityManagerDB.persist(b8);
			entityManagerDB.persist(b9);
			entityManagerDB.persist(b10);
	
			entityManagerDB.persist(q1);
			entityManagerDB.persist(q2);
			entityManagerDB.persist(q3);
			entityManagerDB.persist(q4);
			entityManagerDB.persist(q5);
			entityManagerDB.persist(q6);
	
			entityManagerDB.persist(ev1);
			entityManagerDB.persist(ev2);
			entityManagerDB.persist(ev3);
			entityManagerDB.persist(ev4);
			entityManagerDB.persist(ev5);
			entityManagerDB.persist(ev6);
			entityManagerDB.persist(ev7);
			entityManagerDB.persist(ev8);
			entityManagerDB.persist(ev9);
			entityManagerDB.persist(ev10);
			entityManagerDB.persist(ev11);
			entityManagerDB.persist(ev12);
			entityManagerDB.persist(ev13);
			entityManagerDB.persist(ev14);
			entityManagerDB.persist(ev15);
			entityManagerDB.persist(ev16);
			entityManagerDB.persist(ev17);
			entityManagerDB.persist(ev18);
			entityManagerDB.persist(ev19);
			entityManagerDB.persist(ev20);
			
			entityManagerDB.persist(pronos12);
			entityManagerDB.persist(pronos13);
			entityManagerDB.persist(pronos14);
			entityManagerDB.persist(pronos15);
			entityManagerDB.persist(pronos16);
			entityManagerDB.persist(pronos17);
	
			entityManagerDB.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
