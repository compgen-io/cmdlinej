package io.compgen.impl;

import io.compgen.Exec;
import io.compgen.annotation.Option;

public abstract class AbstractCommand implements Exec {
    protected boolean verbose = false;

    @Option(desc = "Verbose output", charName = "v")
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    @Option(desc = "Show help", charName = "h")
    public void showHelp() {}
}
