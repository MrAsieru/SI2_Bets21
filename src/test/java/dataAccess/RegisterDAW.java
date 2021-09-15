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
	
	@Before
	public void setUp() {
		
	}
	
	/**
	 * Erabiltzailea existitzen da
	 */
	@Test
	public void testRegister1() {
		// Hasieraketa
		Pertsona p = new Bezeroa("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1));
		
		//Sistema konfiguratu
		sut.open(false);
		try {
			sut.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (UserAlreadyExist uae) {
			fail();
		}
		sut.close();
		
		//Sistema probatu
		sut.open(false);
		try {
			sut.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "bezeroa");
			fail();
		} catch (UserAlreadyExist uae) {
			assertTrue(true);
		} finally {
			sut.removeUser("Proba");
			sut.close();
		}
	}
	
	/**
	 * Erabiltzailea ez dauka motarik
	 */
	@Test
	public void testRegister2() {
		//Sistema probatu
		sut.open(false);
		try {
			sut.register("Proba", "Proba", "Proba", "Proba", "Proba", "123456789", "proba@proba.proba", UtilDate.newDate(1970, 1, 1), "ezer");
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		// Konprobatu DB-n ez sdagoela
		sut.open(false);
		Pertsona p = sut.db.find(Pertsona.class, "Proba");
		sut.close();
		
		if (p == null) assertTrue(true);
		else fail();
		
		// Erabiltzailea ezabatu
		sut.open(false);
		sut.removeUser("Proba");
		sut.close();
		
	}
	
	/**
	 * Erabiltzailea admin da
	 */
	@Test
	public void testRegister3() {
		// Hasieraketa
		
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