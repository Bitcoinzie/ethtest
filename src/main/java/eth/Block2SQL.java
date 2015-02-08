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

import org.ethereum.core.Block;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.CompositeEthereumListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Based on methods from http://forum.ethergit.com/discussion/7/how-to-parse-blocks-data-to-sql RomanJ
 * @author Bitcoinzie
 */
public class Block2SQL extends CompositeEthereumListener {
    
    //Listens for and writes blocks to a file named block-sql.txt as they come in from the net.
    public static void runBlock2SQL(Ethereum ethereum) throws IOException {
        ethereum.addListener(new Block2SQL());
        Logger b2sql = LoggerFactory.getLogger("Block2SQL");
        b2sql.info("Block2SQL is running");

    }


    @Override
    public void onBlock(Block block) {

        Writer w = null;

        try {
            w = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("block-sql.txt", true), "utf-8"));


            w.write( "INSERT INTO block (\n");
            w.write( "  number,\n");
            w.write( "  hash,\n");
            w.write( "  uncle_hash,\n");
            w.write( "  nonce,\n");
            w.write( "  state_root,\n");
            w.write( "  tx_trie_root,\n");
            w.write( "  time_stamp,\n");
            w.write( "  gas_limit,\n");
            w.write( "  gas_used,\n");
            w.write( "  miner,\n");
            w.write( "  difficulty,\n");
            w.write( "  extra_data,\n");
            w.write( "  bloom_filter,\n");
            w.write( "  rlp)\n");

            w.write( "VALUES (\n");
            w.write("  '" + block.getNumber()+ "',\n");
            w.write("  '" + Hex.toHexString(block.getHash()) + "',\n");
            w.write("  '" + Hex.toHexString(block.getUnclesHash()) + "',\n");
            w.write("  '" + Hex.toHexString(block.getNonce()) + "',\n");
            w.write("  '" + Hex.toHexString(block.getStateRoot()) + "',\n");
            w.write("  '" + Hex.toHexString(block.getTxTrieRoot()) + "',\n");
            w.write("  " + block.getTimestamp()  + ",\n");
            w.write("  " + block.getGasLimit()  + ",\n");
            w.write("  " + block.getGasUsed()  + ",\n");
            w.write("  '" + Hex.toHexString(block.getCoinbase()) + "',\n");
            w.write("  '" + Hex.toHexString(block.getDifficulty()) + "',\n");
            w.write("  " + "NULL" + ",\n");
            w.write("  '" + Hex.toHexString(block.getLogBloom()) + "',\n");
            w.write("  '" + Hex.toHexString(block.getEncoded()) + "'\n");
            w.write( ");\n");
            
            w.write("\n");
            w.write("\n");
        } catch (IOException ex) {
            // report
        } finally {
            try {w.close();} catch (Exception ex) {}
        }        
                
    }

    
}
