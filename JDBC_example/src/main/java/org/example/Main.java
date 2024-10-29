package org.example;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);
        Integer loggedInUserId = null;

        while (true) {
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {

                String name, email, password, username, phoneNumber;
                System.out.print("Enter name: ");
                name = scanner.nextLine();

                while (true) {
                    System.out.print("Enter email (@gmail.com): ");
                    email = scanner.nextLine();
                    if (email.endsWith("@gmail.com")) break;
                    System.out.println("Error: Email must end with @gmail.com.");
                }

                while (true) {
                    System.out.print("Enter password: ");
                    password = scanner.nextLine();
                    if (isValidPassword(password)) break;
                    System.out.println("Error: Password must be at least 8 characters, contain uppercase, lowercase, number, and special character.");
                }

                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter phone number: ");
                phoneNumber = scanner.nextLine();

                String signUpResult = userDao.signUp(name, email, password, username, phoneNumber);
                System.out.println(signUpResult);

            } else if (option == 2) {

                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                String signInResult = userDao.signIn(email, password);
                System.out.println(signInResult);

                if (signInResult.equals("Tizimga kirish muvaffaqiyatli")) {

                    loggedInUserId = Math.toIntExact(userDao.getUserIdByEmail(email)); // Method to get the user's ID by email

                    while (true) {
                        System.out.println("\n3. Add Card");
                        System.out.println("4. Delete Card");
                        System.out.println("5. Get My Cards");
                        System.out.println("0. Sign Out");
                        System.out.print("Select an option: ");
                        int cardOption = scanner.nextInt();
                        scanner.nextLine();

                        if (cardOption == 3 && loggedInUserId != null) {

                            System.out.print("Enter Bank Name: ");
                            String bankName = scanner.nextLine();
                            Date expiryDate;

                            while (true) {
                                System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
                                try {
                                    expiryDate = Date.valueOf(LocalDate.parse(scanner.nextLine()));
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Error: Invalid date format.");
                                }
                            }

                            String addCardResult = userDao.addCard(loggedInUserId, bankName, expiryDate);
                            System.out.println(addCardResult);

                        } else if (cardOption == 4) {

                            System.out.print("Enter Card ID to delete: ");
                            int cardId = scanner.nextInt();
                            userDao.deleteCard(cardId);
                            System.out.println("Card deleted successfully.");

                        } else if (cardOption == 5) {

                            userDao.getMyCards(loggedInUserId);

                        } else if (cardOption == 0) {
                            loggedInUserId = null;
                            break;
                        } else {
                            System.out.println("Invalid option. Please select again.");
                        }
                    }
                }
            } else if (option == 0) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option selected. Please try again.");
            }
        }
        scanner.close();
    }


    public static boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[^a-zA-Z0-9].*");
    }
}
