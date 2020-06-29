import java.io.*;

public class TestProcess {
	
	public static String PATH = "C:/Users/Damian/AppData/Local/Programs/Python/Python38/python.exe";
	
	public static void main(String[] args) {
		try {
			Process p = Runtime.getRuntime().exec(PATH + " hello.py Chamel");
			
			BufferedReader pout = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			StringBuffer sb = new StringBuffer();
			int ch;
			while((ch = pout.read()) != -1) {
				sb.append((char)ch);
			}
			
			System.out.println(sb);
			System.out.println(p.waitFor());
		} catch(IOException | InterruptedException e) {
			System.err.println(e);
		}
	}
}