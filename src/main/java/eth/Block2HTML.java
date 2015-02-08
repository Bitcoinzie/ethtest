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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Based on methods from http://forum.ethergit.com/discussion/7/how-to-parse-blocks-data-to-sql RomanJ
 * @author Bitcoinzie
 */
public class Block2HTML extends CompositeEthereumListener {
    
    //Listens for and writes blocks to a file named block-sql.txt as they come in from the net.
    private static final Logger blc = LoggerFactory.getLogger("Block2HTML");
    static Writer w = null;
    FileInputStream in;
    int i = 1;
    public static void runBlock2HTML(Ethereum ethereum) throws IOException {
        ethereum.addListener(new Block2HTML());
        blc.info("Block2HTML is running");
    }
    @Override
    public void onBlock(Block block) {
        File f = new File("blocks/blocks" + i + ".html");
        if(!f.exists()){
        try {
            
            w = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("blocks/blocks" + i + ".html", true), "utf-8"));
            w.write("<!doctype html>\n" +
                        "\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"utf-8\">\n" +
                        "\n" +
                        "  <title>Exploring Block: " + block.getNumber() + "</title>\n" +
                        "  <meta name=\"description\" content=\"EthChain Explorer\">\n" +
                        "  <meta name=\"author\" content=\"EthChain Explorer\">\n" +
                        "\n" +
                        "  <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                        "</head>\n" +
                        "\n" +
                        "<body>\n" +
                            "<header>\n" +
                                "<div id = \"logo\"></div>\n" +
                            "</header>\n");
            w.write("  <div id = \"blocks\">\n");
            w.write( "    <p>Block Number: " + block.getNumber() + "</p>\n");
            w.write( "    <p>Block Hash: "+ Hex.toHexString(block.getHash()) + "</p>\n");
            w.write( "    <p>Uncle Hash: " + Hex.toHexString(block.getUnclesHash()) + "</p>\n");
            w.write( "    <p>Nonce: " + Hex.toHexString(block.getNonce()) + "</p>\n");
            w.write( "    <p>State Root: " + Hex.toHexString(block.getStateRoot()) + "</p>\n");
            w.write( "    <p>Tx Trie Root: " + Hex.toHexString(block.getTxTrieRoot()) + "</p>\n");
            w.write( "    <p>Time Stamp: " + block.getTimestamp() + "</p>\n");
            w.write( "    <p>Gas Limit: " + block.getGasLimit() + "</p>\n");
            w.write( "    <p>Gas Used: " + block.getGasUsed() + "</p>\n");
            w.write( "    <p>Miner Coinbase: " + Hex.toHexString(block.getCoinbase()) + "</p>\n");
            w.write( "    <p>Block Difficulty: " + AccountStateUtils.toDecimal("0x" + Hex.toHexString(block.getDifficulty())) + "</p>\n");
            w.write( "    <p>Extra Data: " + "NULL" + "</p>\n");
            w.write( "    <p>Bloom Filter: " + Hex.toHexString(block.getLogBloom()) + "</p>\n");
            w.write( "    <div id = \"rlp\"><span>RLP Encoded: </span><p>" + Hex.toHexString(block.getEncoded()) + "</p></div>\n");
            w.write("  </div>\n"); 
            if(i>1)
                w.write("<div id = \"footer\"><a href=\"blocks" + (i - 1) + ".html\"><< Previous</a>&nbsp;&nbsp;<a href=\"blocks" + (i + 1) +".html\">Next >></a></div>");
            else{w.write("<a href = \"blocks" + (i + 1) +".html\">Next >></a></div>");}
        } catch (IOException ex) {
            // report
        } finally {
            try {
            w.write("</body>\n" + "</html>");
            i++;
            w.close();
        }catch (Exception ex) {}
            
        }
        }else{
            i++;
            try {
                in.close();
            } catch (IOException ex) {}
        }
                
    }
    @Override
    public void onSyncDone(){
        try {
            Logger html = LoggerFactory.getLogger("HTML");
            html.info("HTML Finished");
        }catch (Exception ex) {}
    }

    
}
