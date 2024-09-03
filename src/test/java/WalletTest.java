import com.globant.application.config.UsersLoader;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.InitializeCurrencyPricesUCImpl;
import com.globant.application.usecases.ViewWalletBalanceUCImpl;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.Exchange;
import com.globant.domain.repositories.UserManager;
import com.globant.domain.repositories.Wallet;

import java.math.BigDecimal;

public class WalletTest {
    static UserRepository userRepository = new UserManager();
    static UsersLoader usersLoader = new UsersLoader(userRepository);
    Exchange exchange = new Exchange();
    ViewWalletBalanceUCImpl viewWalletBalanceUC = new ViewWalletBalanceUCImpl();
    BigDecimal dollarPrice;
    BigDecimal euroPrice;
    Wallet wallet;

    private void loadWalletTestData() {
        Currency dollar = Currency.getInstance("USD");
        Currency euro = Currency.getInstance("EUR");

        wallet.deposit((Fiat) dollar, new BigDecimal(100));
        wallet.deposit((Fiat) euro, new BigDecimal(100));
        dollarPrice = dollar.getExchangeCurrencyRate();
        euroPrice = euro.getExchangeCurrencyRate();
    }

    private void testDeposit() {
        loadWalletTestData();
        wallet = ActiveUser.getInstance().getActiveUser().getWallet();

        System.out.println("\n###### Deposit test #####");
        InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC = new InitializeCurrencyPricesUCImpl(exchange);
        initializeCurrencyPricesUC.loadCurrencies();

        Currency.setReferenceCurrency(Currency.getInstance("USD"));

        ActiveUser.getInstance().setActiveUser(userRepository.getByUsername("anthleon"));
        try {
            BigDecimal expectedBalance = dollarPrice.multiply(new BigDecimal(100))
                    .add(euroPrice.multiply(new BigDecimal(100)));

            System.out.println("Actual Balance: " + wallet.getBalance());
            System.out.println("Expected balance: " + expectedBalance);
            if (wallet.getBalance().equals(expectedBalance)) {
                System.err.println("Deposit test passed");
            } else {
                throw new AssertionError("Deposit test failed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        usersLoader.loadUsers();
        WalletTest walletTest = new WalletTest();
        walletTest.testDeposit();
    }
}
