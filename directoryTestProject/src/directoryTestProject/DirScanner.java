package directoryTestProject;


import java.io.IOException;


public class DirScanner {
	
	//final static Logger logger =  Logger.getLogger(DirScanner.class);
	
	public static void main(String[] args) throws IOException {
		
		boolean exitFlag;
		String stringToSplit;
		ScanConfig scanObject;
				
		ScanConfig.helpMessage();
		
		while(true)	{
	
			do{
				
				scanObject = new ScanConfig();
				
				exitFlag = true;
				stringToSplit = Scan.getDirPath();		// В этом классе ему не место
				String[] parameters = stringToSplit.split(" ");
				
				String command = parameters[0].toLowerCase();
				
				switch(command) {
				
				case "scan":
					
					scanObject.initHashMap(); // Обнуляем прошлые значения HM
					exitFlag = scanObject.setCommands(parameters);
				
					if(exitFlag) {
	
						if(scanObject.checkIncludeSubFolders() && scanObject.checkMoveFlag() 
								&& scanObject.checkMask() && scanObject.checkWaitInterval()
								&& scanObject.checkInputDir() && scanObject.checkOutputDir()) {}
						else exitFlag = false;
				
					}
					break;
					
				case "activethreads":
					ThreadInit.getActiveThreads();
					exitFlag = false;
					break;
					
				case "threadstop":
					ThreadInit.stopThread(parameters);
					exitFlag = false;
					break;
					
				case "help":
					ScanConfig.helpMessage();
					exitFlag = false;
					break;
					
				case "exit":
					System.exit(0);
					break;
				
				default:
					exitFlag = false;
					System.out.println("There is no such command: " + parameters[0]+" !");
					
				}
				
			}while(!exitFlag);
			
	
			ThreadInit.initThread(scanObject);
	
		}	
	}
}