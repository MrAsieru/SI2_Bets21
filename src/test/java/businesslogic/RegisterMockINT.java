package businesslogic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import configuration.UtilDate;
import dataaccess.DataAccess;
import domain.Admin;
import domain.Bezeroa;
import domain.Langilea;
import domain.Pertsona;
import exceptions.UserAlreadyExist;
import test.businesslogic.TestFacadeImplementation;

public class RegisterMockINT {
	@Mock
	DataAccess da;
	
	@InjectMocks
	BLFacade sut = new BLFacadeImplementation(da);
	
	TestFacadeImplementation testFacade = new TestFacadeImplementation();
	
	@After
	public void garbituDatuBasea() {
		testFacade.removeUser("Proba");
	}
	
	@Test
	public void testOndoAdmin() {
		Pertsona adm = new Admin("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1));
		
		
		// Mockito konfiguratu
		
		//Konprobatu
		assertTrue(adm != null);
		assertTrue(adm instanceof Admin);
	}
	
	@Test
	public void testOndoLangilea() {
		Pertsona lan = null;

		
		//Konprobatu
		assertTrue(lan != null);
		assertTrue(lan instanceof Langilea);
	}
	
	@Test
	public void testOndoBezeroa() {
		Pertsona bez = null;

		
		//Konprobatu
		assertTrue(bez != null);
		assertTrue(bez instanceof Bezeroa);
	}
	
	@Test
	public void testErabiltzaileIzenaNull() {
		Pertsona per = null;

		
		//Konprobatu
		assertEquals(null, per);
	}
	
	@Test
	public void testErabiltzaileIzenaExistitu() {
		Pertsona per = null;
		
	}
	
	@Test
	public void testMotaDesberdina() {
		Pertsona per = null;

		
		//Konprobatu
		assertEquals(null, per);
	}

}
