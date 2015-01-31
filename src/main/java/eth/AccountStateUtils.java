package eth;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.ethereum.core.Account;
import org.ethereum.core.AccountState;
import static org.ethereum.core.Denomination.toFriendlyString;
import org.ethereum.core.Wallet;
import org.ethereum.db.ContractDetails;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.facade.Repository;
import org.ethereum.net.peerdiscovery.PeerInfo;
import org.ethereum.util.Utils;
import static org.ethereum.util.Utils.hexStringToDecimalString;
import org.ethereum.vm.DataWord;


/**
 *
 * @author Bitcoinzie
 */
public class AccountStateUtils {
    
    private static final Ethereum ethereum = EthTest.ethereum;
    private static final Wallet wallet = ethereum.getWallet();
    private static final Repository repository = ethereum.getRepository();
    private static final ArrayList acntList = new ArrayList<>();
    private static Account account;
    
    private AccountStateUtils() {
    }
    
    /**
     * loads the users wallet accounts.
     * @return ArrayList<byte[]> returns ArrayList of owned Accounts
     */
    public static ArrayList<Account> wallet(){
        for(Iterator<Account> it = wallet.getAccountCollection().iterator(); it.hasNext();) {
            account = it.next();
            System.out.println(account);
             if(!acntList.contains(account))
                 acntList.add(account);
        }
        return acntList;
    }
    
    /**
     *
     * Adds new account to the users wallet
     */
    public static void addNew() {
        wallet.addNewAccount();
    }
    
    //avoids null pointer when getting balance by Account.getBalance
    public static void saveAcnt(byte[] addr){
        repository.addBalance(addr, BigInteger.ZERO);
        repository.createAccount(addr);
    }
    
    /**
     *
     * @param str hex string representing a value in the format 0x21 to be converted to decimal in the format 15
     * @return  string representation of a decimal number
     */
        public static String toDecimal(String str) {
        return hexStringToDecimalString(str);
    }
    
    /**
     *Checks the balance at a give address
     * @param address a 20 character Hex String
     * @return returns BigInt value at the given account
     */
    public static BigInteger balanceAt(String address) {
	byte[] addr = Utils.addressStringToBytes(address);
	if(!repository.isExist(addr)){
            return repository.getBalance(addr);
        }else{            
            return repository.getBalance(addr);
        }
    }
    
    /**
     *
     * @param addr byte [] Address of the Account to check storage
     * @param key DataWord key to the storage location
     * @return the values stored or null if empty
     */
    public static DataWord storageAt(byte[] addr, DataWord key) {
         return repository.getStorageValue(addr, key);
    }
    
    /**
     *
     * @param address
     * @return byte array containing the code 
     */
    public static byte[] codeAt(String address) {
	//Any Valid 20 char hex address make sure to validate your address
	//String address = "cd2a3d9f938e13cd947ec05abc7fe734df8dd826"
	//Use the utils class to make it a byte[] array
	byte[] addr = Utils.addressStringToBytes(address);
	
	//Get the repository and return the contract details of the address provided
	ContractDetails contractDetails = repository.getContractDetails(addr);
	
	//Get the code from inside the contract details and assign it to a byte array
	final byte[] programCode = contractDetails.getCode();
	return programCode;
    }
    
    /**
     *
     * @param addr byte[] array representing the address of the account to get the transactions nonce from
     * @return nonce BigInteger transactions nonce
     */
    public static BigInteger countAt(byte[] addr) {
        return repository.getNonce(addr); 
    }
  
    /**
     *
     * @return int value representing the number of peers this client is connected to
     */
    public static int peerCount() {
        Set<PeerInfo> peers = ethereum.getPeers();
        return peers.size();
    }
    
    /**
     *
     * @param BgIn Balance of the Account
     * @return Word representation of the balance.. "Infinity ETHER"
     */
    public static String toEth(BigInteger BgIn) {
        return toFriendlyString(BgIn);
    }
    
    
}
