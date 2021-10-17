package businesslogic;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Date;






import domain.Question;
import domain.RegisterParameter;
import domain.Apustua;
import domain.ArretaElkarrizketa;
import domain.Bezeroa;
import domain.BezeroaContainer;
import domain.Errepikapena;
import domain.ErrepikatuakContainer;
import domain.Event;
import domain.Langilea;
import domain.Mezua;
import domain.Pertsona;
import domain.Pronostikoa;
import domain.PronostikoaContainer;
import exceptions.EventAlreadyExist;
import exceptions.EventFinished;
import exceptions.PronosticAlreadyExist;
import exceptions.QuestionAlreadyExist;
import exceptions.UserAlreadyExist;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  

	/**
	 * This method creates a question for an event, with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws EventFinished if current data is after data of the event
 	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	@WebMethod Question createQuestion(Event event, String question, double betMinimum) throws EventFinished, QuestionAlreadyExist;
	
	
	/**
	 * This method retrieves the events of a given date 
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
	@WebMethod public Vector<Event> getEvents(Date date);
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	@WebMethod public Vector<Date> getEventsMonth(Date date);
	
	/**
	 * This method calls the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
	/**
	 * Metodo honek erabiltzaile izen eta pasahitz bat jasota, bi hauek dituen pertsona bat bilatzen du datu basean.
	 * Aurkitzen badu itzuli egiten du bestela null balioa.
	 * @param erabiltzaileIzena 
	 * @param pasahitza
	 * @return 
	 */
	@WebMethod public Pertsona isLogin(String erabiltzaileIzena, String pasahitza);
	
	/**
	 * Metodo honek, erabiltzaile izen gisa erabiltzaileIzena duen Pertsonarik ez badago datu basean, sarrerako datuekin
	 * bat sortu eta datu-basean gehitzen du
	 * @param parameterObject TODO
	 * @return
	 * @throws UserAlreadyExist
	 */
	@WebMethod public Pertsona register(RegisterParameter parameterObject) throws UserAlreadyExist;
	
	/**
	 * Metodo honek description eta eventDate dituen gertaerarik ez badago datu basean, sortu eta gehitu egiten du
	 * @param description
	 * @param eventDate
	 * @throws EventAlreadyExist
	 */
	@WebMethod public void createEvent(String description, Date eventDate) throws EventAlreadyExist;
	
	@WebMethod public Vector<Question> getQuestions(Event event);
	
	@WebMethod Pronostikoa createPronostic(Question question, String description, double kuota) throws PronosticAlreadyExist;
	
	@WebMethod public void emaitzaIpini(Question question, Pronostikoa pronostikoa);
	
	@WebMethod public Bezeroa apustuaEgin(ArrayList<Pronostikoa> pronostikoak, double a, Bezeroa bezero);
	
	@WebMethod public Bezeroa deleteApustua(Apustua a) throws EventFinished;
	
	@WebMethod public Bezeroa diruaSartu(double u, Bezeroa bezero);
	
	@WebMethod public void ezabatuGertaera(Event event);
	
	@WebMethod public Bezeroa getBezeroa(String ErabiltzaileIzena);
	
	@WebMethod public Langilea getLangilea(String ErabiltzaileIzena);
	
	@WebMethod public Vector<Bezeroa> getBezeroak(String username, Bezeroa bezeroa);
	
	@WebMethod public Bezeroa bidaliMezua(Bezeroa nork, Bezeroa nori, String mezua, String gaia, String mota, double zenbatApostatu, double hilabeteanZenbat, double zenbatErrepikatuarentzat);

	@WebMethod public Vector<Mezua> getMezuak(Bezeroa bezeroa);
	
	@WebMethod public void mezuaIrakurri(Mezua mezua);
	
	@WebMethod public void removeMezua(Mezua mezua);
	
	@WebMethod public Bezeroa eguneratuEzarpenak(Bezeroa b, double komisioa, boolean publikoa);
	
	@WebMethod public void errepikatu(Bezeroa nork, Bezeroa nori, double apustatukoDena, double hilabetekoMax, double komisioa);
	
	@WebMethod public Vector<PronostikoaContainer> getPronostikoak(Apustua a);
	
	@WebMethod public ArretaElkarrizketa arretaMezuaBidali(ArretaElkarrizketa elkarrizketa, String mezua, boolean langileari);
	
	@WebMethod public ArretaElkarrizketa bezeroaEsleitu(Langilea langilea);
	
	@WebMethod public ArretaElkarrizketa arretaElkarrizketaSortu(Bezeroa bezeroa, String gaia, String mezua);
	
	@WebMethod public BezeroaContainer getBezeroaContainer(Bezeroa b);
	
	@WebMethod public void geldituElkarrizketa(ArretaElkarrizketa ae);
	
	@WebMethod public void amaituElkarrizketa(ArretaElkarrizketa ae);
	
	@WebMethod public void gehituPuntuazioa(ArretaElkarrizketa l, Integer x);
	
	@WebMethod public void eguneratuErrepikapenak();

	@WebMethod public Vector<Langilea> getLangileak();
	
	@WebMethod public ArrayList<ErrepikatuakContainer> getErrepikatzaileak(Bezeroa bezeroa);
	 
	@WebMethod public void jarraitzeariUtzi(Errepikapena errepikapena);
		 
	@WebMethod public ArrayList<ErrepikatuakContainer> getErrepikapenak(Bezeroa bezeroa);
	
	@WebMethod public ArretaElkarrizketa getArretaElkarrizketa(ArretaElkarrizketa ae);
}
