package dataaccess;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.objectdb.o.CLN.q;

import businesslogic.BLFacade;
import configuration.UtilDate;
import domain.Apustua;
import domain.Bezeroa;
import domain.Errepikapena;
import domain.Event;
import domain.Mugimendua;
import domain.Pertsona;
import domain.Pronostikoa;
import domain.Question;
import domain.RegisterParameter;
import exceptions.PronosticAlreadyExist;
import test.dataaccess.TestDataAccess;

public class EmaitzaIpiniDAW { 
	//TODO kodea hobetu ulertu eta garbitu
	
	Event event;
	
	//sut:system under test
	static DataAccess sut=new DataAccess(false);
		
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	
	@Before
	public void ireki() {
		sut.open(false);
		testDA.open();
	}
		
	@After
	public void itxi() {
		sut.close();
		testDA.close();
	}
	
	@After
	public void garbituDatuBasea() {
		testDA.open();
		testDA.removeUser("Proba");
		testDA.removeUser("Proba2");
		testDA.removeEvent(event);
		testDA.close();
	}
	
	@Test
	public void test1() {
		Question q = null;
		Pronostikoa p = null;
		
		event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		try {
			q = event.getQuestions().firstElement();
			p = sut.createPronostic(q, "D", 0.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sut.emaitzaIpini(q, p);
	}
	
	@Test
	public void test2() {
		Question q = null;
		Pronostikoa p1 = null;
		Pronostikoa p2 = null;
		Bezeroa per1 = null;
		ArrayList<Pronostikoa> pronostikoak = new ArrayList<>();
		
		//Erabiltzaileak sortu
		try {
			per1 = (Bezeroa)sut.register(new RegisterParameter("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa"));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		//Galderak sortu
		event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		q = event.getQuestions().firstElement();
		
		//Pronostikoak sortu
		try {
			p1 = sut.createPronostic(q, "A", 0.0);
			p2 = sut.createPronostic(q, "B", 0.0);
			pronostikoak.add(p1);	
			pronostikoak.add(p2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Apustuak sortu
		sut.apustuaEgin(pronostikoak, 0.0, per1);
		
		int asmatukop = p1.getApustuak().get(0).getAsmatutakoKop();
		sut.emaitzaIpini(q, p1);
		assertTrue(asmatukop<p1.getApustuak().get(0).getAsmatutakoKop());
		assertFalse(per1.getDirua()>0.0);
	}
	
	@Test
	public void test3() {
		Question q = null;
		Pronostikoa p1 = null;
		Bezeroa per1 = null;
		ArrayList<Pronostikoa> pronostikoak = new ArrayList<>();
		
		//Erabiltzaileak sortu
		try {
			per1 = (Bezeroa)sut.register(new RegisterParameter("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa"));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		//Galderak sortu
		event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		q = event.getQuestions().firstElement();
		
		//Pronostikoak sortu
		try {
			p1 = sut.createPronostic(q, "A", 0.0);
			pronostikoak.add(p1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Apustuak sortu (mugimendu bat sortzen da)
		sut.apustuaEgin(pronostikoak, 0.0, (Bezeroa)per1);
		
		int asmatukop = p1.getApustuak().get(0).getAsmatutakoKop();
		
		sut.emaitzaIpini(q, p1);
		assertTrue(asmatukop<p1.getApustuak().get(0).getAsmatutakoKop());
		assertTrue(per1.getMugimenduak().size()==2);
	}
	
	@Test
	public void test4() {
		Question q = null;
		Pronostikoa p1 = null;
		Bezeroa per1 = null;
		ArrayList<Pronostikoa> pronostikoak1 = new ArrayList<>();
		ArrayList<Pronostikoa> pronostikoak2 = new ArrayList<>();
		
		//Erabiltzaileak sortu
		try {
		per1 = (Bezeroa)sut.register(new RegisterParameter("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa"));
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		//Galderak sortu
		event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		q = event.getQuestions().firstElement();
		
		//Pronostikoak sortu
		try {
			p1 = sut.createPronostic(q, "A", 0.0);
			pronostikoak1.add(p1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Apustuak sortu
		sut.apustuaEgin(pronostikoak1, 0.0, (Bezeroa)per1);
		Errepikapena errepikapena =  new Errepikapena(per1, per1, 0, 0, 0);
		per1.addErrepikatua(errepikapena);
		List<Apustua> apustuak = per1.getApustuak();
		Apustua apustua = apustuak.get(0);
		apustua.setErrepikatua(per1);
		
		
		int asmatukop = p1.getApustuak().get(0).getAsmatutakoKop();
		sut.emaitzaIpini(q, p1);
		assertTrue(asmatukop<p1.getApustuak().get(0).getAsmatutakoKop());
		assertTrue(per1.getMugimenduak().size()==3); //TODO assert-ak hobetu mugimenduaren hasierako string-a konparatzeko eta ez mugimendu kantitatea
	}
}
