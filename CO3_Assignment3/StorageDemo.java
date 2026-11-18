package Assignment3;

import java.io.IOException;
class ExamException extends Exception {
    ExamException(String message) {
        super(message);
    }
    ExamException(String message, Throwable cause) {
        super(message, cause);
    }
}
class ExamWindowClosedException extends ExamException {
    ExamWindowClosedException() {
        super("The examination window is closed");
    }
}
class StudentNotRegisteredException extends ExamException {
    StudentNotRegisteredException() {
        super("Student is not registered for this course");
    }
}
class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
class ExamService {
    void validate(boolean windowOpen, boolean registered)
            throws ExamException {
        if (!windowOpen) {
            throw new ExamWindowClosedException();
        }
        if (!registered) {
            throw new StudentNotRegisteredException();
        }
    }
}
class Storage {
    void save(String fileName, long sizeKb)
            throws SubmissionStorageException {
        try {
            if (sizeKb > 500) {
                throw new IOException("Disk quota exceeded");
            }
            System.out.println("Saved " + fileName);
        } catch (IOException e) {
            throw new SubmissionStorageException(
                "Could not store " + fileName,
                e
            );
        }
    }
}
public class StorageDemo {
    public static void main(String[] args) {
        Storage storage = new Storage();
        try {
            storage.save("answer01.pdf", 120);
            storage.save("answer02.pdf", 800);
        } catch (ExamException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }
}