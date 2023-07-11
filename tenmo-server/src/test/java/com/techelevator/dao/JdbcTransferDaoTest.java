package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;





    public class JdbcTransferDaoTest extends BaseDaoTests{
        static BigDecimal bd1 = new BigDecimal("525.25");
       protected static final Transfer TRANSFER_1 = new Transfer(7001, 2001, 2002, 1 ,1,500.00);
        protected static final Transfer TRANSFER_2 = new Transfer(3002, 2002, 2003, 2, 2, bd1, "user2", "user1", "Send");
        private final Transfer TRANSFER_3 = new Transfer(3002, 2001,2003, 1, 2, bd1, "user3", "user1", "Pending");
        private static final Transfer TRANSFER_4 = new Transfer(7004, 2001,2003,1 , 2, 536.36 );

       private JdbcTransferDao sut;
        private  Transfer testTransfer;
        @Before
        public void setup() {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            sut = new JdbcTransferDao(jdbcTemplate);

        }



       @Test(expected = IllegalArgumentException.class)
       public void findTransferById_given_zero_throws_exception(){
            sut.getTransferById(0);

   }
     @Test
        public void findTransferByIdMatchesTransfer_returns_transfer(){
           Transfer transfer = sut.createTransfer(TRANSFER_3);
         System.out.println(TRANSFER_3.toString());
         System.out.println(sut.getTransferById(3002).toString());
         assertTransfersMatch(TRANSFER_3, sut.getTransferById(3002));

    }



//    @Test
//    public void updateTransfer_returns_updated_transfer(){
//            Transfer updateTransfer = sut.createTransfer(TRANSFER_2);
//            sut.updateTransfer(TRANSFER_2);
//            updateTransfer.setAccountTo(2003);
//            updateTransfer.setAccountFrom(2001);
//            updateTransfer.setStatusId(1);
//            updateTransfer.setTypeId(2);
//            updateTransfer.setAmount(BigDecimal.valueOf(777.00));
//            sut.updateTransfer(updateTransfer);
//
//        Transfer retrievedTransfer = sut.getTransferById(3001);
//        Assert.assertTrue(updateTransfer.equals(retrievedTransfer));
//    }

//
//        @Test
//        public void findTransferByAccountTo_returns_list_of_all_transfers_for_account(){
//          List<Transfer> transfers = sut.getListOfTransfers(1001);
//          Assert.assertEquals(2, transfers.size());
//           assertTransfersMatch(TRANSFER_1, transfers.get(0)); assertTransfersMatch(TRANSFER_4, transfers.get(1));
//
//           transfers = sut.getListOfTransfers(1002);
//          Assert.assertEquals(1, transfers.size());
//           assertTransfersMatch(TRANSFER_2, transfers.get(0));
//
//       }




        private void assertTransfersMatch(Transfer expected, Transfer actual) {
            Assert.assertEquals(expected, actual);

        }



}