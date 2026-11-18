package Assignment3;

import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
public class ATM {
    private static double balance = 10000.0;
    private static void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount must be positive"
            );
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance -= amount;
        System.out.println("Withdrawal Successful");
        System.out.println("Remaining Balance: " + balance);
    }
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            try (FileReader card = new FileReader("card.txt")) {
                System.out.print("Enter Account Number: ");
                int accountNumber = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter Withdrawal Amount: ");
                int amount = scanner.nextInt();

                System.out.println("Account Number: " + accountNumber);
                withdraw(amount);
            }
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Error: Enter numbers only");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: Card reader file not found");
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        } finally {
            System.out.println("ATM Session Completed");
        }
    }
}