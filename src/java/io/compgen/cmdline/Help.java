package io.compgen.cmdline;

import io.compgen.cmdline.annotation.Command;
import io.compgen.cmdline.annotation.Exec;
import io.compgen.cmdline.annotation.UnnamedArg;

import java.io.IOException;

@Command(name = "help", desc="Help for a specific command", category="help")
public class Help {
	@UnnamedArg(name="command")
	public void setCommand(String cmd) {
	}

	@Exec
	public void exec() throws IOException {
	}
}

