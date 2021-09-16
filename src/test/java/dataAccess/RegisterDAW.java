package dataAccess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import configuration.UtilDate;
import domain.Bezeroa;
import domain.Pertsona;
import exceptions.UserAlreadyExist;
import test.dataAccess.TestDataAccess;

public class RegisterDAW {
	
	//sut:system under test
	static DataAccess sut=new DataAccess(true);
	
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	
	/**
	 * Erabiltzailea existitzen da
	 */
	@Test
	public void testRegister1() {
		//Sistema konfiguratu
		testDA.open();
		try {
			testDA.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (UserAlreadyExist uae) {
			fail();
		}
		testDA.close();
		
		//Sistema probatu
		try {
			sut.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
			fail();
		} catch (UserAlreadyExist uae) {
			assertTrue(true);
		} finally {			
			testDA.open();
			testDA.removeUser("Proba");
			testDA.close();
		}
	}
	
	/**
	 * Erabiltzailea ez dauka motarik
	 */
	@Test
	public void testRegister2() {
		//Sistema probatu
		testDA.open();
		try {
			testDA.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "ezer");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			testDA.close();
		}
		
		// Konprobatu DB-n ez dagoela
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		
		if (p == null) assertTrue(true);
		else fail();
		
		// Erabiltzailea ezabatu
		testDA.open();
		testDA.removeUser("Proba");
		testDA.close();
		
	}
	
	/**
	 * Erabiltzailea admin da
	 */
	@Test
	public void testRegister3() {
		//Sistema probatu
		testDA.open();
		try {
			testDA.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "admin");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			testDA.close();
		}
		
		// Konprobatu DB-n dagoela eta admin motatakoa dela
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		
		if (p == null) assertTrue(true);
		else fail();
		
		// Erabiltzailea ezabatu
		testDA.open();
		testDA.removeUser("Proba");
		testDA.close();
		
	}
	
	/**
	 * Erabiltzailea langilea da
	 */
	@Test
	public void testRegister4() {
		// Hasieraketa
		
	}
	
	/**
	 * Erabiltzailea bezeroa da
	 */
	@Test
	public void testRegister5() {
		// Hasieraketa
		
	}

}
