package com.globant.adapters.console;

import com.globant.application.usecases.*;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.Wallet;
import com.globant.domain.util.NoCurrencyAvailableException;
import com.globant.domain.util.StaticScanner;
import com.globant.domain.util.UserAuthException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

public class ConsoleAdapter {
    private final UserRegistrationUCImpl userRegistrationUC;
    private final UserLoginUCImpl userLoginUC;
    private final InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC;
    private final DepositMoneyUCImpl depositMoneyUC;
    private final ViewWalletBalanceUCImpl viewWalletBalanceUC;
    private final BuyFromExchangeUCImpl buyFromExchangeUC;
    private final ViewTransactionHistoryUCImpl viewTransactionHistoryUC;

    Wallet wallet;

    String[] bootOptions = {"Sign Up", "Log In", "Exit"};
    String[] mainOptions = {
            "Exchange Cryptocurrencies",
            "Deposit",
            "Withdraw",
            "My Wallet",
            "Place Orders",
            "Exit"
    };

    private static boolean invalidData = true;
    public ConsoleAdapter(
            UserRegistrationUCImpl userRegistrationUC,
            UserLoginUCImpl userLoginUC,
            InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC,
            DepositMoneyUCImpl depositMoneyUC,
            ViewWalletBalanceUCImpl viewWalletBalanceUC,
            BuyFromExchangeUCImpl buyFromExchangeUC,
            ViewTransactionHistoryUCImpl viewTransactionHistoryUC
    ){
        this.userRegistrationUC = userRegistrationUC;
        this.userLoginUC = userLoginUC;
        this.initializeCurrencyPricesUC = initializeCurrencyPricesUC;
        this.depositMoneyUC = depositMoneyUC;
        this.viewWalletBalanceUC = viewWalletBalanceUC;
        this.buyFromExchangeUC = buyFromExchangeUC;
        this.viewTransactionHistoryUC = viewTransactionHistoryUC;
    }
    public void boot(){
        System.out.println("### Welcome to the Crypto Exchange App ###");
        printAuthMenu(bootOptions);
        printMainMenu(mainOptions);

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
        wallet = ActiveUser.getInstance().getActiveUser().getWallet();
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
            try {
                if (optionSelected > 0 && optionSelected < mainOptions.length + 1) {
                    if (optionSelected == 1) {
                        userRegistrationUC.existsByEmail(email);
                    } else if (optionSelected == 2) {
                        userLoginUC.correctEmail(email);
                    }
                } else {
                    System.err.println("Invalid option. Try again.");
                }
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

    public void buyFromExchange() {
        Map<Currency, BigDecimal> availableExchangeCurrencies = initializeCurrencyPricesUC.getCurrencyAvailables();

        System.out.print("\nEnter the option number of the currency you want to buy: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        Currency currencySelected = availableExchangeCurrencies.keySet().stream().toList().get(optionSelected - 1);

        String currencyName = currencySelected.getName();
        System.out.printf("You selected: %s\n", currencyName);

        System.out.print("\nEnter the amount you want to buy: ");
        BigDecimal amount = StaticScanner.getInstance().nextBigDecimal();
        StaticScanner.getInstance().nextLine();

        System.out.printf("The total amount to pay is: %s\n", currencySelected.getPrice().multiply(amount).setScale(2, RoundingMode.HALF_UP));

        System.out.println("Confirm the purchase? (y/n)");
        String confirm = StaticScanner.getInstance().nextLine();
        if (confirm.equals("n")) {
            System.out.println("Purchase canceled.");
            return;
        } else if (!confirm.equals("y")) {
            System.err.println("Invalid option. Try again.");
            return;
        }

        try {
            buyFromExchangeUC.buyCurrency(optionSelected, amount);
        } catch (NoCurrencyAvailableException e) {
            System.err.println(e.getMessage());
        }
    }

    public void showTransactions(){
        String[] transactionOptions = {"Show specific details", "Return to my wallet", "Back to Main Menu"};
        System.out.println("\nTRANSACTIONS:");
        List<Transaction> transactions = viewTransactionHistoryUC.getTransactionHistory();
        final int[] index = {1};
        transactions.forEach(transaction ->
                System.out.printf("\t%d. Transaction #%s: %s\n", index[0]++, transaction.getTransactionId(), transaction.getTransactionDate())
        );
        printMenu(transactionOptions, "\nSelect an option:");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                System.out.print("\nEnter the transaction number: ");
                int transactionNumber = StaticScanner.getInstance().nextInt();
                StaticScanner.getInstance().nextLine();

                Transaction transaction = transactions.get(transactionNumber - 1);

                System.out.println("\n" + "-".repeat(3) + " Transaction #" + transaction.getTransactionId() + "-".repeat(33));
                System.out.println(transaction);
                showTransactions();
                break;
            case 2:
                walletMenu();
                break;
            case 3:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                showTransactions();
        }
    }

    public void exchangeMenu(){
        String[] exchangeOptions = {"Buy Cryptocurrency", "Back to Main Menu"};
        System.out.println("\nEXCHANGE MENU:");

        final int[] index = {1};
        Map<Currency, BigDecimal> availableExchangeCurrencies = initializeCurrencyPricesUC.getCurrencyAvailables();
        availableExchangeCurrencies.forEach((currency, amount) ->
                System.out.printf("\t%d. %s | Price: %s | Amount: %s\n", index[0]++, currency.getName(), currency.getPrice(), amount));

        printMenu(exchangeOptions, "Select an option");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                buyFromExchange();
                exchangeMenu();
                break;
            case 2:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                exchangeMenu();
        }
    }

    public void walletMenu(){
        String[] walletOptions = {"Transactions", "Back to Main Menu"};
        System.out.println("\nWALLET:");
        viewWalletBalanceUC.setWallet(wallet);

        System.out.println("-".repeat(3) + " Fiat Currency " + "-".repeat(37));
        viewWalletBalanceUC.viewWalletFiats().forEach((fiat, amount) ->
                System.out.printf("\t%s | Amount: %s\n", fiat.getName(), amount));

        System.out.println("\n" + "-".repeat(3) + " Crypto Currency " + "-".repeat(35));
        viewWalletBalanceUC.viewWalletCryptocurrencies().forEach((crypto, amount) -> {
                BigDecimal amountPrice = crypto.getPrice().multiply(amount).setScale(2, RoundingMode.HALF_UP);
                System.out.printf("\t%s | Price: %s | Amount: %s ≈ %s\n", crypto.getName(), crypto.getPrice(), amount, amountPrice);
        });
        System.out.println("\n" + "-".repeat(55));

        System.out.println("\tBALANCE: " + wallet.getBalance().setScale(2, RoundingMode.HALF_UP) + "\n");

        printMenu(walletOptions, "Select an option:");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                showTransactions();
                break;
            case 2:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                walletMenu();
        }
    }

