package com.madalla.cms.jcr;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

public class NodeDisplay {

	private static final String CLASS_NAME = "ocm_classname";
	private static final String JCR_PREFIX = "jcr:";
	private static final String NULL_CLASS_DISPLAY = "Not an OCM Node";

	private String name;
	private String className;
	private Map<String, NodePropertyDisplay> properties = new HashMap<String, NodePropertyDisplay>();

	public class NodePropertyDisplay{
		public String type;
		public String value;
		public NodePropertyDisplay(String type){
			this.type = type;
		}
	}

	public NodeDisplay(Item item) throws RepositoryException{
		name = item.getName();
		if (item instanceof Node){
			Node node = (Node) item;
			for (PropertyIterator iter = node.getProperties(); iter.hasNext();){
				Property prop = iter.nextProperty();
				String propName = prop.getName();
				if (propName.equals(CLASS_NAME)){
					className = prop.getValue().getString();
					continue;
				}
				if (propName.startsWith(JCR_PREFIX)){
					continue;
				}
				String key = prop.getName();
				NodePropertyDisplay value = new NodePropertyDisplay(PropertyType.nameFromValue(prop.getType()));
				try {
					value.value = prop.getValue().getString();
				} catch (ValueFormatException e){
					value.value = prop.getValues().toString();

				}
				properties.put(key, value);
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className == null? NULL_CLASS_DISPLAY : className;
	}

	public Map<String, NodePropertyDisplay> getProperties() {
		return properties;
	}


}
