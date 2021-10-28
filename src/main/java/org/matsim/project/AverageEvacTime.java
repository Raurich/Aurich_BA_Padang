package org.matsim.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class AverageEvacTime{
	
	public static void main(String[] args) throws IOException {
		
	String nameOfSimulation = "2021-10-13-8-23-43_decongestion_queue";
	
	String pathToMainFolder = "C:/Users/Robin/git/Padang_v2/output/"+nameOfSimulation+"/averageEvacTimes.txt";	
		
	//File myObj = new File(pathToMainFolder);
	FileWriter myWriter = new FileWriter(pathToMainFolder);
	
	myWriter.write("iterationCount"+"	"+"AverageEvacTime"+"\n");
	
	
	for(int iteration=0;iteration<1001;iteration++) {
		
		String iterationCounter = Integer.toString(iteration);
		String pathToLegDuration = "C:/Users/Robin/git/Padang_v2/output/"+nameOfSimulation+"/ITERS/it."+iterationCounter+"/"+iterationCounter+".legdurations.txt";
		
		String output = readLineByLineJava8(pathToLegDuration);
		
		String finalOutput = output.substring(23, 29);
		
		double valueX = Double.parseDouble(finalOutput);
		
		myWriter.write(iteration+"				"+finalOutput+"\n");
		
		//System.out.println(valueX);
	}
	
	myWriter.close();
	
	}
	
	private static String readLineByLineJava8(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        String[] lines = contentBuilder.toString().split("\\n");
        
        return lines[3];
        //return contentBuilder.toString();
    }
}
