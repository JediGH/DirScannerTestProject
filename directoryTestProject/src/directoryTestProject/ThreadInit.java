package directoryTestProject;

import java.util.HashMap;

public class ThreadInit {
	
	private static int threadID = 0;
	private static byte activeThreads = 0;
	
	private static HashMap<Integer, ScanRunnable> threadMap = new HashMap<Integer, ScanRunnable>();
	
	public static void initThread(ScanConfig scanObject) {
		
		threadID++;
	
		if (activeThreads <= 3) {
			

			scanObject.getThreadMessage(threadID);
			var threadRunnable = new ScanRunnable(scanObject.subFolderFlag, scanObject.moveFlag, scanObject.sourcePath, 
					scanObject.targetPath, scanObject.mask, scanObject.waitInterval, scanObject.threadINFO);
			
			new Thread(threadRunnable).start();
			if((int)scanObject.waitInterval == 0)
				threadRunnable.stopThread();
			else threadMap.put(threadID, threadRunnable);
							
		}
		
		else System.out.println("You reached maximum of active threads. Please stop one existed to create new!\n");
		
	}
		
	public static void getActiveThreads() {
		if (threadMap.isEmpty())
			System.out.println("There is no active threads");
		else {
			System.out.println("Active threads info:\n");
			threadMap.forEach( (key, value) -> { value.getInfo(); } 
					);
		}	
	}
	
	public static void stopThread(String[] parameters) {
		
		if(parameters.length < 2) 
			System.out.println("Not enough parameters for threadStop command\nPlease enter existed ThreadID.");
			
		else { 
			try{
				var tID = Integer.parseInt(parameters[1]);
				if(threadMap.containsKey(tID)) {
					
					threadMap.get(tID).stopThread();
					
					threadMap.remove(tID, threadMap.get(tID));
				//	logger.info("Thread ¹" + tID + " stoped!");
					System.out.println("Thread ¹" + tID + " stoped!");
																
					activeThreads--;
				}
				else System.out.println("There is no thread with such ID: " + tID);
				
			}
			catch(NumberFormatException e) {
				System.out.println("ThreadID must be positive itneger!");
			}
		}
	}
	
}
