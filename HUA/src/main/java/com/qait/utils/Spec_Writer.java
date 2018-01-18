package com.qait.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Spec_Writer {

	public String pathName;
	public String title;
	FileWriter fw;

	public Spec_Writer(String title) {
		this.title = title;
		this.pathName = ".//src/test//resources//pageObjectRepository//" + title+".spec";
		try {
			fw = new FileWriter(pathName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToFile(String line) {
		try {
			fw.append(line);
			fw.append("\n");
			fw.flush();
			//fw.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
	}
}
