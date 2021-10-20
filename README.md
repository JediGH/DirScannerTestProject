# DirScannerTestProject
Program scan the specified directory, and copies the files that meet a certain condition in the output directory. Additionally you could create up to 4 threads.

Availible commands:

		scan;

		help;

		threadStop	"ThreadID";

		activeThreads;

		exit;
 
	Example:
	
        scan -inputDir c:/test/in -outputDir c:/test/out -mask .* -waitInterval 30000 -includeSubFolders true -autoDelete false
