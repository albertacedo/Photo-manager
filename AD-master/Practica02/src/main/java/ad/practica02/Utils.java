package ad.practica02;
import java.io.File;
import java.util.UUID;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;


public class Utils {
    public static final String uploads_path = File.separator + "tmp";
    
    public static String get_storage_uuid(String upload_filename) {
        String extension = "";
        int i = upload_filename.lastIndexOf('.');
        if (i > 0) {
            extension = upload_filename.substring(i+1);
        }
        return UUID.randomUUID().toString() + "." + extension;
    }
    
    public static String keyword_process(String keywords) {
        if (keywords == null) return null;
        // Set whitespace as separator
        String kw = keywords.replace(" ", ",");
        // Clean keyword repetitions
        String[] x = kw.split(",");
        Set<String> set = new HashSet<>(x.length);
        Collections.addAll(set, x);
        set.remove("");
        // Sort keywords and convert to string
        String[] kws = set.toArray(new String[set.size()]);
        Arrays.sort(kws);
        return Arrays.toString(kws).replace(" ", "").replace("[","").replace("]","");
    }
}
