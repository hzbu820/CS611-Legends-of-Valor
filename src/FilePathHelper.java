/**
 * Utility class to handle file paths dynamically, ensuring the game can be run from any location.
 * This class provides methods to get the correct path to data files regardless of the execution location.
 */
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathHelper {
    private static final String DATA_DIRECTORY = "data";
    private static final String SRC_DIRECTORY = "src";
    
    /**
     * Gets the path to a data file, dynamically adjusted based on execution location.
     * @param filename The name of the file to get the path for
     * @return The full path to the file as a string
     */
    public static String getDataFilePath(String filename) {
        // First, try the direct path under src/data
        String directPath = SRC_DIRECTORY + File.separator + DATA_DIRECTORY + File.separator + filename;
        File directFile = new File(directPath);
        
        if (directFile.exists()) {
            return directPath;
        }
        
        // Try the current directory's data folder
        String currentDirPath = DATA_DIRECTORY + File.separator + filename;
        File currentDirFile = new File(currentDirPath);
        
        if (currentDirFile.exists()) {
            return currentDirPath;
        }
        
        // Try the data directory directly
        File dataOnlyFile = new File(filename);
        if (dataOnlyFile.exists()) {
            return filename;
        }
        
        // Try one directory up (for cases where execution happens in a subdirectory)
        String upDirPath = ".." + File.separator + DATA_DIRECTORY + File.separator + filename;
        File upDirFile = new File(upDirPath);
        
        if (upDirFile.exists()) {
            return upDirPath;
        }
        
        // As a last resort, return the original path and let the caller handle missing files
        return directPath;
    }
    
    /**
     * Gets the path to the data directory
     * @return The path to the data directory as a string
     */
    public static String getDataDirectory() {
        // Try src/data first
        String srcDataPath = SRC_DIRECTORY + File.separator + DATA_DIRECTORY;
        File srcDataDir = new File(srcDataPath);
        
        if (srcDataDir.exists() && srcDataDir.isDirectory()) {
            return srcDataPath;
        }
        
        // Try just data folder
        File dataDir = new File(DATA_DIRECTORY);
        if (dataDir.exists() && dataDir.isDirectory()) {
            return DATA_DIRECTORY;
        }
        
        // Try one directory up
        String upDataPath = ".." + File.separator + DATA_DIRECTORY;
        File upDataDir = new File(upDataPath);
        
        if (upDataDir.exists() && upDataDir.isDirectory()) {
            return upDataPath;
        }
        
        // As a last resort, create and return the src/data path
        srcDataDir.mkdirs();
        return srcDataPath;
    }
} 