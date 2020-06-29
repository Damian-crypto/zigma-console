public class Test {
	static {
		System.loadLibrary("test");
	}

	private native String sayHello();
	
	public static void main(String[] args) {
		System.out.println(new Test().sayHello());
	}
}