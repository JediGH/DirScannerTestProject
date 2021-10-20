package directoryTestProject;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class Scan {
	
	
	public static Scanner scanner = new Scanner(System.in); // ������ ������ ������������ � ������ getDirPath
	public static String path;
	public static boolean dirExistedFlag;
	public static boolean emptyDirFlag;
	public static boolean wasEmptyFlag;
	
	public Scan() {}
	
	public static String getDirPath() {
		
		System.out.print(">");
		try {
			path = scanner.nextLine();
		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}
		return path;
	
	}
	
	// ����� ��������� ���� �� ��������� ����������� �����, ���� ���� �� ��������� ������� �� ���������� �����������
	private static void wasDirEmpty(boolean f, int counter, int skippedFilesCounter, int emptyDirCounter) { 
		
		if (f || (counter-(emptyDirCounter+skippedFilesCounter) == 0)) wasEmptyFlag = true;
		else wasEmptyFlag = false;
		
	}
	
	
	private static void isDirEmpty(int filesCounter, int movedFilesCounter) {
		
		if ((filesCounter - movedFilesCounter == 0) && (!wasEmptyFlag)) {emptyDirFlag = true; //System.out.println("����� �����!");
		}
		else if((filesCounter - movedFilesCounter == 0) && (wasEmptyFlag)) {emptyDirFlag = false; //System.out.println("����� ���� �����!");
		}
		else {emptyDirFlag = false;//	System.out.println("����� �������� �����!");
		}
		
	}

	
	
	public static void scanDir(Path sourcePath, Path targetPath, String mask) throws IOException {
		
		int skippedFilesCounter = 0;		//������� ����������� ������
		int filesCounter = 0;					// ����� ������� ������
		int emptyDirCounter = 0;			// ������� ���������� ������ �����
		boolean local_wasEmptyFlag = true;  // ��������� ���� ������������ �������� �� ��������� ������� ���� ����� ������ �����/�����
		
		//System.out.println("Standart scanDir");
		String filesToCopy =  mask;
		
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(sourcePath)){
			
			for (Path file: entries) {
				
				local_wasEmptyFlag = false;
				filesCounter++;
				
				//System.out.println("�������������� ����: " + file + " �������� ������: " + Files.isDirectory(file));
				
				if(Files.isDirectory(file)) {
					dirExistedFlag = false;
					// ���� ����� ���������� �� �� ��������� ���������
					if( Files.exists(targetPath.resolve(file.getFileName()))) {
					
				//		System.out.println("����� "+ file.getFileName() + " ������������ �: " + 
				//				targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						dirExistedFlag = true; 
					}
					
					else {
						Files.copy(file,  targetPath.resolve(file.getFileName()));
				//		System.out.println("����� "+ file.getFileName() + " ������� ���������� �: " + 
				//			targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
					}
					
					scanDir(file, targetPath.resolve(file.getFileName()), mask);
					
					if(wasEmptyFlag && !dirExistedFlag) {
						
						emptyDirCounter++;
						Files.delete(targetPath.resolve(file.getFileName()));
					//	System.out.println("������ ����� "+ targetPath.resolve(file.getFileName()) + " �� ����� � ����� ������� ");
					
					}
					

				}
				//����������� ������, ��������������� �������	
				else if( (file.getFileName().toString().endsWith(filesToCopy)) || filesToCopy.equals(".*") ){
					// ����������� ������
					Files.copy(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					//System.out.println("���� "+ file.getFileName() + " ������� ���������� �: " + 
					//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
				
				}
				
				else skippedFilesCounter++;		// ����� ������� ������ ����������� � ��� ������, ���� ���� �� �������� ��� ��������� ��������
				
				//System.out.println();
			}
			
			//System.out.println("������ � ��������:" +sourcePath+ " = "+ filesCounter);
			wasDirEmpty(local_wasEmptyFlag, filesCounter, skippedFilesCounter, emptyDirCounter);
			
		} 
		//catch (IOException e) {e.printStackTrace();}
		catch(AccessDeniedException e) {
			System.out.println("ERROR: Access to file: " + sourcePath.getFileName() + "denied!");
			skippedFilesCounter++;
			wasDirEmpty(local_wasEmptyFlag, filesCounter, skippedFilesCounter, emptyDirCounter);
		}
	}
	
	public static void scanDir(Path sourcePath, Path targetPath, boolean moveFlag, String mask) throws IOException {
		
		int skippedFilesCounter = 0;
		int filesCounter = 0;					// ����� ������� ������
		int movedFilesCounter = 0;
		int emptyDirCounter = 0;			// ������� ���������� ������ �����
		boolean local_wasEmptyFlag = true;  // ��������� ���� ������������ �������� �� ��������� ������� ���� ����� ������ �����/�����
		
		//System.out.println("Move scanDir");
		String filesToCopy = mask;
		
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(sourcePath)){
			
			for (Path file: entries) {
				
				local_wasEmptyFlag = false;
				filesCounter++;
				
			//	System.out.println("�������������� ����: " + file + " �������� ������: " + Files.isDirectory(file));
				
				if(Files.isDirectory(file)) {
					dirExistedFlag = false;
					// ���� ����� ���������� �� �� ��������� ���������
					if( Files.exists(targetPath.resolve(file.getFileName()))) {
					
					//	System.out.println("����� "+ file.getFileName() + " ������������ �: " + 
					//			targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						dirExistedFlag = true;
					}
					
					else {
						Files.copy(file,  targetPath.resolve(file.getFileName()));
						if (moveFlag == false) {
						//	System.out.println("����� "+ file.getFileName() + " ������� ���������� �: " + 
						//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						}
						else {
						//	System.out.println("����� "+ file.getFileName() + " ������� ���������� �: " + 
						//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						}
					}
					
					scanDir(file, targetPath.resolve(file.getFileName()), moveFlag, mask);
					
					if(wasEmptyFlag && !dirExistedFlag) {	// �������� ������ �����
						
						emptyDirCounter++;
						Files.delete(targetPath.resolve(file.getFileName()));
						//System.out.println("������ ����� "+ targetPath.resolve(file.getFileName()) + " �� ����� � ����� ������� ");
					
					}
					
					if(moveFlag && emptyDirFlag) { // �������� �������� �����, ���� ��� ����� �����
						
						//System.out.println("�������� ����� "+ file.getFileName() + " ������� ������ ");
						Files.delete(file);
						movedFilesCounter++;
						
					}
	
				}
				//����������� ������, ��������������� �������	
				else if( (file.getFileName().toString().endsWith(filesToCopy)) || filesToCopy.equals(".*") ){
					Files.move(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					movedFilesCounter++;
				}
				
				else skippedFilesCounter++;		// ����� ������� ������ ����������� � ��� ������, ���� ���� �� �������� ��� ��������� ��������
				
			//	System.out.println();
			}
			
			//System.out.println("������ � ��������:" +sourcePath+ " = "+ filesCounter);
			
			wasDirEmpty(local_wasEmptyFlag, filesCounter, skippedFilesCounter, emptyDirCounter);
			isDirEmpty(filesCounter, movedFilesCounter);
			
		} 
		catch(AccessDeniedException e) {
			System.out.println("ERROR: Access to file in: " + sourcePath.getFileName() + "!");
			skippedFilesCounter++;
			wasDirEmpty(local_wasEmptyFlag, filesCounter, skippedFilesCounter, emptyDirCounter);
			isDirEmpty(filesCounter, movedFilesCounter);
		}
	}

	public static void scanDir(Path sourcePath, Path targetPath, boolean moveFlag, boolean subFolder, String mask) throws IOException {
		
		int skippedFilesCounter = 0;
		int filesCounter = 0;					
		int movedFilesCounter = 0;
		int emptyDirCounter = 0;			
		boolean local_wasEmptyFlag = true;  
		
	//	System.out.println("Standart scanDirSubFolder");
		String filesToCopy = mask;
		
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(sourcePath)){
			
			for (Path file: entries) {
				
				local_wasEmptyFlag = false;
				filesCounter++;
				
				//System.out.println("�������������� ����: " + file + " �������� ������: " + Files.isDirectory(file));
				
				if(Files.isDirectory(file)) {
					
					scanDir(file, targetPath, moveFlag, subFolder, mask);
					
					if(wasEmptyFlag)
						emptyDirCounter++;

					if(moveFlag && emptyDirFlag) {
						
					//	System.out.println("�������� ����� "+ file.getFileName() + " ������� ������ ");
						Files.delete(file);
						movedFilesCounter++;
						
					}
				}
				
				else if( (file.getFileName().toString().endsWith(filesToCopy)) || filesToCopy.equals(".*") ){
					if( moveFlag == false) {
						Files.copy(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					//	System.out.println("���� "+ file.getFileName() + " ������� ���������� �: " + 
					//			targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
					}
					else {
						Files.move(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					//	System.out.println("���� "+ file.getFileName() + " ������� ��������� �: " + 
					//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						movedFilesCounter++;
					}
				}
				else skippedFilesCounter++;
				
			}
			
			wasDirEmpty(local_wasEmptyFlag, filesCounter, skippedFilesCounter, emptyDirCounter);
			isDirEmpty(filesCounter, movedFilesCounter);
			
		} 
		catch(AccessDeniedException e) {
			System.out.println("ERROR: Access to file in: " + sourcePath.getFileName() + "!");
			skippedFilesCounter++;
			wasDirEmpty(local_wasEmptyFlag, filesCounter, skippedFilesCounter, emptyDirCounter);
			isDirEmpty(filesCounter, movedFilesCounter);
		}
	}
	
}
