package directoryTestProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ScanConfig {
	
	private static final String WI = "-waitInterval";
	private static final String ISF = "-includeSubFolders";
	private static final String AD = "-autoDelete";
	private static final String ID = "-inputDir";
	private static final String OD = "-outputDir";
	private static final String M = "-mask";
	
	public String mask;
	public String threadINFO;
	
	public Path sourcePath;
	public Path targetPath;
	
	public boolean moveFlag;
	public boolean subFolderFlag;
	private boolean exitFlag;
	
	public double waitInterval;
	private HashMap<String, String> availibleCommands = new HashMap<String, String>();
	
	public ScanConfig(){//HashMap<String, String> anAvailibleCommands
		
		this.mask = ".*";
		this.subFolderFlag = true;
		this.moveFlag = false;
		this.waitInterval = 0;
		initHashMap();
		
	}
	
	public static void helpMessage() {
		
		System.out.println("Program scan the specified directory, "
				+ "and copies the files that meet a certain condition "
				+ "in the output directory. Additionally you could create up to 4 threads");
		System.out.println("Availible commands:\n	scan\n	help\n	threadStop	\"ThreadID\"\n 	activeThreads\n 	exit");
		System.out.println("Please enter necessary parameters, like in the followin example:");
		System.out.println("scan -inputDir c:/test/in -outputDir c:/test/out -mask .* -waitInterval 30000 "
				+ "-includeSubFolders true -autoDelete false");
		System.out.println(",where\n	-inputDir             Input Directory (required),\r\n"
				+ "        -outputDir            Output Directory (required),\r\n"
				+ "	-mask                 Mask for files to be copied (optional, default - all files \".*\"),\r\n"
				+ "        -waitInterval         Interval with which the scanner scans the specified directory (optional, default - scans 1 time),\r\n"
				+ "        -includeSubFolders    Or may not include the processing of subdirectories (optional, default = true),\r\n"
				+ "        -autoDelete           Delete or not delete, the files after copying (optional, default = false),\n");
		
	}
	
	public void initHashMap() {
		
		availibleCommands.put(ID, null);
		availibleCommands.put(OD, null);
		availibleCommands.put(M, null);
		availibleCommands.put(WI, null);
		availibleCommands.put(ISF, null);
		availibleCommands.put(AD, null);
	
	}
	
	public void getThreadMessage( int threadID ) {
		
		threadINFO =  "\nThread ID ¹" + threadID 
				+ "\nParameters: \ninputDir: " + sourcePath +"\noutputDir: "
				+ targetPath + "\nmask: " + mask +"\nincludeSubFolders: " + subFolderFlag
				+ "\nautoDelete: " + moveFlag +"\nwaitInterval: " + waitInterval/(1000*60) +" min.\n";
		System.out.println(threadINFO);
		
	}
	
	public boolean setCommands(String[] parameters) {
		
		exitFlag = true;
		
		if (parameters.length < 5) {
			System.out.println("Not enough parameters!");
			exitFlag = false;
		}
		else {
			
			for(int i = 0; i < (parameters.length)/2; i++) {
				
				if(availibleCommands.containsKey(parameters[2*i+1])) {
					availibleCommands.put(parameters[2*i+1], parameters[2*i+2]);
				}
				else {
					System.out.println("ERROR: There is no such command: " + parameters[2*i+1]);
					exitFlag = false;
				}
			}
		}
		return exitFlag;
}
	
	public boolean checkWaitInterval() {
		exitFlag = true;
		if(availibleCommands.get(WI) == null) {
			System.out.println("WARNING: Setted default value for waitInterval - it will scan 1 time!");
		}
		else {
			try{
				waitInterval = Integer.parseInt(availibleCommands.get(WI));
				if(waitInterval < 0) {
					System.out.println("ERROR: waitInterval must be positive integer!");
					exitFlag = false;
				}
			}
			catch(NumberFormatException e) {
				System.out.println("ERROR: waitInterval must be positive integer!");
				exitFlag = false;
			}
		}
		return exitFlag;
	}
	
	public boolean checkIncludeSubFolders() {
		exitFlag = true;
		if(availibleCommands.get(ISF) == null)
			System.out.println("WARNING: Setted default value (true) for includeSubfolders!");
		else if(availibleCommands.get(ISF).equals("false"))
			subFolderFlag = false;	
		else if(!availibleCommands.get(ISF).equals("true")) {
			System.out.println("ERROR: Invalid value for includeSubfolders: " + 
				availibleCommands.get("-includeSubFolders") + " (must be true or false)");
			exitFlag = false;
		}
		return exitFlag;
	}
	
	public boolean checkMoveFlag() {
		exitFlag = true;
		if(availibleCommands.get(AD) == null) {
			System.out.println("WARNING: Setted default value (false) for autoDelete!");
		}
		else if(availibleCommands.get(AD).equals("true"))
			moveFlag = true;	
		else if(!availibleCommands.get(AD).equals("false")) {
			System.out.println("ERROR: Invalid value for autoDelete: " + 
				availibleCommands.get(AD) + " (must be true or false)");
			exitFlag = false;
		}
		return exitFlag;
	}
	
	public boolean checkMask() {
		exitFlag = true;
		if(availibleCommands.get(M) == null) {
			System.out.println("WARNING: No active mask.");
		}
		else if(availibleCommands.get(M).charAt(0) == '.')
			mask = availibleCommands.get(M);
		else {
			System.out.println("ERROR: Illegal symbols in mask: " + 
				availibleCommands.get(M) + " (mask must start with \".*\")");
			exitFlag = false;
		}
		return exitFlag;
	}
	
	public boolean checkInputDir() {
		
		exitFlag = true;
		
		if( availibleCommands.get(ID) != null ) {
			
			sourcePath = Paths.get(availibleCommands.get(ID));
			if(!Files.exists(sourcePath)) {
				System.out.println("ERROR: "+ sourcePath + " - there is no such dir!");
				exitFlag = false;
			}
		}
		else{
			System.out.println("ERROR: -inputDir is required parameter!");
			exitFlag = false;
		}

		return exitFlag;
	}
	
	public boolean checkOutputDir() throws IOException {
		
		exitFlag = true;
		
		if(availibleCommands.get(OD) != null) {
			targetPath = Paths.get(availibleCommands.get(OD));
			if (Files.exists(targetPath)) {exitFlag = isDirEqual(sourcePath, targetPath);}
			else if( (targetPath.getRoot() != null) && (Files.isDirectory(targetPath.getRoot())) ) {
				System.out.println("WARNING: There is no such directory on disk " + targetPath.getRoot()
						+ "\nIt will be created");
				Files.createDirectories(targetPath);
				exitFlag = isDirEqual(sourcePath, targetPath);
			}
			else {
				System.out.println("ERROR: There is no disk with such name: " + targetPath);
				exitFlag = false;
			}
			
		}
		
		else {
			System.out.println("ERROR: -outputDir is required parameter!");
			exitFlag = false;
		}
		return exitFlag;
	}
	
	private boolean isDirEqual(Path sourcePath, Path targetPath) throws IOException {
		if(Files.isSameFile(sourcePath, targetPath)) {
			System.out.println("ERROR: -inputDir and -outputDir must be different!");
			return false;
		}
		else return true;
	}

	
}
