package businesslogic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import dataaccess.DataAccess;
import domain.ArretaMezua;
import domain.Bezeroa;
import domain.BezeroartekoMezua;
import domain.Mezua;
import domain.Pronostikoa;
import domain.Question;

public class RemoveMezuaMockINT {
	DataAccess da = Mockito.mock(DataAccess.class);
	
	BLFacade sut = new BLFacadeImplementation(da);
	@Test
	public void test1() {
		BezeroartekoMezua mezua = new BezeroartekoMezua();
		sut.removeMezua(mezua);
		Mockito.verify(da).removeMezua(mezua);
		
	}
	@Test
	public void test2() {
		ArretaMezua mezua = new ArretaMezua();
		sut.removeMezua(mezua);
		Mockito.verify(da).removeMezua(mezua);
	}
	@Test
	public void test3() {
		Mockito.doThrow(new IllegalArgumentException()).when(da).removeMezua(null);
		try {
			sut.removeMezua(null);
			fail();
		}catch(IllegalArgumentException e) {
			assertTrue(true);
		}
	
		Mockito.verify(da).removeMezua(null);
	}
	@Test
	public void test4() {
		class ClassMatcher extends ArgumentMatcher<Mezua>{

			@Override
			public boolean matches(Object argument) {
				if(argument instanceof BezeroartekoMezua || argument instanceof ArretaMezua ) {
					return false;
				}else {
					return true;
				}
			}
			
		}
		class MezuEzberdina extends Mezua{
			private static final long serialVersionUID = 1L;
			public MezuEzberdina() {
				super("MezuEzberdina");
			}
		}
		Mockito.doThrow(IllegalArgumentException.class).when(da).removeMezua(Mockito.argThat(new ClassMatcher()));
		try {
			sut.removeMezua(null);
			fail();
		}catch(IllegalArgumentException e) {
			assertTrue(true);
		}
	
		Mockito.verify(da).removeMezua(null);
	}

}
