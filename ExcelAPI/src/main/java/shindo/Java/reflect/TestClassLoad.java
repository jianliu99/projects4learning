package shindo.Java.reflect;

import java.lang.reflect.Method;

public class TestClassLoad {
    public static void main(String[] args) throws Exception {
        // Class.forName方法调用时，要写完整的类名，否则会出现ClassNotFoundException
        Class<?> clz = Class.forName("shindo.Java.reflect.Hello");
        Object o = clz.newInstance();
        Method m = clz.getMethod("foo", String.class);
        m.invoke(o, "shindo");

    }

}
