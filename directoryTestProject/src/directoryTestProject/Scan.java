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
	
	
	public static Scanner scanner = new Scanner(System.in); // Почему нельзя использовать в методе getDirPath
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
	
	// Метод указывает была ли выбранная дирректория пуста, если была ее требуется удалить из копируемой дирректории
	private static void wasDirEmpty(boolean f, int counter, int skippedFilesCounter, int emptyDirCounter) { 
		
		if (f || (counter-(emptyDirCounter+skippedFilesCounter) == 0)) wasEmptyFlag = true;
		else wasEmptyFlag = false;
		
	}
	
	
	private static void isDirEmpty(int filesCounter, int movedFilesCounter) {
		
		if ((filesCounter - movedFilesCounter == 0) && (!wasEmptyFlag)) {emptyDirFlag = true; //System.out.println("Папка пуста!");
		}
		else if((filesCounter - movedFilesCounter == 0) && (wasEmptyFlag)) {emptyDirFlag = false; //System.out.println("Папка была пуста!");
		}
		else {emptyDirFlag = false;//	System.out.println("Папка содержит файлы!");
		}
		
	}

	
	
	public static void scanDir(Path sourcePath, Path targetPath, String mask) throws IOException {
		
		int skippedFilesCounter = 0;		//счетчик пропущенных файлов
		int filesCounter = 0;					// Общий счетчик файлов
		int emptyDirCounter = 0;			// Считчик количества пустых папок
		boolean local_wasEmptyFlag = true;  // Локальный флаг определяющий содержит ли выбранный каталог хоть какие нибудь файлы/папки
		
		//System.out.println("Standart scanDir");
		String filesToCopy =  mask;
		
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(sourcePath)){
			
			for (Path file: entries) {
				
				local_wasEmptyFlag = false;
				filesCounter++;
				
				//System.out.println("Расматриваемый файл: " + file + " является папкой: " + Files.isDirectory(file));
				
				if(Files.isDirectory(file)) {
					dirExistedFlag = false;
					// если папка существует ее не требуется создавать
					if( Files.exists(targetPath.resolve(file.getFileName()))) {
					
				//		System.out.println("Папка "+ file.getFileName() + " существовала в: " + 
				//				targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						dirExistedFlag = true; 
					}
					
					else {
						Files.copy(file,  targetPath.resolve(file.getFileName()));
				//		System.out.println("Папка "+ file.getFileName() + " успешно скопирован в: " + 
				//			targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
					}
					
					scanDir(file, targetPath.resolve(file.getFileName()), mask);
					
					if(wasEmptyFlag && !dirExistedFlag) {
						
						emptyDirCounter++;
						Files.delete(targetPath.resolve(file.getFileName()));
					//	System.out.println("Пустая папка "+ targetPath.resolve(file.getFileName()) + " не нужна и будет удалена ");
					
					}
					

				}
				//Копирование файлов, удовлетворяющих формату	
				else if( (file.getFileName().toString().endsWith(filesToCopy)) || filesToCopy.equals(".*") ){
					// Копирование файлов
					Files.copy(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					//System.out.println("Файл "+ file.getFileName() + " успешно скопирован в: " + 
					//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
				
				}
				
				else skippedFilesCounter++;		// Общий счетчик файлов уменьшается в том случае, если файл не попадает под указанные критерии
				
				//System.out.println();
			}
			
			//System.out.println("файлов в каталоге:" +sourcePath+ " = "+ filesCounter);
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
		int filesCounter = 0;					// Общий счетчик файлов
		int movedFilesCounter = 0;
		int emptyDirCounter = 0;			// Считчик количества пустых папок
		boolean local_wasEmptyFlag = true;  // Локальный флаг определяющий содержит ли выбранный каталог хоть какие нибудь файлы/папки
		
		//System.out.println("Move scanDir");
		String filesToCopy = mask;
		
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(sourcePath)){
			
			for (Path file: entries) {
				
				local_wasEmptyFlag = false;
				filesCounter++;
				
			//	System.out.println("Расматриваемый файл: " + file + " является папкой: " + Files.isDirectory(file));
				
				if(Files.isDirectory(file)) {
					dirExistedFlag = false;
					// если папка существует ее не требуется создавать
					if( Files.exists(targetPath.resolve(file.getFileName()))) {
					
					//	System.out.println("Папка "+ file.getFileName() + " существовала в: " + 
					//			targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						dirExistedFlag = true;
					}
					
					else {
						Files.copy(file,  targetPath.resolve(file.getFileName()));
						if (moveFlag == false) {
						//	System.out.println("Папка "+ file.getFileName() + " успешно скопирован в: " + 
						//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						}
						else {
						//	System.out.println("Папка "+ file.getFileName() + " успешно перемещена в: " + 
						//		targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
						}
					}
					
					scanDir(file, targetPath.resolve(file.getFileName()), moveFlag, mask);
					
					if(wasEmptyFlag && !dirExistedFlag) {	// Удаление пустой папки
						
						emptyDirCounter++;
						Files.delete(targetPath.resolve(file.getFileName()));
						//System.out.println("Пустая папка "+ targetPath.resolve(file.getFileName()) + " не нужна и будет удалена ");
					
					}
					
					if(moveFlag && emptyDirFlag) { // Удаление исходной папки, если она стала пуста
						
						//System.out.println("Исходная папка "+ file.getFileName() + " успешно удален ");
						Files.delete(file);
						movedFilesCounter++;
						
					}
	
				}
				//Копирование файлов, удовлетворяющих формату	
				else if( (file.getFileName().toString().endsWith(filesToCopy)) || filesToCopy.equals(".*") ){
					Files.move(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					movedFilesCounter++;
				}
				
				else skippedFilesCounter++;		// Общий счетчик файлов уменьшается в том случае, если файл не попадает под указанные критерии
				
			//	System.out.println();
			}
			
			//System.out.println("файлов в каталоге:" +sourcePath+ " = "+ filesCounter);
			
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
				
				//System.out.println("Расматриваемый файл: " + file + " является папкой: " + Files.isDirectory(file));
				
				if(Files.isDirectory(file)) {
					
					scanDir(file, targetPath, moveFlag, subFolder, mask);
					
					if(wasEmptyFlag)
						emptyDirCounter++;

					if(moveFlag && emptyDirFlag) {
						
					//	System.out.println("Исходная папка "+ file.getFileName() + " успешно удален ");
						Files.delete(file);
						movedFilesCounter++;
						
					}
				}
				
				else if( (file.getFileName().toString().endsWith(filesToCopy)) || filesToCopy.equals(".*") ){
					if( moveFlag == false) {
						Files.copy(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					//	System.out.println("Файл "+ file.getFileName() + " успешно скопирован в: " + 
					//			targetPath.resolve(file.getFileName()).toAbsolutePath().normalize());
					}
					else {
						Files.move(file,  targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					//	System.out.println("Файл "+ file.getFileName() + " успешно перемещен в: " + 
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
