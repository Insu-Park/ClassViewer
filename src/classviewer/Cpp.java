package classviewer;

// C++ �޼ҵ峪 ���� �̸�, Ÿ��, Access ���� 

public class Cpp {

	private String name;
	private String type;
	private String access;

	public Cpp(String name, String type, String access){	
		
		this.name = name;
		this.type = type;
		this.access = access;
	}
	
	public String getName(){
		
		return name;
	}
	
	public String getType(){
	
		return type;
	}
	
	public String getAccess(){
		
		return access;
	}
}
