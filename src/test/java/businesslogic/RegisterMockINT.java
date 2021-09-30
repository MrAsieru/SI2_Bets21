package businesslogic;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import configuration.UtilDate;
import dataaccess.DataAccess;
import domain.Admin;
import domain.Bezeroa;
import domain.Langilea;
import domain.Pertsona;
import exceptions.UserAlreadyExist;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMockINT {
	DataAccess da = Mockito.mock(DataAccess.class);
	
	BLFacade sut = new BLFacadeImplementation(da);
	
	
	@Test
	public void testOndoAdmin() {
		Pertsona adm = new Admin("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1));
		
		try {
			// Mockito konfiguratu
			Mockito.doReturn(adm).when(da).register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "admin");
			
			// Sistema probatu
			Pertsona jaso = sut.register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "admin");
			
			// Konprobatu
			assertEquals(adm, jaso);
		} catch (Exception e) {
			fail();
		}		
	}
	
	@Test
	public void testOndoLangilea() {
		Pertsona lan = new Langilea("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1));

		try {
			// Mockito konfiguratu
			Mockito.doReturn(lan).when(da).register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "langilea");
			
			// Sistema probatu
			Pertsona jaso = sut.register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "langilea");
			
			// Konprobatu
			assertEquals(lan, jaso);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testOndoBezeroa() {
		Pertsona bez = new Bezeroa("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1));
		
		try {
			// Mockito konfiguratu
			Mockito.doReturn(bez).when(da).register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
			
			// Sistema probatu
			Pertsona jaso = sut.register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
			
			// Konprobatu
			assertEquals(bez, jaso);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testErabiltzaileIzenaNull() {
		Pertsona per = null;

		try {
			// Mockito konfiguratu
			Mockito.doReturn(null).when(da).register("Mockito", "Probak", "Probak", null, "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
			
			// Sistema probatu
			Pertsona jaso = sut.register("Mockito", "Probak", "Probak", null, "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
			
			// Konprobatu
			assertEquals(null, jaso);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testErabiltzaileIzenaExistitu() {		
		try {
			// Mockito konfiguratu
			Mockito.doThrow(UserAlreadyExist.class).when(da).register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
			
			// Sistema probatu
			sut.register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "bezeroa");
			
			// Konprobatu
			fail();
		} catch (Exception e) {
			if (e instanceof UserAlreadyExist) assertTrue(true);
			else fail();
		}
	}
	
	@Test
	public void testMotaDesberdina() {
		Pertsona per = null;

		try {
			// Mockito konfiguratu
			Mockito.doReturn(null).when(da).register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "beste");
			
			// Sistema probatu
			Pertsona jaso = sut.register("Mockito", "Probak", "Probak", "Proba", "1234", "123456789", "Mock@junit.org", UtilDate.newDate(1970, 1, 1), "beste");
			
			// Konprobatu
			assertEquals(null, jaso);
		} catch (Exception e) {
			fail();
		}
	}
}
