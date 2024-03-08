public class Test {
    public native void printHelloWorld();

    public static void main(String[] args) {// jni
        System.loadLibrary("HelloWorld");
        Test test = new Test();
        test.printHelloWorld();
    }
}
