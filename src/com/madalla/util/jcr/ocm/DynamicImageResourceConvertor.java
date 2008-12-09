package com.madalla.util.jcr.ocm;

import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.jackrabbit.ocm.exception.IncorrectAtomicTypeException;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverter;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;

public class DynamicImageResourceConvertor implements AtomicTypeConverter {
    
    public Value getValue(ValueFactory valueFactory, Object propValue)
    {
        if (propValue == null)
        {
            return null;
        }
        return null; //We will get data into repository another way.
//        try {
//            DynamicImageResource imageResource = (DynamicImageResource) propValue;
//            return valueFactory.createValue( imageResource.getResourceStream().getInputStream());
//        } catch (ResourceStreamNotFoundException e) {
//            throw new IncorrectAtomicTypeException("Failed to get value from Image Resource.",e);
//        }
    }

    /**
     * 
     * @see org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverter#getObject(javax.jcr.Value)
     */
    public Object getObject(Value value)
    {
        try
        {
            InputStream inputStream = value.getStream();
            BufferedDynamicImageResource webResource = null;
            try {
                webResource = new BufferedDynamicImageResource();
                webResource.setImage(ImageIO.read(inputStream));
                return webResource;
            } catch (Exception e) {
                throw new RepositoryException("getObject - Exception creating DynamicImageResource",e);
            }
        }
        catch (RepositoryException e)
        {
            throw new IncorrectAtomicTypeException("Impossible to convert the value : " + value.toString()  , e);
        }
    }


    /**
     * 
     * @see org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverter#getStringValue(java.lang.Object)
     */
    public String getXPathQueryValue(ValueFactory valueFactory,Object object)
    {       
        throw new IncorrectAtomicTypeException("Binary cannot be used in queries");
    }
}
