package fileCombiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class fileCombDriver {

	static ArrayList<String> arrlist = new ArrayList<String>();

	public static void main(String args[]) throws IOException{
		//logs
		long start = System.currentTimeMillis();
		System.out.println("<begin>");

		//getting main directory and out file from user
		String[] init = getDirectory().split("~");
		File mainDir = new File(init[0]);
		String out = init[1];		
		
		//redirect sys out to text file from user
		PrintStream o = new PrintStream(new File(out));
		PrintStream console = System.out;
		System.setOut(o);

		//parse directory and output to file
		dirRecur(mainDir, out);

		//logs
		long stop = System.currentTimeMillis();
		System.setOut(console);
		System.out.println("\naccessed files: \n");
		for (int i=0; i<arrlist.size(); i++) 
			System.out.println(arrlist.get(i));		
		System.out.println("\n<finished>");
		System.out.println("runtime: "+(stop - start)+ " ms");

	}//main 

	//searches through entire directory for txt files, calls concat method
	public static void dirRecur(File dir, String out) throws IOException{
		File[] dirList = dir.listFiles();
		for(int i=0; i<dirList.length; i++){
			if( dirList[i].isFile() && checkFileExt(dirList[i].getPath()) ){
				System.out.println(dirList[i].getPath());
				arrlist.add(dirList[i].getPath());
				concat(dirList[i].getPath());
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
	
	//concats the txt files to the out file

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
	
	//gets target dir and out file from user

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


	//set sysout to out file


}//class
