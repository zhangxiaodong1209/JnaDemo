package org.example;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class Demo {//jna
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
                Native.load((Platform.isWindows() ? "msvcrt" : "c"),
                        CLibrary.class);

        void printf(String format, Object... args);
    }

    public interface DemoLibrary extends Library {
        DemoLibrary INSTANCE = (DemoLibrary)
                Native.load(Platform.isWindows() ? "demo" :"Demo",
                        DemoLibrary.class);

        void myFunction();
    }

    public interface DemoDllLibrary extends Library {
        DemoDllLibrary INSTANCE = (DemoDllLibrary)
                Native.load(Platform.isWindows() ? "demodll" :"Demo",
                        DemoDllLibrary.class);

        int add(int a,int b);
    }

    public static void main(String[] args) {
        System.out.println("start..");
        CLibrary.INSTANCE.printf("Hello, World~~java jna!\n");
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }

        DemoLibrary.INSTANCE.myFunction();

        System.out.println(DemoDllLibrary.INSTANCE.add(10,2));
        System.out.println("end..");
    }
}
