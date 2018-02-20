package util;

public class HTTPUtil {
    public static String getHttpContentType(String filename){
        String ext = filename.substring(filename.indexOf('.')+1, filename.length());
        switch(ext.toLowerCase()){
            case "html":
                return "text/html";
            case "txt":
                return "text/txt";
            case "jpg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            default:
                return "unknown";
        }
    }
}
