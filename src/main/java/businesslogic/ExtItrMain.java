package businesslogic;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Locale;

import javax.naming.ConfigurationException;
import javax.swing.UIManager;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Event;
import gui.MainGUI;

public class ExtItrMain {

	public static void main(String[] args) throws Exception {
		boolean isLocal = true;

		ConfigXML c = ConfigXML.getInstance();

		if (isLocal != c.isBusinessLogicLocal()) {
			throw new Exception("Ez dago ondo konfiguratua main hau exekutatzeko");
		}

		System.out.println(c.getLocale());

		Locale.setDefault(new Locale(c.getLocale()));

		System.out.println("Locale: " + Locale.getDefault());

		// Facade objektua lortu lehendabiziko ariketa erabiliz
		BusinessLogicFactory blf = new BusinessLogicFactory();

		try {
			BLFacade appFacadeInterface = blf.getBL(c);
			ExtendedIterator<Event> i = appFacadeInterface.getEvents(UtilDate.newDate(2021, 2, 17));
			Event ev;
			i.goLast();
			while (i.hasPrevious()) {
				ev = i.previous();
				System.out.println(ev);
			}
			// Nahiz eta suposatu hasierara ailegatu garela, eragiketa egiten dugu.
			i.goFirst();
			while (i.hasNext()) {
				ev = i.next();
				System.out.println(ev);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
