package dataaccess;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.objectdb.o.CLN.q;

import businesslogic.BLFacade;
import configuration.UtilDate;
import domain.Bezeroa;
import domain.Event;
import domain.Pertsona;
import domain.Pronostikoa;
import domain.Question;
import exceptions.PronosticAlreadyExist;
import test.dataaccess.TestDataAccess;

public class EmaitzaIpiniDAW { 
	
	//sut:system under test
	static DataAccess sut=new DataAccess(false);
		
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	
	@Before
	public void ireki() {
		sut.open(false);
	}
		
	@After
	public void itxi() {
		sut.close();
	}
	
	@Test
	public void test1() {
		Question q = null;
		Pronostikoa p = null;
		
		Event event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		try {
			q = sut.getQuestions(event).get(0);
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
		per1 = (Bezeroa)sut.register("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		//Galderak sortu
		Event event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		q = sut.getQuestions(event).get(0);
		
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
		sut.apustuaEgin(pronostikoak, 0.0, (Bezeroa)per1);
		
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
		per1 = (Bezeroa)sut.register("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		//Galderak sortu
		Event event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		q = sut.getQuestions(event).get(0);
		
		//Pronostikoak sortu
		try {
			p1 = sut.createPronostic(q, "A", 0.0);
			pronostikoak.add(p1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Apustuak sortu
		sut.apustuaEgin(pronostikoak, 0.0, (Bezeroa)per1);
		
		int asmatukop = p1.getApustuak().get(0).getAsmatutakoKop();
		sut.emaitzaIpini(q, p1);
		assertTrue(asmatukop<p1.getApustuak().get(0).getAsmatutakoKop());
		assertTrue(per1.getDirua()>0.0);
	}
	
	@Test
	public void test4() {
		Question q = null;
		Pronostikoa p1 = null;
		Bezeroa per1 = null;
		ArrayList<Pronostikoa> pronostikoak = new ArrayList<>();
		
		//Erabiltzaileak sortu
		try {
		per1 = (Bezeroa)sut.register("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		//Galderak sortu
		Event event = testDA.addEventWithQuestion("E", new Date(), "Q", 0);
		q = sut.getQuestions(event).get(0);
		
		//Pronostikoak sortu
		try {
			p1 = sut.createPronostic(q, "A", 0.0);
			pronostikoak.add(p1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Apustuak sortu
		sut.apustuaEgin(pronostikoak, 0.0, (Bezeroa)per1);
		sut.apustuaEgin(pronostikoak, 0.0, (Bezeroa)per1);
		
		int asmatukop = p1.getApustuak().get(0).getAsmatutakoKop();
		sut.emaitzaIpini(q, p1);
		assertTrue(asmatukop<p1.getApustuak().get(0).getAsmatutakoKop());
		assertTrue(per1.getDirua()>0.0);
	}
}
