import com.globant.application.config.UsersLoader;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.DepositMoneyUCImpl;
import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.repositories.ActiveUser;
import com.globant.domain.repositories.UserManager;

import java.math.BigDecimal;

public class DepositFiatMoneyTest {
    static UserRepository userRepository = new UserManager();
    static UsersLoader usersLoader = new UsersLoader(userRepository);
    static User activeUser;
    DepositMoneyUCImpl depositMoneyUC;
    private void loadDepositData() {
        usersLoader.loadUsers();
        ActiveUser.getInstance().setActiveUser(userRepository.getByUsername("anthleon"));
        depositMoneyUC = new DepositMoneyUCImpl();
    }
    private void testDepositFiatMoney() {
        try {
            activeUser = ActiveUser.getInstance().getActiveUser();
            BigDecimal initialBalance = activeUser.getWallet().getBalance();
            System.out.println("Balance before deposit: " + initialBalance);

            depositMoneyUC.depositFiat(Currency.getInstance("USD"), BigDecimal.valueOf(100));
            BigDecimal balanceAfterDeposit = activeUser.getWallet().getBalance();
            System.out.println("Balance after deposit: " + balanceAfterDeposit);

            if (balanceAfterDeposit.compareTo(initialBalance.add(BigDecimal.valueOf(100))) == 0) {
                System.err.println("Deposit was successful");
            } else {
                throw new AssertionError("Deposit failed");
            }
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        DepositFiatMoneyTest depositFiatMoneyTest = new DepositFiatMoneyTest();
        depositFiatMoneyTest.loadDepositData();
        depositFiatMoneyTest.testDepositFiatMoney();
    }
}
