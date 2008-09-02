package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface IEntryConvertor {

	void populateNode(Node node, IContentData content) throws RepositoryException;
}
