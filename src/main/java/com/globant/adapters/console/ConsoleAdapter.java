package com.globant.adapters.console;

import com.globant.application.port.in.*;
import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.entities.orders.BuyOrder;
import com.globant.domain.entities.orders.SellOrder;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.util.NoCurrencyAvailableException;
import com.globant.domain.util.StaticScanner;
import com.globant.domain.util.UserAuthException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

public class ConsoleAdapter {
    private final UserRegistrationUC userRegistrationUC;
    private final UserLoginUC userLoginUC;
    private final UserLogoutUC userLogoutUC;
    private final InitializeCurrencyPricesUC initializeCurrencyPricesUC;
    private final DepositMoneyUC depositMoneyUC;
    private final ViewWalletBalanceUC viewWalletBalanceUC;
    private final BuyFromExchangeUC buyFromExchangeUC;
    private final ViewTransactionHistoryUC viewTransactionHistoryUC;
    private final PlaceBuyOrderUC placeBuyOrderUC;
    private final PlaceSellOrderUC placeSellOrderUC;

    WalletRepository wallet;

    String[] bootOptions = {"Sign Up", "Log In", "Exit"};
    String[] mainOptions = {
            "Exchange Cryptocurrencies",
            "Deposit",
            "My Wallet",
            "Orders",
            "Place Orders",
            "Logout",
            "Exit"
    };

    private static boolean invalidData = true;
    public ConsoleAdapter(
            UserRegistrationUC userRegistrationUC,
            UserLoginUC userLoginUC,
            UserLogoutUC userLogoutUC,
            InitializeCurrencyPricesUC initializeCurrencyPricesUC,
            DepositMoneyUC depositMoneyUC,
            ViewWalletBalanceUC viewWalletBalanceUC,
            BuyFromExchangeUC buyFromExchangeUC,
            ViewTransactionHistoryUC viewTransactionHistoryUC,
            PlaceBuyOrderUC placeBuyOrderUC,
            PlaceSellOrderUC placeSellOrderUC
    ){
        this.userRegistrationUC = userRegistrationUC;
        this.userLoginUC = userLoginUC;
        this.userLogoutUC = userLogoutUC;
        this.initializeCurrencyPricesUC = initializeCurrencyPricesUC;
        this.depositMoneyUC = depositMoneyUC;
        this.viewWalletBalanceUC = viewWalletBalanceUC;
        this.buyFromExchangeUC = buyFromExchangeUC;
        this.viewTransactionHistoryUC = viewTransactionHistoryUC;
        this.placeBuyOrderUC = placeBuyOrderUC;
        this.placeSellOrderUC = placeSellOrderUC;
    }
    public void boot(){
        System.out.println("### Welcome to the Crypto Exchange App ###");
        printAuthMenu(bootOptions);
        printMainMenu(mainOptions);

    }
    private void registerUser(int optionSelected){
        System.out.println("\nSIGN UP:");
        String username = getUsernameFromConsole();
        String email = getEmailFromConsole(optionSelected);
        System.out.print("Enter your password: ");
        String password = StaticScanner.getInstance().nextLine();
        userRegistrationUC.registerUser(username, email, password);
    }
    private void loginUser(int optionSelected){
        System.out.println("\nLOG IN:");
        String email = getEmailFromConsole(optionSelected);
        String password = getPasswordFromConsole(email);
        userLoginUC.logInUser(email, password);
        wallet = ActiveUser.getInstance().getActiveUser().getWallet();
        viewWalletBalanceUC.setWallet(wallet);
    }

