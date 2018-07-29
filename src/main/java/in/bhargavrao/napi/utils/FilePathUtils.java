package in.bhargavrao.napi.utils;

/**
 * Created by bhargav.h on 09-Mar-17.
 */
public class FilePathUtils {
    static String propertiesFile = "file.properties";


    // File names

    public static String blacklistedFile = "BlackListedWords.txt";
    public static String whitelistedFile = "WhiteListedWords.txt";
    public static String salutationsFile = "Salutations.txt";
    public static String outputLogFile = "output.csv";
    public static String reportsLogFile = "fullReports.txt";
    public static String reportsFile = "reports.txt";

    public static String getSitePathFromSiteName(String site) {
        String logsPath;
        switch (site) {
            case "askubuntu": logsPath = "auLogsPath"; break;
            default: logsPath = "logsPath"; break;
        }
        return logsPath;
    }

}
