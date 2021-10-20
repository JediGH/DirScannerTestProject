package directoryTestProject;

import java.io.IOException;
import java.nio.file.Path;

public class ScanRunnable implements Runnable{
	
	private boolean stopFlag;
	private boolean subFolderFlag;
	private boolean moveFlag;
	private Path sourcePath;
	private Path targetPath;
	private String mask;
	private String threadINFO;
	private double waitInterval;
	
	public ScanRunnable(boolean aSubFolderFlag, boolean aMoveFlag, 
			Path aSourcePath, Path aTargetPath, String aMask, double aWaitInterval, String aThreadINFO){
		
		this.stopFlag = false;
		this.threadINFO = aThreadINFO;
		this.subFolderFlag = aSubFolderFlag;
		this.moveFlag = aMoveFlag;
		this.sourcePath = aSourcePath;
		this.targetPath = aTargetPath;
		this.mask = aMask;
		this.waitInterval = aWaitInterval;
		
	}
	
	public void getInfo() {
		
		System.out.println(threadINFO);
		
	}
	
	public void stopThread() {
		
		stopFlag = true;
		
	}
	
	@Override
	public void run() {
		try {
			
			do {			
					if(!moveFlag && subFolderFlag)
						Scan.scanDir(sourcePath, targetPath, mask);	
					else if(moveFlag && subFolderFlag) 
						Scan.scanDir(sourcePath, targetPath, moveFlag, mask);
					else
						Scan.scanDir(sourcePath, targetPath, moveFlag, subFolderFlag, mask);
					try {
						if((int) waitInterval == 0)
							return;
						else Thread.sleep((int) waitInterval);
					}
					catch(InterruptedException e) {}
				
				if(stopFlag) return;
				
			}while(true);
			
		}catch(IOException e) {}
		
	}

}
