package fileCombiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class fileCombDriver {

	static ArrayList<String> arrlist = new ArrayList<String>();
	static ArrayList<TFile> filearr = new ArrayList<TFile>();
	
	public static void main(String args[]) throws IOException{
		
		fileCombDriver b = new fileCombDriver();
			
		//logs
		long start = System.currentTimeMillis();
		System.out.println("<begin>");

		//getting main directory and out file from user
	    
		
		String[] init = getDirectory().split("~");
		File mainDir = new File(init[0]);
		String out = init[1];		
		
		//File mainDir = new File("C:\\Users\\Tony\\Desktop\\School");
		//String out = "C:\\Users\\Tony\\Desktop\\dd.txt";
		
		//redirect sys out to text file from user
		PrintStream o = new PrintStream(new File(out));
		PrintStream console = System.out;
		System.setOut(o);

	
		//parse directory and populate arrlist
		dirRecur(mainDir, out);
		
		//sort arrlist and concat files
		Collections.sort(filearr, new compareByDate());
		for (int i=0; i<filearr.size(); i++) {
			System.out.println(filearr.get(i).getPath());
			System.out.println(filearr.get(i).getBirth());
			concat(filearr.get(i).getPath());
		}

		//logs
		long stop = System.currentTimeMillis();
		System.setOut(console);
		System.out.println("\naccessed files: \n");
		for (int i=0; i<arrlist.size(); i++) 
			System.out.println(arrlist.get(i));		
		System.out.println("\n<finished>");
		System.out.println("runtime: "+(stop - start)+ " ms");

	}//main 

	
	//searches through entire directory for txt files, adds time file obj to arrlist
	public static void dirRecur(File dir, String out) throws IOException{
		File[] dirList = dir.listFiles();
		for(int i=0; i<dirList.length; i++){
			if( dirList[i].isFile() && checkFileExt(dirList[i].getPath()) ){
				arrlist.add(dirList[i].getPath());
				TFile tmp = new TFile(dirList[i].getPath());
				filearr.add(tmp);
			}
			else if(dirList[i].isDirectory()){
				dirRecur(dirList[i], out);
			}
		}//for
	}//method
	
	//makes sure the files we try to concat are .txt
	//helper function to get file type (to check if txt)
	public static Boolean checkFileExt(String fullName) {
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf('.');
		String res =  fileName.substring(dotIndex + 1);
		if(res.equals("txt"))
			return true;
		else
			return false;	
	}//method
	
	//concats each text file to output file
	@SuppressWarnings("resource")
	public static void concat(String in) throws IOException{
		BufferedReader rd = null;
		String line;
		rd = new BufferedReader(new FileReader(new File(in)));
		while((line = rd.readLine()) != null){
			System.out.println(line);
		}
		System.out.println("================================================================================");
	}//method
	
	//get main dir and output file from user
	public static String getDirectory() throws IOException{
		Scanner scan = new Scanner(System.in);
		System.out.println();
		System.out.println("enter full target directory location/path: ");
		//System.out.println();
		String dir = scan.nextLine();
		System.out.println("enter output file location/path: ");
		String out = scan.nextLine();
		String res = dir + "~" + out;
		scan.close();
		return res;
	}//gD

}//class



class TFile extends File implements Comparator<TFile>{

	public TFile(String arg1){super(arg1);}
	
	public FileTime getBirth() throws IOException{
		String path = this.getPath();
		Path file = Paths.get(path);
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		FileTime tim =  attr.creationTime();
		return tim;
	}
	
	public int compareTo(TFile obj) throws IOException{
		return (obj.getBirth().compareTo(this.getBirth()));
	}

	@Override
	public int compare(TFile arg0, TFile arg1) {
		try {
			return (arg0.getBirth().compareTo(arg1.getBirth()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}//class

 class compareByDate implements Comparator<TFile>{
	public int compare(TFile arg0, TFile arg1) {
		try {
			return (arg0.getBirth().compareTo(arg1.getBirth()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}//


