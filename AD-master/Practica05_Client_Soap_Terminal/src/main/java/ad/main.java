/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import soap.Soap;
import soap.Soap_Service;
import soap.Image;
import java.util.Scanner;

/**
 *
 * @author alumne
 */
public class main {

    /**
     * @param args the command line arguments
     */
    

    public static void main(String[] args) {
        System.out.println("Enter commands. Enter help for more info.");
        Scanner scan = new Scanner(System.in);
        String line;
        while(scan.hasNextLine() && !((line = scan.nextLine()).equals("exit"))){
            try {
                if (line.startsWith("searchid")) {
                    List<String> parts = parse_args(line);
                    Image img = SearchbyId(Integer.valueOf(parts.get(0)));
                    print_image(img);
                }
                else if (line.startsWith("registerimg")) {
                    List<String> parts = parse_args(line);
                    System.out.println(parts);

                    Image img = new Image();
                    img.setTitle(parts.get(0));
                    img.setAuthor(parts.get(1));
                    img.setCreator(parts.get(2));
                    img.setCaptureDate(parts.get(3));
                    String file = parts.get(4);
                    img.setFilename(file);
                    try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
                            ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
                        int ch;
                        while ((ch = bin.read()) != -1) {
                            bout.write(ch);
                        }
                        bout.flush();
                        img.setData(bout.toByteArray());
                    }catch(IOException e) {
                        System.err.println(e.getMessage());
                    }
                    img.setKeywords(parts.get(5));
                    img.setDescription(parts.get(6));
                    boolean ok = registerImage(img) >= 0;
                    System.out.println(ok ? "OK" : "ERROR");
                }
                else if (line.startsWith("listimg")) {
                    List<Image> images = ListImages();
                    print_images(images);
                }
                else if (line.startsWith("searchtitlekw")) {
                    List<String> parts = parse_args(line);
                    List<Image> images = SearchTitleKeywords(parts.get(0), parts.get(1));
                    print_images(images);
                }
                else if (line.startsWith("searchkw")) {
                    List<String> parts = parse_args(line);
                    List<Image> images = SearchbyKeywords(parts.get(0));
                    print_images(images);
                }
                else if (line.startsWith("searchcreadate")) {
                    List<String> parts = parse_args(line);
                    List<Image> images = SearchCreaDate(parts.get(0));
                    print_images(images);
                }
                else if (line.startsWith("searchtitle")) {
                    List<String> parts = parse_args(line);
                    List<Image> images = SearchbyTitle(parts.get(0));
                    print_images(images);
                }
                else if (line.startsWith("searchauthor")) {
                    List<String> parts = parse_args(line);
                    List<Image> images = SearchbyAuthor(parts.get(0));
                    print_images(images);
                }
                else if (line.startsWith("delete")) {
                    //without user verification
                    
                    List<String> parts = parse_args(line);
                    int id = Integer.valueOf(parts.get(0));
                    boolean ok = DeleteImage(id) >= 0;
                    System.out.println(ok ? "IMG DELETED" : "ERROR");

                }
                else if (line.startsWith("modify")) {
                                        List<String> parts = parse_args(line);
                    String id = parts.get(0);
                    String title = parts.get(1);
                    String author = parts.get(2);
                    String cdate = parts.get(3);
                    String filename = parts.get(4);
                    String kw = parts.get(5);
                    String desc = parts.get(6);
                    
                    int post_id = Integer.parseInt(id);
                    title = title.equals("") ? null : title;
                    desc = desc.equals("") ? null : desc;
                    kw = kw.equals("") ? null : kw;
                    author = author.equals("") ? null : author;
                    cdate = cdate.equals("") ? null : cdate;
                    filename = filename.equals("") ? null : filename;
                    
                    Image img = new Image();
                    img.setId(post_id);
                    img.setTitle(title);
                    img.setDescription(desc);
                    img.setKeywords(kw);
                    img.setAuthor(author);
                    img.setCaptureDate(cdate);
                    img.setFilename(filename);
                    
                    if (filename != null) {
                        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filename));
                            ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
                            int ch;
                            while ((ch = bin.read()) != -1) {
                                bout.write(ch);
                            }
                            bout.flush();
                            byte[] data = bout.toByteArray();
                            img.setData(data);
                        }
                        catch(IOException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    
                    boolean ok = ModifyImage(img) >= 0;
                    System.out.println(ok ? "IMG MODIFIED" : "ERROR"); 
                    Image print = SearchbyId(post_id);
                    print_image(print); 
                }
                else {
                    print_help();
                }
            } catch(Exception e) {
                System.out.println("\nRemember to use \"\" around arguments!");
                System.err.println(e);
            }
        }
    }
    
    public static List<String> parse_args(String line) {
        String[] ss = line.split(String.valueOf('"'));
        List<String> result = new ArrayList<>();
        for (int i = 1; i < ss.length; i += 2) {
            result.add(ss[i]);
        }
        return result;
    }
    
    public static void print_help() {
        System.out.println("Commands follow this pattern: ACTION \"ARG1\" \"ARG2\" ... \"ARGN\"");
        System.out.println("");
        System.out.println("registerimg \"title\" \"creator\" \"author\" \"capture date\" \"file\" \"keywords\" \"description\"");
        System.out.println("delete \"id\"");
        System.out.println("modify \"id\" \"title\" \"author\" \"capture date\" \"file\" \"keywords\" \"description\"");
        System.out.println("listimg");
        System.out.println("searchid \"id\"");
        System.out.println("searchcreadate \"dd/mm/yyyy or mm/yyyy\"");
        System.out.println("searchkw \"keywords\"");
        System.out.println("searchtitle \"title\"");
        System.out.println("searchtitlekw \"title\" \"keywords\"");
        System.out.println("searchauthor \"author\"");
        System.out.println("exit");

    }
    
    public static void print_image(Image img) {
        if (img == null) { 
            System.out.println("N/A");
            return;
        }
        System.out.println("[ID:"+String.valueOf(img.getId())+"]: " + img.getTitle());
        System.out.println("Captured on " + img.getCaptureDate() + " by " + img.getCreator());
        System.out.println("Uploaded on " + img.getStorageDate() + " by " + img.getAuthor());
        System.out.println("Image at " + img.getStorageserverpath());
        System.out.println("\n" + img.getDescription());
        System.out.println(img.getKeywords());
        System.out.println("----------------------------------------------");
    }
    
    public static void print_images(List<Image> images) {
        for (Image img : images) {
            print_image(img);
        }
    }
    
    public static int registerImage(Image image) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.registerImage(image);
    }

    public static Image SearchbyId(int id) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.searchbyId(id);
    }

    public static List<Image> ListImages() {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.listImages();
    }

    public static List<Image> SearchTitleKeywords(String Title, String Keywords) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.searchTitleKeywords(Title, Keywords);
    }

    public static List<Image> SearchbyKeywords(String keywords) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.searchbyKeywords(keywords);
    }

    public static List<Image> SearchCreaDate(String creaDate) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.searchCreaDate(creaDate);
    }
    
    public static List<Image> SearchbyTitle(String title) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.searchbyTitle(title);
    }
    
    public static List<Image> SearchbyAuthor(String author) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.searchbyAuthor(author);
    }
    
    public static int DeleteImage(int id) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.deleteImage(id);
    }
    
    public static int ModifyImage(Image img) {
        Soap_Service service = new Soap_Service();
        Soap port = service.getSoapPort();
        return port.modifyImage(img);
    }
    
    
}