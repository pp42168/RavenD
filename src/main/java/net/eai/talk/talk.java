	package net.eai.talk;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import net.eai.dev.DataMapper;
import net.eai.dev.JarUtil;
import net.eai.dev.JsonClassGen;
import net.eai.dev.UmlException;
import net.eai.dev.ioUtil;
import net.eai.umlcg.basecg.*;
import net.eai.umlcg.framework.elespring.ESServiceFramework;
import net.eai.umlmodel.DEVProject;

public class talk {
	public static void main(String[] args) throws IOException {

		//ioUtil.writeFile("fse/fs/fsef/fds", "ff");
		
		//JarUtil u = new JarUtil();
		//u.tagJarPath("pack/eleSpringTemplate");
		//FileUtil.readJarPath("eleSpringTemplate");
		//u.readJarPath("eleSpringTemplate");
		//ioUtil.deleteFile("fse");
		
		Listener listener = new Listener();		
		listener.listenCommand();
		UmlCG cg = new UmlCG();
		cg.setTemplatePath("templates/eleSpringTemplate");
		try {
			cg.gen("example.mdj", "j");
		} catch (UmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		if(args.length == 0)
		{
			String helpString = "RavenD help:\r\n"
					+ "-u umlfile\r\n"
					+ "-t targetpath\r\n"
					+ "-g codegen";
			System.out.print(helpString);
			return;
		}
		
		
		for(int i = 0;i<args.length;i++)
		{
			
		}
		String url = "http://api.supplytest.zoo.elenet.me/internal/user_token/f711c78bcf4c0cef1a5da3c761c4452b852555e16aa82c13055221e1fe900149";
	*/	
	//	ioUtil.writeFile("api.txt" ,mapreturn);
	
	/*	JsonClassGen json = new JsonClassGen(
				"me.ele.mercuris.ops.dto",
				"codeTemplate",
				"OrderApi",
				"src/");
		json.addApi("getOrder", "");
		json.genCode();*/

	//	String ret = HttpGetter.httpGet("http://gss3.map.baidu.c	om/?newmap=1&reqflag=pcmap&biz=1&from=webmap&qt=bkg_data&c=289&ie=utf-8&wd=停车&l=13&xy=1651_444&b=(13509846.26,3637884.36;13545078.26,3647900.36)", "GET", "");
		//String ret = HttpGetter.httpGet("http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&qt=s&c=289&wd=%E5	%81%9C%E8%BD%A6&da_src=pcmappg.map&on_gel=1&l=11&gr=1&b=(13467510.26,3622908.36;13608438.26,3662972.36)&tn=B_NORMAL_MAP&nn=0&ie=utf-8", "GET", "");


	//	ioUtil.genPathWithTemplate("eleSpringTemplate", "../testProject/aa", "dd");

		///DEVProject project = DEVProject.importFromJson("../baseSettings/base.js");
	/*	DEVProject project = DEVProject.importFromJson("../testProject/Fragment.js");
		project.setProjectPath("../Mercurius");
		
		

		ESServiceFramework es = new ESServiceFramework("../Mercurius-MobileService","eleSpringTemplate");
		//ESServiceFramework es = new ESServiceFramework("../testProject/src","eleApiDocTemplate");
		
		CodeSkeletonBuilder skeleton = new CodeSkeletonBuilder(
				"../Mercurius-MobileService",
				"eleSpringTemplate/projectSkeleton",
			//	"../elemall/ApiDocs",
			//	"eleApiDocTemplate/skeleton",
				project
				);
		
		//skeleton.addFilter("weixin");
		skeleton.setFramework(es);		
		skeleton.setM_orgPath("me.ele");
		skeleton.genCodeSkeleton();		*/
		
		/*
		FrameworkManager m = new FrameworkManager();		
		m.addFramework("es", es);
		m.genCode("es",project);
		*/
		

		/*
		DEVPackage pack = DEVPackage.importFromJson(jsonStr);
		pack.setTemplatePath("codeTemplate");
		pack.setPackageName("net.eai.parking");
		
		project.addPackage(pack);
		project.exportCode();*/
		
		//ret =  ret + ";";
		//Company.select("order by id desc");
/*
		DEVProject project = new DEVProject();
		project.setProjectPath("D:/workspace/EAIDev/src");
		//project.setProjectPath("/Users/apple/Documents/workspace/EAIDev/src");
		
		
		DEVPackage pack = DEVPackage.importFromJson(jsonStr);
		pack.setTemplatePath("D:/workspace/EAIDev/codeTemplate");
		//pack.setTemplatePath("/Users/apple/Documents/workspace/EAIDev/codeTemplate");
		pack.setPackageName("net.eai.parking");
		
		project.addPackage(pack);
		project.exportCode();*/
	 //   StarUmlFragment obj = gson.fromJson(jsonStr, StarUmlFragment.class);
	  //  obj.getName();
		
	//	testfun(12148313.20075,3122166.7140485);
		System.out.print("Done");
	}
	
	
}
