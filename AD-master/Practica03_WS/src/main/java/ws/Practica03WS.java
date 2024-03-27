/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import bd.Database;
import ad.practica03.Utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Part;

/**
 *
 * @author alumne
 */
@WebService(serviceName = "Practica03WS")
public class Practica03WS {

    

    /**
     * Web service operation
     */
    @WebMethod(operationName = "registerImage")
    public int registerImage(@WebParam(name = "image") Image image) {
        Database db = new Database();
        //Utils.get_storage_uuid(upload_filename)
        String file_uuid = Utils.get_storage_uuid(image.getFilename());
        String storage_path = Utils.uploads_path + File.separator + file_uuid;
        boolean success = Utils.write_to_disk(image.getData(), storage_path);
        if (success) {
            success = db.insert_post(image.getTitle(), 
                    image.getDescription(),
                    image.getKeywords(),
                    image.getAuthor(),
                    image.getCreator(),
                    image.getCaptureDate(),
                    file_uuid);
            return success ? 0 : -1;
        }
        return -1;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "validAccount")
    public boolean validAccount(@WebParam(name = "user") String user, @WebParam(name = "password") String password) {
        Database db = new Database();
        return db.is_correct_account(user, password);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "SearchbyId")
    public Image SearchbyId(@WebParam(name = "id") int id) {
        Database db = new Database();
        List<Map<String, Object>> result = db.search_by_id(id);
        
        if (!result.isEmpty()) {
            Image img = Utils.map_to_image(result.get(0));
            return img;
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ListImages")
    public List<Image> ListImages() {
        Database db = new Database();
        List<Map<String, Object>> result = db.dump_all_posts();
        List<Image> out = new ArrayList<>();
        for (Map<String, Object> m : result) {
            Image img = Utils.map_to_image(m);
            out.add(img);
        }
        return out;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "SearchTitleKeywords")
    public List<Image> SearchTitleKeywords(@WebParam(name = "Title") String Title, @WebParam(name = "Keywords") String Keywords) {
        Database db = new Database();
        List<Map<String, Object>> result = db.search_posts(Title, Keywords);
        List<Image> out = new ArrayList<>();
        for (Map<String, Object> m : result) {
            Image img = Utils.map_to_image(m);
            out.add(img);
        }
        return out;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "SearchbyKeywords")
    public List<Image> SearchbyKeywords(@WebParam(name = "keywords") String keywords) {
        Database db = new Database();
        List<Map<String, Object>> result = db.search_by_kw(keywords);
        List<Image> out = new ArrayList<>();
        for (Map<String, Object> m : result) {
            Image img = Utils.map_to_image(m);
            out.add(img);
        }
        return out;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "SearchCreaDate")
    public List<Image> SearchCreaDate(@WebParam(name = "creaDate") String creaDate) {
        Database db = new Database();
        List<Map<String, Object>> result = db.search_by_storedate(creaDate);
        List<Image> out = new ArrayList<>();
        for (Map<String, Object> m : result) {
            Image img = Utils.map_to_image(m);
            out.add(img);
        }
        return out;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "SearchbyTitle")
    public List<Image> SearchbyTitle(@WebParam(name = "title") String title) {
        Database db = new Database();
        List<Map<String, Object>> result = db.search_by_title(title);
        List<Image> out = new ArrayList<>();
        for (Map<String, Object> m : result) {
            Image img = Utils.map_to_image(m);
            out.add(img);
        }
        return out;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "SearchbyAuthor")
    public List<Image> SearchbyAuthor(@WebParam(name = "author") String author) {
        Database db = new Database();
        List<Map<String, Object>> result = db.search_by_author(author);
        List<Image> out = new ArrayList<>();
        for (Map<String, Object> m : result) {
            Image img = Utils.map_to_image(m);
            out.add(img);
        }
        return out;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "createAccount")
    public boolean createAccount(@WebParam(name = "user") String user, @WebParam(name = "password") String password) {
        Database db = new Database();
        return db.create_account(user, password);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "DeleteImage")
    public int DeleteImage(@WebParam(name = "id") int id) {
        //No user comprovation if its not the creathor
        boolean success = false;
        Database db = new Database();
        String deleted_file = db.delete_post(id);
        success = deleted_file != null;
        if (success) {
            File img = new File(Utils.uploads_path + File.separator + deleted_file);
            img.delete();
            return 0;
        }
        return -1;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ModifyImage")
    public int ModifyImage(@WebParam(name = "image") Image image) {
        boolean success = false;
        Database db = new Database();

        int id = image.getId();
        String filename = image.getFilename();
        byte[] data_array = image.getData();
        String title = image.getTitle();
        String description = image.getDescription();
        String keywords = image.getKeywords();
        String author = image.getAuthor();
        String capt_date = image.getCaptureDate();
        
        String old_filename = "";
        List<Map<String, Object>> old = db.search_by_id(id);
        if (!old.isEmpty()) {
            old_filename = (String) old.get(0).get("FILENAME");
            success = true;
        }

        if (success) {
            String file_uuid = "";
            if (filename != null) {
                file_uuid = Utils.get_storage_uuid(filename);
                String storage_path = Utils.uploads_path + File.separator + file_uuid;
                success = Utils.write_to_disk(data_array, storage_path);
            }
            else {
                file_uuid = old_filename;
            }
            
            success = db.update_post(id, title, description, keywords, author, capt_date, file_uuid);
        }
        
        if (success && (filename != null)) {
            File img = new File(Utils.uploads_path + File.separator + old_filename);
            img.delete();
            success = true;
        }
        return success ? 0 : -1;
    }
    
}
