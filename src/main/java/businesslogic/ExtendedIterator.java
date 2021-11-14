package businesslogic;

import java.util.Iterator;

public interface ExtendedIterator<E> extends Iterator<E> {
	//uneko elementua itzultzen du eta aurrekora pasatzen da
	public E previous();
	//true aurreko elementua existitzen bada.
	public boolean hasPrevious();
	//Lehendabiziko elementuan kokatzen da.
	public void goFirst();
	//Azkeneko elementuan kokatzen da.
	public void goLast();
}
