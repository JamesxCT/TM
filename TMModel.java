import java.io.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

class TaskLogEntry{
	
	String cmd;
	String data;
	String description;
	String size;
	LocalDateTime timeN;
}

public class TMModel implements ITMModel
{
	
		private TreeSet<String> nombres;
		private Log log;
		private LocalDateTime timeN;
		private LinkedList<TaskLogEntry> LineL;
		private ArrayList<String> AL;
		private TreeSet<String> variations;
		private long TotalTime;
		public String Size = "";
		
		
		public TMModel() {
			try {
				log = new Log();
				TotalTime = 0;
				timeN = LocalDateTime.now();
				variations = new TreeSet<String>();
				nombres = new TreeSet<String>();
				LineL = log.readFile();
				for(TaskLogEntry enterohero : LineL) {
					nombres.add(enterohero.data);
					if (enterohero.cmd.equals("SIZE"))
						variations.add(enterohero.description);
				}
			}
			catch (IOException err) {
				System.err.println("ERROR: Log File is unreadable");
			}
			catch (NullPointerException err) {
				System.err.println("ERROR: Communication between the application and the log file is faulty");
			}
		}





		void cmdSummary(Log log) throws IOException{


			Scanner mine = null;

			File myfile = new File("TM.log");

			mine = new Scanner(myfile);

			while(mine.hasNext()) {


				String lineOfFile = mine.nextLine();

				if(!(lineOfFile.contains("null"))) {

					System.out.println(lineOfFile);
				}
			}

			log.readFile();

		}

	 
		long cmdSummary(String todo, Log log) throws IOException {

			LinkedList<TaskLogEntry> lines = log.readFile();
			
			Task sumTask = new Task(todo, lines);
			
			System.out.println(sumTask.toString());
			
			return sumTask.totTime;
			
		}
		
		
	
