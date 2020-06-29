import java.io.*;
import java.util.*;

public class TestExecredirect {
	
	private static String path = "C:/Users/Damian/AppData/Local/Programs/Python/Python38/python.exe";
	
	public static void main(String[] arsg) {
		try {
			
			int x = 0;
			String s = "";
			Scanner sc = new Scanner(System.in);
			Process p = Runtime.getRuntime().exec(path + " showing.py");
			
			PrintStream pin = new PrintStream(new BufferedOutputStream(p.getOutputStream()));
			BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			do {
				System.out.print("In Java: ");
				s = sc.nextLine();
				pin.print(s);
				pin.close();
				
				StringBuffer sb = new StringBuffer();
				int ch;
				while((ch = pout.read()) != -1) {
					sb.append((char)ch);
				}
				System.out.println(sb);
				
				x = p.waitFor();
				System.out.println(x);
			} while(!s.equals("exit"));
		} catch(IOException | InterruptedException e) {
			System.out.println(e);
		}
	}
}