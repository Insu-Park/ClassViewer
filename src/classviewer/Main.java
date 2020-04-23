package classviewer;

import javax.swing.JFrame;

class Main{
	
	public static void main(String[] args){
		
		try{
			JFrame ex = new MyFrame();
			ex.setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
