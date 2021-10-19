package dataaccess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import configuration.UtilDate;
import domain.Admin;
import domain.Bezeroa;
import domain.Langilea;
import domain.Pertsona;
import domain.RegisterParameter;
import exceptions.UserAlreadyExist;
import test.dataaccess.TestDataAccess;

public class Register2DAB {

	//sut:system under test
	static DataAccessRegister sut=new DataAccessRegister(false);
	
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	
	@After
	public void garbituDatuBasea() {
		testDA.open();
		testDA.removeUser("Proba");
		testDA.close();
	}
	
	/*@Test
	public void testOndoAdmin() {
		Pertsona adm = null;
		//Sistema probatu
		try {
			sut.open(false);
			adm = sut.register(new RegisterParameter("Kutxa", "Beltzeko", "Probak", "Proba", "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "admin"));
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		//Konprobatu
		assertTrue(adm != null);
		assertTrue(adm instanceof Admin);
	}*/
	
	/*@Test
	public void testOndoLangilea() {
		Pertsona lan = null;
		//Sistema probatu
		try {
			sut.open(false);
			lan = sut.register(new RegisterParameter("Kutxa", "Beltzeko", "Probak", "Proba", "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "langilea"));
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		//Konprobatu
		assertTrue(lan != null);
		assertTrue(lan instanceof Langilea);
	}*/
	
	@Test
	public void testOndoBezeroa() {
		Pertsona bez = null;
		//Sistema probatu
		try {
			sut.open(false);
			bez = sut.register(new RegisterParameter("Kutxa", "Beltzeko", "Probak", "Proba", "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa"));
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		//Konprobatu
		assertTrue(bez != null);
		assertTrue(bez instanceof Bezeroa);
	}
	
	@Test
	public void testErabiltzaileIzenaNull() {
		Pertsona per = null;
		//Sistema probatu
		try {
			sut.open(false);
			per = sut.register(new RegisterParameter("Kutxa", "Beltzeko", "Probak", null, "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa"));
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		//Konprobatu
		assertEquals(null, per);
	}
	
	@Test
	public void testErabiltzaileIzenaExistitu() {
		Pertsona per = null;
		
		//Sistema konfiguratu
		testDA.open();
		try {
			testDA.register("Kutxa", "Beltzeko", "Probak", "Proba", "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
		} catch (UserAlreadyExist uae) {
			fail();
		}
		testDA.close();
		
		//Sistema probatu
		try {
			sut.open(false);
			per = sut.register(new RegisterParameter("Kutxa", "Beltzeko", "Probak", "Proba", "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa"));
			fail();
		} catch (UserAlreadyExist uae) {
			assertTrue(true);
		} finally {
			sut.close();
		}
	}
	
	@Test
	public void testMotaDesberdina() {
		Pertsona per = null;
		//Sistema probatu
		try {
			sut.open(false);
			per = sut.register(new RegisterParameter("Kutxa", "Beltzeko", "Probak", "Proba", "1234", "123456789", "DAB@junit.org", UtilDate.newDate(1970, 1, 1), "beste"));
		} catch (UserAlreadyExist uae) {
			fail();
		} finally {
			sut.close();
		}
		
		//Konprobatu
		assertEquals(null, per);
	}

}
