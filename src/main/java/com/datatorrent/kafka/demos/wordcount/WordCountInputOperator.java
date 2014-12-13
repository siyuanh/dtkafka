package com.datatorrent.kafka.demos.wordcount;

import com.datatorrent.lib.io.SimpleSinglePortInputOperator;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>WordCountInputOperator class.</p>
 *
 * @since 0.3.2
 */
public class WordCountInputOperator extends SimpleSinglePortInputOperator<String> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WordCountInputOperator.class);
    protected long averageSleep = 300;
    protected long sleepPlusMinus = 100;
    protected String fileName = "com/datatorrent/demos/wordcount/samplefile.txt";

    public void setAverageSleep(long as) {
        averageSleep = as;
    }

    public void setSleepPlusMinus(long spm) {
        sleepPlusMinus = spm;
    }

    public void setFileName(String fn) {
        fileName = fn;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        DataInputStream in = null;
        InputStream fstream = null;

        while (true) {
            try {
                String line;
                fstream = this.getClass().getClassLoader().getResourceAsStream(fileName);

                in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));

                while ((line = br.readLine()) != null) {
                    String[] words = line.trim().split("[\\p{Punct}\\s\\\"\\'“”]+");
                    for (String word : words) {
                        word = word.trim().toLowerCase();
                        if (!word.isEmpty()) {
                            //System.out.println("Sending "+word);
                            outputPort.emit(word);
                        }
                    }
                    try {
                        Thread.sleep(averageSleep + (new Double(sleepPlusMinus * (Math.random() * 2 - 1))).longValue());
                    } catch (InterruptedException ex) {
                        // nothing
                    }
                }

            } catch (IOException ex) {
                logger.debug(ex.toString());
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (IOException exc) {
                    // nothing
                }
            }
        }
    }
}
