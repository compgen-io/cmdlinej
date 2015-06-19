package io.compgen.cmdline;

import io.compgen.cmdline.MainBuilder;
import io.compgen.cmdline.annotation.Command;

import java.io.IOException;

@Command(name = "license", desc="Show the license", category="help")
public class License {
	public void exec() throws IOException {
		System.out.println(MainBuilder.readFile("LICENSE"));
	}
}

