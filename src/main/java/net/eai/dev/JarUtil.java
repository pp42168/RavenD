package net.eai.dev;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class JarUtil {

	private boolean fromJar;
	
	public void tagJarPath(String path)
	{	
		File root = new File(path);  
		File[] files = root.listFiles();
		String content = "";


		if(files != null)
		{
			for(File file:files){   
				//process with directories
				if(file.isDirectory()){
					content += file.getName() + "/\n";
					tagJarPath(path + "/" + file.getName());
				}
				// process with files
				else{	
					if(!file.getName().contains(".DS_Store") &&
							!file.getName().contains(".jarpathtag"))
						content += file.getName() + "\n";
				}
			}
		}	    
		writeFile(path + "/.jarpathtag",content);
	}


	public void copyFromJarPath(String jarPath,String path) throws IOException 
	{
		List<String> files = readJarPath(jarPath);
		for(String file:files)
		{
			//System.out.print("\ncopying file:" + jarPath + "/  " + file + "\n");
			
			if(file.contains("/")){
				file = file.replace("/", "");
				copyFromJarPath(jarPath + "/" + file,path + "/" + file);
			}
			else{
				InputStream is= this.getClass().getResourceAsStream(jarPath + "/" + file);   
				BufferedReader br = new BufferedReader(new InputStreamReader(is));  
				String data = "";
				String s=""; 
				while((s=br.readLine())!=null)  
				{
					data += s + "\n";
				}

				ioUtil.writeFile(path + "/" + file, data);
			}

			
		}
	}
	
	
	private List<String> readJarPath(String path) throws IOException
	{
	//	System.out.print("\nreading Path: " + path + "\n");
		LinkedList<String> files = new LinkedList<String> ();
		
	//	System.out.print("\nreading Path jarfile: " + path + "/" + ".jarpathtag" + "\n");
		InputStream is= this.getClass().getResourceAsStream(path + "/" + ".jarpathtag");   
		BufferedReader br = new BufferedReader(new InputStreamReader(is));  
		String s=""; 
	//	System.out.print("\ntempPath:\n");
		while((s=br.readLine())!=null)  {
		//	System.out.print(s + "\n");
			files.add(s);
		}

		return files;
	}


	private List<String> readPath(String path) throws IOException
	{
		LinkedList<String> files = new LinkedList<String> ();
		InputStream is= this.getClass().getResourceAsStream("/sso.mdj");   
		BufferedReader br = new BufferedReader(new InputStreamReader(is));  
		String s=""; 
		while((s=br.readLine())!=null)  
		//	System.out.print(s);
			files.add(s);

		return files;
	}
	
	static public void writeFile(String filename,String data)
	{
		File file = new File(filename);  

		try {	

			File parent = file.getParentFile();
			if(parent!=null&&!parent.exists()){
				parent.mkdirs();
			}

			//if(file.exists())
			//	return;

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));    
			writer.write(data);			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public boolean isFromJar() {
		return fromJar;
	}

	public void setFromJar(boolean fromJar) {
		this.fromJar = fromJar;
	}

}
