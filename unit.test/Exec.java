import java.io.*;

public class Exec {
	public static void main(String[] args) {
		try {
			String line;
			Process p = Runtime.getRuntime().exec("cmd /c dir");
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			while((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			
			while((line = bre.readLine()) != null) {
				System.out.println(line);
			}
			bre.close();
			System.out.println(p.waitFor());
			System.out.println("Done");
		} catch(IOException | InterruptedException e) {
			System.err.println(e);
		}
	}
}