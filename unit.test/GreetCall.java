import java.io.*;

public class GreetCall {
	public static void main(String[] args) {
		try {
			Process p = Runtime.getRuntime().exec("greet.exe");
			
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line;
			while((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			
			while((line = bre.readLine()) != null) {
				System.out.println(line);
			}
			
			System.out.println(p.waitFor());
			System.out.println("Process exit value: " + p.exitValue());
			
		} catch(IOException | InterruptedException e) {
			System.err.println(e);
		}
	}
}