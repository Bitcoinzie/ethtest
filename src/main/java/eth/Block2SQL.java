package eth;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.ethereum.core.Block;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.spongycastle.util.encoders.Hex;

/**
 * Based on methods from http://forum.ethergit.com/discussion/7/how-to-parse-blocks-data-to-sql RomanJ
 * @author Bitcoinzie
 */
public class Block2SQL extends EthereumListenerAdapter {
    
    //Listens for and writes blocks to a file named block-sql.txt as they come in from the net.
    public static void runBlock2SQL(Ethereum ethereum) throws IOException {
        ethereum.addListener(new Block2SQL());
        System.out.println("Block2SQL is running");

    }


    @Override
    public void onBlock(Block block) {

        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("block-sql.txt", true), "utf-8"));


            writer.write( "INSERT INTO block (\n");
            writer.write( "  number,\n");
            writer.write( "  hash,\n");
            writer.write( "  uncle_hash,\n");
            writer.write( "  nonce,\n");
            writer.write( "  state_root,\n");
            writer.write( "  tx_trie_root,\n");
            writer.write( "  time_stamp,\n");
            writer.write( "  gas_limit,\n");
            writer.write( "  gas_used,\n");
            writer.write( "  miner,\n");
            writer.write( "  difficulty,\n");
            writer.write( "  extra_data,\n");
            writer.write( "  bloom_filter,\n");
            writer.write( "  rlp)\n");

            writer.write( "VALUES (\n");
            writer.write("  '" + block.getNumber()+ "',\n");
            writer.write("  '" + Hex.toHexString(block.getHash()) + "',\n");
            writer.write("  '" + Hex.toHexString(block.getUnclesHash()) + "',\n");
            writer.write("  '" + Hex.toHexString(block.getNonce()) + "',\n");
            writer.write("  '" + Hex.toHexString(block.getStateRoot()) + "',\n");
            writer.write("  '" + Hex.toHexString(block.getTxTrieRoot()) + "',\n");
            writer.write("  " + block.getTimestamp()  + ",\n");
            writer.write("  " + block.getGasLimit()  + ",\n");
            writer.write("  " + block.getGasUsed()  + ",\n");
            writer.write("  '" + Hex.toHexString(block.getCoinbase()) + "',\n");
            writer.write("  '" + Hex.toHexString(block.getDifficulty()) + "',\n");
            writer.write("  " + "NULL" + ",\n");
            writer.write("  '" + Hex.toHexString(block.getLogBloom()) + "',\n");
            writer.write("  '" + Hex.toHexString(block.getEncoded()) + "'\n");
            writer.write( ");\n");
            
            writer.write("\n");
            writer.write("\n");
        } catch (IOException ex) {
            // report
        } finally {
            try {writer.close();} catch (Exception ex) {}
        }        
                
    }

    
}
