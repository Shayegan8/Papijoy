import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/* 
 * @author shayegan8
 * 
 */
public class PropertiesAPI {

	private static List<String> secretList;
	private static String alphabets[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "j", "l", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private static final String SPLITOR = "@";
	private static final String LIST_SPLITOR = " - ";

	public static List<String> getSecretList() {
		return secretList;
	}

	public static void setSecretList(List<String> ls) {
		secretList = ls;
	}

	public static int getByID(JavaPlugin instance, String str, String fileName) {
		CompletableFuture<Integer> result = new CompletableFuture<Integer>();
		ValueGetter getter = new ValueGetter();
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
			int n = 0;

			try {
				while (n < Files.readAllLines(Paths.get(fileName)).size()) {
					if (Files.readAllLines(Paths.get(fileName)).get(n).equalsIgnoreCase(str)) {
						result.complete(n);
					}
					n++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			result.complete(-1);
		});

		result.exceptionally((exp) -> {
			throw new IllegalStateException("A problem with complete()\n" + exp);
		});

		result.thenAccept((reslt) -> {
			getter.setInt(reslt);
		});
		return getter.getInt();
	}

	public static int getByID_NS(String str, String fileName) {
		int n = 0;

		try {
			while (n < Files.readAllLines(Paths.get(fileName)).size()) {
				if (Files.readAllLines(Paths.get(fileName)).get(n).equalsIgnoreCase(str)) {
					return n;
				}
				n++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String[] getAlphabets() {
		return PropertiesAPI.alphabets;
	}

	public static void setListProperties(JavaPlugin instance, String key, String fileName, String... args) {
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
			int i = 0;

			try (FileWriter writer = new FileWriter(fileName, true)) {
				writer.write("\n" + "* " + key + "\n");
				while (i < args.length) {
					writer.write(i + LIST_SPLITOR + args[i] + "\n");
					writer.flush();
					i++;
				}
				writer.write("* endif " + key);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void setListProperties_NS(String key, String fileName, List<String> args) {
		int i = 0;

		try (FileWriter writer = new FileWriter(fileName, true)) {
			writer.write("\n" + "* " + key + "\n");
			while (i < args.size()) {
				writer.write(i + LIST_SPLITOR + args.get(i) + "\n");
				writer.flush();
				i++;
			}
			writer.write("* endif " + key);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setListProperties(JavaPlugin instance, String key, String fileName, List<String> args) {
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

			int i = 0;

			try (FileWriter writer = new FileWriter(fileName, true)) {
				writer.write("\n" + "* " + key + "\n");
				while (i < args.size()) {
					writer.write(i + LIST_SPLITOR + args.get(i) + "\n");
					writer.flush();
					i++;
				}
				writer.write("* endif " + key);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
	}

	public static void setProperties_NS(String key, String value, String fileName) {
		try (FileWriter writer = new FileWriter(fileName, true)) {
			writer.write("\n" + key + SPLITOR + value + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setProperties(JavaPlugin instance, String key, String value, String fileName) {
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
			try (FileWriter writer = new FileWriter(fileName, true)) {
				writer.write("\n" + key + SPLITOR + value + "\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static List<String> getListProperties_NS(boolean listChecker, String key, String fileName,
			String... defaultValues) {
		if (listChecker && getSecretList().size() == 0 && defaultValues != null) {
			return Arrays.asList(defaultValues);
		}
		return getListPropertiesProcess_NS(key, fileName);
	}

	public static List<String> getListProperties(JavaPlugin instance, boolean listChecker, String key, String fileName,
			String... defaultValues) {
		CompletableFuture<List<String>> result = new CompletableFuture<List<String>>();
		ValueGetter getter = new ValueGetter();
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
			if (listChecker == true && getSecretList().size() == 0 && defaultValues != null) {
				result.complete(Arrays.asList(defaultValues));
			}
			result.complete(getListPropertiesProcess(instance, key, fileName));
		});

		result.exceptionally((x) -> {
			throw new IllegalStateException("Problem with getListPropertiesProcess or complete()\n" + x);
		});

		result.thenAccept((reslt) -> {
			getter.setLValue(reslt);
		});

		return getter.getLValue();
	}

	private static List<String> getListPropertiesProcess(JavaPlugin instance, String key, String fileName) {
		List<String> ls = new ArrayList<String>();
		try {
			if (getSecretList() == null || getSecretList() != Files.readAllLines(Paths.get(fileName)))
				setSecretList(Files.readAllLines(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int ini = getByID(instance, "* " + key, fileName) + 1;
		int ini2 = getByID(instance, "* endif " + key, fileName) - 1;
		while (ini <= ini2) {
			ls.add(getSecretList().get(ini).split(LIST_SPLITOR)[1]);
			ini++;
		}
		return ls;
	}

	private static List<String> getListPropertiesProcess_NS(String key, String fileName) {
		List<String> ls = new ArrayList<String>();
		try {
			if (getSecretList() == null || getSecretList() != Files.readAllLines(Paths.get(fileName)))
				setSecretList(Files.readAllLines(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int ini = getByID_NS("* " + key, fileName) + 1;
		int ini2 = getByID_NS("* endif " + key, fileName) - 1;
		while (ini <= ini2) {
			ls.add(getSecretList().get(ini).split(LIST_SPLITOR)[1]);
			ini++;
		}
		return ls;
	}

	public static String getProperties(JavaPlugin instance, boolean listChecker, String key, String defaultValue,
			String file) {
		CompletableFuture<String> result = new CompletableFuture<String>();
		ValueGetter getter = new ValueGetter();
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
			result.complete(getPropertiesProcess(listChecker, key, defaultValue, file));
		});

		result.exceptionally((x) -> {
			throw new IllegalStateException("Problem with getPropertiesProcess or complete()\n" + x);
		});

		result.thenAccept(r -> {
			getter.setValue(r);
		});
		return getter.getValue();
	}

	private static String getPropertiesProcess(boolean listChecker, String key, String defaultValue, String fileName) {
		try {
			if (listChecker && getSecretList() == null || getSecretList() != Files.readAllLines(Paths.get(fileName)))
				setSecretList(Files.readAllLines(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ((getSecretList().size() == 0)) {
			return defaultValue;
		}
		for (String i : getSecretList()) {
			if (i.contains(key + SPLITOR)) {
				String gotten[] = i.split(SPLITOR);
				if (gotten.length == 2 && gotten[1] != null) {
					String in_s = gotten[1];
					String bef_in_s = gotten[0];
					if (bef_in_s.equals(key)) {
						return in_s;
					}
				} else {
					return defaultValue;
				}
			}
		}
		return defaultValue;
	}

	public static void fakeFreeSecretList() {
		secretList = null;
	}

	public static boolean isNum(JavaPlugin instance, String str) {
		CompletableFuture<Boolean> result = new CompletableFuture<Boolean>();
		ValueGetter getter = new ValueGetter();
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
			result.complete(isNumProcess(str));
		});

		result.exceptionally((x) -> {
			throw new IllegalStateException("Problem with isNumberProcess or complete()\n" + x);
		});

		result.thenAccept((reslt) -> {
			getter.setBoolean(reslt);
		});

		return getter.getBoolean();
	}

	private static boolean isNumProcess(String str) {
		int i = 0;

		while (i < str.toCharArray().length) {
			if (str.charAt(i) == '.' && str.charAt(i + 1) == '.') {
				return false;
			} else if (str.charAt(i) == '.') {
				i++;
				continue;
			}
			if (Character.isDigit(str.charAt(i))) {
				i++;
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

}
