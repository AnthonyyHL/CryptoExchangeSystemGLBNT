import com.globant.application.config.UsersLoader;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.BuyFromExchangeUCImpl;
import com.globant.application.usecases.InitializeCurrencyPricesUCImpl;
import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.repositories.UserManager;
import com.globant.domain.util.NoCurrencyAvailableException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BuyFromExchangeTest {
    static UserRepository userRepository = new UserManager();
    static UsersLoader usersLoader = new UsersLoader(userRepository);
    static User activeUser;
    Exchange exchange = new Exchange();
    BuyFromExchangeUCImpl buyFromExchangeUC;
    private void loadExchangeData() {
        InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC = new InitializeCurrencyPricesUCImpl(exchange);
        initializeCurrencyPricesUC.loadCurrencies();
        initializeCurrencyPricesUC.loadCurrencyOnExchange();
        ActiveUser.getInstance().setActiveUser(userRepository.getByUsername("anthleon"));
        buyFromExchangeUC = new BuyFromExchangeUCImpl(exchange);
        System.out.println("Active user: " + ActiveUser.getInstance().getActiveUser().getUsername());
    }
    private void testBuyFromExchange() {
        try {
            System.out.println("\n###### Buy from exchange test #####");
            Map<Currency, BigDecimal> currencies = exchange.getCurrencies();
            List<Currency> currenciesList = currencies.keySet().stream().toList();
            BigDecimal currentAmount = currencies.get(currenciesList.get(0));
            System.out.println("Current amount: " + currentAmount);

            try {
                buyFromExchangeUC.buyCurrency(1, new BigDecimal(20.5));
            } catch (NoCurrencyAvailableException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Amount after buying: " + currencies.get(currenciesList.get(0)));

            if (currentAmount.compareTo(currencies.get(currenciesList.get(0))) > 0) {
                System.err.println("Buy from exchange test passed");
            } else {
                throw new AssertionError("Buy from exchange test failed");
            }
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }

    private void testUserWalletUpdate() {
        try {
            System.out.println("\n###### User wallet update test #####");
            Currency currency = Currency.getInstance("ETH");
            BigDecimal amount = new BigDecimal(20.5);

            BigDecimal walletAmount = activeUser.getWallet().getCryptocurrencies().get(currency);
            System.out.println("Current wallet amount: " + walletAmount);

            buyFromExchangeUC.updateUserWallet(currency, amount);

            BigDecimal currentWalletAmount = activeUser.getWallet().getCryptocurrencies().get(currency);
            System.out.println("Wallet amount after update: " + currentWalletAmount);

            if (currentWalletAmount.compareTo(walletAmount.add(amount)) == 0) {
                System.err.println("User wallet update test passed");
            } else {
                throw new AssertionError("User wallet update test failed");
            }
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        usersLoader.loadUsers();
        BuyFromExchangeTest buyFromExchangeTest = new BuyFromExchangeTest();
        buyFromExchangeTest.loadExchangeData();
        activeUser = ActiveUser.getInstance().getActiveUser();

        buyFromExchangeTest.testBuyFromExchange();
        buyFromExchangeTest.testUserWalletUpdate();
    }
}
