package com.madalla.util.jcr.ocm;

import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.jackrabbit.ocm.exception.IncorrectAtomicTypeException;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverter;
import org.joda.time.DateTime;


public class JodaDateTimeTypeConverter implements AtomicTypeConverter{

    public Value getValue(ValueFactory valueFactory, Object propValue)
	{
		if (propValue == null)
		{
			return null;
		}		
		DateTime dateTime = (DateTime) propValue;
		return valueFactory.createValue(dateTime.toString());		
	}


	public Object getObject(Value value)
    {
		try
		{
			return new DateTime(value.getString());
		}
		catch (Exception e)
		{
			throw new IncorrectAtomicTypeException("Impossible to convert the value : " + value.toString(), e);
		}

	}
	
	public String getXPathQueryValue(ValueFactory valueFactory, Object object)
	{
		try 
		{
			
			String isoDateTimeString = (String) object;
			return isoDateTimeString;

		} 
		catch (Exception e) 
		{
			throw new IncorrectAtomicTypeException("Impossible to get the sting value ", e);
		}
	}
}
