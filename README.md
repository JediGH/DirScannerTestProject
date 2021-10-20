# DirScannerTestProject
Program scan the specified directory, and copies the files that meet a certain condition in the output directory. Additionally you could create up to 4 threads
Availible commands:
	scan
	help
	threadStop	"ThreadID"
 	activeThreads
 	exit
Please enter necessary parameters, like in the followin example:
scan -inputDir c:/test/in -outputDir c:/test/out -mask .* -waitInterval 30000 -includeSubFolders true -autoDelete false
,where
	      -inputDir             Input Directory (required),
        -outputDir            Output Directory (required),
	      -mask                 Mask for files to be copied (optional, default - all files ".*"),
        -waitInterval         Interval with which the scanner scans the specified directory (optional, default - scans 1 time),
        -includeSubFolders    Or may not include the processing of subdirectories (optional, default = true),
        -autoDelete           Delete or not delete, the files after copying (optional, default = false),
