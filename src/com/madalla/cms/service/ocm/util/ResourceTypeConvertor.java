package com.madalla.cms.service.ocm.util;

import java.io.InputStream;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.jackrabbit.ocm.exception.IncorrectAtomicTypeException;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverter;

/**
 * Converter for InputStream
 * 
 * @author Eugene Malan
 *
 */
public class ResourceTypeConvertor implements AtomicTypeConverter 
	{
		/**
		 * Converts the Property Object to a Value for inserting into CMS
		 * 
		 */
	    public Value getValue(ValueFactory valueFactory, Object propValue)
	    {
	        if (propValue == null)
	        {
	            return null;
	        }
	        return valueFactory.createValue((InputStream) propValue);
	    }

	    /**
	     * Converts the CMS Value to an Object ready to be set to the property in the Java Bean
	     * 
	     */
	    public Object getObject(Value value)
	    {
	    	try
	    	{
			    return value.getStream();
			}
			catch (RepositoryException e)
			{
				throw new IncorrectAtomicTypeException("Impossible to convert the value : " + value.toString()  , e);
			}
	    }


	    /**
	     *
	     */
		public String getXPathQueryValue(ValueFactory valueFactory,Object object)
		{		
			throw new IncorrectAtomicTypeException("Binary cannot be used in queries");
		}
}
