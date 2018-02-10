package com.neemshade.sniper.filemetrics.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neemshade.sniper.filemetrics.FileMetrics;
import com.neemshade.sniper.filemetrics.FileMetricsResult;

public class FileMetricsTester {

	final public static String PathOfAction = "F:\\jbalaji\\personal\\neemShade\\software\\sniper\\files";
//	final public static String PathOfAction = "F:\\jbalaji\\personal\\neemShade\\software\\sniper\\files\\sub";
	

	private void startTesting() {
		FileMetrics fileMetrics = new FileMetrics();
		
		List<File> fileList = gatherFileNames();
		
		List<File> unsupportedFileList = new ArrayList<File>();
		
		Integer totalFiles = 0;
		Integer successfulProcessing = 0;
		Integer failedProcessing = 0;
		Integer unsupportedFiles = 0;
		
		for(File file : fileList)
		{
			if(file == null || file.getName().startsWith("~")) continue;
			
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
			
			if(extension == null || extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")) continue;
			if(extension.equalsIgnoreCase("zip")) continue;
			
			totalFiles++;
			
			if(!fileMetrics.isSupported(extension))
			{
				unsupportedFileList.add(file);
				unsupportedFiles++;
				continue;
			}
			
			try {
				FileMetricsResult fmr = fileMetrics.calculateMetrics(file, extension);
				System.out.println(
						String.format("|%4s", (fmr.getAudioDuration() == null ? "" : fmr.getAudioDuration())) +
						String.format("|%4s", (fmr.getWsLineCount() == null ? "" :  fmr.getWsLineCount())) +
						String.format("|%4s| ", (fmr.getWosLineCount() == null ? "" :  fmr.getWosLineCount())) +
						file.getName()
						);
				successfulProcessing++;
			} catch (Exception e) {
				failedProcessing++;
				System.out.println("error! " + e.getMessage() + " " + file.getName());
//				e.printStackTrace();
			}
		}
		
		System.out.println("");
		System.out.println("******************");
		System.out.println(String.format("%30s", "Total files ") + totalFiles);
		System.out.println(String.format("%30s", "unsupported files ") + unsupportedFiles);
		System.out.println(String.format("%30s", "Successful processing ") + successfulProcessing);
		System.out.println(String.format("%30s", "unsuccessful processing ") + failedProcessing);
		
		System.out.println("\nUnsupported file formats :::");
		for(File unsupportedFile : unsupportedFileList)
		{
			System.out.println(unsupportedFile.getName());
		}
	}
	
	
	public List<File> gatherFileNames()
	{
		try {
			System.out.println("path = " + PathOfAction);
			List<File> filesInFolder = Files.walk(Paths.get(PathOfAction))
			        .filter(Files::isRegularFile)
			        .map(Path::toFile)
			        .collect(Collectors.toList());
			
			return filesInFolder;
		} catch (IOException e) {
			System.err.println("Error in collecting files");
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	public static void main(String[] args) {
		FileMetricsTester fmt = new FileMetricsTester();
		fmt.startTesting();
	}


}
