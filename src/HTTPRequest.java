import util.Headers;

import java.util.HashMap;

public class HTTPRequest {
    private String method;
    private String path;
    private String httpVersion;
    public HashMap<String, String> headers;
    private String data;

    public HTTPRequest(String request){
        String[] temp = request.split(" ");
        method = temp[0];
        path = temp[1];
        httpVersion = temp[2].split("\r\n")[0];
        headers = getHeaders(request);
        if(method.equals("POST")){
            data = request.split("\r\n\r\n")[1];
        }

    }

    public String getHttpVersion() {
        return httpVersion;
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

    public HashMap<String, String> getHeaders(){
        return headers;
    }

    private HashMap<String, String> getHeaders(String request){
        HashMap<String, String> headers = new HashMap<>();
        String[] temp = request.split("\r\n");
        int endIndex;
        if (method.equals("PUT") || method.equals("POST"))
            endIndex = temp.length - 2;
        else
            endIndex = temp.length;

        for(int i = 1; i < endIndex; i++){
            String header = temp[i];
            String[] h_v = header.split(": ");
            headers.put(h_v[0].trim(), h_v[1].trim());
        }
        return headers;
    }

    public boolean correct(){

        return true;
    }
    public boolean isCorrect(){
        Headers headersValue = new Headers();
        for (String key: headers.keySet())
            if(!headersValue.is(key))
                return false;
        return true;
    }
}
