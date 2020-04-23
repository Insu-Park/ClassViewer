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
			
			if(token.compareTo("class") == 0){ 				// 클래스 이름 저장
				
				token = st.nextToken();
				cpplist.setName(token);
				continue;
			}
			else if(token.compareTo("public:") == 0){		// access 저장
				
				access = "public";
				continue;
			}
			else if(token.compareTo("private:") == 0){		// access 저장
				
				access = "private";
				continue;
			}
			else if(token.compareTo("}") == 0){				// 다음 중괄호에 제거
				break;
			}
			
			if(token.compareTo("bool") == 0 || token.compareTo("int") == 0 || token.compareTo("void") == 0 ){	// 자료형이 나오면 저장
				
				String var;
				var = token;
				token = st.nextToken();
				
				if(access.compareTo("private") == 0){
					if(token.indexOf('[')!=-1){
						token = token.substring(0, token.indexOf('['));		// [가 나왔을 시 제거
						var += "[]";
					}
					token = token + ": "+ var;
				}
				cpplist.cpp[i] = new Cpp(token, var, access);		// 저장
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
			if(token.indexOf("::"+method)!=-1 || flag){			// ::로 선언 함수 구분
				flag = true;
				
				if(token.indexOf("{")!=-1)						// '{'수만큼 '}'가 나오면 break;
					i++;
				else if(token.indexOf("}")!=-1 && i>0)
					i--;
				else if(source.compareTo("")!=0 && i==0)
					break;
				
				source += token +"\n";
			}
		}
		
		st = new StringTokenizer(source, "\n\t 0123456789+()[]%:!=;");	
		member = "멤버변수 : ";
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
		return method.substring(0, method.length()-2);	// 마지막 ", " 부분 제거
	}
	
	public String saveText(String str, String pr, String ch){

		str = str.substring(0, str.indexOf(pr)) + ch + str.substring(str.indexOf(pr) + pr.length());	// 기존 텍스트 pr을 제거하고 ch로 대체
		return str;
	}
}
