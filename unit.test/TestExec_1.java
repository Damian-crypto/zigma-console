import java.io.*;
import java.util.Scanner;

public class TestExec_1 {
	public static void main(String[] args) {
		try {
			Process p = Runtime.getRuntime().exec(TestProcess.PATH + " showing.py");
			
			Scanner sc = new Scanner(System.in);
			
			PrintStream toP = new PrintStream(new BufferedOutputStream(p.getOutputStream()));
			BufferedReader fromP = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			int exitCode = 0;
			int threadCode = 0;
			String line = "";
			StringBuffer sb = new StringBuffer();
			
			do {
				System.out.print("In Java: ");
				line = sc.nextLine();
				toP.print(line);
				toP.close();
				
				sb.delete(0, sb.length());
				String str = "";
				while((str = fromP.readLine()) != null) {
					sb.append(str);
				}
				System.out.println(sb);
				
				threadCode = p.waitFor();
				exitCode = p.exitValue();
				//System.out.println("current thread to wait: " + threadCode);
				System.out.println("exit value: " + exitCode);
			} while(exitCode != 0);
		} catch(IOException | InterruptedException e) {
			System.err.println(e);
		}
	}
}