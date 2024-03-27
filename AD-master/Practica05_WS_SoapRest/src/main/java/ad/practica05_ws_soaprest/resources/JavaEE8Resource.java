package ad.practica05_ws_soaprest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import bd.Database;
import ad.practica05_ws_soaprest.Utils;
import java.io.File;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Path("rest")
public class JavaEE8Resource {

    @Path("valid_account")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validAccount(@FormParam("user") String user,
            @FormParam("pass") String pass) {
        Database db = new Database();
        boolean valid = db.is_correct_account(user, pass);
        return Response
                .ok("{\"valid\":" + String.valueOf(valid) + "}")
                .build();
    }

    /**
     * POST method to register a new image
     *
     * @param title
     * @param description
     * @param keywords
     * @param author
     * @param creator
     * @param capt_date
     * @param filename
     * @param data
     * @return
     */
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerImage(@FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("keywords") String keywords,
            @FormParam("author") String author,
            @FormParam("creator") String creator,
            @FormParam("capture") String capt_date,
            @FormParam("filename") String filename,
            @FormParam("data") String data) {

        Database db = new Database();
        String file_uuid = Utils.get_storage_uuid(filename);
        String storage_path = Utils.uploads_path + File.separator + file_uuid;
        byte[] data_array = Base64.getDecoder().decode(data.getBytes());
        boolean success = Utils.write_to_disk(data_array, storage_path);

        if (success) {
            success = db.insert_post(title, description, keywords, author, creator, capt_date, file_uuid);
        }

        return Response
                .ok("{\"success\":" + String.valueOf(success) + "}")
                .build();
    }

    /**
     * POST method to modify an existing image
     *
     * @param id
     * @param title
     * @param description
     * @param keywords
     * @param author
     * @param creator
     * @param capt_date
     * @param filename
     * @param data
     * @return
     */
    @Path("modify")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyImage(@FormParam("id") String id,
            @FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("keywords") String keywords,
            @FormParam("author") String author,
            @FormParam("creator") String creator,
            @FormParam("capture") String capt_date,
            @FormParam("filename") String filename,
            @FormParam("data") String data) {

        Database db = new Database();
        boolean success = false;

        String old_filename = "";
        List<Map<String, Object>> old = db.search_by_id(Integer.valueOf(id));
        if (!old.isEmpty()) {
            old_filename = (String) old.get(0).get("FILENAME");
            success = true;
        }

        if (success) {
            String file_uuid = "";
            if (filename != null) {
                file_uuid = Utils.get_storage_uuid(filename);
                String storage_path = Utils.uploads_path + File.separator + file_uuid;
                byte[] data_array = Base64.getDecoder().decode(data.getBytes());
                Utils.write_to_disk(data_array, storage_path);
            }
            else {
                file_uuid = old_filename;
            }
            success = db.update_post(Integer.valueOf(id), title, description,
                    keywords, author, creator, capt_date, file_uuid);
        }

        if (success && (filename != null)) {
            File img = new File(Utils.uploads_path + File.separator + old_filename);
            img.delete();
            success = true;
        }

        return Response
                .ok("{\"success\":" + String.valueOf(success) + "}")
                .build();
    }

    /**
     * POST method to delete an existing image
     *
     * @param id
     * @return
     */
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteImage(@FormParam("id") String id) {

        boolean success = false;
        int num_id = Integer.valueOf(id);
        Database db = new Database();
        String deleted_file = db.delete_post(num_id);
        boolean deleted = deleted_file != null;

        if (deleted) {
            File img = new File(Utils.uploads_path + File.separator + deleted_file);
            img.delete();
            success = true;
        }

        return Response
                .ok("{\"success\":" + String.valueOf(success) + "}")
                .build();
    }

    /**
     * GET method to list images
     *
     * @return
     */
    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listImages() {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.dump_all_posts(), false);
        return Response
                .ok(json)
                .build();
    }

    /**
     * GET method to search images by id
     *
     * @param id
     * @return
     */
    @Path("searchID/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByID(@PathParam("id") int id) {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.search_by_id(id), true);
        return Response
                .ok(json)
                .build();
    }

    /**
     * GET method to search images by title
     *
     * @param title
     * @return
     */
    @Path("searchTitle/{title}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitle(@PathParam("title") String title) {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.search_by_title(title), false);
        return Response
                .ok(json)
                .build();
    }

    /**
     * GET method to search images by creation date.
     *
     * @param date
     * @return
     */
    @Path("searchCreationDate/{date}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCreationDate(@PathParam("date") String date) {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.search_by_storedate(date), false);
        return Response
                .ok(json)
                .build();
    }

    /**
     * GET method to search images by author
     *
     * @param author
     * @return
     */
    @Path("searchAuthor/{author}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByAuthor(@PathParam("author") String author) {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.search_by_author(author), false);
        return Response
                .ok(json)
                .build();
    }

    /**
     * GET method to search images by keyword
     *
     * @param keywords
     * @return
     */
    @Path("searchKeywords/{keywords}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByKeywords(@PathParam("keywords") String keywords) {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.search_by_kw(keywords), false);
        return Response
                .ok(json)
                .build();
    }

    @Path("searchTitleKeywords")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitleKeywords(@QueryParam("title") String title,
            @QueryParam("keywords") String keywords) {
        Database db = new Database();
        String json = Utils.map_list_to_json(db.search_posts(title, keywords), false);
        return Response
                .ok(json)
                .build();
    }

    @Path("register_account")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerAccount(@FormParam("user") String user,
            @FormParam("pass") String pass) {
        Database db = new Database();
        boolean valid = db.create_account(user, pass);
        return Response
                .ok("{\"valid\":" + String.valueOf(valid) + "}")
                .build();
    }

    @Path("valid_creator/{creator}")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validCreator(@PathParam("creator") String creator) {
        Database db = new Database();
        boolean valid = db.is_correct_creator(creator);
        return Response
                .ok("{\"valid\":" + String.valueOf(valid) + "}")
                .build();
    }

}

