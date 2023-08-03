package toDoListConsole;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project: Console based to do List.
 * Description: Basic to do list with a console interface.
 * Version: 1.1
 * 
 * Note: The previous version of the program was lost attempting to correct install issues. 
 */
public class toDoListConsole {
	static TaskManager tm;
	public static void main(String[] args) {
		tm = new TaskManager();
	}
	
	/**
	 * Task Manager class that manages the creation, deletion, saving and loading of task.
	 */
	static class TaskManager {
		ArrayList<task> taskList;
		String filename = "taskDataConsole.txt";

		/**
		 * Called to print the current task list.
		 */
		void generateTaskList() {
			if (!taskList.isEmpty()) {
				System.out.println("Index\t Started\t Completed\t Task");
				taskList.forEach(temp -> {
					System.out.println(Integer.toString(taskList.indexOf(temp)) + '\t' + temp.formatData());
				});
			}
		}
		
		/**
		 * Called to read input entered into the console.
		 * 
		 * @return	String, Returns the String of what is typed in.
		 */
		String readInput() {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			try
		    {
		      return br.readLine().strip();
		    }
		    catch (IOException ex)
		    {
		      System.out.println(ex);
		    }
			
			return null;
		}
		
		/**
		 * Calls readInput and returns the integer of it.
		 * 
		 * @return	Integer, Returns the integer of the String returned from readInput.
		 */
		Integer readInt() {
			return Integer.parseInt(readInput());
		}
		
		/**
		 * Called to create the save file if it does not exist and then write the task data into the save file.
		 */
		void saveData() {
			try {
				//creates task file if missing
				File taskFile = new File(filename);
			    taskFile.createNewFile();
			    
			    //writes task to file
			    FileWriter fileWriter = new FileWriter(filename);
			    taskList.forEach(task -> {
			    	try {
			    		fileWriter.write(task.getData());
			    	}
			    	catch (IOException e) {
						System.out.println("An error occurred saving task list.");
					    e.printStackTrace();
			    	}
			    }) ;
			    fileWriter.close();
			} 
			catch (IOException e) {
				System.out.println("An error occurred creating task file.");
			    e.printStackTrace();
			}
		}
		
		/**
		 * Reads the save file, if it exist, line by line, splitting each line and adding a new task to the list
		 * using the split information.
		 */
		void loadData() {
			try {
				File taskFile = new File(filename);
				if (taskFile.exists() && !taskFile.isDirectory()) {
					String data;
					String[] dataArray;
					Scanner fileReader = new Scanner(taskFile);
					while (fileReader.hasNextLine()) {
						data = fileReader.nextLine();
						
						dataArray = data.split(",");
						//state, startTime, completeTime, taskText
						taskList.add(new task(Boolean.parseBoolean(dataArray[0]), dataArray[1], dataArray[2], dataArray[3]));
					}
					fileReader.close();				    
				}
			}
			catch (FileNotFoundException e) {
				System.out.println("An error occurred loading task data.");
			    e.printStackTrace();
			}
		}
		
		/**
		 * Called to create a new task, request the text input and add it to the array list.
		 */
		void createTask() {
			System.out.println("Please enter the task: ");
			taskList.add(new task(readInput()));
		}
		
		/**
		 * Called to complete a task, request the task index number to select the task to be completed.
		 */
		void completeTask() {
			System.out.println("Please enter task index.");
			int a = readInt();
			if (a < taskList.size()) {
				taskList.get(a).completeTask();
			}
			else { System.out.println("That task does not exist."); }
		}
		
		/**
		 * Called to delete the task, request the task index number to select the task for deletion.
		 */
		void deleteTask() {
			System.out.println("Please enter task index.");
			int a = readInt();
			if (a < taskList.size()) {
				taskList.remove(taskList.get(a));
			}
			else { System.out.println("That task does not exist."); }
		}
		
		/**
		 * Called to end the program, saving the task data before hand.
		 */
		void exit() {
			saveData();
			System.out.println("Goodbye");
			System.exit(0);
		}
		
		/**
		 * Default constructor, used to display current task, menu options and process menu selection.
		 */
		TaskManager() {
			taskList = new ArrayList<task>();
			String input;
			
			loadData();
			
			while (true) {
				generateTaskList();
				System.out.println("(A)dd Task, (C)omplete Task, (D)elete Task, (E)xit Program");
				input = readInput();
				
				switch(input) {
					case "A": break;
					case "a": createTask(); break;
					case "C": break;
					case "c": completeTask(); break;
					case "D": break;
					case "d": deleteTask(); break;
					case "E": break;
					case "e": exit(); break;
					default: System.out.println("Invalid selection.");
				}
			}
		}
	}
	
	/**
	 * Task class file used for the console version.
	 */
	static class task {
		String startTime;
		String completeTime;
		String text;
		boolean state;
		
		/**
		 * Default task constructor, request the current time to set as the started time and is supplied with the text
		 * of the task as a String.
		 * 
		 * @param tText	String, Text of the to do task.
		 */
		task(String tText) {
			startTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
			completeTime = "Pending";
			text = tText;
		}
		
		/**
		 * Constructor used when loading task data from save file.
		 * 
		 * @param tState		Boolean, State of task, false for pending and true for completed.
		 * @param tStart		String, Start time of the task.
		 * @param tComplete		String, Completion time of the task.
		 * @param tText			String, Text of the task.
		 */
		task(boolean tState, String tStart, String tComplete, String tText) {
			state = tState;
			startTime = tStart;
			completeTime = tComplete;
			text = tText;
		}
		
		/**
		 * Called to set the task as completed.
		 */
		void completeTask() {
			if (!state) {
				state = true;
				completeTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
			}
		}
		
		/**
		 * Called to format the data as a string to display in the menu.
		 *  
		 * @return	String, String used to display the task in the console.
		 */
		String formatData() {
			return startTime + '\t' + completeTime + '\t' + text;
		}
		
		/**
		 * Called to format data into a String to save to the save file.
		 * Changed to an override of toString in other programs.
		 * 
		 * @return	String, Formatted task data as Boolean state, String start time, String complete time, String task text.
		 */
		String getData() {
			return Boolean.toString(state) + "," + startTime + "," + completeTime + "," + text + "\n";
		}
	}
}
