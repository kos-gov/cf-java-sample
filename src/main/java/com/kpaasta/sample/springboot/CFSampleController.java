package com.kpaasta.sample.springboot;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.*;

import org.springframework.ui.Model;

@Controller
@SuppressWarnings("unchecked")
public class CFSampleController {

	public static final String VCAP_APPLICATION = "VCAP_APPLICATION";
	public static final String VCAP_SERVICES = "VCAP_SERVICES";

	@Value("${VCAP_APPLICATION:{}}")
	private String application;

	@Value("${VCAP_SERVICES:{}}")
	private String services;
	
	@RequestMapping("/")
	public String index(Model model) {

		ObjectMapper objectMapper = new ObjectMapper(); 
		
		LinkedHashMap<String, Object> cfapp = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> limit = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> cfsvc = new LinkedHashMap<String, Object>();
		
		if (application == null || application.equalsIgnoreCase("{}") || application.isEmpty()) {
			try {
				application = System.getenv(VCAP_APPLICATION);
				System.out.println("application: " + application);
			} catch (Exception e) {}
		}

		if (services == null || services.equalsIgnoreCase("{}") || services.isEmpty()) {
			try {
				services = System.getenv(VCAP_SERVICES);
				System.out.println("services: " + application);
			} catch (Exception e) {}
		}
		
		if (application == null || application.isEmpty()) {
			application = CustomUtil.getContent("./jcap_app.json");
			System.out.println("application: " + application);
		}
		
		if (services == null || services.isEmpty()) {
			services = CustomUtil.getContent("./jcap_services.json");
			System.out.println("services: " + services);
		}

		try {
			String cfsvcName = null;
			String n = null;
			
			// --- for application
			
			JsonNode rootNode1 = objectMapper.readTree(application);
			
			if (rootNode1.get("VCAP_APPLICATION") != null) { // if RootNode is "VCAP_APPLICATION", then ...
				Iterator<Map.Entry<String, JsonNode>> fields = rootNode1.fields();
				while (fields.hasNext()) {
				   Map.Entry<String, JsonNode> entry1 = fields.next();
				   System.out.println("VCAP_APP: " + entry1.getKey() + " ==> " + entry1.getValue());
					   
				   n = String.format("%s", entry1.getValue());
				   break;
				}
			} else { // if RootNode is not "VCAP_APPLICATION", then ...
				n = application;
			}
			
			JsonNode rootNode2 = objectMapper.readTree(n);
			Iterator<Map.Entry<String, JsonNode>> field2 = rootNode2.fields();

			while (field2.hasNext()) {
			    Map.Entry<String, JsonNode> entry2 = field2.next();
			    System.out.println(entry2.getKey() + " ==> " + entry2.getValue());

				n = String.format("%s", entry2.getValue());

					String keyName = null;
					if (entry2.getKey().equalsIgnoreCase("name")) {
					keyName = "application_name";
				} else if (entry2.getKey().equalsIgnoreCase("uris")) {
					keyName = "application_uris";
				} else {
					keyName = entry2.getKey();
				}
			   
			    if (entry2.getKey().equalsIgnoreCase("limits")) {
			    	System.out.println("this is limits ==> " + entry2.getKey());
			    	n = String.format("%s", entry2.getValue());

				    JsonNode rootNode3 = objectMapper.readTree(n);
				    Iterator<Map.Entry<String, JsonNode>> field3 = rootNode3.fields();
				    while (field3.hasNext()) {
					    Map.Entry<String, JsonNode> entry3 = field3.next();
					    System.out.println(entry3.getKey() + " ==> " + entry3.getValue());
					    String str = entry3.getValue().toString().replaceAll("\"", "");
					    limit.put(entry3.getKey(), str);
				    }
				    cfapp.put(keyName, limit);
			    } else if (entry2.getKey().equalsIgnoreCase("uris")) {
			    	System.out.println("this is uris ==> " + entry2.getKey());
			    	List<String> arr = objectMapper.readValue(n, List.class);
			    	for (String str : arr) {
			    		System.out.println("List Of uri: " + "\"" + str.replaceAll("\"", "") + "\"");
			    	}
			    	cfapp.put(keyName, arr);
			    } else {
			    	System.out.println("this is others ==> " + entry2.getKey());
			    	String str = entry2.getValue().toString().replaceAll("\"", "");
				    	cfapp.put(keyName, str);
				    }
			   }	

			   for ( String k : cfapp.keySet() ) {
				   System.out.println( String.format("Key : %s, Value : %s", k, cfapp.get(k)) );
			}

			if (!cfapp.containsValue("instance_index")) {
			   cfapp.put("instance_index", "0");
			}
			
			// --- for services
			   
			JsonNode rootNode5 = objectMapper.readTree(services);
			if (rootNode5.get("VCAP_SERVICES") != null) {// if RootNode is "VCAP_SERVICES", then ...
				Iterator<Map.Entry<String, JsonNode>> field5 = rootNode5.fields();
				while (field5.hasNext()) {
				   Map.Entry<String, JsonNode> entry5 = field5.next();
				   System.out.println("VCAP_SVC: " + entry5.getKey() + " ==> " + entry5.getValue());
				   
				   n = String.format("%s", entry5.getValue());
				   break;
				}
			} else { // if RootNode is not "VCAP_SERVICES", then ...
				n = services;
			}
			
			JsonNode rootNode6 = objectMapper.readTree(n);
			Iterator<Map.Entry<String, JsonNode>> field6 = rootNode6.fields();

			while (field6.hasNext()) {
			    Map.Entry<String, JsonNode> entry6 = field6.next();
			    System.out.println(entry6.getKey() + " ==> " + entry6.getValue());

			    cfsvcName = String.format("%s", entry6.getKey());
			 	System.out.println("serviceName: " + cfsvcName);

				n = String.format("%s", entry6.getValue());
				//System.out.println("service1: " + n);
				
				n = CustomUtil.removeFirstChar(n);
				n = CustomUtil.removeLastChar(n);
				
				//System.out.println("service2: " + n);
				
			    JsonNode rootNode7 = objectMapper.readTree(n);
			    Iterator<Map.Entry<String, JsonNode>> field7 = rootNode7.fields();
			    while (field7.hasNext()) {
				    Map.Entry<String, JsonNode> entry7 = field7.next();
				    System.out.println(cfsvcName + ": " + entry7.getKey() + " ==> " + entry7.getValue());
				    String str = entry7.getValue().toString().replaceAll("\"", "");
				    
				    cfsvc.put(entry7.getKey(), str);
			    }
			}
			for ( String k : cfsvc.keySet() ) {
			   System.out.println( String.format("Key : %s, Value : %s", k, cfsvc.get(k)) );
			}

			 
			model.addAttribute("cfapp", cfapp);
			model.addAttribute("cfservicename", cfsvcName);
			model.addAttribute("cfservice", cfsvc);

		} catch (Exception ex) {
			// No services
			System.out.println("error: " + ex.getMessage());
			model.addAttribute("cfservicename", "");
			model.addAttribute("cfservice", "");
		}
	
		return "index.html";
	}
}
