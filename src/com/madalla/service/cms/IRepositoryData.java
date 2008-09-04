package com.madalla.service.cms;

import javax.jcr.RepositoryException;
import javax.jcr.Session;


/**
 * Implementations should make sure that all returned values are final and immutable
 * 
 * @author exmalan
 *
 */
interface IRepositoryData {

	String getGroup();
	String getName();
	String getId();
	String processEntry(Session session, IRepositoryService service) throws RepositoryException;
}