    public void printMenu(String[] options, String title) {
        int i;
        System.out.println(title);
        for (i = 0; i < options.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, options[i]);
        }
    }

    public void printAuthMenu(String[] bootOptions){
        while (true){
            printMenu(bootOptions, "Create an account or log in:");
            System.out.println("\nEnter an option: ");
            try {
                int optionSelected = StaticScanner.getInstance().nextInt();
                StaticScanner.getInstance().nextLine();
                switch (optionSelected) {
                    case 1:
                        registerUser(optionSelected);
                        System.out.println("User registered successfully!. Now you can log in.\n");
                        printAuthMenu(bootOptions);
                        break;
                    case 2:
                        loginUser(optionSelected);
                        System.out.println("User logged in successfully!\n");
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        if (optionSelected > 0 && optionSelected < bootOptions.length + 1)
                            System.err.println("Selección inválida. Intenta de nuevo.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("El valor ingresado no es el correcto. Por favor ingrese un número.");
                StaticScanner.getInstance().nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Se esperaba un número. Por favor, intente de nuevo.");
                StaticScanner.getInstance().nextLine();
            }
        }
    }

    public void printMainMenu(String[] mainOptions){
        String activeUserName = ActiveUser.getInstance().getActiveUser().getUsername();

        while (true){
            printMenu(mainOptions, "Welcome " + activeUserName + "! Choose an option:");
            System.out.println("\nEnter an option number: ");
            try {
                int optionSelected = StaticScanner.getInstance().nextInt();
                StaticScanner.getInstance().nextLine();
                switch (optionSelected){
                    case 1:
                        exchangeMenu();
                        break;
                    case 2:
                        System.out.println("Option 2 selected");
                        break;
                    case 3:
                        System.out.println("Option 3 selected");
                        break;
                    case 4:
                        walletMenu();
                        break;
                    case 5:
                        System.out.println("Option 5 selected");
                        break;
                    case 6:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        if (optionSelected > 0 && optionSelected < mainOptions.length + 1) {
                            System.err.println("Invalid option. Try again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("The value entered is not correct. Please enter a number.\n");
                StaticScanner.getInstance().nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: A number was expected. Please try again.\n");
                StaticScanner.getInstance().nextLine();
            }
        }
    }
}