package com.globant.adapters.console;

import com.globant.application.usecases.InitializeCurrencyPricesUCImpl;
import com.globant.application.usecases.UserLoginUCImpl;
import com.globant.application.usecases.UserRegistrationUCImpl;
import com.globant.domain.util.StaticScanner;
import com.globant.domain.util.UserAuthException;

import java.util.InputMismatchException;

public class ConsoleAdapter {
    private final UserRegistrationUCImpl userRegistrationUC;
    private final UserLoginUCImpl userLoginUC;
    private final InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC;
    private static boolean invalidData = true;
    public ConsoleAdapter(
            UserRegistrationUCImpl userRegistrationUC,
            UserLoginUCImpl userLoginUC,
            InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC
    ){
        this.userRegistrationUC = userRegistrationUC;
        this.userLoginUC = userLoginUC;
        this.initializeCurrencyPricesUC = initializeCurrencyPricesUC;
    }
    public void boot(){
        String[] bootOptions = {"Sign Up", "Log In", "Exit"};
        System.out.println("### Welcome to the Crypto Exchange App ###");
        printAuthMenu(bootOptions);

    }
    public void registerUser(int optionSelected){
        System.out.println("\nSIGN UP:");
        String username = getUsernameFromConsole();
        String email = getEmailFromConsole(optionSelected);
        System.out.print("Enter your password: ");
        String password = StaticScanner.getInstance().nextLine();
        userRegistrationUC.registerUser(username, email, password);
    }
    public void loginUser(int optionSelected){
        System.out.println("\nLOG IN:");
        String email = getEmailFromConsole(optionSelected);
        String password = getPasswordFromConsole(email);
        userLoginUC.logInUser(email, password);
    }

    public String getUsernameFromConsole(){
        String username = "";
        while(invalidData){
            try{
                System.out.print("Enter your username: ");
                username = StaticScanner.getInstance().nextLine();
                userRegistrationUC.existsByUsername(username);
                invalidData = false;
            }catch (UserAuthException e){
                System.err.println(e.getMessage());
            }
        }
        invalidData = true;
        return username;
    }

    public String getEmailFromConsole(int optionSelected){
        String email = "";
        while(invalidData){
            System.out.print("Enter your email: ");
            email = StaticScanner.getInstance().nextLine();
            try{
                if (optionSelected == 1)
                    userRegistrationUC.existsByEmail(email);
                else if (optionSelected == 2)
                    userLoginUC.correctEmail(email);
                invalidData = false;
            }catch (UserAuthException e){
                System.err.println(e.getMessage());
            }
        }
        invalidData = true;
        return email;
    }

    public String getPasswordFromConsole(String email){
        String password = "";
        while(invalidData){
            System.out.print("Enter your password: ");
            password = StaticScanner.getInstance().nextLine();
            try{
                userLoginUC.correctPassword(email, password);
                invalidData = false;
            }catch (UserAuthException e){
                System.err.println(e.getMessage());
            }
        }
        invalidData = true;
        return password;
    }

    public void printMenu(String[] options, String title) {
        int i;
        System.out.println(title);
        for (i = 0; i < options.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, options[i]);
        }
    }

    public void printAuthMenu(String[] bootOptions){
        printMenu(bootOptions, "Create an account or log in:");

        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        while (invalidData){
            try {
                if (optionSelected >= 0 && optionSelected < bootOptions.length + 1) {
                    if (optionSelected == 1) {
                        registerUser(optionSelected);
                        System.out.println("User registered successfully!. Now you can log in.\n");
                        printAuthMenu(bootOptions);
                    } else if (optionSelected == 2) {
                        loginUser(optionSelected);
                        System.out.println("User logged in successfully!\n");
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
}