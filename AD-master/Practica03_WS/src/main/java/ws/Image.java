
package ws;


public class Image {
    
    private int id;
    private String title;
    private String description;
    private String keywords;
    private String author;
    private String creator;
    private String capturedate;
    private String storagedate;
    private String filename;
    private String storageserverpath;

    public String getStorageserverpath() {
        return storageserverpath;
    }

    public void setStorageserverpath(String storageserverpath) {
        this.storageserverpath = storageserverpath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    private byte[] data;
    
    
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

    public void setData(byte[] data) {
        this.data = data;
    }
}
