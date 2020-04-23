package classviewer;

// c++ 파일필터

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFilter extends FileFilter {
	
	String type;
	String desc;
	
	public MyFilter(String type, String desc)
	{
			this.type = "." + type;
			this.desc = desc;
	}
	
	@Override		
	public boolean accept(File f) {
		if(f.getName().endsWith(type) || f.isDirectory())
			return  true;
		else
			return false;
	}

	@Override		 
	public String getDescription() {
		return desc;
	}
}
