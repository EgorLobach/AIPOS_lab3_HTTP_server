import java.util.HashMap;

public class HTTPRequest {
    private String method;
    private String path;

    HTTPRequest(String request){
        String[] temp = request.split(" ");
        method = temp[0];
        path = temp[1];
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
