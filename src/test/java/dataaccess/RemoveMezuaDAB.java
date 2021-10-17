package dataaccess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import configuration.UtilDate;
import domain.Bezeroa;
import domain.BezeroartekoMezua;
import domain.Langilea;
import domain.Mezua;
import test.dataaccess.TestDataAccess;

public class RemoveMezuaDAB {
	//sut:system under test
	static DataAccess sut=new DataAccess(false);

	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	@Ignore
	@Test
	public void test4() {
		try {
			sut.open(false);
			sut.removeMezua(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}finally {
			sut.close();
		}
	}
	@Ignore
	@Test
	public void test5() {
		String idBez1 = "ID_Proba";
		String idBez2 = "ID_Proba2";
		String mota = "bezeroa";
		Bezeroa b1, b2;
		Mezua m = null;
		try {
			sut.open(false);
			
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			//Erabiltzaileak sortu
			b1 = (Bezeroa) sut.register(new RegisterParameter("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota));
			b2 = (Bezeroa) sut.register(new RegisterParameter("Pepe", "Goi", "Zuri", idBez2, "1234", "987654321", "pepe@proba.proba", UtilDate.newDate(1970, 1, 1), mota));
			m = new BezeroartekoMezua("Mezua", "Gaia", "Mota", 0, 0, 0, b1, b2);
			sut.removeMezua(m);
			assertTrue(true);
			assertFalse(b1.getBidalitakoBezeroMezuak().contains(m));
			assertFalse(b2.getJasotakoMezuak().contains(m));
		} catch (Exception e) {
			assertTrue(false);
		}finally {
			sut.close();
			testDA.open();
			testDA.removeUser(idBez1);
			testDA.removeUser(idBez2);
			testDA.close();
		}
	}
	
	@Test
	public void test6() {
		
		class MezuEzberdina extends Mezua{
			private static final long serialVersionUID = 1L;
			public MezuEzberdina() {
				super("MezuEzberdina");
			}
		}
		
		Mezua m = null;
		try {
			sut.open(false);
			
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			
			m = new MezuEzberdina();
			sut.removeMezua(m);
			fail();
			
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}finally {
			sut.close();
		}
		
	}
	

}
