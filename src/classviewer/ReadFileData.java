package classviewer;

import java.io.BufferedReader;
import java.io.FileReader;

public class ReadFileData {
	
	private String buffer = "";
	
	@SuppressWarnings("resource")
	public ReadFileData(String filename){
		
		try{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";							
			while ((temp = br.readLine()) != null)	// 전체 텍스트 저장
			{
				buffer += temp;						
				buffer += "\n";	
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void setString(String str){
		buffer = str;
	}
	
	public String getString(){
		return buffer;
	}
}
