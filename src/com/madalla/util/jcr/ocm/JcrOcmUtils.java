package com.madalla.util.jcr.ocm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeTypeManager;

import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverterProvider;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.impl.DefaultAtomicTypeConverterProvider;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.manager.objectconverter.impl.ObjectConverterImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.wicket.WicketRuntimeException;
import org.joda.time.DateTime;

import com.madalla.service.cms.ocm.RepositoryInfo.RepositoryType;

public class JcrOcmUtils {
	
	/** namespace prefix constant */
    public static final String OCM_NAMESPACE_PREFIX   = "ocm";

    /** namespace constant */
    public static final String OCM_NAMESPACE = "http://jackrabbit.apache.org/ocm";
    
    public static ObjectContentManager getObjectContentManager(Session session){

    	try {
			JcrOcmUtils.setupOcmNodeTypes(session);
		} catch (RepositoryException e) {
			throw new WicketRuntimeException("Error setting up OCM ObjectContentManager.",e);
		}
    	
    	//Setup OCM annotated classes
		List<Class> classes = new ArrayList<Class>();
		for(RepositoryType type: RepositoryType.values()){
			classes.add(type.typeClass);
		}
		Mapper mapper = new AnnotationMapperImpl(classes);
				
		//Setup Convertors
		Map convertors = new HashMap();
		convertors.put(DateTime.class, JodaDateTimeTypeConverter.class);
		AtomicTypeConverterProvider atomicTypeConverterProvider = new DefaultAtomicTypeConverterProvider(convertors);
		
		
		ObjectContentManagerImpl ocm =  new ObjectContentManagerImpl(session, mapper);
		ObjectConverterImpl converterImpl = new ObjectConverterImpl(mapper, atomicTypeConverterProvider);
		ocm.setObjectConverter(converterImpl);
		
		return ocm;
    }
    
    
    private static void setupOcmNodeTypes(Session session) throws RepositoryException{
    	createNamespace(session);
    	registerOcmNodeType(session);
    }
	
	/**
    * Create the ocm namespace
    * 
    */
    private static void createNamespace(Session session) throws RepositoryException
    {
         try
         {
            String[] jcrNamespaces = session.getWorkspace().getNamespaceRegistry().getPrefixes();
            boolean createNamespace = true;
            for (int i = 0; i < jcrNamespaces.length; i++)
            {
                if (jcrNamespaces[i].equals(OCM_NAMESPACE_PREFIX))
                {
                    createNamespace = false;
                }
            }

            if (createNamespace)
            {
                session.getWorkspace().getNamespaceRegistry().registerNamespace(OCM_NAMESPACE_PREFIX, OCM_NAMESPACE);
            }

        }
        catch (Exception e)
        {
            throw new RepositoryException(e.getMessage());
        }
    }
	
	private static void registerOcmNodeType(Session session) throws RepositoryException{
		InputStream xml = JcrOcmUtils.class.getResourceAsStream("ocm-discriminator.xml");
		registerNodeTypes(session, xml );
	}
	
	private static void registerNodeTypes(Session session, InputStream xml) throws RepositoryException{
        try {
			NodeTypeDef[] types = NodeTypeReader.read(xml);

			Workspace workspace = session.getWorkspace();
			NodeTypeManager ntMgr = workspace.getNodeTypeManager();
			NodeTypeRegistry ntReg = ((NodeTypeManagerImpl) ntMgr).getNodeTypeRegistry();

			for (int j = 0; j < types.length; j++) {
			    NodeTypeDef def = types[j];

			    try {
			        ntReg.getNodeTypeDef(def.getName());
			    }
			    catch (NoSuchNodeTypeException nsne) {
			        // HINT: if not already registered than register custom node type
			        ntReg.registerNodeType(def);
			    }

			}
		} 
        catch (Exception e) 
		{
        	throw new RepositoryException("Impossible to register node types", e);
		}
		
	}

	public static void registerNodeTypes(Session session, String nodeTypeFile) throws RepositoryException
    {
        try {
			InputStream xml = new FileInputStream(nodeTypeFile);
			registerNodeTypes(session, xml);

		} 
        catch (Exception e) 
		{
        	throw new RepositoryException("Impossible to register node types", e);
		}
    }		
}
