
package eth;

import static eth.BlockUtils.block;

import java.util.ArrayList;

import org.ethereum.core.Block;
import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;

/**
 *
 * @author Bitcoinzie
 */
public class TransUtils {
    //TODO: include get block info by hash
    private TransUtils(){
    }
    
    //static ArrayList<Transaction> t;
    public static ArrayList<Transaction> blockTransactions(Long blockindex, ArrayList<Transaction> t) throws InterruptedException {
        Block block = block(blockindex);
        for (Transaction transaction : block.getTransactionsList()) {
           Transaction trans = transaction;
           t.add(transaction);
        }return t;
    }
    
    public static String transSender(Transaction t) {
        return ByteUtil.toHexString(t.getSender());
    }
    
    public static String transGasPrice(Transaction t) {
        return ByteUtil.toHexString(t.getGasPrice());
    }
    
    public static String transMaxGas(Transaction t) {
        return ByteUtil.toHexString(t.getGasLimit());
    }
    
    public static String transReceiver(Transaction t) {
        return ByteUtil.toHexString(t.getReceiveAddress());
    }
    
    public static String transHash(Transaction t) {
        return ByteUtil.toHexString(t.getHash());
    }
    
    public static String transValue(Transaction t) {
        return t.getValue() != null ? ByteUtil.toHexString(t.getValue
                ()) : "No Transactions";
    }
    
    public static String transNonce(Transaction t) {
        return ByteUtil.toHexString(t.getNonce());
    }
    
    public static String transData(Transaction t) {
        return ByteUtil.toHexString(t.getData());
    }
    
}
