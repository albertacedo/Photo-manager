/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica04_client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.json.JsonReader;
import java.io.StringReader;
import javax.json.Json;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Base64;

class RestClient {

    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Practica04_WS/resources/rest";

    public RestClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
    }

    public List<Image> listImages() throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/list");
        String json_data = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return Utils.json_to_image_list(json_data);
    }

    public boolean validAccount(String user, String pass) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/valid_account");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("user", user);
        formData.add("pass", pass);
        String json_data = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.form(formData), String.class);
        try (JsonReader reader = Json.createReader(new StringReader(json_data))) {
            return reader.readObject().getBoolean("valid");
        }
    }

    public List<Image> searchByID(int id) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/searchID/{id}");
        String json_data = resource
                .resolveTemplate("id", id)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        return Utils.json_to_image_list(json_data);
    }
    
    public List<Image> searchByTitleKeywords(String title, String keywords) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/searchTitleKeywords");
        String json_data = resource
                .queryParam("title", title)
                .queryParam("keywords", keywords)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        return Utils.json_to_image_list(json_data);
    }
    
    public List<Image> searchByKeywords(String keywords) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/searchKeywords/{kw}");
        String json_data = resource
                .resolveTemplate("kw", keywords)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        return Utils.json_to_image_list(json_data);
    }
    
    public List<Image> searchByCreationDate(String date) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/searchCreationDate/{date}");
        String json_data = resource
                .resolveTemplate("date", date)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        return Utils.json_to_image_list(json_data);
    }
    
    public boolean registerImage(Image img) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/register");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("title", img.getTitle());
        formData.add("description", img.getDescription());
        formData.add("keywords", img.getKeywords());
        formData.add("author", img.getAuthor());
        formData.add("creator", img.getCreator());
        formData.add("capture", img.getCaptureDate());
        formData.add("filename", img.getFilename());
        String encoded = new String(Base64.getEncoder().encode(img.getData()));
        formData.add("data", encoded);
        String json_data = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.form(formData), String.class);
        try (JsonReader reader = Json.createReader(new StringReader(json_data))) {
            return reader.readObject().getBoolean("success");
        }
    }

    public void close() {
        client.close();
    }

    public boolean deleteImage(int id) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/delete");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("id", Integer.toString(id));
        String json_data = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.form(formData), String.class);
        try (JsonReader reader = Json.createReader(new StringReader(json_data))) {
            return reader.readObject().getBoolean("success");
        }
    }
    
   public List<Image> SearchbyTitle(String title) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/searchTitle/{title}");
        String json_data = resource
                .resolveTemplate("title", title)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        return Utils.json_to_image_list(json_data);
    }

    public List<Image> SearchbyAuthor(String author) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/searchAuthor/{author}");
        String json_data = resource
                .resolveTemplate("author", author)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        return Utils.json_to_image_list(json_data);
    }           

    public boolean registerAccount(String user, String pass) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/register_account");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("user", user);
        formData.add("pass", pass);
        String json_data = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.form(formData), String.class);
        try (JsonReader reader = Json.createReader(new StringReader(json_data))) {
            return reader.readObject().getBoolean("valid");
        }
    }
    
    public boolean modifyImage(Image img) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/modify");
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("id", Integer.toString(img.getId()));
        formData.add("title", img.getTitle());
        formData.add("description", img.getDescription());
        formData.add("keywords", img.getKeywords());
        formData.add("author", img.getAuthor());
        formData.add("creator", img.getCreator());
        formData.add("capture", img.getCaptureDate());
        formData.add("filename", img.getFilename());
        String encoded = new String(Base64.getEncoder().encode(img.getData()));
        formData.add("data", encoded);
        String json_data = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.form(formData), String.class);
        try (JsonReader reader = Json.createReader(new StringReader(json_data))) {
            return reader.readObject().getBoolean("success");
        }
    }
    
    public boolean validCreator(String creator) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI + "/valid_creator/{creator}");
        String json_data = resource
                .resolveTemplate("creator", creator)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(String.class);
        try (JsonReader reader = Json.createReader(new StringReader(json_data))) {
            return reader.readObject().getBoolean("valid");
        }
    }
    
}