    private String getUsernameFromConsole(){
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

    private String getEmailFromConsole(int optionSelected){
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

    private String getPasswordFromConsole(String email){
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

    private void buyFromExchange() {
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

    private void depositMoney(){
        System.out.println("\n\nDEPOSIT:");


        List<Fiat> fiatAvailables = initializeCurrencyPricesUC.getFiatAvailables();
        int[] index = {1};
        fiatAvailables.forEach((currency) ->
                System.out.printf("\t%d. %s\n", index[0]++, currency.getName()));

        System.out.println("Select the type of fiat money you want to deposit:");
        int currencySelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();
        Currency currency = fiatAvailables.get(currencySelected - 1);
        System.out.printf("You selected: %s\n", currency.getName());

        System.out.println("\nEnter the amount to deposit: ");
        BigDecimal amount = StaticScanner.getInstance().nextBigDecimal().setScale(2, RoundingMode.HALF_UP);
        StaticScanner.getInstance().nextLine();

        System.out.printf("\nThe total amount to deposit is: %s\n", amount);

        System.out.println("Confirm the deposit? (y/n)");
        String confirm = StaticScanner.getInstance().nextLine();
        if (confirm.equals("n")) {
            System.err.println("Deposit canceled.");
            return;
        } else if (!confirm.equals("y")) {
            System.err.println("Invalid option. Try again.");
            return;
        }
        depositMoneyUC.depositFiat(currency, amount);
        System.out.printf("Deposit successful!. Your new balance is: %s\n\n", viewWalletBalanceUC.getWalletBalance());
    }

    private void showTransactions(){
        String[] transactionOptions = {"Show specific details", "Return to my wallet", "Back to Main Menu"};
        System.out.println("\n\nTRANSACTIONS:");
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

    private void exchangeMenu(){
        String[] exchangeOptions = {"Buy Cryptocurrency", "Back to Main Menu"};
        System.out.println("\n\nEXCHANGE MENU:");

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

    private void walletMenu(){
        String[] walletOptions = {"Transactions", "Back to Main Menu"};
        System.out.println("\n\nWALLET:");

        System.out.println("-".repeat(3) + " Fiat Currency " + "-".repeat(37));
        viewWalletBalanceUC.getWalletFiats().forEach((fiat, amount) -> {
                BigDecimal amountEquivalence = fiat.getExchangeCurrencyRate();
                System.out.printf("\t%s | Amount: %s ≈ %s %s\n",
                fiat.getName(), amount, amount.multiply(amountEquivalence).setScale(2, RoundingMode.HALF_UP), Currency.getReferenceCurrency().getName());
        });

        System.out.println("\n" + "-".repeat(3) + " Crypto Currency " + "-".repeat(35));
        viewWalletBalanceUC.getWalletCryptocurrencies().forEach((crypto, amount) -> {
                BigDecimal amountPrice = crypto.getPrice().multiply(amount).setScale(2, RoundingMode.HALF_UP);
                System.out.printf("\t%s | Price: %s | Amount: %s ≈ %s %s\n", crypto.getName(), crypto.getPrice(), amount, amountPrice, Currency.getReferenceCurrency().getName());
        });
        System.out.println("\n" + "-".repeat(55));

        System.out.println("\tBALANCE: " + viewWalletBalanceUC.getWalletBalance().setScale(2, RoundingMode.HALF_UP) + "\n");

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

    private void placeOrdersMenu() {
        String[] placeOrderOptions = {"Place Buy Order", "Place Sell Order", "Back to Main Menu"};
        printMenu(placeOrderOptions, "\n\nSelect an option:");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                placeBuyOrder();
                break;
            case 2:
                placeSellOrder();
                break;
            case 3:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                placeOrdersMenu();
        }
        placeOrdersMenu();
    }


    private void showBuyOrders() {
        String[] showBuyOrderOptions = {"Return to my orders", "Back to Main Menu"};

        System.out.println("\n\nBUY ORDERS");
        placeBuyOrderUC.getBuyOrders().forEach(
            (currency, orders) ->
                orders.forEach((price, orderList) ->
                    orderList.forEach(order ->
                        System.out.printf("\tBUY ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n",
                        ((BuyOrder) order).getBuyOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount())))
        );

        printMenu(showBuyOrderOptions, "\nSelect an option:");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                showOrders();
                break;
            case 2:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                showBuyOrders();
        }
    }

    private void showSellOrders() {
        String[] showSellOrderOptions = {"Return to my orders", "Back to Main Menu"};

        System.out.println("\n\nSELL ORDERS");
        placeSellOrderUC.getSellOrders().forEach(
                (currency, orders) ->
                        orders.forEach((price, orderList) ->
                                orderList.forEach(order ->
                                        System.out.printf("\tSELL ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n",
                                                ((SellOrder) order).getSellOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount())))
        );

        printMenu(showSellOrderOptions, "\nSelect an option:");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                showOrders();
                break;
            case 2:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                showBuyOrders();
        }
    }

    private void showOrders() {
        String[] showOrderOptions = {"Show Buy Orders", "Show Sell Orders", "Back to Main Menu"};

        System.out.println("\n\nMY ORDERS");

        System.out.println("-".repeat(3) + " Buy Orders " + "-".repeat(75));
        placeBuyOrderUC.getBuyOrdersByUsername(ActiveUser.getInstance().getActiveUser().getUsername()).forEach(
            (currency, orders) ->
                orders.forEach((price, orderList) ->
                    orderList.forEach(order ->
                        System.out.printf("\tBUY ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n",
                        ((BuyOrder) order).getBuyOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount())))
        );

        System.out.println("\n" + "-".repeat(3) + " Sell Orders " + "-".repeat(74));
        placeSellOrderUC.getSellOrdersByUsername(ActiveUser.getInstance().getActiveUser().getUsername()).forEach(
                (currency, orders) ->
                    orders.forEach((price, orderList) ->
                            orderList.forEach(order ->
                                    System.out.printf("\tSELL ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n",
                                            ((SellOrder) order).getSellOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount())))
        );
        System.out.println("\n" + "-".repeat(90));

        printMenu(showOrderOptions, "\nSelect an option:");
        System.out.println("\nEnter an option: ");
        int optionSelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();

        switch (optionSelected){
            case 1:
                showBuyOrders();
                break;
            case 2:
                showSellOrders();
                break;
            case 3:
                printMainMenu(mainOptions);
                break;
            default:
                System.err.println("Invalid option. Try again.");
                showOrders();
        }
    }

    private void placeBuyOrder() {
        System.out.println("\n\nPLACE BUY ORDER:");

        System.out.println("-".repeat(3) + " Fiat currencies availables: " + "-".repeat(29));
        Map<Currency, BigDecimal> availableExchangeCurrencies = initializeCurrencyPricesUC.getCurrencyAvailables();
        int[] index = {1};
        availableExchangeCurrencies.forEach((currency, amount) ->
                System.out.printf("\t%d. %s | Price: %s\n", index[0]++, currency.getName(), currency.getPrice()));
        System.out.println("-".repeat(40) + "\n");

        System.out.println("Select the currency you want to buy:");
        int currencySelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();
        Currency currency = availableExchangeCurrencies.keySet().stream().toList().get(currencySelected - 1);
        System.out.printf("You selected: %s\n", currency.getName());

        System.out.println("\nEnter the amount you want to buy: ");
        BigDecimal amount = StaticScanner.getInstance().nextBigDecimal();
        StaticScanner.getInstance().nextLine();

        System.out.println("\nEnter the maximum price you want to pay per unit: ");
        BigDecimal price = StaticScanner.getInstance().nextBigDecimal();
        StaticScanner.getInstance().nextLine();

        placeBuyOrderUC.createBuyOrder(currency, amount, price);
    }

    private void placeSellOrder() {
        System.out.println("\n\nPLACE SELL ORDER:");

        System.out.println("-".repeat(3) + " Fiat currencies availables: " + "-".repeat(29));
        Map<Currency, BigDecimal> availableExchangeCurrencies = initializeCurrencyPricesUC.getCurrencyAvailables();
        int[] index = {1};
        availableExchangeCurrencies.forEach((currency, amount) ->
                System.out.printf("\t%d. %s | Price: %s\n", index[0]++, currency.getName(), currency.getPrice()));
        System.out.println("-".repeat(40) + "\n");

        System.out.println("Select the currency you want to sell:");
        int currencySelected = StaticScanner.getInstance().nextInt();
        StaticScanner.getInstance().nextLine();
        Currency currency = availableExchangeCurrencies.keySet().stream().toList().get(currencySelected - 1);
        System.out.printf("You selected: %s\n", currency.getName());

        System.out.println("\nEnter the amount you want to sell: ");
        BigDecimal amount = StaticScanner.getInstance().nextBigDecimal();
        StaticScanner.getInstance().nextLine();

        System.out.println("\nEnter the minimum price you want to get per unit: ");
        BigDecimal price = StaticScanner.getInstance().nextBigDecimal();
        StaticScanner.getInstance().nextLine();

        placeSellOrderUC.createSellOrder(currency, amount, price);
    }

    private void printMenu(String[] options, String title) {
        int i;
        System.out.println(title);
        for (i = 0; i < options.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, options[i]);
        }
    }

    private void printAuthMenu(String[] bootOptions){
        while (true){
            printMenu(bootOptions, "\nCreate an account or log in:");
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
                        printMainMenu(mainOptions);
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

    private void printMainMenu(String[] mainOptions){
        String activeUserName = ActiveUser.getInstance().getActiveUser().getUsername();

        while (true){
            printMenu(mainOptions, "\n\nWelcome " + activeUserName + "! Choose an option:");
            System.out.println("\nEnter an option number: ");
            try {
                int optionSelected = StaticScanner.getInstance().nextInt();
                StaticScanner.getInstance().nextLine();
                switch (optionSelected){
                    case 1:
                        exchangeMenu();
                        break;
                    case 2:
                        depositMoney();
                        break;
                    case 3:
                        walletMenu();
                        break;
                    case 4:
                        showOrders();
                        break;
                    case 5:
                        placeOrdersMenu();
                        break;
                    case 6:
                        userLogoutUC.logout();
                        System.out.println("\nUser logged out successfully!\n");
                        printAuthMenu(bootOptions);
                        break;
                    case 7:
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