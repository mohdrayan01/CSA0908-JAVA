package assignment2;

import java.util.*;
class Student {
    int id;
    String name;
    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Student))
            return false;
        Student s = (Student) obj;
        return id == s.id;
    }
}
class CorrectStudent {
    int id;
    String name;
    CorrectStudent(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CorrectStudent))
            return false;
        CorrectStudent s = (CorrectStudent) obj;
        return id == s.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
public class Attendance {
    public static void main(String[] args) {
        Set<Student> set1 = new HashSet<>();
        set1.add(new Student(101, "Ravi"));
        set1.add(new Student(101, "Ravi"));
        System.out.println("Without hashCode(): " + set1.size());
        Set<CorrectStudent> set2 = new HashSet<>();
        set2.add(new CorrectStudent(101, "Ravi"));
        set2.add(new CorrectStudent(101, "Ravi"));
        System.out.println("With hashCode(): " + set2.size());
        Map<CorrectStudent, String> map = new HashMap<>();
        CorrectStudent s = new CorrectStudent(102, "Arun");
        map.put(s, "Present");
        System.out.println("Before modification: " + map.get(s));
        s.id = 103;
        System.out.println("After modification: " + map.get(s));
    }
}