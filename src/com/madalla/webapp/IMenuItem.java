package com.madalla.webapp;

import java.io.Serializable;

import org.apache.wicket.Page;


public interface IMenuItem extends Serializable {
	Class<? extends Page> getItemClass();
	String getItemName();
}
