package zigma;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Path;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of Console
 * Life cycle of console:
 * ~~~~~~~~~~~~~~~~~~~~~~~
 * 1) Enter a command and press enter
 * 2) Check if the command is internal or external
 * 3) If internal execute it internally,
 *	  If external call that module with arguments
 * 4) If command is not internal or external then it checks is there a file called that name
 *	  in this directory
 * 5) If a file found then it invokes the file with arguments,
 *	  If not shows an error message
 */
public class ConsoleController {

	@FXML private TabPane tabbedPane; // Vairable name must be same as fx:id
	@FXML private TextField cmdField;
	@FXML private Label caretChar;
	@FXML private TextArea txtArea;

	private Map<String, String> globalVariables;

	private String currentCommand = null; // Most recent command
	private String previousCommand = null; // Last command
	private String correctCommand = null;
	private List<String> commandList = null;
	private StringBuilder txtAreaCurrentText = new StringBuilder(); // Currently showing text
	private String lineSeparator = System.getProperty("line.separator"); // For platform independent
	private boolean posibility = false;
	private File currentDirectory;

	private boolean validateCommand(String cmd, int length) {
		return (currentCommand.length() >= length) ? currentCommand.substring(0, length).contains(cmd) : false;
	}

	private String validateReturn(String cmd, String finalResult) {
		if(currentCommand.equals(cmd)) {
			return finalResult;
		} else {
			correctCommand = cmd;
			previousCommand = currentCommand;
			return "Did you mean '" + cmd + "' ?";
		}
	}

	private String executeCommand() {
		String returnMessage = "'" + currentCommand + "' is not recognized as internal or external command";

		if(validateCommand("help", 4)) {
			switch(currentCommand) {
				case "help": return validateReturn("help", "Help menu");
				case "help -u": return validateReturn("help -u", "Hello");
				default: return validateReturn(currentCommand, null);
			}
		} else if(validateCommand("dir", 3)) {
			StringBuilder fileList = new StringBuilder();
			for(File file: currentDirectory.listFiles()) {
				fileList.append(lineSeparator).append(file.isDirectory() ? "Dir: " : "File: ").append(file.getAbsolutePath());
			}
			return validateReturn("dir", fileList.toString());
		} else if(validateCommand("drive", 5)) {
			switch(currentCommand) {
				case "drive":
					Path currentPath = Paths.get(currentDirectory.getAbsolutePath()).getRoot();
					return validateReturn(currentCommand, currentPath.toString());
				case "drive --list":
					StringBuilder drives = new StringBuilder();
					for(File file: currentDirectory.listRoots()) {
						drives.append(lineSeparator).append(file.toString());
					}
					return validateReturn(currentCommand, drives.toString());
				default:
					String drive = currentCommand.replace("drive", "").strip();
					for(File file: currentDirectory.listRoots()) {
						if(drive.contains(file.toString())) {
							currentDirectory = new File(file.getAbsolutePath());
							caretChar.setText(currentDirectory.toString().replace(File.separator, ""));
							return validateReturn(currentCommand, drive);
						}
					}
			}
		} else if(validateCommand("del", 3)) {
			File _file = new File(currentCommand.replace("del", "").strip());
			return _file.getAbsolutePath() + (_file.delete() ? " is deleted!" : " is not deleted!");
		} else if(validateCommand("restore", 7)) {
			txtArea.clear();
			commandList.clear();
			currentDirectory = new File(System.getProperty("user.dir"));
			return "";
		} else if(validateCommand("_cmd_list", 9)) {
			return validateReturn(currentCommand, commandList.toString());
		} else if(validateCommand("_cwd", 4)) {
			return validateReturn(currentCommand, currentDirectory.toString());
		} else if(validateCommand("yes", 3)) {
			if(previousCommand != null) {
				doCommand(correctCommand);
				previousCommand = null;
				return "";
			} else {
				posibility = true;
				return "";
			}
		} else if(validateCommand("no", 2)) {
			if(previousCommand != null) {
				final String _msg = "'" + previousCommand + "' is not recognized as internal or external command";
				previousCommand = null;
				return _msg;
			} else {
				posibility = false;
				return "";
			}
		} else if(validateCommand("quit", 4)) {
			new Alert(Alert.AlertType.WARNING, "Are you sure you want to close this program?", ButtonType.YES, ButtonType.NO)
				.showAndWait().ifPresent(response -> {
					if(response == ButtonType.YES) System.exit(0);
				});
			return "";
		} else if(!traverseVariables(currentCommand).equals("None")) {
			String _key = traverseVariables(currentCommand);
			File _file = new File((String)globalVariables.get(_key));
			System.out.println(_key);
			return callExternalFile(_file, currentCommand.replaceFirst(_key, ""));
		} else {
			for(File file: currentDirectory.listFiles()) {
				if(currentCommand.contains(file.getName())) {
					if(file.isDirectory()) {
						currentDirectory = new File(file.getAbsolutePath());
						Path path = Paths.get(currentDirectory.getAbsolutePath()).getRoot();
						caretChar.setText(path.toString().replace(File.separator, ""));
						return "";
					} else {
						return callExternalFile(file, currentCommand.replace(file.getName(), ""));
					}
				}
			}
		}

		return returnMessage;
	}

