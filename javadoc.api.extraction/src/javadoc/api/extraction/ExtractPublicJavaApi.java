package javadoc.api.extraction;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractPublicJavaApi {

	private static final String JAVASE_API = "http://docs.oracle.com/javase/8/docs/api/overview-summary.html";

	private static final String PUBLIC_API_PACKAGES = "output/publicJavaApiPackages";
	private static final String BND_PACKAGES = "output/bndPackageList";
	private static final String INCLUDED_PACKAGE_CLASSES = "output/includePackageClasses";
	private static final String INCLUDED_PACKAGE_SOURCES = "output/includePackageSources";

	private static final String CONCAT_VERSION_0_0_0 = ";version=0.0.0,";
	private static final String CONCAT_JAVA = "/*.java\n";
	private static final String CONCAT_CLASS = "/*.class\n";

	private static String outPath = System.getProperty("user.dir");
	private static final Path apiPackages = Paths.get(outPath, PUBLIC_API_PACKAGES);
	private static final Path bndImportPackages = Paths.get(outPath, BND_PACKAGES);
	private static final Path classInclude = Paths.get(outPath, INCLUDED_PACKAGE_CLASSES);
	private static final Path javaInclude = Paths.get(outPath, INCLUDED_PACKAGE_SOURCES);

	public static void main(String[] args) {
		try {
			URL javadocURL = new URL(JAVASE_API);
			Scanner scanner = new Scanner(javadocURL.openStream());
			String summary = scanner.useDelimiter("\\A").next();
			Pattern p = Pattern
					.compile("<td class=\"colFirst\"><a href=\".*/package-summary\\.html\">[<code>]*?([^<]*).*</a>");
			Matcher m = p.matcher(summary);
			while (m.find()) {
				String publicPackage = m.group(1);
				System.out.println("adding package: " + publicPackage);
				writeFile(apiPackages, publicPackage.concat("\n"));
				writeFile(bndImportPackages, publicPackage.concat(CONCAT_VERSION_0_0_0));
				writeFile(classInclude, publicPackage.replaceAll("\\.", "/").concat(CONCAT_CLASS));
				writeFile(javaInclude, publicPackage.replaceAll("\\.", "/").concat(CONCAT_JAVA));
			}

			scanner.close();

		} catch (IOException e) {
			if (e.getMessage().contains("Connection timed out")) {
				System.out.println("Do you require proxy settings? Try adding the following vm args with your proxy-host and proxy-port settings");
				System.out.println("-Dhttp.proxyHost=proxy-host");
				System.out.println("-Dhttp.proxyPort=proxy-port");
				System.out.println("-Dhttps.proxyHost=proxy-host");
				System.out.println("-Dhttps.proxyPort=proxy-port");
			} else {
				e.printStackTrace();
			}
		}
	}

	private static void writeFile(Path path, String text) {
		try {
			Files.write(path, text.getBytes(), StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
