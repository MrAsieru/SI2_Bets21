package businesslogic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.glassfish.pfl.dynamic.copyobject.impl.FastCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import configuration.UtilDate;
import dataaccess.DataAccess;
import domain.Apustua;
import domain.Bezeroa;
import domain.Errepikapena;
import domain.Event;
import domain.Pronostikoa;
import domain.Question;
import test.dataaccess.TestDataAccess;

@RunWith(MockitoJUnitRunner.class)
public class EmaitzaIpiniMockINT {
	DataAccess dataAccess=Mockito.mock(DataAccess.class);
	
	@InjectMocks
	BLFacade sut=new BLFacadeImplementation(dataAccess);
	
	@Test
	public void test1() {
		Question question = new Question();
		Pronostikoa pronostikoa = new Pronostikoa();
		sut.emaitzaIpini(question, pronostikoa);
		Mockito.verify(dataAccess).emaitzaIpini(question, pronostikoa);
	}

	@Test
	public void test2() {
		Pronostikoa pronostikoa = new Pronostikoa();
		sut.emaitzaIpini(null, pronostikoa);
		Mockito.verify(dataAccess).emaitzaIpini(null, pronostikoa);
	}
	
	@Test
	public void test3() {
		Question question = new Question();
		sut.emaitzaIpini(question, null);
		Mockito.verify(dataAccess).emaitzaIpini(question, null);
	}
	
	@Test
	public void test4() {
		Question question = new Question();
		Pronostikoa pronostikoa = new Pronostikoa();
		sut.emaitzaIpini(question, pronostikoa);
		Mockito.verify(dataAccess).emaitzaIpini(question, pronostikoa);
	}
	
	@Test
	public void test5() {
		Question question = new Question();
		Pronostikoa pronostikoa = new Pronostikoa();
		sut.emaitzaIpini(question, pronostikoa);
		Mockito.verify(dataAccess).emaitzaIpini(question, pronostikoa);
	}
}
