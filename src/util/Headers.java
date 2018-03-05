package util;

import java.util.ArrayList;
import java.util.List;

public class Headers {
    List<String> headers = new ArrayList<>();

    public Headers(){
        headers.add("Host");
        headers.add("User-Agent");
        headers.add("Accept");
        headers.add("Accept-Language");
        headers.add("Accept-Encoding");
        headers.add("Accept-Charset");
        headers.add("Keep-Alive");
        headers.add("Connection");
        headers.add("Cookie");
        headers.add("Pragma");
        headers.add("Cache-Control");
        headers.add("Authorization");
        headers.add("Content-Disposition");
        headers.add("Postman-Token");
        headers.add("Origin");
        headers.add("Content-Length");
        headers.add("Content-Type");
    }
    public boolean is(String str){
        for (String temp : headers)
            if(str.equals(temp))
                return true;
        return false;
    }
}
