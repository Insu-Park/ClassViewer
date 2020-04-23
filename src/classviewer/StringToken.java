package classviewer;

import java.util.StringTokenizer;

public class StringToken {

	CppList cpplist = new CppList();
	private StringTokenizer st;
	private String member;
	
	public void makeList(String str){
		
		st = new StringTokenizer(str, "\n\t =;{");
		String token;
		String access = null;
		int i=0;
		
		for(; st.hasMoreTokens(); ){
			
			token = st.nextToken();
			
			if(token.compareTo("class") == 0){ 				// Ŭ���� �̸� ����
				
				token = st.nextToken();
				cpplist.setName(token);
				continue;
			}
			else if(token.compareTo("public:") == 0){		// access ����
				
				access = "public";
				continue;
			}
			else if(token.compareTo("private:") == 0){		// access ����
				
				access = "private";
				continue;
			}
			else if(token.compareTo("}") == 0){				// ���� �߰�ȣ�� ����
				break;
			}
			
			if(token.compareTo("bool") == 0 || token.compareTo("int") == 0 || token.compareTo("void") == 0 ){	// �ڷ����� ������ ����
				
				String var;
				var = token;
				token = st.nextToken();
				
				if(access.compareTo("private") == 0){
					if(token.indexOf('[')!=-1){
						token = token.substring(0, token.indexOf('['));		// [�� ������ �� ����
						var += "[]";
					}
					token = token + ": "+ var;
				}
				cpplist.cpp[i] = new Cpp(token, var, access);		// ����
			}
			else													
				cpplist.cpp[i] = new Cpp(token, "void", access);
			i++;
		}
	}
	
	public String getMethod(String str, String method){
		
		String token, source = "";
		st = new StringTokenizer(str, "\n");
		boolean flag = false;
		int i=0;
		method = method.substring(0, method.indexOf('('));
		
		for(; st.hasMoreTokens();){
			
			token = st.nextToken();
			if(token.indexOf("::"+method)!=-1 || flag){			// ::�� ���� �Լ� ����
				flag = true;
				
				if(token.indexOf("{")!=-1)						// '{'����ŭ '}'�� ������ break;
					i++;
				else if(token.indexOf("}")!=-1 && i>0)
					i--;
				else if(source.compareTo("")!=0 && i==0)
					break;
				
				source += token +"\n";
			}
		}
		
		st = new StringTokenizer(source, "\n\t 0123456789+()[]%:!=;");	
		member = "������� : ";
		String[] memb = {"", "", "", ""};
		int count=0;
		for(; st.hasMoreTokens();){
			token = st.nextToken();
			for(int k=0; k<100; k++){
				if(cpplist.cpp[k].getName().compareTo("")!=0){
					if(cpplist.cpp[k].getName().indexOf(':')!=-1){
						if(token.compareTo(cpplist.cpp[k].getName().substring(0, cpplist.cpp[k].getName().indexOf(':')))==0){
							if(memb[0].compareTo(token)!=0 && memb[1].compareTo(token)!=0 && memb[2].compareTo(token)!=0 && memb[3].compareTo(token)!=0){
								memb[count] = cpplist.cpp[k].getName().substring(0, cpplist.cpp[k].getName().indexOf(':'));
								member += memb[count] + " ";
								count++;
							}
						}
					}
				}
			}
		}
		return source;
	}
	public String getmem(){
		return member;
	}
	
	public String getMember(String str, String member){
		
		String token, method = "";
		String[] var = new String[100];
		String[] meth = {"", "", "", "", "", ""};
		int count = 0, k = 0;
		st = new StringTokenizer(str, "\n");
		
		for(;st.hasMoreTokens();){
			token = st.nextToken();
			var[count] = token;
			count++;
			if(token.indexOf(member)!=-1){
				count--;
				for(;count>0;count--){
					if(var[count].indexOf("::")!=-1 && var[(count-1)].indexOf("}")!=-1){
						if(meth[0].compareTo(var[count])!=0 && meth[1].compareTo(var[count])!=0 && meth[2].compareTo(var[count])!=0 &&
								meth[3].compareTo(var[count])!=0 &&	meth[4].compareTo(var[count])!=0 && meth[5].compareTo(var[count])!=0){
							meth[k] = var[count];
							count=0;
							method += meth[k].substring(meth[k].indexOf("::")+2) + ", ";
							k++;
							break;
						}
					}
				}
				count = 0;
			}
		}
		return method.substring(0, method.length()-2);	// ������ ", " �κ� ����
	}
	
	public String saveText(String str, String pr, String ch){

		str = str.substring(0, str.indexOf(pr)) + ch + str.substring(str.indexOf(pr) + pr.length());	// ���� �ؽ�Ʈ pr�� �����ϰ� ch�� ��ü
		return str;
	}
}