	public boolean startTask(String name)
	{
				try {
						log.writeLine(timeN + "_" + name + "_start");
					} catch (Exception err) {
						System.err.println("ERROR: Could not write to log file\n");
						return false;
					}
					return true;
	}
    public boolean stopTask(String name)
    {
    			try {
    					log.writeLine(timeN + "_" + name + "_stop");
    				} catch (Exception err) {
    					System.err.println("ERROR: Could not write to log file\n");
    					return false;
    				}
    				return true;
    }
    public boolean describeTask(String name, String description)
    {
    		try {
    					log.writeLine(timeN + "_" + name + "_describe_" + description);
    				} catch (Exception err) {
    					System.err.println("ERROR: Could not write to log file\n");
    					return false;
    				}
    				return true;    	
    	
    }
    public boolean sizeTask(String name, String size)
    {
    	try {
    					log.writeLine(timeN + "_" + name + "_size_" + size);
    				} catch (Exception err) {
    					System.err.println("ERROR: Could not write to log file\n");
    					return false;
    				}
    				return true;

    }
    public boolean deleteTask(String name)
    {
		List<String> newLines = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(Paths.get("TM.log"), StandardCharsets.UTF_8)) {
			    if (line.contains("_"+ name + "_")) {
			       newLines.add(line.replace("_"+ name + "_", "_#Recently Deleted#_"));
			    } else {
			       newLines.add(line);
			    }
			}
			Files.write(Paths.get("TM.log"), newLines, StandardCharsets.UTF_8);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    }
    public boolean renameTask(String oldName, String newName)
    {
		ArrayList<String> LineList = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(Paths.get("TM.log"), StandardCharsets.UTF_8)) {
				if(line.contains("_" + oldName + "_"))
					LineList.add(line.replace("_" + oldName + "_", "_" + newName + "_"));
				else
					LineList.add(line);
			}
			Files.write(Paths.get("TM.log"), LineList, StandardCharsets.UTF_8);
		} catch (Exception err) {
			System.err.println("ERROR: Problem Occurred During Task Renaming Method");
			return false;
		}
		return true;
    }

    // return information about our tasks
    //
    public String taskElapsedTime(String name)
    {
    	Task task = new Task(name, LineL);
    			return task.totTime();
    }
    public String taskSize(String name)
    {
    	Task task = new Task(name, LineL);
    		return task.SSize();
    }
    public String taskDescription(String name)
    {
    	Task task = new Task(name, LineL);
    			return task.descript();
    }

    // return information about some tasks
    
    
    public String minTimeForSize(String size)
    {
		long minimumT = Long.MAX_VALUE;
		for (String tasks : nombres ) {
			Task task = new Task(tasks, LineL);
			if (task.SS.equals(size)) {
				if (task.totTime < minimumT) {
					minimumT = task.totTime;
				}
			}
		}
		return TimeUtil.toElapsedTime(minimumT);	
    }
    public String maxTimeForSize(String size)
    {
		long maximumTime = 0;
		for (String nombre : nombres) {
			Task task = new Task(nombre, LineL);
			if (task.SS.equals(size) && task.totTime > maximumTime)
				maximumTime = task.totTime;
		}
		return TimeUtil.toElapsedTime(maximumTime);
    }
    public String avgTimeForSize(String size)
    {
		long totalT = 0;
		int i = 0;
		for ( String nombre : nombres ) {
			Task task = new Task(nombre, LineL);
			if (task.SS.equals(size)) {
				totalT = totalT + task.totTime;
				i++;
			}
		}
		totalT = totalT/i;
		return TimeUtil.toElapsedTime(totalT);	
    }

    public Set<String> taskNamesForSize(String size)
    {
		Set<String> taskNamesForSize = new TreeSet<String>();
		for (String iTask : nombres) {
			Task task = new Task(iTask, LineL);
			if(task.SS.equals(size)) {
				taskNamesForSize.add(iTask);
			}
		}
		return taskNamesForSize;	
    }

    // return information about all tasks
    //
    public String elapsedTimeForAllTasks()
    {
		long totalTime = 0;
		for (String tasks : taskNames() ) {
			Task task = new Task(tasks, LineL);
			totalTime += task.totTime;
		}
		return TimeUtil.toElapsedTime(totalTime);	
    }
    public Set<String> taskNames()
    {
    	return nombres;
    }
    public Set<String> taskSizes()
    {
    	return variations;
    }
}


	class TimeUtil {
		 


		static String toElapsedTime(long totSecs) {

			long hours = totSecs/3600;
			long mins = (totSecs % 3600) / 60;
			long secs = (totSecs % 60);

			String time = (String.format("%02d:%02d:%02d", hours, mins, secs));
			return time;

		}

	}

	class Task {

		private String name = "";
		private StringBuilder description = new StringBuilder("");
		private String timeNow = "";
		public long totTime = 0;
		public String SS;

		public Task(String name, LinkedList<TaskLogEntry> entries) {
			this.name = name;
			LocalDateTime lastStart = null;
			long timeOverall = 0;
			for(TaskLogEntry entry : entries) {
				if(entry.data.equals(name)) {
					switch(entry.cmd) {
						case "start":
							lastStart = entry.timeN;
							break;
						case "stop":
							if(lastStart != null) {
								timeOverall += taskDuration(lastStart, entry.timeN);
							}
							lastStart = null;
							break;
						case "describe":

							if (description.toString().equals(""))
								description.append(" " + entry.description);
							else
								description.append("\n" + entry.description);
							if (entry.size != null)
								SS = entry.size;
							break;
						case "size":
							SS = entry.description;
								
					}
				}
			}
			this.timeNow = TimeUtil.toElapsedTime(timeOverall);
			this.totTime = timeOverall;
		}


		public String toString() {
			String stringbean = ("\nSummary for Task: " + this.name + "\nDescription for Task: " + this.description + "\nDuration for Task: " + this.timeNow );
			return stringbean;
		}

		long taskDuration(LocalDateTime start, LocalDateTime stop){
				long dur = ChronoUnit.SECONDS.between(start, stop);
				return dur;

		}

		public String totTime() {
			return this.timeNow;
		}

		public String SSize() {
			return this.SS;
		}

		public String descript() {
			return this.description.toString();
		}

	}





class Log{
	 
 	String type;
 	String name;
 	String input;
 
 	
 	public FileWriter fw;
 
 
 	public PrintWriter pw; 
 
 	File myFile = new File("TM.log");
 

 	public Log() throws IOException {
 		if(!(myFile.exists())) {
 			myFile.createNewFile();
 		}
 	}
 
 
 	void writeLine(String line) throws IOException{
 
 		fw = new FileWriter(myFile, true);
 		fw.append(line);
 		fw.write("\n");
 		fw.close();
 			
 		
 	}
 	

 
 	LinkedList<TaskLogEntry> readFile() throws IOException {
 
		int stringTokCount = 0;
 		LinkedList<TaskLogEntry> LineL = new LinkedList<TaskLogEntry>();
 			
 		File logF = new File("TM.log");
 		Scanner file = new Scanner(logF);
 			
 		String thisLine;
 		while(file.hasNextLine()) {
 
 			TaskLogEntry entry = new TaskLogEntry();
 			thisLine = file.nextLine();

			StringTokenizer stringTok = new StringTokenizer(thisLine, "_");
 			entry.timeN = LocalDateTime.parse(stringTok.nextToken());
 			entry.data = stringTok.nextToken();
 			entry.cmd = stringTok.nextToken();
 

			if(stringTok.hasMoreTokens()) {
 				entry.description = stringTok.nextToken();

			}
			if(stringTok.hasMoreTokens()) {
				entry.size = stringTok.nextToken();
			}
 
 			LineL.add(entry);
 
 		}
 		file.close();
 		return LineL;
 
 	}
 }