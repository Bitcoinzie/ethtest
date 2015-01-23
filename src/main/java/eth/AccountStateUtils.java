package eth;

import static java.lang.Thread.sleep;
import org.ethereum.core.Account;
import static org.ethereum.core.Denomination.toFriendlyString;
import org.ethereum.core.Wallet;
import org.ethereum.db.ContractDetails;
import org.ethereum.facade.Repository;
import org.ethereum.util.Utils;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.ethereum.core.Block;
import org.ethereum.net.peerdiscovery.PeerInfo;
import static org.ethereum.util.Utils.hexStringToDecimalString;
import org.ethereum.vm.DataWord;


/**
 *
 * @author Bitcoinzie
 */
public class AccountStateUtils {
    
    private AccountStateUtils() {
    }
    
    /**
     * loads the users wallet accounts.
     * @return ArrayList<byte[]> returns ArrayList of owned Accounts
     */
    public static ArrayList<Account> wallet(){
        Wallet wallet = EthTest.ethereum.getWallet();
        ArrayList acntList = new ArrayList<>();
        Account account;
        for (Iterator<Account> it = wallet.getAccountCollection().iterator(); it.hasNext();) {
            account = it.next();
            acntList.add(account);
        }
        return acntList;
    }
    
    public static void addNew(){
        Wallet wallet = EthTest.ethereum.getWallet();
        wallet.addNewAccount();
    }
    
    /**
     *
     * @param str hex string representing a value in the format 0x21 to be converted to decimal 15
     * @return  string representation of a decimal number
     */
        public static String toDecimal(String str){
        return hexStringToDecimalString(str);
    }
    
    /**
     *Checks the balance at a give address
     * @param address a 20 character Hex String
     * @return returns BigInt value at the given account
     */
    public static BigInteger balanceAt(String address){
        Repository repository = EthTest.ethereum.getRepository();
	byte[] addr = Utils.addressStringToBytes(address);
	if(!repository.isExist(addr)){
            return null;
        }else{            
            return repository.getBalance(addr);
        }
    }
    
    public static DataWord storageAt(byte[] addr, DataWord key){
        Repository repository = EthTest.ethereum.getRepository();
        return repository.getStorageValue(addr, key);
    }
    
    /**
     *
     * @param address
     * @return byte array containing the code 
     */
    public static byte[] codeAt(String address){
	//Any Valid 20 char hex address make sure to validate your address
	//String address = "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
	//Use the utils class to make it a byte[] array
	byte[] addr = Utils.addressStringToBytes(address);
	
	//Get the repository and return the contract details of the address provided
	ContractDetails contractDetails = EthTest.ethereum.getRepository().getContractDetails(addr);
	
	//Get the code from inside the contract details and assign it to a byte array
	final byte[] programCode = contractDetails.getCode();
	return programCode;
    }
    
    /**
     *
     * @param addr byte[] array representing the address of the account to get the transactions nonce from
     * @return nonce BigInteger transactions nonce
     */
    public static BigInteger countAt(byte[] addr){
        return EthTest.ethereum.getRepository().getNonce(addr); 
    }
  
    /**
     *
     * @return int value representing the number of peers this client is connected to
     */
    public static int peerCount(){
        Set<PeerInfo> peers = EthTest.ethereum.getPeers();
        return peers.size();
    }
    
    /**
     *
     * @param blockindex long number representation of the block number
     * @return the block for the given number
     * @throws InterruptedException
     */
    public static Block block(Long blockindex) throws InterruptedException{
        while(blockindex>EthTest.ethereum.getBlockchain().getSize()){
            sleep(10L);  
        }return EthTest.ethereum.getBlockchain().getBlockByNumber(blockindex);
    }
    
    public static String toEth(BigInteger BgIn){
        return toFriendlyString(BgIn);
    }
    
}
