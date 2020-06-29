import java.io.*;
import java.util.*;

public class TestExec {
	
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			String s = "";
			Process p = Runtime.getRuntime().exec(TestProcess.PATH + " showing.py");
			
			PrintStream toP = new PrintStream(new BufferedOutputStream(p.getOutputStream()));
			BufferedReader fromP = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			for(;;) {
				System.out.print("In Java: ");
				s = sc.nextLine();
				toP.print(s);
				toP.close();
				
				StringBuffer sb = new StringBuffer();
				int ch;
				while((ch = fromP.read()) != -1) {
					sb.append((char)ch);
				}
				System.out.println("Return from python-> " + sb.toString());
				System.out.println("Return value: " + p.waitFor());
			}
		} catch(IOException | InterruptedException e) {
			System.err.println(e);
		}
	}
}