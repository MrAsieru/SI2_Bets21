package gui;

import javax.swing.table.AbstractTableModel;

import domain.Bezeroa;

public class BezeroApostuaAdapter extends AbstractTableModel{
	private Bezeroa bezeroa;
	
	public BezeroApostuaAdapter(Bezeroa b) {
		this.bezeroa = b;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return this.bezeroa.getApustuak().size();
	}

	@Override
	public Object getValueAt(int x, int y) {
		Object object = new Object();
		//Apostuaren identifikazioa lortuko dugu
		if (y==0) {
			object = this.bezeroa.getApustuak().get(x).getIdentifikadorea(); 
		}
		//Apostuaren pronostiko kopurua lortuko dugu
		else if (y==1) {
			object = this.bezeroa.getApustuak().get(x).getPronostikoKop();
		}
		//Apostuaren asmatutako pronostiko kopurua lortuko dugu
		else if (y==2){
			object = this.bezeroa.getApustuak().get(x).getAsmatutakoKop();
		}
		//Apostuaren kuota totala kopurua lortuko dugu
		else if (y==3) {
			object = this.bezeroa.getApustuak().get(x).getKuotaTotala();
		}
		return object;
	}
	
	public String getColumnName(int index) {
		String izena = "Err";
		switch (index) {
		case 0:
			izena = "Identifikadorea";
			break;
		case 1:
			izena = "Pronostiko Kop";
			break;
		case 2:
			izena = "Pronostiko Asmatuak";
			break;
		case 3:
			izena = "Kuota";
			break;

		default:
			izena = "errorea";
			break;
		}
		return izena;
	}
}
