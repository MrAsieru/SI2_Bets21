package test.businesslogic;


import java.util.Date;

import configuration.ConfigXML;
import domain.Event;
import domain.Pertsona;
import test.dataaccess.TestDataAccess;

public class TestFacadeImplementation {
	TestDataAccess dbManagerTest;
 	
    
	   public TestFacadeImplementation()  {
			
			System.out.println("Creating TestFacadeImplementation instance");
			ConfigXML c=ConfigXML.getInstance();
			dbManagerTest=new TestDataAccess(); 
			dbManagerTest.close();
		}
		
		 
		public boolean removeEvent(Event ev) {
			dbManagerTest.open();
			boolean b=dbManagerTest.removeEvent(ev);
			dbManagerTest.close();
			return b;

		}
		
		public Event addEventWithQuestion(String desc, Date d, String q, float qty) {
			dbManagerTest.open();
			Event o=dbManagerTest.addEventWithQuestion(desc,d,q, qty);
			dbManagerTest.close();
			return o;

		}

		public Pertsona removeUser(String erabiltzaileIzena) {
			dbManagerTest.open();
			Pertsona p = dbManagerTest.removeUser(erabiltzaileIzena);
			dbManagerTest.close();
			return p;
		}

}
