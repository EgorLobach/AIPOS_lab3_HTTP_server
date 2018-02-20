import util.HTTPUtil;
import view.Subscriber;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HTTPServer implements Runnable {
    private Socket clientSocket = null;
    private String rootPath = System.getProperty("user.dir");
    private Subscriber listener;

    HTTPServer(Socket clientSocket, Subscriber listener) {
        this.clientSocket = clientSocket;
        this.listener = listener;
    }

    private HTTPResponse GET(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse();
        File file = new File(rootPath + httpRequest.getPath());
        if (file.exists()) {
            if (file.isDirectory()) {
                String fileList = "<!DOCTYPE html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "</head" +
                        "<body>" +
                        "<a href='..'" + "'>" + file.getAbsolutePath() + "</a><br>";
                httpResponse.setDataFlag(false);
                for (File f : file.listFiles()) {
                    try {
                        fileList = fileList + "<a href='" + f.getAbsolutePath().replace(rootPath, "") + "'>" + f.getCanonicalPath() + "</a><br>";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fileList = fileList + "</body></html>";
                httpResponse.setCurrentDate(new Date());
                httpResponse.setData(fileList);
                httpResponse.addHeader("Content-Type", "text/html");
                httpResponse.setCode(200);
                httpResponse.setExplanation("OK");
                httpResponse.setLastModificationDate(new Date(file.lastModified()));
            } else {
                byte[] buffer = new byte[(int) file.length()];
                try {
                    InputStream fis = new FileInputStream(file);
                    fis.read(buffer);
                    fis.close();
                    String data = new String(buffer);
                    httpResponse.setCurrentDate(new Date());
                    httpResponse.setData(data);
                    httpResponse.setLastModificationDate(new Date(file.lastModified()));
                    httpResponse.setDataFlag(true);
                    httpResponse.addHeader("Content-Type", HTTPUtil.getHttpContentType(file.getName()));
                    httpResponse.setCode(200);
                    httpResponse.setExplanation("OK");
                } catch (Exception exc) {
                    httpResponse.setCode(500);
                    httpResponse.setDataFlag(false);
                    httpResponse.setExplanation("Inner server error");
                }
            }
        } else {
            httpResponse.setData("");
            httpResponse.setCode(404);
            httpResponse.setExplanation("Not found");
        }
        return httpResponse;
    }

    private HTTPResponse HEAD(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse();
        File file = new File(rootPath + httpRequest.getPath());
        if (!file.exists()) {
            httpResponse.setCode(404);
            httpResponse.setExplanation("Not found");
            httpResponse.setCurrentDate(new Date());
        } else {
            if (file.isDirectory()) {
                httpResponse.addHeader("Content-Type", "text/html");
            } else {
                httpResponse.addHeader("Content-Type", HTTPUtil.getHttpContentType(file.getName()));
            }
            try {
                httpResponse.setCurrentDate(new Date());
                httpResponse.setLastModificationDate(new Date(file.lastModified()));
                httpResponse.setDataFlag(true);
                httpResponse.setCode(200);
                httpResponse.setExplanation("OK");
            } catch (Exception exc) {
                httpResponse.setCode(500);
                httpResponse.setDataFlag(false);
                httpResponse.setExplanation("Inner server error");
            }
        }
        return httpResponse;
    }

    private HTTPResponse POST(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse();
        File file = new File(rootPath + httpRequest.getPath());
        if(file.exists()) {
            httpResponse.setCode(200);
            httpResponse.setExplanation("OK");
            httpResponse.addHeader("Content-Length", "" + 0);
            try {
                FileOutputStream fos = new FileOutputStream(rootPath + "/lastPost.txt");
                httpResponse.addHeader("Location", "/lastPost.txt");
                fos.write(httpRequest.getData().getBytes());
                fos.close();
            }
            catch (Exception exc){
                httpResponse.setCode(500);
                httpResponse.setExplanation("Inner server error");
            }
        }
        else{
            httpResponse.setCode(404);
            httpResponse.setExplanation("Not found");
        }

        return httpResponse;
    }

    private void handleRequest(String request, OutputStream os) {
        HTTPRequest httpRequest = new HTTPRequest(request);
        listener.update(request + HTTPResponse.CRLF + HTTPResponse.CRLF);
        HTTPResponse httpResponse = null;

        switch (httpRequest.getMethod()) {
            case "GET":
                httpResponse = GET(httpRequest);
                break;
            case "POST":
                httpResponse = POST(httpRequest);
                break;
            case "HEAD":
                httpResponse = HEAD(httpRequest);
                break;
            default:
                httpResponse = new HTTPResponse();
                httpResponse.setCode(405);
                httpResponse.setExplanation("Method not allowed");
        }

        byte[] buffer = null;
        String response = httpResponse.formResponse();
        listener.update(response + HTTPResponse.CRLF + HTTPResponse.CRLF);

        if (httpResponse.getDataFlag()) {
            File file = new File(rootPath + httpRequest.getPath());
            buffer = new byte[(int) file.length() + response.length()];
            byte[] responseBuffer = response.getBytes();
            byte[] dataBuffer = new byte[(int) file.length()];
            try {
                (new FileInputStream(file)).read(dataBuffer);
            } catch (Exception exc) {
            }
            int i;
            for (i = 0; i < response.length(); i++) {
                buffer[i] = responseBuffer[i];
            }
            for (i = 0; i < dataBuffer.length; i++) {
                buffer[i + response.length()] = dataBuffer[i];
            }
            byte[] crlfBuffer = "\r\n".getBytes();
            buffer[i++] = crlfBuffer[0];
            buffer[i++] = crlfBuffer[1];
        } else {
            buffer = response.getBytes();
        }
        try {
            os.write(buffer, 0, buffer.length);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    }


    @Override
    public void run() {
        String request;
        byte[] buffer = new byte[4096];
        try {
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();
            while (true) {
                int read = is.read(buffer, 0, 4096);
                if (read > 0) {
                    request = new String(buffer, 0, read);
                    handleRequest(request, os);
                    buffer = new byte[4096];
                }
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    }
}
