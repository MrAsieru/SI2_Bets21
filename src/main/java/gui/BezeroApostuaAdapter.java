package gui;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import domain.Apustua;
import domain.Bezeroa;
import domain.Pronostikoa;

public class BezeroApostuaAdapter extends AbstractTableModel{
	private ArrayList<Pronostikoa> pronostikoak = new ArrayList<>();
	
	public BezeroApostuaAdapter(Bezeroa b) {
		for (Apustua apostuas : b.getApustuak()) {
			for (Pronostikoa pronostikoa : apostuas.getPronostikoak()) {
				this.pronostikoak.add(pronostikoa);
			}
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return this.pronostikoak.size();
	}

	@Override
	public Object getValueAt(int x, int y) {
		Object object = new Object();
		switch (y) {
		case 0:
			object = this.pronostikoak.get(x).getDeskripzioa();
			break;
		case 1:
			object = this.pronostikoak.get(x).getQuestion();
			break;
		case 2:
			object = this.pronostikoak.get(x).getQuestion().getEvent().getEventDate();
			break;
		case 3:
			object = this.pronostikoak.get(x).getKuota();
			break;

		default:
			break;
		}
		return object;
	}
	
	public String getColumnName(int index) {
		String izena = "Err";
		switch (index) {
		case 0:
			izena = "Deskripzioa";
			break;
		case 1:
			izena = "Galdera";
			break;
		case 2:
			izena = "Date";
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
