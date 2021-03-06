import java.util.Date;
import java.util.HashMap;

public class HTTPResponse {

    static final String CRLF = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1";

    private boolean dataFlag;
    private int code;
    private String explanation;
    private String data = "";
    private HashMap<String, String> headers;

    HTTPResponse() {
        headers = new HashMap<>();
        addHeader("Server", "My Server");
    }

    void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setDataFlag(boolean dataFlag) {
        this.dataFlag = dataFlag;
    }

    public void setData(String data) {
        this.data = data;
        addHeader("Content-length", "" + data.length());
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String formResponse() {
        String response = "";
        response += HTTP_VERSION + " " + code + " " + explanation;
        for (String key : headers.keySet())
            response +=CRLF + key + ": " + headers.get(key);
        response += CRLF + CRLF;
        if (dataFlag) {
            response += data;
        }
        return response;
    }

    public boolean getDataFlag() {
        return dataFlag;
    }
    public String getLocation(){
        return headers.get("Location");
    }
}
