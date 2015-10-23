package net.eai.talk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import net.eai.talk.script.CommandRunner;
import net.eai.talk.script.ScriptRunner;
import net.eai.umlcg.basecg.UmlCG;
import net.eai.umlmodel.DEVProject;

public class Listener {

	private Memory memory = new Memory();
	private EarCognition earCognition = new EarCognition(memory);
	
	private List<String> heard;
	private List<HearDust> dusts;
	private CommandRunner currentRunner;
	private Map<String,CommandRunner> runnerPath;
	private String currentRunnerPath ;
	

	public Listener()
	{
		dusts = new LinkedList<HearDust>();
		currentRunner = new CommandRunner();
		runnerPath = new LinkedHashMap<String,CommandRunner> ();
		currentRunnerPath = "";
		runnerPath.put("", currentRunner);
	}
	
	public void createRunnerPath(String path)
	{
		CommandRunner r = new CommandRunner();
		runnerPath.put(path, r);
	}
		
	public void gotoRunner(String path)
	{
		CommandRunner r = new CommandRunner();
		runnerPath.put(path, r);
	}
	
	
	public void listenCommand()
	{
		byte[] b = new byte[1024];
		int n = 0;

		loadInBuildWords();
		
		try{
			while(true){
				//提示信息
				System.out.print(">");
				//读取数据
				n = System.in.read(b);
				//转换为字符串
				String s = new String(b,0,n - 1);
				//判断是否是quit
				if(s.equalsIgnoreCase("quit")){
					break; //结束循环
				}
				//回显内容
				System.out.println(run(s));
				//tell(s);
				 //System.out.println("输入内容为：" + s);

			} 
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private String run(String content)
	{
		return currentRunner.run(content);
	}
	

	private void tell(String content)
	{
		heard = new LinkedList<String>();
	//	String[] choppedContent = content.split(" ");

	//	for(String word:choppedContent)
	//	{
	//		heard.add(word);
			//Scenario scene = new Scenario();
			//scene.setContet(content);
			//earCognition.beBlown(scene);
	//	}

		
	}

	
	
	private void loadInBuildWords()
	{		
		UmlCG cg = new UmlCG();
		Tool tool = new Tool(cg);
		currentRunner.registerTool(tool);
		
		//memory.knowingTool(tool);
	
		//scenario.addWord(word);
		
		/*
		 * load uml 
		 * filename = d:/wewefwef/sdfas
		 * the umlModel
		 * export
		 * 
		 * create service agent
		 * 
		 * 
		 * 
		*/
	//	scenario.addWord(new );
		//scenario.addCommand("gen", );
		//wordType.put("a", "quantifier");
	}
}
