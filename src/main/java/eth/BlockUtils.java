/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eth;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import org.ethereum.core.Block;
import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.Utils;
import org.spongycastle.util.encoders.Hex;

/**
 *
 * @author Bitcoinzie
 */
public class BlockUtils {
    
    private BlockUtils() {
    }
    
    /**
     *
     * @param blockindex 
     * @return the uncle Block for the block given
     * @throws InterruptedException 
     */
    public static String uncle(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return block.getUncleList().toString();
    }
    
    /**
     *
     * @param blockindex long number representation of the block number
     * @return the block for the given number
     * @throws InterruptedException
     */
    public static Block block(Long blockindex) throws InterruptedException {
        while(blockindex>EthTest.ethereum.getBlockchain().getSize()){
            sleep(10L);  
        }return EthTest.ethereum.getBlockchain().getBlockByNumber(blockindex);
    }
    
    public static String blockHash(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getHash());
    }
    
    public static String uncHash(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getUnclesHash());
    }
    
    public static String blockNonce(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getNonce());
    }
    
    public static String blockState(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getStateRoot());
    }
    
    public static String blockTime(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return Utils.longToDateTime(block.getTimestamp());
    }
    
    public static Long blockGas(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return block.getGasLimit();
    }
    
    public static Long gasUsed(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return block.getGasUsed();
    }
    
    public static String txTrRoot(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getTxTrieRoot());
    }
    
    public static String blockMiner(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getCoinbase());
    }
    
    public static String blockDif(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getDifficulty());
    }
    
    public static String blockLBloom(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return ByteUtil.toHexString(block.getLogBloom());
    }
    
    public static String parentHash(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
         return ByteUtil.toHexString(block.getParentHash());
    }
    
    public static String blockEncoded(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return Hex.toHexString(block.getEncoded());
    }
    
    public static byte [] blockData(Long blockindex) throws InterruptedException {
        Block block = block(blockindex);
        return block.getExtraData();
    }
    

}
