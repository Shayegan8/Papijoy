 /** 
 * @author shayegan8
 */

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

	public static int getByID(String str, String fileName) {
		return CompletableFuture.supplyAsync(() -> {
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
		}).join();
	}

	public static String[] getAlphabets() {
		return PropertiesAPI.alphabets;
	}

	public static void setListProperties(String key, String fileName, String... args) {
		CompletableFuture.runAsync(() -> {

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

	public static void setListProperties(String key, String fileName, List<String> args) {
		CompletableFuture.runAsync(() -> {

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

	public static void setProperties(String key, String value, String fileName) {
		CompletableFuture.runAsync(() -> {

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
		if (listChecker == true && getSecretList().size() == 0 && defaultValues != null) {
			return Arrays.asList(defaultValues);
		}
		return getListPropertiesProcess(key, fileName);
	}

	public static CompletableFuture<List<String>> getListProperties(boolean listChecker, String key, String fileName,
			String... defaultValues) {
		ValueGetter getter = new ValueGetter();
		return CompletableFuture.supplyAsync(() -> {
			if (listChecker == true && getSecretList().size() == 0 && defaultValues != null) {
				return Arrays.asList(defaultValues);
			}
			return getListPropertiesProcess(key, fileName);
		});
	}

	private static List<String> getListPropertiesProcess(String key, String fileName) {
		List<String> ls = new ArrayList<String>();
		try {
			if (getSecretList() == null || getSecretList() != Files.readAllLines(Paths.get(fileName)))
				setSecretList(Files.readAllLines(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int ini = getByID("* " + key, fileName) + 1;
		int ini2 = getByID("* endif " + key, fileName) - 1;
		while (ini <= ini2) {
			ls.add(getSecretList().get(ini).split(LIST_SPLITOR)[1]);
			ini++;
		}
		return ls;
	}

	public static String getProperties_NS(boolean listChecker, String key, String defaultValue, String file) {
		String process = getPropertiesProcess(listChecker, key, defaultValue, file);
		return process;
	}

	public static String getProperties(boolean listChecker, String key, String defaultValue, String file) {
		ValueGetter getter = new ValueGetter();
		try {
			CompletableFuture.runAsync(() -> {
				String process = getPropertiesProcess(listChecker, key, defaultValue, file);
				getter.setValue(process);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getter.getValue();
	}

	private static String getPropertiesProcess(boolean listChecker, String key, String defaultValue, String fileName) {
		try {
			if (listChecker == true && getSecretList() == null
					|| getSecretList() != Files.readAllLines(Paths.get(fileName)))
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

	public static boolean isNum(String str) {
		return CompletableFuture.supplyAsync(() -> {
			boolean process = isNumProcess(str);
			return process;
		}).join();
	}

	private static boolean isNumProcess(String str) {
		char cstr[] = str.toCharArray();
		int i = 0;

		while (i < cstr.length) {
			if (cstr[i] == '.' && cstr[i + 1] == '.') {
				return false;
			} else if (cstr[i] == '.') {
				i++;
				continue;
			}
			if (Character.isDigit(cstr[i])) {
				i++;
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public static void fakeFreeSecretList() {
		secretList = null;
	}

}
