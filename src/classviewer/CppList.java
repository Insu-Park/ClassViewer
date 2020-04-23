package classviewer;

// cpp 클래스 리스트

public class CppList {

	private String classname;
	Cpp[] cpp = new Cpp[100];
	
	public CppList(){
		
		for(int i=0; i<100; i++)
			cpp[i] = new Cpp("", "", "");
	}
	
	public void setName(String classname){
		
		this.classname = classname;
	}
	
	public String getName(){
		
		return classname;
	}
}
