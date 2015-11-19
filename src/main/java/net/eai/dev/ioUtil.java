package net.eai.dev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.eai.umlmodel.Entity;

public class ioUtil {
	static public void writeFile(String filename,String data)
	{
		File file = new File(filename);  
	
		try {	
			
			File parent = file.getParentFile();
			if(parent!=null&&!parent.exists()){
				parent.mkdirs();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));    
			writer.write(data);			
		    writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	static public void writeFileWithExistCode(String filename,String data)
	{
		LinkedHashMap<String,String> keptCode = getManualCode(filename);
		Iterator<Entry<String, String>> entityIter = keptCode.entrySet().iterator();
		while (entityIter.hasNext()) {
			Entry<String, String> entry =  entityIter.next();      
			
    		String key = entry.getKey();
    		String val = entry.getValue();

    		if(key.startsWith("%pre_"))
    			data = data.replace("//business logic - " + key.substring(5), 
    					"//business logic - " + key.substring(5) + val);
    		else
    			data = data.replace("//wrap up - " + key.substring(5),
    					"//wrap up - " + key.substring(5) + val);    		
		}
		
		writeFile(filename, data);
		
	}
	
	static public String fillTemplate(String fileName,LinkedHashMap<String,String> tokens)
	{
		File file = new File(fileName);   		
	    String content = "";

		if(file.exists())
		{
			try {
				String line;
			    BufferedReader reader = new BufferedReader(new FileReader(file));  
				line = reader.readLine();				

				
				while(line !=null){  
			        Iterator<Entry<String, String>> entityIter = tokens.entrySet().iterator();
			        while (entityIter.hasNext()) {
			            Entry<String, String> entry = entityIter.next();
	
			            String token = entry.getKey();
			            String value = entry.getValue();		            
	
						line = line.replace("@" + token + "@", value);
						line = line.replace("@^" + token + "@", 
								value.toUpperCase().substring(0, 1) + value.substring(1,value.length()));					
						line = line.replace("@^^" + token + "@", 
								value.toUpperCase());
						line = line.replace("@.." + token + "@", 
								value.toLowerCase());
						line = line.replace("@." + token + "@", 
								 value.toLowerCase().substring(0, 1) + value.substring(1,value.length()));
						
			        }
					content += line + "\r\n"; 
					line = reader.readLine();
			    }  
			    reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return content;
		
		
	}
	
	
	
	static public String replaceF(String line,String token,String value)
	{
		line = line.replace("@" + token + "@", value);

		line = line.replace("@^" + token + "@", 
				value.toUpperCase().substring(0, 1) + value.substring(1,value.length()));
		
		line = line.replace("@^^" + token + "@", 
				value.toUpperCase());

		line = line.replace("@.." + token + "@", 
				value.toLowerCase());

		line = line.replace("@." + token + "@", 
				 value.toLowerCase().substring(0, 1) + value.substring(1,value.length()));
		

		line = line.replace("@." + token + "@", 
				 value.toLowerCase().substring(0, 1) + value.substring(1,value.length()));
		

		line = line.replace("@_" + token + "@", 
				convertAttSpellToColumnSpell(value));
		
		line = line.replace("@~" + token + "@", 
				convertColumnSpeelToAttSpell(value));
			
		return line;
	}
	
	static public String capFirstLetter(String value)
	{
		return value.toUpperCase().substring(0, 1) + value.substring(1,value.length());
	}
	
	static public boolean containF(String line,String token)
	{
		if(line.contains("@" + token + "@"))
			return true;
		else if(line.contains("@^" + token + "@"))
			return true;
		else if(line.contains("@." + token + "@"))
			return true;
		else if(line.contains("@.." + token + "@"))
			return true;
		else if(line.contains("@^^" + token + "@"))
			return true;
		else if(line.contains("@_" + token + "@"))
			return true;
		else if(line.contains("@~" + token + "@"))
			return true;
		
		return false;
	}
	
	
	static public String readFile(String filename)
	{
		File file = new File(filename);   
		
	    String content = "";

		if(file.exists())
		{
			try {
				String line;
			    BufferedReader reader = new BufferedReader(new FileReader(file));  
				line = reader.readLine();
				
				while(line !=null){  
					content += line + "\r\n"; 
					line = reader.readLine();
			    }  
			    reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return content;

	}
	
	

	public static void writeSectionCode(String filename,String sectionStart,String sectionEnd,String content)
	{
		String fileData = readFile(filename);
		int startInd = fileData.indexOf(sectionStart);
		int endInd = fileData.indexOf(sectionEnd,startInd);
		
		if(startInd == -1)
			return ;
		
		String output = fileData.substring(0,startInd) + sectionStart;
		
		output += content + sectionEnd;
		
		output += fileData.substring(endInd + sectionEnd.length());
		
		writeFile(filename,output);
		
	}
	
	public static String convertColumnSpeelToAttSpell(String att)
	{
		String columnName = "";
		for(int i = 0; i< att.length();i++)
		{
			char ch = att.charAt(i);
			if(ch == '_'){
				i++;
				if(i< att.length())
					columnName += String.valueOf(att.charAt(i)).toUpperCase();
				else
					break;
			}
			else 
				columnName += att.charAt(i);
		}
		
		return columnName;
	}
	
	public static String convertAttSpellToColumnSpell(String att)
	{
		String columnName = "";
		for(int i = 0; i< att.length();i++)
		{
			char ch = att.charAt(i);
			if(ch <= 'Z' && ch >= 'A')
				columnName += "_" + ch;
			else 
				columnName += ch;
		}
		columnName = columnName.toLowerCase();
		
		return columnName;
		
	}
	
	
	
	public static void genPathWithTemplate(String templatePath,String targetPath,String prjName)
	{
		File root = new File(templatePath);  
	    File[] files = root.listFiles();
	    if(files != null)
	    {
	    	 for(File file:files){     
	 	    	if(file.isDirectory()){
	 	    		int parentPathLength = templatePath.length();
	 	    		String tempPath = file.getPath().substring(parentPathLength);
	 	    		tempPath = tempPath.replace("@prj@", prjName);
	 	    		genPathWithTemplate(file.getPath(),targetPath  + tempPath,prjName);
	 	    	}
	 	    	else
	 	    	{
	 	    		String fileData = readFile(file.getAbsolutePath());
	 	    		fileData = fileData.replace("@prj@", prjName);
	 	    		String targetFileName = file.getName().replace("@prj@", prjName);
	 	    		writeFile(targetPath + "/" + targetFileName,fileData);
	 	    	}
	 	    }
	    }
	}

	private static LinkedHashMap<String,String> getManualCode(String filename)
	{
		LinkedHashMap<String,String> codes = new LinkedHashMap<String,String>();
		String fileData = readFile(filename);
		int preCodeStart = fileData.indexOf("//business logic - ");
		int afterCodeStart = fileData.indexOf("//wrap up - ");
		int codeEnd;

		int startPreLength = "//business logic - ".length();
		int startAfterLength = "//wrap up - ".length();
		
		while(preCodeStart != -1)
		{
			int opNameEndInd = fileData.indexOf("\r\n", preCodeStart);
			String opName = fileData.substring(preCodeStart + startPreLength,
					opNameEndInd);
			codeEnd = fileData.indexOf("\r\n\t\t//logic ends",opNameEndInd);
			if(codeEnd == -1)
			{
				codeEnd = fileData.indexOf("\r\n\t\t\t//logic ends",opNameEndInd);				
			}
			
			String code = fileData.substring(opNameEndInd,codeEnd);
			codes.put("%pre_" + opName,code);
			
			preCodeStart = fileData.indexOf("//business logic - ",codeEnd);
		}
		
		while(afterCodeStart != -1)
		{
			int opNameEndInd = fileData.indexOf("\r\n", afterCodeStart);
			String opName = fileData.substring(afterCodeStart + startAfterLength,
					opNameEndInd );
			codeEnd = fileData.indexOf("\r\n\t\t//logic ends",opNameEndInd);
			
			String code = fileData.substring(opNameEndInd,codeEnd);
			codes.put("%aft_" + opName,code);

			afterCodeStart = fileData.indexOf("//wrap up - ",codeEnd);
		}
		
		
		
		return codes;
	}


}
