package assignment2;

public class StringBenchmark {
    static void testString(int n) {
        String s = "";
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            s = s + "A";
        }
        long end = System.nanoTime();
        System.out.println("String Time: " + (end - start) + " ns");
    }
    static void testStringBuilder(int n) {
        StringBuilder sb = new StringBuilder();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            sb.append("A");
        }
        long end = System.nanoTime();
        System.out.println("StringBuilder Time: " + (end - start) + " ns");
    }
    static void testStringBuffer(int n) {
        StringBuffer sb = new StringBuffer();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            sb.append("A");
        }
        long end = System.nanoTime();
        System.out.println("StringBuffer Time: " + (end - start) + " ns");
    }
    public static void main(String[] args) {
        int n = 10000;
        System.out.println("String Performance Comparison");
        testString(n);
        testStringBuilder(n);
        testStringBuffer(n);
    }
}
