import java.io.*;
import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;



public class TM 
{
	ITMModel tmModel = new TMModel();
	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
		
		
		new TM().appMain(args);
						
	}
	
	ITMModel TMModel;
	public TM()throws IOException
	{
		 TMModel = new TMModel();
	}
	
	public void Usage()
	{
		System.out.println("To begin usage enter one of the following.");
		System.out.println("TM Start, Stop, Describe, Summary, Size, Rename, or Delete, followed by Task Name, and Data.");
	}
	
	void appMain(String args[]) throws IOException
	{
		String cmd = args[0];
		
		Log log = new Log();
		
		
		LocalDateTime timeN = LocalDateTime.now(); 
		
		
		if(args.length==0) {
			Usage();
			return;
		}
		
		switch(cmd) 
		{
			case "start":
				TMModel.startTask(args[1]);
				break;
			case "stop":
				TMModel.stopTask(args[1]);
				break;
			case "describe":
				if (args.length == 3) {
					tmModel.describeTask(args[1], args[2]);
				}
				if (args.length == 4) {
					tmModel.sizeTask(args[1], args[3]);
				}
				break;
			case "summary":
				if(args.length==1)
					summaryFull();
				else
					summary(args[1]);
				break;
			case "size":
				TMModel.sizeTask(args[1],args[2]);
				break;
			case "delete":
				TMModel.deleteTask(args[1]);
				break;
			case "rename":
				TMModel.renameTask(args[1],args[2]);
				break;
			default:
				Usage();
			
				
		}
		return;
	}
	

	public void summaryFull() {
		Set<String> tasks = tmModel.taskNames();
		Set<String> Sizes = tmModel.taskSizes();
		for (String individual : tasks) {
			summary( individual );
		}

		for (String single : Sizes) {
			Set<String> strset = tmModel.taskNamesForSize(single);

			if (strset.size() >= 1) {
				System.out.println("Size: " + single + " Tasks: " + strset + "\n Min Time: " + tmModel.minTimeForSize(single)
								   + "\n Avg Time: " + tmModel.avgTimeForSize(single)
								   + "\n Max Time: " + tmModel.maxTimeForSize(single) + "\n");
			}
		}

	}


	public void summary ( String name ) {
		String size = tmModel.taskSize(name);
		String description = tmModel.taskDescription(name);
		String timeOverall = tmModel.taskElapsedTime(name);

		System.out.println("\n");
		System.out.println("\n Task: " + name + "\n Task Description: " + description
						   + "\n Task Size: " + size
						   + "\n Overall Time: " + timeOverall );
		System.out.println("\n");
	}
	
}	
	