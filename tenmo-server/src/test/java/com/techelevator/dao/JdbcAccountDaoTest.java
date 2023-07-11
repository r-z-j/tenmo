package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTest extends BaseDaoTests {
    static BigDecimal bd_1 = new BigDecimal("1000.00");
    protected static final Account ACCOUNT_1 = new Account (2001, 1001, bd_1);
    protected static final Account ACCOUNT_2 = new Account (2002, 1002, bd_1);
    protected static final Account ACCOUNT_3 = new Account (2003, 1003, bd_1);
    private JdbcAccountDao sut;
    @Before
    public void setup() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);

    }

    @Test
    public void getAccountByAccountId_returns_account(){
        sut.getAccountById(2001);
    }


    @Test
    public void getBalanceByAccountId_returns_balance(){
        sut.getBalanceByAccountId(2003);
    }


    @Test
    public void getBalanceByUserId_returns_balance(){
        sut.getBalanceByUserId(1001);
    }


    @Test
    public void getAccountByUserId_returns_account(){
        sut.getAccountByUserId(1002);
    }

}
