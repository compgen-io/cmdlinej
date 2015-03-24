package io.compgen.cmdline.impl;

import io.compgen.cmdline.annotation.Option;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public abstract class AbstractOutputCommand extends AbstractCommand {
    protected OutputStream out = System.out;

    @Option(desc="Output filename (optionally gzip/bzip2 compressed)", charName="o", defaultValue="-", name="output")
    public void setOutputName(String outputName) throws IOException {
        if (outputName.equals("-")) {
            out = System.out;
        } else if (outputName.endsWith(".gz")) {
            out = new GZIPOutputStream(new FileOutputStream(outputName));
        } else {
            out = new BufferedOutputStream(new FileOutputStream(outputName));
        }
    }
    
    public void close() throws IOException {
        if (out != System.out) {
            out.close();
        }
    }
}
