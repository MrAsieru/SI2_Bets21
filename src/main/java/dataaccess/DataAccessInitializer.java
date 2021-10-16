package dataaccess;

import javax.persistence.EntityManager;

public interface DataAccessInitializer {

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 * @param entityManagerDB TODO
	 */
	void initializeDB(EntityManager entityManagerDB);

}