package businesslogic;

import java.util.List;
import java.util.NoSuchElementException;

public class ExtendedIteratorImpl<E> implements ExtendedIterator<E> {
	
	private List<E> lst;
	private int index;
	
	public ExtendedIteratorImpl(List<E> lst) {
		super();
		this.lst = lst;
		this.index = 0;
	}

	@Override
	public boolean hasNext() {
		return this.lst.size() > this.index;
	}

	@Override
	public E next() {
		if(!this.hasNext()) {
			throw new NoSuchElementException();
		}
		return this.lst.get(index++);
	}

	@Override
	public E previous() {
		if(!this.hasPrevious()) {
			throw new NoSuchElementException();
		}
		return this.lst.get(--index);
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return this.index > 0;
	}

	@Override
	public void goFirst() {
		this.index = 0;

	}

	@Override
	public void goLast() {
		this.index = this.lst.size();

	}

}