	private String traverseVariables(String cmd) {
		System.out.println(cmd);
		Iterator<String> iter = globalVariables.keySet().iterator();
		String key = null;
		while(iter.hasNext())
			if(cmd.contains(key = iter.next())) return key;
			else key = "None";
		return key;
	}

	private String callExternalFile(File file, String args) {
		try {
			System.out.println(file.getAbsolutePath() + (args.isEmpty() ? "" : " " + args.strip()));
			Process process = Runtime.getRuntime().exec(file.getAbsolutePath() + (args.isEmpty() ? "" : " " + args.strip()));

			// Write into the standard input of the subprocess
			PrintStream pin = new PrintStream(new BufferedOutputStream(process.getOutputStream()));
			// Read from the standard output of the subprocess
			BufferedReader pout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			// Read from the standard error output of the subprocess
			BufferedReader perr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			// Save the output in a StringBuffer for further processing
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = pout.readLine()) != null) {
				sb.append(line);
			}

			line = "";
			while((line = perr.readLine()) != null) {
				sb.append(line);
			}
			System.out.println("current process exit value: " + process.exitValue());
			return sb.toString().strip();
		} catch(IOException e) {
			return e.toString();
		}
		//return "";
	}

	private void doCommand(String txt) {
		currentCommand = txt;
		commandList.add(txt);
		String returnStr = executeCommand().strip();
		txtAreaCurrentText.append(returnStr.equals("") ? "" : lineSeparator).append(returnStr);
		txtArea.setText(txtAreaCurrentText.toString().strip());
		txtArea.selectEnd();
		txtArea.deselect();
	}

	private void loadVariables() {
		globalVariables = new HashMap<>();
		Properties props = new Properties();
		try(FileInputStream fis = new FileInputStream("config.properties")) {
			props.load(fis);
			String _str = null;
			for(Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); globalVariables.put((_str = (String)e.nextElement()), props.getProperty(_str)));
		} catch(IOException ex) {
			new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).show();
		}
	}

	/**
	 * This method will be invoked first of all
	 */
	@FXML
	protected void initialize() {
		currentDirectory = new File(System.getProperty("user.dir"));
		loadVariables();
		commandList = new ArrayList<>();
		cmdField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int pressings = 0;
			@Override
			public void handle(KeyEvent evt) {
				if(evt.getCode() == KeyCode.ENTER) {
					doCommand(cmdField.getText());
					cmdField.clear();
				} else if(evt.getCode() == KeyCode.UP) {
					cmdField.setText(commandList.get((pressings < commandList.size() - 1) ? ++pressings : 0));
				} else if(evt.getCode() == KeyCode.DOWN) {
					cmdField.setText(commandList.get(pressings > 0 ? --pressings : 0));
				}
			}
		});
	}
}
