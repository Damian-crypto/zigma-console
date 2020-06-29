import java.io.File;
import java.io.IOException;

public class TestFiles {
	public static void main(String[] args) {
		File f = new File(System.getProperty("user.dir"));
		for(File file: f.listFiles()) {
			System.out.println(file.getAbsolutePath());
		}
	}
}