package dataaccess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import configuration.UtilDate;
import dataaccess.DataAccess;
import domain.Bezeroa;
import domain.Pertsona;
import exceptions.UserAlreadyExist;
import test.dataaccess.TestDataAccess;

public class RegisterDAW {
	
	//sut:system under test
	static DataAccess sut=new DataAccess(false);
	
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	
	@After
	public void garbituDatuBasea() {
		testDA.open();
		testDA.removeUser("Proba");
		testDA.close();
	}
	
	/**
	 * Erabiltzailea existitzen da
	 */
	@Test
	public void testRegister1() {
		//Sistema konfiguratu
		testDA.open();
		try {
			testDA.register("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (UserAlreadyExist uae) {
			fail();
		}
		testDA.close();
		
		//Sistema probatu
		try {
			sut.open(false);
			sut.register("Izena1", "Abizena1", "Abizena1", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
			fail();
		} catch (UserAlreadyExist uae) {
			assertTrue(true);
		} finally {	
			sut.close();
		}
	}
	
	/**
	 * Erabiltzailea ez dauka motarik
	 */
	@Test
	public void testRegister2() {		
		//Sistema probatu
		try {
			sut.open(false);
			sut.register("Izena2", "Abizena2", "Abizena2", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "ezer");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		// Konprobatu DB-n ez dagoela
		sut.open(false);
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		sut.close();
		
		if (p == null) assertTrue(true);
		else fail();	
	}
	
	/**
	 * Erabiltzailea admin da
	 */
	@Test
	public void testRegister3() {
		//Sistema probatu
		try {
			sut.open(false);
			sut.register("Izena3", "Abizena3", "Abizena3", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "admin");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		// Konprobatu DB-n dagoela eta admin motatakoa dela
		sut.open(false);
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		sut.close();
		
		if (p != null && p.izena.equals("Izena3")) assertTrue(true);
		else fail();
	}
	
	/**
	 * Erabiltzailea langilea da
	 */
	@Test
	public void testRegister4() {
		//Sistema probatu
		try {
			sut.open(false);
			sut.register("Izena4", "Abizena4", "Abizena4", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "langilea");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		// Konprobatu DB-n dagoela eta admin motatakoa dela
		sut.open(false);
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		sut.close();
		
		if (p != null && p.izena.equals("Izena4")) assertTrue(true);
		else fail();
	}
	
	/**
	 * Erabiltzailea bezeroa da
	 */
	@Test
	public void testRegister5() {
		//Sistema probatu
		try {
			sut.open(false);
			sut.register("Izena5", "Abizena5", "Abizena5", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		// Konprobatu DB-n dagoela eta admin motatakoa dela
		sut.open(false);
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		sut.close();
		
		if (p != null && p.izena.equals("Izena5")) assertTrue(true);
		else fail();
	}

}
