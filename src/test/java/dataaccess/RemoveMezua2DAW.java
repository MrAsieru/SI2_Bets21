package dataaccess;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.Collection;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import configuration.UtilDate;
import exceptions.UserAlreadyExist;
import test.dataaccess.TestDataAccess;
import domain.ArretaElkarrizketa;
import domain.ArretaMezua;
import domain.Bezeroa;
import domain.BezeroartekoMezua;
import domain.Langilea;
import domain.Mezua;
import domain.Pertsona;
import domain.RegisterParameter;

public class RemoveMezua2DAW {
	//sut:system under test
	static DataAccessRemoveMezua sut=new DataAccessRemoveMezua(false);

	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();

	// Bezeroarteko mezua da

	@Test
	public void testRemoveMezua1() {
		// Define parameters
		Bezeroa b1 = null;
		Bezeroa b2 = null;
		BezeroartekoMezua mezua = null;
		String mota = "bezeroa";
		String idBez1 = "ID_Proba";
		String idBez2 = "ID_Proba2";

		try {
			// configure the state of the system (create object in the dabatase)
			testDA.open();

			//Erabiltzaileak sortu
			b1 = (Bezeroa) testDA.register("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota);
			b2 = (Bezeroa) testDA.register("Jose", "Golo", "Pera", idBez2, "1234", "987654321", "jose@proba.proba", UtilDate.newDate(1970, 1, 2), mota);

			// Erabiltzaileak era egokian sortu direla frogatu
			assumeNotNull(b1);
			assumeNotNull(b2);
			mezua = testDA.bidaliMezua(b1, b2, "Mezua", "Gaia", "Mota", 1.0, 1.0, 1.0);
			assumeNotNull(testDA.mezuaJaso(mezua));

			// Mezua era egokian gehitu dela frogatu
			assumeTrue(b1.getBidalitakoBezeroMezuak().contains(mezua));
			assumeTrue(b2.getJasotakoMezuak().contains(mezua));
			testDA.close();

			//assumeTrue(sut.getBezeroa(idBez1).getBidalitakoBezeroMezuak().contains(mezua));
			//assumeTrue(sut.getBezeroa(idBez2).getJasotakoMezuak().contains(mezua));



			//Invoke sut:
			sut.open(false);
			sut.removeMezua(mezua);
			sut.close();

			//verify the results
			testDA.open();
			assertNull(testDA.mezuaJaso(mezua));
			mezua = (BezeroartekoMezua) testDA.mezuaJaso(mezua);
			testDA.close();

			sut.open(false);
			assertFalse(sut.getBezeroa(idBez1).getBidalitakoBezeroMezuak().contains(mezua));
			assertFalse(sut.getBezeroa(idBez2).getJasotakoMezuak().contains(mezua));
			sut.close();

			//Restore database state:

		} catch (UserAlreadyExist uae) {
			assumeTrue(false);
		} finally {
			testDA.open();
			testDA.removeUser(idBez1);
			testDA.removeUser(idBez2);
			testDA.close();
		}

	}


	// Bezeroarteko mezua da
	/*
	@Test
	public void testRemoveMezua3() {
		// Define parameters
		Bezeroa b1 = null;
		Langilea l1 = null;
		ArretaMezua mezua = null;
		ArretaElkarrizketa arretElk = null;
		String mota1 = "bezeroa";
		String mota2 = "langilea";
		String idBez1 = "ID_Proba";
		String idLang1 = "ID_Proba2";


		try {
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			//Erabiltzaileak sortu
			b1 = (Bezeroa) sut.register(new RegisterParameter("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota1));
			l1 = (Langilea) sut.register(new RegisterParameter("Pepe", "Goi", "Zuri", idLang1, "1234", "987654321", "pepe@proba.proba", UtilDate.newDate(1970, 1, 1), mota2));

			// Erabiltzaileak era egokian sortu direla frogatu
			assumeNotNull(b1);
			assumeNotNull(l1);


			// Mezua era egokian gehitu dela frogatu				

			arretElk = sut.arretaElkarrizketaSortu(b1, "GaiaArretElk", "Mezua");
			assumeTrue(b1.getArretaElkarrizketak().contains(arretElk));

			sut.bezeroaEsleitu(l1);
			sut.arretaMezuaBidali(arretElk, "GaiaArretMez", false);
			mezua = arretElk.getMezuak().stream()
					.filter(am -> "GaiaArretMez".equals(am.getMezua())).findFirst().orElse(null); // Mezua bilatu

			assumeNotNull(mezua); // Mezua era egokian gehitu da arreta elkarhizketara
			assumeTrue(!arretElk.isAmaituta()); // Elkarhizketa ez dago amaituta

			//assumeTrue(sut.getBezeroa(idBez1).getBidalitakoBezeroMezuak().contains(mezua));
			//assumeTrue(sut.getBezeroa(idBez2).getJasotakoMezuak().contains(mezua));
			sut.close();


			//Invoke sut:
			sut.open(false);
			sut.removeMezua(mezua);
			sut.close();

			//verify the results
			testDA.open();
			mezua = (ArretaMezua) testDA.mezuaJaso(mezua);
			assertNotNull(mezua);
			assertFalse(mezua.isIkusgaiBezeroarentzat());
			testDA.close();

			sut.open(false);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			assertTrue(sut.getBezeroa(idBez1).getArretaElkarrizketak().contains(arretElk));
			mezua = arretElk.getMezuak().stream()
					.filter(am -> "GaiaArretMez".equals(am.getMezua())).findFirst().orElse(null); // Mezua bilatu
			assertNotNull(mezua);
			sut.close();

			//Restore database state:

		} catch (UserAlreadyExist uae) {
			assumeTrue(false);
		} finally {
			testDA.open();
			testDA.removeArretaElkarhizketaGuztiz(arretElk);
			testDA.removeUser(idBez1);
			testDA.removeUser(idLang1);
			testDA.close();
		}

	}
	*/
	/*
	@Test
	public void testRemoveMezua4() {
		// Define parameters
		Bezeroa b1 = null;
		Langilea l1 = null;
		ArretaMezua mezua = null;
		ArretaMezua mezua2 = null;
		ArretaElkarrizketa arretElk = null;
		String mota1 = "bezeroa";
		String mota2 = "langilea";
		String idBez1 = "ID_Proba";
		String idLang1 = "ID_Proba2";


		try {
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			//Erabiltzaileak sortu
			b1 = (Bezeroa) sut.register(new RegisterParameter("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota1));
			l1 = (Langilea) sut.register(new RegisterParameter("Pepe", "Goi", "Zuri", idLang1, "1234", "987654321", "pepe@proba.proba", UtilDate.newDate(1970, 1, 1), mota2));

			// Erabiltzaileak era egokian sortu direla frogatu
			assumeNotNull(b1);
			assumeNotNull(l1);


			// Mezua era egokian gehitu dela frogatu				

			arretElk = sut.arretaElkarrizketaSortu(b1, "GaiaArretElk", "Mezua");

			sut.bezeroaEsleitu(l1);
			sut.arretaMezuaBidali(arretElk, "LangileArretaMezua", false);
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			mezua = arretElk.getMezuak().stream()
					.filter(am -> "LangileArretaMezua".equals(am.getMezua())).findFirst().orElse(null); // Mezua bilatu

			assumeNotNull(mezua); // Mezua era egokian gehitu da arreta elkarhizketara

			sut.amaituElkarrizketa(arretElk);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			assumeTrue(arretElk.isAmaituta()); // Elkarhizketa amaitu da
			sut.close();


			//Invoke sut:
			sut.open(false);
			sut.removeMezua(mezua);
			sut.close();

			//verify the results
			testDA.open();
			mezua = (ArretaMezua) testDA.mezuaJaso(mezua);

			assertNull(mezua);

			testDA.close();

			sut.open(false);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			assertNotNull(arretElk);
			sut.close();

			//Restore database state:

		} catch (UserAlreadyExist uae) {
			assumeTrue(false);
		} finally {
			testDA.open();
			testDA.removeArretaElkarhizketaGuztiz(arretElk);
			testDA.removeUser(idBez1);
			testDA.removeUser(idLang1);
			testDA.close();
		}

	}
	*/
	/*
	// Bezeroarteko mezua da
	@Test
	public void testRemoveMezua2_SahiestuNullPointerException() {
		// Define parameters
		Bezeroa b1 = null;
		Langilea l1 = null;
		ArretaMezua mezua = null;
		ArretaElkarrizketa arretElk = null;
		String mota1 = "bezeroa";
		String mota2 = "langilea";
		String idBez1 = "ID_Proba";
		String idLang1 = "ID_Proba2";


		try {
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			//Erabiltzaileak sortu
			b1 = (Bezeroa) sut.register(new RegisterParameter("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota1));
			l1 = (Langilea) sut.register(new RegisterParameter("Pepe", "Goi", "Zuri", idLang1, "1234", "987654321", "pepe@proba.proba", UtilDate.newDate(1970, 1, 1), mota2));

			// Erabiltzaileak era egokian sortu direla frogatu
			assumeNotNull(b1);
			assumeNotNull(l1);


			// Mezua era egokian gehitu dela frogatu				

			arretElk = sut.arretaElkarrizketaSortu(b1, "GaiaArretElk", "Mezua");
			assumeTrue(b1.getArretaElkarrizketak().contains(arretElk));

			sut.bezeroaEsleitu(l1);
			sut.arretaMezuaBidali(arretElk, "Aaaa", false);
			sut.arretaMezuaBidali(arretElk, "Aaaa", true);
			assumeTrue(!arretElk.isAmaituta()); // Elkarhizketa ez dago amaituta

			//assumeTrue(sut.getBezeroa(idBez1).getBidalitakoBezeroMezuak().contains(mezua));
			//assumeTrue(sut.getBezeroa(idBez2).getJasotakoMezuak().contains(mezua));
			sut.amaituElkarrizketa(arretElk);
			sut.close();

			testDA.open();
			Vector<ArretaMezua> arDatuBok = testDA.getArretaMezuakDatubaseanBadaude(arretElk);
			testDA.close();

			//Invoke sut:
			sut.open(false);
			arDatuBok.stream().forEach(am -> sut.removeMezua(am));
			sut.close();

			//verify the results

			sut.open(false);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			assertNull(arretElk);
			sut.close();

			//Restore database state:

		} catch (UserAlreadyExist uae) {
			assumeTrue(false);
		} finally {
			testDA.open();
			testDA.removeArretaElkarhizketaGuztiz(arretElk);
			testDA.removeUser(idBez1);
			testDA.removeUser(idLang1);
			testDA.close();
		}
	}
	*/
	/*
	// Bezeroarteko mezua da
	@Test
	public void testRemoveMezua2() {
		// Define parameters
		Bezeroa b1 = null;
		Langilea l1 = null;
		ArretaMezua mezua = null;
		ArretaElkarrizketa arretElk = null;
		String mota1 = "bezeroa";
		String mota2 = "langilea";
		String idBez1 = "ID_Proba";
		String idLang1 = "ID_Proba2";




		try {
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			//Erabiltzaileak sortu
			b1 = (Bezeroa) sut.register(new RegisterParameter("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota1));
			l1 = (Langilea) sut.register(new RegisterParameter("Pepe", "Goi", "Zuri", idLang1, "1234", "987654321", "pepe@proba.proba", UtilDate.newDate(1970, 1, 1), mota2));

			// Erabiltzaileak era egokian sortu direla frogatu
			assumeNotNull(b1);
			assumeNotNull(l1);


			// Mezua era egokian gehitu dela frogatu				

			arretElk = sut.arretaElkarrizketaSortu(b1, "GaiaArretElk", "Mezua");
			assumeTrue(b1.getArretaElkarrizketak().contains(arretElk));

			sut.bezeroaEsleitu(l1);

			assumeTrue(!arretElk.isAmaituta()); // Elkarhizketa ez dago amaituta
;
			sut.amaituElkarrizketa(arretElk);
			sut.close();



			//Invoke sut: //Borratu mezu guztiak
			sut.open(false);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			Vector<ArretaMezua> amv;
			amv = new Vector<ArretaMezua>(arretElk.getLangileakBidalitakoak());
			amv.stream().forEach(a -> sut.removeMezua(a));
			arretElk = sut.getArretaElkarrizketa(arretElk);
			amv = new Vector<ArretaMezua>(arretElk.getBezeroakBidalitakoak());
			amv.stream().forEach(a -> sut.removeMezua(a));
			sut.close();

			//verify the results

			sut.open(false);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			assertNull(arretElk);
			sut.close();

			//Restore database state:

		} catch (UserAlreadyExist uae) {
			assumeTrue(false);
		} finally {
			testDA.open();
			testDA.removeArretaElkarhizketaGuztiz(arretElk);
			testDA.removeUser(idBez1);
			testDA.removeUser(idLang1);
			testDA.close();
		}

	}
	*/
	/*
	@Test
	public void testRemoveMezua_ZentzurikGabekoErantzunak() {
		// Define parameters
		Bezeroa b1 = null;
		Langilea l1 = null;
		ArretaMezua mezua = null;
		ArretaMezua mezua2 = null;
		ArretaElkarrizketa arretElk = null;
		String mota1 = "bezeroa";
		String mota2 = "langilea";
		String idBez1 = "ID_Proba";
		String idLang1 = "ID_Proba2";


		try {
			// configure the state of the system (create object in the dabatase)
			sut.open(false);

			//Erabiltzaileak sortu
			b1 = (Bezeroa) sut.register(new RegisterParameter("Andrea", "Iturburu", "Gorri", idBez1, "1234", "123456789", "andrea@proba.proba", UtilDate.newDate(1970, 1, 1), mota1));
			l1 = (Langilea) sut.register(new RegisterParameter("Pepe", "Goi", "Zuri", idLang1, "1234", "987654321", "pepe@proba.proba", UtilDate.newDate(1970, 1, 1), mota2));

			// Erabiltzaileak era egokian sortu direla frogatu
			assumeNotNull(b1);
			assumeNotNull(l1);


			// Mezua era egokian gehitu dela frogatu				

			arretElk = sut.arretaElkarrizketaSortu(b1, "GaiaArretElk", "Mezua");

			sut.bezeroaEsleitu(l1);
			sut.arretaMezuaBidali(arretElk, "LangileArretaMezua", false);
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			mezua = arretElk.getMezuak().stream()
					.filter(am -> "LangileArretaMezua".equals(am.getMezua())).findFirst().orElse(null); // Mezua bilatu

			assumeNotNull(mezua); // Mezua era egokian gehitu da arreta elkarhizketara
			System.out.println("Elkarhizketa maitu aurretik");
			System.out.println(arretElk.getBezeroakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			sut.amaituElkarrizketa(arretElk);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			assumeTrue(arretElk.isAmaituta()); // Elkarhizketa amaitu da

			System.out.println("Amaitu egin da elkarhizketa");
			System.out.println(arretElk.getBezeroakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));

			sut.close();

			testDA.open();
			System.out.println(testDA.mezuaJaso(mezua));
			testDA.close();


			//Invoke sut:
			sut.open(false);
			System.out.println("Before test");
			System.out.println(arretElk.getBezeroakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			sut.removeMezua(mezua);
			System.out.println("after test");
			System.out.println(arretElk.getBezeroakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));

			arretElk=sut.getArretaElkarrizketa(arretElk);
			System.out.println("after test2");
			System.out.println(arretElk.getBezeroakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			sut.close();

			//verify the results
			testDA.open();
			mezua = (ArretaMezua) testDA.mezuaJaso(mezua);

			assertNull(mezua);

			testDA.close();

			sut.open(false);
			arretElk = sut.getArretaElkarrizketa(arretElk);
			System.out.println("Before test");
			System.out.println(arretElk.getBezeroakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));
			System.out.println(arretElk.getLangileakBidalitakoak().stream().map(ArretaMezua::getMezua).collect(Collectors.toList()));

			assertNotNull(arretElk);
			sut.close();

			//Restore database state:

		} catch (UserAlreadyExist uae) {
			assumeTrue(false);
		} finally {
			testDA.open();
			testDA.removeArretaElkarhizketaGuztiz(arretElk);
			testDA.removeUser(idBez1);
			testDA.removeUser(idLang1);
			testDA.close();
		}	
	}
	*/

}
