package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class StringFileReader {
	public StringFileReader()
	{

	}

	public String ReadFile(String path)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			StringBuilder string_builder = new StringBuilder();
			String line;
			try {
				while((line = reader.readLine()) != null){
					string_builder.append(line + "\n");
				}
				return string_builder.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "Unable to read file";
			}
		} catch (FileNotFoundException e) {
			System.out.println("Path is: " + path);
			return ReadFile(getClass().getResourceAsStream(path.replace("\\","/")));
		}
	}

	static public String ReadFile(InputStream in)
	{
		if(in == null){
			return "Unable to read file";
		}
		Reader stream_reader = new InputStreamReader(in);
		BufferedReader buffered_reader = new BufferedReader(stream_reader);
		StringBuilder content = new StringBuilder();
		try {
			String line;
			while( (line = buffered_reader.readLine()) != null){
				content.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content.toString();	
	}

	static public ArrayList<String> GetLineArrayFromStream(InputStream in)
	{
		if(in == null){
			return null;
		}
		ArrayList<String> lines = new ArrayList<String>();
		Reader stream_reader = new InputStreamReader(in);
		BufferedReader buffered_reader = new BufferedReader(stream_reader);
		String line;
		try {
			while( (line = buffered_reader.readLine()) != null){
				lines.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;	
	}

}
