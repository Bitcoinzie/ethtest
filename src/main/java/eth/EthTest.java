package eth;


import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.SwingUtilities;
import static org.ethereum.config.SystemProperties.CONFIG;
import org.ethereum.core.Account;
import static org.ethereum.core.Denomination.toFriendlyString;
import org.ethereum.core.Transaction;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.vm.DataWord;
import org.spongycastle.util.encoders.Hex;


/**
 *
 * @author Bitcoinzie
 */
public class EthTest extends EthereumListenerAdapter {
    static Ethereum ethereum;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        ethereum =  EthereumFactory.createEthereum();
        SwingUtilities.invokeLater(() -> {
            ethereum.connect(CONFIG.activePeerIP(),
                    CONFIG.activePeerPort());
        });
        ethereum.addListener(new EthereumListenerAdapter() {
        @Override
        public void trace(final String output) {
            if (output != null){
                //System.out.println(output);
            }
        }
        });
        
        Block2SQL.runBlock2SQL(ethereum);
        
        int i = 0;//Just something to use as a simple iterator
        
        //arraylist to store the accounts
        ArrayList<Account> accounts = AccountStateUtils.wallet();
        
        //Assign accounts
        Account acnt0 = accounts.get(i);
        Account acnt1 = accounts.get(i+1);
        AccountStateUtils.addNew();
        ArrayList<Account> accountsNew = AccountStateUtils.wallet();
        ArrayList<Transaction> t = null;
        Account acnt2 = accountsNew.get(i+2);
        //byte [] ac = acnt0.getBytes();
        BigInteger nonce = AccountStateUtils.countAt(Hex.toHexString(acnt0.getEcKey().getAddress()).getBytes());
        System.out.println("Account 1 Nonce: " + nonce);
        Long num = 23L;
        
        System.out.println(BlockUtils.block(num));
        System.out.println(BlockUtils.uncle(num));
        System.out.println("Disecting the block #" + num);
        System.out.println("hash,\n" + BlockUtils.blockHash(num));
        System.out.println("parent hash,\n" + BlockUtils.parentHash(num));
        System.out.println("uncle hash,\n" + BlockUtils.uncHash(num));
        System.out.println("nonce,\n" + BlockUtils.blockNonce(num));
        System.out.println("state_root,\n" + BlockUtils.blockState(num));
        System.out.println("tx_trie_root,\n" + BlockUtils.txTrRoot(num));
        System.out.println("time_stamp,\n" + BlockUtils.blockTime(num));
        System.out.println("gas_limit,\n" + BlockUtils.blockGas(num));
        System.out.println("gas_used,\n" + BlockUtils.gasUsed(num));
        System.out.println("miner,\n" + BlockUtils.blockMiner(num));
        System.out.println("difficulty,\n" + BlockUtils.blockDif(num));
        System.out.println("extra_data,\n" + Arrays.toString(BlockUtils.blockData(num)));
        System.out.println("bloom_filter,\n" + BlockUtils.blockLBloom(num));
        System.out.println("rlp,\n" + BlockUtils.blockEncoded(num));
        System.out.println("Transactions,\n" + TransUtils.blockTransactions(num, t));
        
        System.out.println(Hex.toHexString(ethereum.getRepository().getAccountsKeys().iterator().next()));
        
        //This call (balanceAt) is sent a 20 character Hex string 
        BigInteger bal = AccountStateUtils.balanceAt(Hex.toHexString(acnt0.getEcKey().getAddress()));
        BigInteger bal1 = AccountStateUtils.balanceAt(Hex.toHexString(acnt1.getEcKey().getAddress()));
        BigInteger bal2 = AccountStateUtils.balanceAt(Hex.toHexString(acnt2.getEcKey().getAddress()));
        
        DataWord key = new DataWord(acnt0.getEcKey().getPrivKeyBytes());
        System.out.println("Storage at " + acnt0 + " is " + AccountStateUtils.storageAt(Hex.toHexString(acnt0.getEcKey().getAddress()).getBytes(), key));
       
        //This call can be sent like above 
        byte[] code = AccountStateUtils.codeAt(Hex.toHexString(acnt0.getEcKey().getAddress()));
        if (code.length == 0){
            System.out.println("No code associated with this Account");
        }else{
            System.out.println(Hex.toHexString(code));
        }
        
        //Just printing our results from above.. we'll do something with the data we generated later
        System.out.println(AccountStateUtils.toDecimal("0x15"));
        System.out.println(AccountStateUtils.toDecimal("0x657468657265756d000000000000000000000000000000000000000000000000"));
        System.out.println("Connected to: " + AccountStateUtils.peerCount() + " Peers");
        System.out.println(accounts);
        System.out.println(accountsNew);
        System.out.println("Account 1: " + Hex.toHexString(acnt0.getEcKey().getAddress()).toUpperCase());
        System.out.println("Balance 1: Friendly " + toFriendlyString(bal) + " Long Balance " + bal);
        System.out.println("Account 2: " + Hex.toHexString(acnt1.getEcKey().getAddress()).toUpperCase());
        System.out.println("Balance 2: " + bal1);
        System.out.println("Account 3: " + Hex.toHexString(acnt2.getEcKey().getAddress()).toUpperCase());
        System.out.println("Balance 3: " + bal2);
        System.out.println(AccountStateUtils.toEth(bal));
    }
}
