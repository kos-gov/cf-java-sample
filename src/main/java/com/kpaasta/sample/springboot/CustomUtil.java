package com.kpaasta.sample.springboot;

import java.util.Map;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
//import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;

public class CustomUtil {

	/* --- The following function is not used on 2020-06-18 by SangKeun,Lee --- *
	public static Map<String, Object> jsonMapper(ObjectMapper mapper, String json){
	   try {
	       return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
	   }catch (Exception e){
	       e.printStackTrace();
	   }
	   return null;
    }
	
	public static Map<String, Object> jsonFile(String fileName, String json){
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			return mapper.readValue(new File(fileName), new TypeReference<Map<String, Object>>() {});
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	 * ------------------------------------------------------------------------ */
	
    public static boolean isEmpty(Object obj) {
        if (obj == null) { return true; }
        if ((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; } 
        if (obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); }
        if (obj instanceof List) { return ((List<?>)obj).isEmpty(); }
        if (obj instanceof Object[]) { return (((Object[])obj).length == 0); } 

        return false;
    }
    
    private static String readLineByLineJava8(URI filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
    
    public static String getContent(String FileName) {
    	ClassPathResource resource = new ClassPathResource(FileName);
    	try {
    		return readLineByLineJava8(resource.getURI());
    	} catch (IOException ex) {
    		return "";
    	}
    }
    
    public static String removeFirstChar(String s){
    	return s.substring(1);
    }
    
    public static String removeLastChar(String s){
    	return s.substring(0, s.length() - 1);
    }
    
}
