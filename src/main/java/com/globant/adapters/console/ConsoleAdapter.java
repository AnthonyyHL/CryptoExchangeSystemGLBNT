package com.globant.adapters.console;

import com.globant.application.usecases.UserRegistrationUCImpl;
import com.globant.domain.util.StaticScanner;
import com.globant.domain.util.UserCreationException;

import java.util.InputMismatchException;

public class ConsoleAdapter {
    private final UserRegistrationUCImpl userRegistrationUC;
    private static boolean invalidData = true;
    public ConsoleAdapter(UserRegistrationUCImpl userRegistrationUC){
        this.userRegistrationUC = userRegistrationUC;
    }
    public void boot(){
        String[] bootOptions = {"Sign Up", "Log In", "Exit"};
        System.out.println("### Welcome to the Crypto Exchange App ###");
        printMenu(bootOptions, "Create an account or log in:");

        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        while (invalidData){
            try {
                if (optionSelected >= 0 && optionSelected < bootOptions.length + 1) {
                    if (optionSelected == 1) {
                        registerUser();
                    } else if (optionSelected == 2) {
                        System.out.println("Log In");
                    } else if (optionSelected == 3) {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    invalidData = false;
                } else {
                    System.out.println("Selección inválida. Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("El valor ingresado no es el correcto. Por favor ingrese un número.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Se esperaba un número. Por favor, intente de nuevo.");
            }
        }
        invalidData = true;

    }
    public void registerUser(){
        System.out.println("\nSIGN UP:");
        String username = getUsernameFromConsole();
        String email = getEmailFromConsole();
        System.out.print("Enter your password: ");
        String password = StaticScanner.getInstance().nextLine();
        userRegistrationUC.registerUser(username, email, password);
    }

    public String getUsernameFromConsole(){
        String username = "";
        while(invalidData){
            try{
                System.out.print("Enter an username: ");
                username = StaticScanner.getInstance().nextLine();
                userRegistrationUC.existsByUsername(username);
                invalidData = false;
            }catch (UserCreationException e){
                System.err.println(e.getMessage());
            }
        }
        invalidData = true;
        return username;
    }

    public String getEmailFromConsole(){
        String email = "";
        while(invalidData){
            System.out.print("Enter an email: ");
            email = StaticScanner.getInstance().nextLine();
            try{
                userRegistrationUC.existsByEmail(email);
                invalidData = false;
            }catch (UserCreationException e){
                System.err.println(e.getMessage());
            }
        }
        invalidData = true;
        return email;
    }

    public void printMenu(String[] options, String title) {
        int i;
        System.out.println(title);
        for (i = 0; i < options.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, options[i]);
        }
    }
}