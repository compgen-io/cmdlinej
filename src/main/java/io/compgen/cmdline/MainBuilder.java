package io.compgen.cmdline;

import io.compgen.cmdline.annotation.Command;
import io.compgen.cmdline.annotation.Exec;
import io.compgen.cmdline.annotation.Option;
import io.compgen.cmdline.annotation.UnknownArgs;
import io.compgen.cmdline.annotation.UnnamedArg;
import io.compgen.cmdline.exceptions.CommandArgumentException;
import io.compgen.cmdline.exceptions.MissingCommandException;
import io.compgen.cmdline.exceptions.MissingExecException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainBuilder {
	public class CmdArgs {
		public final Map<String, String> cmdargs;
		public final List<String> unnamed;
		private List<String> usedArgs = new ArrayList<String>();

		public CmdArgs(Map<String, String> cmdargs, List<String> unnamed) { //, Map<String, String> unknown) {
			this.cmdargs = Collections.unmodifiableMap(cmdargs);
			this.unnamed = (unnamed == null) ? null: Collections.unmodifiableList(unnamed);
//			this.unknown = Collections.unmodifiableMap(unknown);
		}
		
		public void setArgUsed(String arg) {
			usedArgs.add(arg);
		}
		
		public List<String[]> getUnusedArgs() {
			List<String[]> out = new ArrayList<String[]>();
			
			for (String k: cmdargs.keySet()) {
				if (!usedArgs.contains(k)) {
					String[] kv = new String[] { k, cmdargs.get(k) };
					out.add(kv);
				}
			}
			
			return out;
		}
	}
	
	public class OptionHelp {
		public final String name;
		public final String charName;
		public final String desc;
		public final boolean isBoolean;
		public final String helpVal;
		public OptionHelp(String name, String charName, String desc, boolean isBoolean, String helpVal) {
			this.name = name;
			this.charName = charName;
			this.desc = desc;
			this.isBoolean = isBoolean;
			this.helpVal = helpVal;
		}
		public int size() {
			return (name.length() > 0 ? name.length() : charName.length()) + (isBoolean ? 0 : helpVal.length() + 1);
		}
	}

	private static Map<String, Class<?>> execs = new HashMap<String, Class<?>>();

	private String defaultCategory = "General";
	private String progname = null;
	private String defaultUsage = null;
	private String helpHeader = null;
	private String helpFooter = null;
	
	private String[] categoryOrder = null;
	
	public MainBuilder setCategoryOrder(String[] categoryOrder) {
		this.categoryOrder = categoryOrder;
		return this;
	}	
	
	public MainBuilder setDefaultUsage(String defaultUsage) {
		this.defaultUsage = defaultUsage;
		return this;
	}

	public MainBuilder setProgName(String progname) {
		this.progname = progname;
		return this;
	}

	public MainBuilder setHelpHeader(String helpHeader) {
		this.helpHeader = helpHeader;
		return this;
	}

	public MainBuilder setHelpFooter(String helpFooter) {
		this.helpFooter = helpFooter;
		return this;
	}

	public MainBuilder setDefaultCategory(String defaultCategory) {
		this.defaultCategory = defaultCategory;
		return this;
	}

	
	public MainBuilder addCommand(Class<?> clazz) throws MissingExecException {
		String name = clazz.getSimpleName();
		Command annotation = clazz.getAnnotation(Command.class);
		if (annotation != null) {
			name = annotation.name();
		
		}

		Method execMethod = findExecMethod(clazz);
		
		if (execMethod == null) {
			throw new MissingExecException("Could not find a valid @Exec method for class: "+clazz.getName());
		}

		execs.put(name, clazz);
		return this;
	}

	public void showCommands() throws MissingCommandException {
		showCommands(System.err);
	}

	public void showCommands(OutputStream out) throws MissingCommandException {
		PrintStream ps = new PrintStream(out);
		if (helpHeader != null) {
			ps.println(helpHeader);
			ps.println();
		}
		if (defaultUsage != null) {
			ps.println(defaultUsage);
			ps.println();
		}
		ps.println("Available commands:");

		boolean hasExperimental = false;
		
		int minsize = 4;
		String spacer = "    ";
		for (String cmd : execs.keySet()) {
			if (cmd.length() > minsize) {
	            Command c = execs.get(cmd).getAnnotation(Command.class);
	            if (c.experimental()) {
	            	hasExperimental = true;
	            }
	            if (c.deprecated()) {
	                continue;
	            }
                minsize = cmd.length();
	            if (c.experimental()) {
	                minsize += 1;
	            }
			}
		}
		Map<String, List<String>> progs = new HashMap<String, List<String>>();

		for (String cmd : execs.keySet()) {
			Command c = execs.get(cmd).getAnnotation(Command.class);
			if (c != null) {
                if (c.deprecated() || c.hidden()) {
                    continue;
                }
                
                String cat = c.category().equals("") ? defaultCategory: c.category();
                
				if (!progs.containsKey(cat)) {
					progs.put(cat, new ArrayList<String>());
				}

				if (!c.desc().equals("")) {
					spacer = "";
					for (int i = c.experimental() ? cmd.length()+1: cmd.length(); i < minsize; i++) {
						spacer += " ";
					}
					spacer += " - ";
					if (c.experimental()) { 
                        progs.get(cat).add("  " + cmd + "*" + spacer + c.desc());
                    } else {
					    progs.get(cat).add("  " + cmd + spacer + c.desc());
				    }   
				} else {
                    if (c.experimental()) { 
                        progs.get(cat).add("  " + cmd + "*");
                    } else {
                        progs.get(cat).add("  " + cmd);
                    }   
				}
			} else {
				throw new MissingCommandException("Command: "+cmd+ "missing @Command annotation");
			}
		}

		List<String> cats = new ArrayList<String>();
		if (categoryOrder == null) {
			cats.addAll(progs.keySet());
			Collections.sort(cats);
		} else {
			for (String c: categoryOrder) {
				cats.add(c);
			}
		}

		for (String cat : cats) {
            ps.println("[" + cat + "]");
            if (progs.get(cat) != null) {
				Collections.sort(progs.get(cat));
				for (String line : progs.get(cat)) {
					ps.println(line);
				}
            }
            ps.println("");
		}

//		spacer = "";
//		for (int i = 12; i < minsize; i++) {
//			spacer += " ";
//		}
//		spacer += " - ";
//		System.err.println("[help]");
//        System.err.println("  help command" + spacer
//                + "Help message for the given command");
//        System.err.println("  license     " + spacer
//                + "Display licenses");
		
        if (hasExperimental) {
        	ps.println("* = experimental command");
        	ps.println();
        }
        if (helpFooter != null) {
        	ps.println(helpFooter);
        }
    	ps.println();
	}

	public void showCommandHelp(Class<?> clazz) throws MissingCommandException {
		showCommandHelp(clazz, System.err);
	}

	public void showCommandHelp(String cmd) throws MissingCommandException {
		showCommandHelp(cmd, System.err);
	}

	public void showCommandHelp(String cmd, OutputStream out) throws MissingCommandException {
		if (!execs.containsKey(cmd)) {
			throw new MissingCommandException();
		}
		Class<?> clazz = execs.get(cmd);
		showCommandHelp(clazz, out);
	}
	
	public void showCommandHelp(Class<?> clazz, OutputStream out) throws MissingCommandException {
		SortedMap<String, OptionHelp> opts = new TreeMap<String, OptionHelp>();
		int minsize = 4;
		boolean showCharOptions = false;
		
		for (Method m: clazz.getMethods()) {
			Option opt = m.getAnnotation(Option.class);
			if (opt != null) {
				String k = "";
				String name = "";
				String charName = "";
				String desc = opt.desc();
				String helpValue = opt.helpValue();
				boolean isBoolean = isOptionBoolean(m);
				boolean isInt = isOptionInteger(m);
				
				if (!opt.name().equals("")) {
					name = opt.name();
					k = name;
				}
				
				if (!opt.charName().equals("")) {
					charName = opt.charName();
					showCharOptions = true;
					if (k.equals("")) {
						k = charName;
					}
				} 
				
				if (name.equals("") && charName.equals("")) {
					if (m.getName().startsWith("set")) {
						name = m.getName().substring(3).toLowerCase();
					} else {
						name = m.getName().toLowerCase();
					}
					k = name;
				}
								
				if (!opt.defaultValue().equals("")) {
					if (opt.defaultText().equals("")) {
						desc += " (default: " + opt.defaultValue()+ ")";
					} else {
						desc += " (default: " + opt.defaultText()+ ")";
					}
				}
				
				if (helpValue.equals("") && !isBoolean) {
					if (isInt) {
						helpValue = "N";
					} else {
						helpValue = "val";
					}
				}
				
				OptionHelp optHelp = new OptionHelp(name, charName, desc, isBoolean, helpValue);
				opts.put(k, optHelp);
				minsize = Math.max(minsize, optHelp.size());
			}
		}

		PrintStream ps = new PrintStream(out);
		
		if (helpHeader != null) {
			ps.println(helpHeader);
			ps.println();
		}
		
		Command command = clazz.getAnnotation(Command.class);
		ps.print(command.name());
		if (!command.desc().equals("")) {
			ps.print(" - " + command.desc());
			ps.println();
		}
		if (!command.doc().equals("")) {
			ps.print(command.doc());
			ps.println();
		}
		ps.println();
		ps.print("Usage:" + (progname == null ? "" : " " +progname )+ " "+ command.name() + ((opts.size()>0)? " [options]":""));
		for (Method m: clazz.getMethods()) {
			UnnamedArg uarg = m.getAnnotation(UnnamedArg.class);
			if (uarg != null) {
				if (uarg.required()) {
					ps.print(" "+uarg.name());
				} else {
					ps.print(" {"+uarg.name()+"}");
				}
			}
		}

		ps.println();

		if (opts.size() > 0) {
			ps.println();
			ps.println("Options:");
		}
		
		for (String k:opts.keySet()) {
			String spacer = "";
			OptionHelp optHelp = opts.get(k);
			for (int i=optHelp.size(); i<minsize; i++) {
				spacer += " ";
			}

			String s = "  ";
			
			if (!optHelp.charName.equals("")) {
				s += "-" + optHelp.charName;
				if (!optHelp.name.equals("")) {
					s += " ";
				}
			} else if (showCharOptions) {
				s += "   ";
			}
			
			if (!optHelp.name.equals("")) {
				s += "--" + optHelp.name;
			}
			
			if (!optHelp.isBoolean) {
				s += " " + optHelp.helpVal;
			}
			if (optHelp.name.equals("")) {
				s += "    ";
			}

			s += spacer + "  : " + optHelp.desc;
			
			ps.println(s);
			
//			int spacePos = k.indexOf(' ');		
//			if (k.endsWith("  ")) {
//				ps.println("     --" + k + spacer + " : " + opts.get(k));
//			} else if (k.endsWith(" ")) {
//				ps.println("  -" + k + spacer + "     : " + opts.get(k));
//			} else if (spacePos > -1) {
//				String switched = "-" + k.substring(spacePos+1) + " --" + k.substring(0, spacePos);
//				ps.println("  " + switched + spacer + "   : " + opts.get(k));
//			} else {
//				if (k.endsWith(" ")) {
//					ps.println("  --" + k + spacer + " : " + opts.get(k));
//				}
//			}
		}
		
		if (!command.footer().equals("")) {
			ps.println();
			ps.println(command.footer());
		}
		
		if (helpFooter != null) {
			ps.println();
			ps.println(helpFooter);
		}
    	ps.println();
	}

	public void runClass(Class<?> clazz, String[] args) throws Exception {
		CmdArgs cmdargs = extractArgs(args, clazz, 0);
		findAndRunInner(clazz, cmdargs);
	}

	public void findAndRun(String[] args) throws Exception {
		if (args.length == 0) {
			showCommands();
			return;
		} else if (args[0].equals("help")) {
			if (args.length == 1) {
				showCommands();
				return;
			} else if (!execs.containsKey(args[1])) {
				System.err.println("ERROR: Unknown command: " + args[1]);
				System.err.println();
				showCommands();
				System.exit(1);
			} else{
				showCommandHelp(args[1]);
			}
			return;
		} else if (!execs.containsKey(args[0])) {
			System.err.println("ERROR: Unknown command: " + args[0]);
			System.err.println();
			showCommands();
			System.exit(1);
		}

		Class<?> clazz = execs.get(args[0]);
		CmdArgs cmdargs = extractArgs(args, clazz);
		findAndRunInner(clazz, cmdargs);
	}
	
	private void findAndRunInner(Class<?> clazz, CmdArgs cmdargs) throws Exception {
		List<String> errors = new ArrayList<String>();

		Method execMethod = findExecMethod(clazz);
		if (execMethod == null) {
			throw new RuntimeException("Missing @Exec method in class: "+clazz.getCanonicalName());
		}

		Object obj = clazz.newInstance();
		
		try {
			for (Method m: clazz.getMethods()) {
				if (m.getName().equals("setMainBuilder") && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(MainBuilder.class)) {
					m.invoke(obj, this);
				}
				Option opt = m.getAnnotation(Option.class);
				if (opt != null) {
					String val = null;
					if (cmdargs.cmdargs.containsKey(opt.charName())) {
						val = cmdargs.cmdargs.get(opt.charName());
						cmdargs.setArgUsed(opt.charName());
					} else if (cmdargs.cmdargs.containsKey(opt.name())) {
						val = cmdargs.cmdargs.get(opt.name());
						cmdargs.setArgUsed(opt.name());
					} else {
						if (m.getName().startsWith("set")) {
							String k = m.getName().substring(3).toLowerCase();
							val = cmdargs.cmdargs.get(k);
							cmdargs.setArgUsed(k);
						} else {
							String k = m.getName().toLowerCase();
							val = cmdargs.cmdargs.get(k);
							cmdargs.setArgUsed(k);
						}
					}
					if (opt.showHelp() && val != null) {
						showCommandHelp(clazz);
						System.exit(1);
					}
					
					if (val == null) {
						// missing value, try defaults
						if (!opt.defaultValue().equals("")) {
							invokeMethod(obj, m, opt.defaultValue());
						} else if (opt.required()) {
							errors.add("Missing argument: "+opt.name());
						}
					} else if (val.equals("")) {
						// naked option w/o value
						invokeMethodBoolean(obj, m, true);
					} else {
						invokeMethod(obj, m, val);
					}
					continue;
				}
				
				UnnamedArg unnamed = m.getAnnotation(UnnamedArg.class);
				if (unnamed != null) {
					if (cmdargs.unnamed == null) {
						if (unnamed.required()) {
							errors.add("Missing argument: "+unnamed.name());
						} else if (!unnamed.defaultValue().equals("")) {
							invokeMethod(obj, m, unnamed.defaultValue());
						}
						continue;
					}
	
					if (m.getParameterTypes()[0].isArray()) {					
						String[] ar = (String[]) cmdargs.unnamed.toArray(new String[cmdargs.unnamed.size()]);
						m.invoke(obj, (Object) ar);
					} else if (m.getParameterTypes()[0].equals(List.class)) {
						m.invoke(obj, Collections.unmodifiableList(cmdargs.unnamed));				
					} else {
						invokeMethod(obj, m, cmdargs.unnamed.get(0));
					}
				}
			}

			for (Method m: clazz.getMethods()) {
				UnknownArgs unknown = m.getAnnotation(UnknownArgs.class);
				if (unknown != null) {
					for (String[] kv: cmdargs.getUnusedArgs()) {
						m.invoke(obj, (Object[]) kv);
					}
				}
			}
			
			if (errors.size() == 0) {
				execMethod.invoke(obj);
			} else {
				for (String error: errors) {
					System.err.println("ERROR: "+error);
				}
				System.err.println();
				showCommandHelp(clazz);
				System.exit(1);
			}
		} catch (Exception e) {
			if (e instanceof CommandArgumentException) {
				System.err.println("ERROR: " + e.getMessage());
				System.err.println();
				showCommandHelp(clazz);
				System.exit(1);
			} else if (e.getCause() != null && e.getCause() instanceof CommandArgumentException) {
				System.err.println("ERROR: " + e.getCause().getMessage());
				System.err.println();
				showCommandHelp(clazz);
				System.exit(1);
			} else {
				System.err.println("ERROR: " + e.getMessage());
				e.printStackTrace();					
				System.exit(1);
			}
		}
	}

	private Method findExecMethod(Class<?> clazz) {
		Method namedExecMethod = null;
		for (Method m: clazz.getMethods()) {
			if (m.getName().equals("exec") && m.getParameterTypes().length == 0) {
				namedExecMethod = m;
			}
			Exec exec = m.getAnnotation(Exec.class);
			if (exec != null) {
				return m;
			}
		}
		return namedExecMethod;
	}

	private boolean isOptionBoolean(Class<?> clazz, String name) {
		for (Method m:clazz.getMethods()) {
			Option opt = m.getAnnotation(Option.class);
			if (opt != null && (opt.name().equals(name) || opt.charName().equals(name))) {
				return isOptionBoolean(m);
			}
		}
		return false;
	}
	
	private boolean isOptionBoolean(Method m) {
		if (m.getParameterTypes().length == 0) {
			return true;
		} else if (m.getParameterTypes().length == 1) {
			Class<?> param = m.getParameterTypes()[0];
			if (param.equals(Boolean.class) || param.equals(Boolean.TYPE)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isOptionInteger(Class<?> clazz, String name) {
		for (Method m:clazz.getMethods()) {
			Option opt = m.getAnnotation(Option.class);
			if (opt != null && (opt.name().equals(name) || opt.charName().equals(name))) {
				return isOptionInteger(m);
			}
		}
		return false;
	}
	
	private boolean isOptionInteger(Method m) {
		if (m.getParameterTypes().length > 0) {
			Class<?> param = m.getParameterTypes()[0];
			if (param.equals(Integer.class) || param.equals(Integer.TYPE) || param.equals(Long.class) || param.equals(Long.TYPE)) {
				return true;
			}
		}
		return false;
	}

	private CmdArgs extractArgs(String[] args, Class<?> clazz) {
		return extractArgs(args, clazz, 1);
	}
	private CmdArgs extractArgs(String[] args, Class<?> clazz, int startIndex) {
		Map<String, String> cmdargs = new HashMap<String, String>();
		List<String> unnamed = null;
		
		int i=startIndex;
		while (i < args.length) {
			String arg = args[i];
			if (unnamed != null) {
				unnamed.add(arg);
				i++;
				continue;
			}
			if (arg.equals("--")) {
				unnamed = new ArrayList<String>();
				i++;
			} else if (arg.startsWith("--")) {
				if (i+1 < args.length) {
					if (isOptionInteger(clazz, arg.substring(2))) {
						cmdargs.put(arg.substring(2), args[i+1]);
						i += 2;
						continue;						
					} else if (args[i+1].startsWith("-") || isOptionBoolean(clazz, arg.substring(2))) {
						cmdargs.put(arg.substring(2), "");
						i += 1;
						continue;
					} else {
						cmdargs.put(arg.substring(2), args[i+1]);
						i += 2;
						continue;						
					}
				} else {
					cmdargs.put(arg, "");
					i += 1;
				}
			} else if (arg.startsWith("-") && !arg.equals("-")) {
				for (int j=1; j<arg.length(); j++) {
					if (j == arg.length()-1) {

						if (isOptionInteger(clazz, ""+arg.charAt(j))) {
							cmdargs.put(""+arg.charAt(j), args[i+1]);
							i += 2;
							break;
						} else if ((args.length > (i+1) && args[i+1].startsWith("-")) || isOptionBoolean(clazz, ""+arg.charAt(j))) {
							cmdargs.put(""+arg.charAt(j), "");
							i += 1;
							continue;
						} else if (args.length > (i+1)) {
							cmdargs.put(""+arg.charAt(j), args[i+1]);
							i += 2;
							break;						
						} else {
							cmdargs.put(""+arg.charAt(j), "");
						}
					} else {
						cmdargs.put(""+arg.charAt(j), "");
						i += 1;
					}
				}
			} else {			
				unnamed = new ArrayList<String>();
				unnamed.add(arg);
				i++;
			}
		}
		
		return new CmdArgs(cmdargs, unnamed);
	}

	public void invokeMethod(Object obj, Method m, String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, CommandArgumentException {
		Class<?> param = m.getParameterTypes()[0];
		if (val == null) {
			m.invoke(obj, new Object[] {null});
		} else if (param.equals(String.class)) {
			m.invoke(obj, val);
		} else if (param.equals(Integer.class) || param.equals(Integer.TYPE)) {
			m.invoke(obj, Integer.parseInt(val));
		} else if (param.equals(Long.class) || param.equals(Long.TYPE)) {
			m.invoke(obj, Long.parseLong(val));
		} else if (param.equals(Float.class) || param.equals(Float.TYPE)) {
			m.invoke(obj, Float.parseFloat(val));
		} else if (param.equals(Double.class) || param.equals(Double.TYPE)) {
			m.invoke(obj, Double.parseDouble(val));
		} else {
			throw new CommandArgumentException(m, param.getName(), param.getClass(), val );
		}
	}

	public void invokeMethodBoolean(Object obj, Method m, boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, CommandArgumentException {
		if (m.getParameterTypes().length == 0) {
			m.invoke(obj);
		} else {
			Class<?> param = m.getParameterTypes()[0];
			if (param.equals(Boolean.class) || param.equals(Boolean.TYPE)) {
				m.invoke(obj, val);
			} else {
				throw new CommandArgumentException(m, "");
			}
		}
	}

	public static String readFile(String fname) throws IOException {
		String s = "";
		InputStream is = MainBuilder.class.getClassLoader().getResourceAsStream(fname);
		if (is == null) {
			throw new IOException("Can't load file: "+fname);
		}
		int c;
		while ((c = is.read()) > -1) {
			s += (char) c;
		}
		is.close();
		return s;
	}
}
