package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface IEntryProcessor {

	void processNode(Node node, IContentData content) throws RepositoryException;
}
