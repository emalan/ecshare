package com.madalla.service.cms;

import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public interface IRepositoryService {

	String getSite();
    String getLocaleId(String id, Locale locale);
    Node getApplicationNode(Session session) throws RepositoryException;
    Node getSiteNode(Session session) throws RepositoryException;
    Node getCreateNode(String nodeName, Node parent) throws RepositoryException;
}
