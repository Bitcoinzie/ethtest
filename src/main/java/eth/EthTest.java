/*
The MIT License (MIT)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package eth;

import org.ethereum.core.Account;
import org.ethereum.core.AccountState;
import static org.ethereum.core.Denomination.toFriendlyString;
import org.ethereum.core.Transaction;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.listener.CompositeEthereumListener;
import org.ethereum.vm.DataWord;

import org.spongycastle.util.encoders.Hex;

import javax.swing.SwingUtilities;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;



/**
 *
 * @author Bitcoinzie
 */
public class EthTest {
    public static Ethereum eth;
    public static Account acnt0;
    public static Account acnt1;
    public static Account acnt2;
    //private static final Logger logger = LoggerFactory.getLogger("general");
    public static void main(String[] args) throws IOException, InterruptedException {
        eth =  EthereumFactory.createEthereum();
        String ip = "5.1.83.141";
        int port = 30303;
        SwingUtilities.invokeLater(() -> {
            eth.connect(ip, port);
        });
        Block2SQL.runBlock2SQL(eth);
        eth.addListener(new CompositeEthereumListener() {
            @Override
            public void onSyncDone() {
                
                try {
                    int i = 0;//Just something to use as a simple iterator
                    //arraylist to store the accounts
                    ArrayList<Account> accounts = AccountStateUtils.wallet();
                    //Assign accounts
                    acnt0 = accounts.get(i);
                    acnt1 = accounts.get(++i);
                    //Add a new account
                    AccountStateUtils.addNew();
                    System.out.println(accounts);
                    //Re-Assign Accounts
                    accounts = AccountStateUtils.wallet();
                    System.out.println(accounts);
                    ArrayList<Transaction> t = null;
                    acnt2 = accounts.get(++i);

                    AccountStateUtils.saveAcnt(acnt2.getAddress());
                    //System.out.println("get balance at new account through world manager: " + wm.getRepository().getBalance(acnt0.getAddress()));

                    BigInteger nonce = AccountStateUtils.countAt(acnt0.getAddress());
                    System.out.println("Account 1 Nonce: " + nonce);
                    Long num = 23L;
                    //Testing for peers
                    boolean prin = true;
                    System.out.println("Connected to: " + AccountStateUtils.peerCount() + " Peers");
                    int pri = 0;
                    while(AccountStateUtils.peerCount() == 0 && prin) {
                        if(pri != 1){
                            System.out.println("No Peers... \nAttempting to connect to peers at" + " " + ip + " " + port);
                            pri++;
                        }
                        if(AccountStateUtils.peerCount() > 0)
                            prin = false;
                    }
                    //Print block information
                    System.out.println("Connected to: " + AccountStateUtils.peerCount() + " Peers");
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

                    System.out.println(Hex.toHexString(eth.getRepository().getAccountsKeys().iterator().next()));
                    
                    //This call (balanceAt) is sent a 20 character Hex string 
                    AccountState account = eth.getRepository().getAccountState(acnt2.getAddress());
                    System.out.println(account);
                    account = eth.getRepository().createAccount(acnt2.getAddress());
                    System.out.println("acnt2 bal: " + account.getBalance());
                    BigInteger bal = AccountStateUtils.balanceAt(Hex.toHexString(acnt0.getEcKey().getAddress()));
                    BigInteger bal1 = AccountStateUtils.balanceAt(Hex.toHexString(acnt1.getEcKey().getAddress()));
                    BigInteger bal2 = AccountStateUtils.balanceAt(Hex.toHexString(acnt2.getEcKey().getAddress()));

                    System.out.println("New Account is: " + acnt2);
                    //eth.getRepository().addBalance(acnt2.getAddress(), BigInteger.TEN);
                    /*
                    * acnt2.getBalance()); new accounts cause nullpointer exception when queried from withing the Account
                    * So for now we get the balance of an new account by calling the repository and getting the balance at address: 
                    * eth.getRepository().getBalance(acnt2.getAddress())
                    */
                    System.out.println("New Acnt Bal: " + eth.getRepository().getBalance(acnt2.getAddress()));
                    System.out.println("Acnt1 Bal: " + acnt0.getBalance());
                    System.out.println("Acnt2 Bal: " + acnt1.getBalance());

                    DataWord key = new DataWord(acnt0.getEcKey().getPrivKeyBytes());
                    System.out.println("Storage at " + Hex.toHexString(acnt0.getAddress()) + " is " + AccountStateUtils.storageAt((acnt0.getAddress()), key));

                    byte[] code = AccountStateUtils.codeAt(Hex.toHexString(acnt0.getAddress()));
                    if (code.length == 0){
                        System.out.println("No code associated with this Account");
                    }else{
                        System.out.println(Hex.toHexString(code));
                    }

                    //Just printing our results from above.. we'll do something with the data we generated later
                    System.out.println(AccountStateUtils.toDecimal("0x15"));
                    System.out.println(AccountStateUtils.toDecimal("0x657468657265756d000000000000000000000000000000000000000000000000"));

                    System.out.println("Account 1: " + Hex.toHexString(acnt0.getAddress()).toUpperCase());
                    System.out.println("Balance 1: Friendly " + toFriendlyString(bal) + " Long Balance " + bal);
                    System.out.println("Account 2: " + Hex.toHexString(acnt1.getAddress()).toUpperCase());
                    System.out.println("Balance 2: " + bal1);
                    System.out.println("Account 3: " + Hex.toHexString(acnt2.getAddress()).toUpperCase());
                    System.out.println("Balance 3: " + bal2);
                    System.out.println(AccountStateUtils.toEth(bal));
                    System.out.println("Now Connected to: " + AccountStateUtils.peerCount() + " Peers");
                    System.out.println(AccountStateUtils.countAt(acnt0));
                    //System.out.println(wm.getRepository().getAccountState(acnt0.getAddress())); //nullpointer
                    //System.out.println(wm.getActivePeer().toString()); //nullpointer
                    //System.out.println(Arrays.toString(wm.getBlockchain().getBestBlock().getCoinbase())); //nullpointer
                    //System.out.println(wm.isBlockchainLoading()); //nullpointer
                    //WalletUtils.accountTest_1();
                    System.exit(0);
                } catch (InterruptedException ex) {
                }
            }
        });
    }
}

