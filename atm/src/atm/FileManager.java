package atm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
	File file;
	FileWriter fw;
	FileReader fr;
	BufferedReader br;
	
	public FileManager() {
		this.file = new File("ATM.text");
	}
}
