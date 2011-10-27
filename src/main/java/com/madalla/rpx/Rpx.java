package com.madalla.rpx;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Rpx {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String apiKey;
	private String baseUrl;
	private String callback;

	public Rpx(){

	}

    public Rpx(String apiKey, String baseUrl) {
        while (baseUrl.endsWith("/"))
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public Element authInfo(String token) {
        Map query = new HashMap();
        query.put("token", token);
        return apiCall("auth_info", query);
    }
    public HashMap allMappings() {
        Element rsp = apiCall("all_mappings", null);
        Element mappings_node = (Element)rsp.getFirstChild();
        HashMap result = new HashMap();
        NodeList mappings = getNodeList("/rsp/mappings/mapping", rsp);
        for (int i = 0; i < mappings.getLength(); i++) {
            Element mapping = (Element)mappings.item(i);
            List identifiers = new ArrayList();
            NodeList rk_list = getNodeList("primaryKey", mapping);
            NodeList id_list = getNodeList("identifiers/identifier", mapping);
            String remote_key = ((Element)rk_list.item(0)).getNodeValue();
            for (int j = 0; j < id_list.getLength(); j++) {
                Element ident = (Element) id_list.item(j);
                identifiers.add(ident.getNodeValue());
            }
            result.put(remote_key, identifiers);
        }
        return result;
    }
    private NodeList getNodeList(String xpath_expr, Element root) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        try {
            return (NodeList) xpath.evaluate(xpath_expr, root, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            return null;
        }
    }
    public List mappings(Object primaryKey) {
        Map query = new HashMap();
        query.put("primaryKey", primaryKey);
        Element rsp = apiCall("mappings", query);
        Element oids = (Element)rsp.getFirstChild();
        List result = new ArrayList();
        NodeList nl = oids.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            result.add(node.getNodeValue());
        }
        return result;
    }
    public void map(String identifier, Object primaryKey) {
        Map query = new HashMap();
        query.put("identifier", identifier);
        query.put("primaryKey", primaryKey);
        apiCall("map", query);
    }
    public void unmap(String identifier, Object primaryKey) {
        Map query = new HashMap();
        query.put("identifier", identifier);
        query.put("primaryKey", primaryKey);
        apiCall("unmap", query);
    }
    private Element apiCall(String methodName, Map partialQuery) {
        Map query = null;
        if (partialQuery == null) {
            query = new HashMap();
        } else {
            query = new HashMap(partialQuery);
        }
        query.put("format", "xml");
        query.put("apiKey", apiKey);
        StringBuffer sb = new StringBuffer();
        for (Iterator it = query.entrySet().iterator(); it.hasNext();) {
            if (sb.length() > 0) sb.append('&');
            try {
                Map.Entry e = (Map.Entry)it.next();
                sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unexpected encoding error", e);
            }
        }
        String data = sb.toString();
        try {
            URL url = new URL(baseUrl + "/api/v2/" + methodName);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            OutputStreamWriter osw = new OutputStreamWriter(
                conn.getOutputStream(), "UTF-8");
            osw.write(data);
            osw.close();

//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//            	log.debug(line);
//            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(conn.getInputStream());
            Element response = (Element)doc.getFirstChild();
            if (!response.getAttribute("stat").equals("ok")) {
            	log.error("Exception during RPX authentication.");
            	return null;
            }
            return response;
        } catch (Exception e) {
            log.error("Exception during RPX authentication.", e);
            return null;
        }
    }
    public static void main(String [] args) {
        if (args.length < 3) {
            System.out.println("Usage: Rpx <api key> <RPX service URL> <map|unmap|mappings|all_mappings> [...]");
            System.exit(1);
        }
        Rpx r = new Rpx(args[0], args[1]);
        if (args[2].equals("mappings")) {
            System.out.println("Mappings for " + args[3] + ":");
            System.out.println(r.mappings(args[3]));
        }
        if (args[2].equals("map")) {
            System.out.println(args[3] + " mapped to " + args[4]);
            r.map(args[3], args[4]);
        }
        if (args[2].equals("unmap")) {
            System.out.println(args[3] + " unmapped from " + args[4]);
            r.unmap(args[3], args[4]);
        }
        if (args[2].equals("all_mappings")) {
            System.out.println("All mappings:");
            System.out.println(r.allMappings().toString());
        }
    }

    public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getCallback() {
		return callback;
	}
}
