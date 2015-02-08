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

import org.ethereum.config.SystemProperties;
import org.ethereum.core.Account;
import org.ethereum.core.AccountState;
import static org.ethereum.core.Denomination.toFriendlyString;
import org.ethereum.core.Transaction;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.listener.CompositeEthereumListener;
import org.ethereum.util.Utils;
import org.ethereum.vm.DataWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger introLogger = LoggerFactory.getLogger("Intro");
    private static final Logger user = LoggerFactory.getLogger("User Out");
    public static void main(String[] args) throws IOException, InterruptedException {
        String version = SystemProperties.CONFIG.projectVersion();

        introLogger.info("");
        introLogger.info("|Ξ|  EthereumJ [v" + version + "]");
        introLogger.info("|Ξ|  Code by Roman Mandeleil, (c) 2014.");
        introLogger.info("|Ξ|  Contribution: Nick Savers ");
        introLogger.info("|Ξ|  Based on a design by Vitalik Buterin.");
        introLogger.info("");
        introLogger.info("java.version: " + System.getProperty("java.version"));
        introLogger.info("java.home:    " + System.getProperty("java.home"));
        introLogger.info("java.vendor:  " + System.getProperty("java.vendor"));
        introLogger.info("");

        if (Utils.JAVA_VERSION < 1.7 && Utils.JAVA_VERSION != 0) {
            introLogger.info("EthereumJ support version 1.7 and higher of Java Runtime please update");
            System.exit(0);
        }
        eth =  EthereumFactory.createEthereum();
        String ip = "5.1.83.141";
        int port = 30303;
        SwingUtilities.invokeLater(() -> {
            eth.connect(ip, port);
        });
        Block2HTML.runBlock2HTML(eth);
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
                    user.info(accounts.toString());
                    //Re-Assign Accounts
                    accounts = AccountStateUtils.wallet();
                    user.info(accounts.toString());
                    ArrayList<Transaction> t = null;
                    acnt2 = accounts.get(++i);

                    AccountStateUtils.saveAcnt(acnt2.getAddress());
                    //introLogger.info("get balance at new account through world manager: " + wm.getRepository().getBalance(acnt0.getAddress()));

                    BigInteger nonce = AccountStateUtils.countAt(acnt0.getAddress());
                    user.info("Account 1 Nonce: " + nonce);
                    Long num = 23L;
                    //Testing for peers
                    boolean prin = true;
                    user.info("Connected to: " + AccountStateUtils.peerCount() + " Peers");
                    int pri = 0;
                    while(AccountStateUtils.peerCount() == 0 && prin) {
                        if(pri != 1){
                            user.info("No Peers... \nAttempting to connect to peers at" + " " + ip + " " + port);
                            pri++;
                        }
                        if(AccountStateUtils.peerCount() > 0)
                            prin = false;
                    }
                    //Print block information
                    user.info("Connected to: " + AccountStateUtils.peerCount() + " Peers");
                    user.info(BlockUtils.block(num).toFlatString());
                    user.info(BlockUtils.uncle(num));
                    user.info("Disecting the block #" + num);
                    user.info("parent hash: " + BlockUtils.parentHash(num));
                    user.info("uncle hash: " + BlockUtils.uncHash(num));
                    user.info("nonce: " + BlockUtils.blockNonce(num));
                    user.info("state_root: " + BlockUtils.blockState(num));
                    user.info("tx_trie_root: " + BlockUtils.txTrRoot(num));
                    user.info("time_stamp: " + BlockUtils.blockTime(num));
                    user.info("gas_limit: " + BlockUtils.blockGas(num));
                    user.info("gas_used: " + BlockUtils.gasUsed(num));
                    user.info("miner: " + BlockUtils.blockMiner(num));
                    user.info("difficulty: " + BlockUtils.blockDif(num));
                    user.info("extra_data: " + Arrays.toString(BlockUtils.blockData(num)));
                    user.info("bloom_filter: " + BlockUtils.blockLBloom(num));
                    user.info("rlp: " + BlockUtils.blockEncoded(num));
                    user.info("Transactions: " + TransUtils.blockTransactions(num, t));

                    user.info(Hex.toHexString(eth.getRepository().getAccountsKeys().iterator().next()));
                    
                    //This call (balanceAt) is sent a 20 character Hex string 
                    AccountState account = eth.getRepository().getAccountState(acnt2.getAddress());
                    user.info(account.toString());
                    account = eth.getRepository().createAccount(acnt2.getAddress());
                    user.info("acnt2 bal: " + account.getBalance());
                    BigInteger bal = AccountStateUtils.balanceAt(Hex.toHexString(acnt0.getEcKey().getAddress()));
                    BigInteger bal1 = AccountStateUtils.balanceAt(Hex.toHexString(acnt1.getEcKey().getAddress()));
                    BigInteger bal2 = AccountStateUtils.balanceAt(Hex.toHexString(acnt2.getEcKey().getAddress()));

                    user.info("New Account is: " + acnt2);
                    //eth.getRepository().addBalance(acnt2.getAddress(), BigInteger.TEN);
                    /*
                    * acnt2.getBalance()); new accounts cause nullpointer exception when queried from withing the Account
                    * So for now we get the balance of an new account by calling the repository and getting the balance at address: 
                    * eth.getRepository().getBalance(acnt2.getAddress())
                    */
                    user.info("New Acnt Bal: " + eth.getRepository().getBalance(acnt2.getAddress()));
                    user.info("Acnt1 Bal: " + acnt0.getBalance());
                    user.info("Acnt2 Bal: " + acnt1.getBalance());

                    DataWord key = new DataWord(acnt0.getEcKey().getPrivKeyBytes());
                    user.info("Storage at " + Hex.toHexString(acnt0.getAddress()) + " is " + AccountStateUtils.storageAt((acnt0.getAddress()), key));

                    byte[] code = AccountStateUtils.codeAt(Hex.toHexString(acnt0.getAddress()));
                    if (code.length == 0){
                        user.info("No code associated with this Account");
                    }else{
                        user.info(Hex.toHexString(code));
                    }

                    //Just printing our results from above.. we'll do something with the data we generated later
                    user.info(AccountStateUtils.toDecimal("0x15"));
                    user.info(AccountStateUtils.toDecimal("0x657468657265756d000000000000000000000000000000000000000000000000"));

                    user.info("Account 1: " + Hex.toHexString(acnt0.getAddress()).toUpperCase());
                    user.info("Balance 1: Friendly " + toFriendlyString(bal) + " Long Balance " + bal);
                    user.info("Account 2: " + Hex.toHexString(acnt1.getAddress()).toUpperCase());
                    user.info("Balance 2: " + bal1);
                    user.info("Account 3: " + Hex.toHexString(acnt2.getAddress()).toUpperCase());
                    user.info("Balance 3: " + bal2);
                    user.info(AccountStateUtils.toEth(bal));
                    user.info("Now Connected to: " + AccountStateUtils.peerCount() + " Peers");
                    user.info(AccountStateUtils.countAt(acnt0).toString());
                    
                    //user.info(wm.getRepository().getAccountState(acnt0.getAddress())); //nullpointer
                    //user.info(wm.getActivePeer().toString()); //nullpointer
                    //user.info(Arrays.toString(wm.getBlockchain().getBestBlock().getCoinbase())); //nullpointer
                    //user.info(wm.isBlockchainLoading()); //nullpointer
                    //WalletUtils.accountTest_1();
                    System.exit(0);
                } catch (InterruptedException e) {
                }
            }
        });
    }
}

