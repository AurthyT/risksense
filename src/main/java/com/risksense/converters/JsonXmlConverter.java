package com.risksense.converters;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class JsonXmlConverter {
	public static Element test(org.json.simple.JSONObject j, Document document,String s,boolean insideArray) throws ParserConfigurationException {
		Element root = document.createElement("object");
		if(!insideArray)
		root.setAttribute("name",s);
        for(Iterator iterator = j.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Element element = null;
            if(j.get(key)!=null && !j.get(key).toString().contains(",")) {
            	String datatype = j.get(key).getClass().getSimpleName().toLowerCase();
            	if(datatype.equals("long") || datatype.equals("integer") ||
            			datatype.equals("double") || datatype.equals("float")) {
            		datatype="number";
            	}
            	element = document.createElement(datatype);
            	element.setAttribute("name", key);
            	element.setTextContent(String.valueOf(j.get(key)));
            } else if(j.get(key)==null){
            	element = document.createElement("null");
            	element.setAttribute("name", key);
            } else {
            	
            }
            root.appendChild(element);
        }
            return root;
	}
	
	public static Element test1(org.json.simple.JSONArray ja, Document document,String key,boolean insideArray) throws DOMException, ParserConfigurationException {
    	//JSONArray ja = (JSONArray)j.get(key);
    	Element array = document.createElement("array");
    	if(!insideArray)
    	array.setAttribute("name", key);
    	
    	for (int i = 0; i < ja.size(); i++) {
    		if(ja.get(i).toString().contains("[")) {
            	array.appendChild(test((org.json.simple.JSONObject) ja.get(i),document,key,false));
    		} else if(ja.get(i).toString().contains("{")) {
    			array.appendChild(test((org.json.simple.JSONObject) ja.get(i),document,key,true));
    		} else {
    			String datatype = ja.get(i).getClass().getSimpleName().toLowerCase();
            	if(datatype.equals("long") || datatype.equals("integer") ||
            			datatype.equals("double") || datatype.equals("float")) {
            		datatype="number";
            	}
    		Element e = document.createElement(datatype);
    		e.setTextContent(String.valueOf(ja.get(i)));
    		array.appendChild(e);
    		}
		}
		return array;
	}
	 public void convert(String inputpath,String outputpath){
	        JSONParser parser = new JSONParser();
	        try {
	            Object obj = parser.parse(new FileReader(inputpath));
	            org.json.simple.JSONObject j = (org.json.simple.JSONObject) obj;
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                Element root = document.createElement("object");
                document.appendChild(root);
	            for(Iterator iterator = j.keySet().iterator(); iterator.hasNext();) {
	                String key = (String) iterator.next();
	                Element element = null;
	                if(j.get(key)!=null && !j.get(key).toString().contains(",")	
	                		&& !j.get(key).toString().contains("[")) {
	                	String datatype = j.get(key).getClass().getSimpleName().toLowerCase();
	                	if(datatype.equals("long") || datatype.equals("integer") ||
	                			datatype.equals("double") || datatype.equals("float")) {
	                		datatype="number";
	                	}
	                	element = document.createElement(datatype);
	                	element.setAttribute("name", key);
	                	element.setTextContent(String.valueOf(j.get(key)));
	                	root.appendChild(element);
	                } else if(j.get(key)==null){
	                	element = document.createElement("null");
	                	element.setAttribute("name", key);
	                	root.appendChild(element);
	                } else if(j.get(key).toString().contains("[")){
	                	JSONArray ja = (JSONArray)j.get(key);
	                	Element array = document.createElement("array");
	                	array.setAttribute("name", key);
	                	
	                	for (int i = 0; i < ja.size(); i++) {
	                		if(ja.get(i).toString().contains("[")) {
	                			JSONArray jaa = (JSONArray)ja.get(i);
	                			for (int k = 0; k < jaa.size(); k++) {
	    	                	array.appendChild(test1(jaa,document,key,true));
			                	root.appendChild(array);
	                			}
	                		} else if(ja.get(i).toString().contains("{")) {
	                			array.appendChild(test((org.json.simple.JSONObject) ja.get(i),document,key,true));
			                	root.appendChild(array);
	                		} else {
	                			String datatype = ja.get(i).getClass().getSimpleName().toLowerCase();
	                        	if(datatype.equals("long") || datatype.equals("integer") ||
	                        			datatype.equals("double") || datatype.equals("float")) {
	                        		datatype="number";
	                        	}
	                		Element e = document.createElement(datatype);
	                		e.setTextContent(String.valueOf(ja.get(i)));
	                		array.appendChild(e);
		                	root.appendChild(array);
	                		}
						}
	                	
	                } else if(j.get(key).toString().contains(",")){
	                	root.appendChild(test((org.json.simple.JSONObject) j.get(key),document,key,false));
	                }
                     
	            }
	            TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File(outputpath));
                transformer.transform(source, result);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}


