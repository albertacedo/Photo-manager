/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica04_client;

import java.util.Map;
import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArray;
import ad.practica04_client.Image;
import java.util.Base64;

public class Utils {

    public static Image json_to_image(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject obj = reader.readObject();
            return json_to_image(obj);
        }
    }

    public static Image json_to_image(JsonObject obj) {
        Image img = new Image();
        img.setId(obj.getInt("ID"));
        img.setTitle(obj.getString("TITLE"));
        img.setAuthor(obj.getString("AUTHOR"));
        img.setDescription(obj.getString("DESCRIPTION"));
        img.setKeywords(obj.getString("KEYWORDS"));
        img.setCreator(obj.getString("CREATOR"));
        img.setCaptureDate(obj.getString("CAPTURE_DATE"));
        img.setStorageDate(obj.getString("STORAGE_DATE"));
        img.setFilename(obj.getString("FILENAME"));
        if (!obj.getString("DATA").isEmpty()) {
            byte[] data_array = Base64.getDecoder().decode(obj.getString("DATA").getBytes());
            img.setData(data_array);
        }
        return img;
    }

    public static List<Image> json_to_image_list(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonArray arr = reader.readArray();
            return json_to_image_list(arr);
        }
    }

    public static List<Image> json_to_image_list(JsonArray arr) {
        List<Image> data = new ArrayList<>();
        for (int i = 0; i < arr.size(); ++i) {
            data.add(json_to_image(arr.getJsonObject(i)));
        }
        return data;
    }
}
