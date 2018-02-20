import java.util.HashMap;

public class HTTPRequest {
    private String method;
    private String path;
    private String data;

    HTTPRequest(String request){
        String[] temp = request.split(" ");
        method = temp[0];
        path = temp[1];
        if(method.equals("POST")){
            data =  request.split("\r\n\r\n")[1];
        }
    }

    public String getData(){
        return data;
    }

    public String getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    }
}
