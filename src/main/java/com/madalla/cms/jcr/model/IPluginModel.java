package com.madalla.cms.jcr.model;

import java.util.Map;

import org.apache.wicket.model.IModel;

public interface IPluginModel extends IModel<ContentNode> {
	public Map<String, String> getMapRepresentation();
}
