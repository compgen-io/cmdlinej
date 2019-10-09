package io.compgen.cmdline.impl;

import io.compgen.cmdline.annotation.Option;

public abstract class AbstractCommand  {
    protected boolean verbose = false;

    @Option(desc = "Verbose output", charName = "v")
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    @Option(desc = "Show help", charName = "h", name="help", showHelp=true)
    public void showHelp() {}
}
