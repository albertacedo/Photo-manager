
package ad.practica04_client;


public class Image {
    
    protected String author;
    protected String capturedate;
    protected String creator;
    protected byte[] data;
    protected String description;
    protected String filename;
    protected int id;
    protected String keywords;
    protected String storagedate;
    protected String title;
    


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCaptureDate() {
        return capturedate;
    }

    public void setCaptureDate(String capturedate) {
        this.capturedate = capturedate;
    }

    public String getStorageDate() {
        return storagedate;
    }

    public void setStorageDate(String storagedate) {
        this.storagedate = storagedate;
    }
   
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] value) {
        this.data = value;
    }
}
