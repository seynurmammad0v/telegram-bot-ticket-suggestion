package az.telegram.bot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpRequestUtil {

    private final String boundary = UUID.randomUUID().toString();
    private static final String LINE = "\r\n";
    private final HttpURLConnection httpConn;
    private final String charset;
    private final PrintWriter writer;

    public HttpRequestUtil(String requestURL, String charset, Map<String, String> headers) throws IOException {
        this.charset = charset;
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                httpConn.setRequestProperty(key, value);
            }
        }
        OutputStream outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }


    public void addFormField(String name, String value) {
        writer.append("--").append(boundary).append(LINE);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(LINE);
        writer.append(LINE);
        writer.append(value).append(LINE);
        writer.flush();
    }

    public String execute() throws IOException {
        String response;
        writer.flush();
        writer.append("--").append(boundary).append("--").append(LINE);
        writer.close();
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = httpConn.getInputStream().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            response = result.toString(this.charset);
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        var url = new ObjectMapper().readValue(response, HashMap.class);
        return url.get("url").toString();
    }

}