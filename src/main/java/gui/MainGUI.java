package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.junit.experimental.results.PrintableResult;

import businesslogic.BLFacade;
import businesslogic.BLFacadeImplementation;
import dataaccess.DataAccess;
import domain.Apustua;
import domain.Bezeroa;
import domain.Event;
import domain.Pronostikoa;
import domain.Question;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;
	private JButton jButtonMaite = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	protected JLabel jLabelSelectOption;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	/**
	 * This is the default constructor
	 */
	public MainGUI() {
		super();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					//if (ConfigXML.getInstance().isBusinessLogicLocal()) facade.close();
				} catch (Exception e1) {
					System.out.println("Error: "+e1.toString()+" , probably problems with Business Logic or Database");
				}
				System.exit(1);
			}
		});

		initialize();
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		// this.setSize(271, 295);
		this.setSize(495, 290);
		this.setContentPane(getJContentPane());
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainTitle"));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridLayout(4, 1, 0, 0));
			jContentPane.add(getLblNewLabel());
			jContentPane.add(getMaiteBoton());
			jContentPane.add(getBoton3());
			jContentPane.add(getBoton2());
			jContentPane.add(getPanel());
		}
		return jContentPane;
	}


	/**
	 * This method initializes boton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBoton2(){
		if (jButtonCreateQuery == null) {
			jButtonCreateQuery = new JButton();
			jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("logIn")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
			jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					irekiLogin();
				}
			});
		}
		return jButtonCreateQuery;
	}
	
	private JButton getMaiteBoton(){
		if (jButtonMaite == null) {
			jButtonMaite = new JButton();
			jButtonMaite.setText(ResourceBundle.getBundle("Etiquetas").getString("maite")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-2$
			jButtonMaite.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					irekiMaiteApustuakn();
				}
			});
		}
		return jButtonMaite;
	}
	
	/**
	 * This method initializes boton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBoton3() {
		if (jButtonQueryQueries == null) {
			jButtonQueryQueries = new JButton();
			jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("QueryQueries"));
			jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFrame a = new FindQuestionsGUI();

					a.setVisible(true);
				}
			});
		}
		return jButtonQueryQueries;
	}
	

	private JLabel getLblNewLabel() {
		if (jLabelSelectOption == null) {
			jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("SelectOption"));
			jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
			jLabelSelectOption.setForeground(Color.BLACK);
			jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return jLabelSelectOption;
	}
	private JRadioButton getRdbtnNewRadioButton() {
		if (rdbtnNewRadioButton == null) {
			rdbtnNewRadioButton = new JRadioButton("English");
			rdbtnNewRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Locale.setDefault(new Locale("en"));
					System.out.println("Locale: "+Locale.getDefault());
					redibujar();				}
			});
			buttonGroup.add(rdbtnNewRadioButton);
		}
		return rdbtnNewRadioButton;
	}
	private JRadioButton getRdbtnNewRadioButton_1() {
		if (rdbtnNewRadioButton_1 == null) {
			rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
			rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Locale.setDefault(new Locale("eus"));
					System.out.println("Locale: "+Locale.getDefault());
					redibujar();				}
			});
			buttonGroup.add(rdbtnNewRadioButton_1);
		}
		return rdbtnNewRadioButton_1;
	}
	private JRadioButton getRdbtnNewRadioButton_2() {
		if (rdbtnNewRadioButton_2 == null) {
			rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
			rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Locale.setDefault(new Locale("es"));
					System.out.println("Locale: "+Locale.getDefault());
					redibujar();
				}
			});
			buttonGroup.add(rdbtnNewRadioButton_2);
		}
		return rdbtnNewRadioButton_2;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getRdbtnNewRadioButton_1());
			panel.add(getRdbtnNewRadioButton_2());
			panel.add(getRdbtnNewRadioButton());
		}
		return panel;
	}
	
	private void redibujar() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("QueryQueries"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("logIn"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainTitle"));
	}
	
	public void irekiLogin() {
		this.setVisible(false);
		JFrame a = new LoginGUI(this);
		a.setVisible(true);
	}
	
	public void irekiMaiteApustuakn() {
		this.setVisible(false);
		
		AbstractTableModel a = new BezeroApostuaAdapter(maiteSortu());
		
		JTable table = new JTable(a);
		JScrollPane tableSP = new JScrollPane(table);
        
        JFrame apostuaFrame = new JFrame();
        apostuaFrame.add(tableSP);
        apostuaFrame.setVisible(true);
        apostuaFrame.setLocationRelativeTo(null);
        apostuaFrame.setBounds(100,100,600,400);
	}
	
	public Bezeroa maiteSortu() {
		Bezeroa maite = new Bezeroa("MaiteUrreta", "", "", "MaiteUrreta", "MaiteUrreta", "", "", null);
	
		Pronostikoa pronostikoa1 = new Pronostikoa();
		pronostikoa1.setDeskripzioa("Desk1");
		pronostikoa1.setQuestion(new Question("Galdera1", 0.0, new Event(1, "Desk1", new Date())));
		pronostikoa1.setKuota(1.1);
		Pronostikoa pronostikoa2 = new Pronostikoa();
		pronostikoa2.setDeskripzioa("Desk2");
		pronostikoa2.setQuestion(new Question("Galdera2", 0.0, new Event(2, "Desk1", new Date())));
		pronostikoa2.setKuota(22.2);
		Pronostikoa pronostikoa3 = new Pronostikoa();
		pronostikoa3.setDeskripzioa("Desk3");
		pronostikoa3.setQuestion(new Question("Galdera3", 0.0, new Event(3, "Desk1", new Date())));
		pronostikoa3.setKuota(333.3);
		Pronostikoa pronostikoa4 = new Pronostikoa();
		pronostikoa4.setDeskripzioa("Desk4");
		pronostikoa4.setQuestion(new Question("Galdera4", 0.0, new Event(4, "Desk1", new Date())));
		pronostikoa4.setKuota(0.1);

		ArrayList<Pronostikoa> pronostikoak = new ArrayList<>();
		
		pronostikoak.add(pronostikoa1);
		pronostikoak.add(pronostikoa2);
		
		maite.addApustua(pronostikoak, 100, maite);
		Vector<Apustua> apustuak = maite.getApustuak();
		Apustua apustua = apustuak.get(0);
		apustua.setIdentifikadorea(88);
		apustua.setAsmatutakoKop(2);
		apustua.setKuotaTotala(4);
		
		ArrayList<Pronostikoa> pronostikoak2 = new ArrayList<>();
		pronostikoak2.add(pronostikoa3);
		pronostikoak2.add(pronostikoa4);
		
		maite.addApustua(pronostikoak2, 10, maite);
		Vector<Apustua> apustuak2 = maite.getApustuak();
		Apustua apustua2 = apustuak2.get(1);
		apustua2.setIdentifikadorea(66);
		apustua2.setAsmatutakoKop(11);
		apustua2.setKuotaTotala(3);
		
		return maite;
	}
} // @jve:decl-index=0:visual-constraint="0,0"

