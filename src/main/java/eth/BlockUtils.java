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

import static java.lang.Thread.sleep;

import org.ethereum.core.Block;
import org.ethereum.facade.Ethereum;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.Utils;

import org.spongycastle.util.encoders.Hex;

/**
 *
 * @author Bitcoinzie
 */
public class BlockUtils {
    //TODO: include get block info by hash    
    private BlockUtils() {
    }
    private static final Ethereum ethereum = EthTest.ethereum;
    
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
        Boolean prin = true;
        while(blockindex>ethereum.getBlockchain().getSize()){
            if (prin){
                System.out.println("Sleeping until enough Blocks recieved");
                prin = false;
            }
            sleep(10L);  
        }return ethereum.getBlockchain().getBlockByNumber(blockindex);
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
