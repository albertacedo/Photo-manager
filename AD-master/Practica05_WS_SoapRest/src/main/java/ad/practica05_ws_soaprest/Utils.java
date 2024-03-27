/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica05_ws_soaprest;

import soap.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class Utils {

    public static final String uploads_path = File.separator + "tmp";

    public static String get_storage_uuid(String upload_filename) {
        String extension = "";
        int i = upload_filename.lastIndexOf('.');
        if (i > 0) {
            extension = upload_filename.substring(i + 1);
        }
        return UUID.randomUUID().toString() + "." + extension;
    }

    public static String keyword_process(String keywords) {
        if (keywords == null) {
            return null;
        }
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
        return Arrays.toString(kws).replace(" ", "").replace("[", "").replace("]", "");
    }

    public static String map_to_json(Map<String, Object> m, boolean include_data) {
        String encoded = "";
        if (include_data) {
            String path = uploads_path + File.separator + (String) m.get("FILENAME");
            byte[] data = read_from_disk(path);
            encoded = new String(Base64.getEncoder().encode(data));
        }
        String json = String.format("{\"ID\":%d,\"TITLE\":\"%s\",\"AUTHOR\":\"%s\",\"DESCRIPTION\":\"%s\","
                + "\"KEYWORDS\":\"%s\",\"CREATOR\":\"%s\",\"CAPTURE_DATE\":\"%s\","
                + "\"STORAGE_DATE\":\"%s\",\"FILENAME\":\"%s\",\"DATA\":\"%s\"}",
                (int) m.get("ID"), (String) m.get("TITLE"), (String) m.get("AUTHOR"),
                (String) m.get("DESCRIPTION"), (String) m.get("KEYWORDS"),
                (String) m.get("CREATOR"), (String) m.get("CAPTURE_DATE"),
                (String) m.get("STORAGE_DATE"), (String) m.get("FILENAME"),
                encoded);
        return json;
    }

    public static String map_list_to_json(List<Map<String, Object>> l, boolean include_data) {
        if (l.isEmpty()) {
            return "[]";
        } else {
            String json = "[" + map_to_json(l.get(0), include_data);
            for (int i = 1; i < l.size(); ++i) {
                String obj = map_to_json(l.get(i), include_data);
                json += "," + obj;
            }
            json += "]";
            return json;
        }
    }

    public static boolean write_to_disk(byte[] image, String storage_path) {
        try (ByteArrayInputStream bin = new ByteArrayInputStream(image);
                BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(storage_path, false));) {
            int ch;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
            }
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static byte[] read_from_disk(String storage_path) {
        byte[] data;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(storage_path));
                ByteArrayOutputStream bout = new ByteArrayOutputStream();) {
            int ch;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
            }
            bout.flush();
            data = bout.toByteArray();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new byte[0];
        }
        return data;
    }

    public static Image map_to_image(Map<String, Object> m) {
        Image img = new Image();
        img.setId((int) m.get("ID"));
        img.setTitle((String) m.get("TITLE"));
        img.setAuthor((String) m.get("AUTHOR"));
        img.setDescription((String) m.get("DESCRIPTION"));
        img.setKeywords((String) m.get("KEYWORDS"));
        img.setCreator((String) m.get("CREATOR"));
        img.setCaptureDate((String) m.get("CAPTURE_DATE"));
        img.setStorageDate((String) m.get("STORAGE_DATE"));
        img.setFilename((String) m.get("FILENAME"));
        String path = uploads_path + File.separator + img.getFilename();
        img.setStorageserverpath(path);
        img.setData(read_from_disk(path));
        return img;
    }
}
