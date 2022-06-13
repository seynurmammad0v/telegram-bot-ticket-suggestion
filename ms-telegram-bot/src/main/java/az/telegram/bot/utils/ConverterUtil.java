package az.telegram.bot.utils;

import az.telegram.bot.dao.OfferDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConverterUtil {

    String desc = "{description}";
    String price = "{price}";
    String notes = "{notes}";
    String company = "{company_name}";
    String date = "{date}";
    String url = "{url}";

    @Value("${offerTemplate.width}")
    String templateWidth;
    @Value("${offerTemplate.height}")
    String templateHeight;
    @Value("${offerTemplate.converter.url}")
    String converterUrl;
    @Value("${offerTemplate.converter.userId}")
    String USER_ID;
    @Value("${offerTemplate.converter.apiKey}")
    String API_KEY;

    public byte[] htmlToImage(String templatePath, OfferDetails offer, String companyName) {
        StringBuilder sb = readHtml(templatePath);
        editHtmlForImage(offer, companyName, sb);
        try {
            return getImage(new URL(getImageUrl(sb.toString())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String htmlToEmail(String templatePath, String token) {
        StringBuilder sb = readHtml(templatePath);
        replace(sb, token, url);
        return sb.toString();
    }

    public StringBuilder readHtml(String templatePath) {
        String line;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(templatePath)))) {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }

    private void editHtmlForImage(OfferDetails offer, String companyName, StringBuilder sb) {
        replace(sb, offer.getDateInterim(), date);
        replace(sb, companyName, company);
        replace(sb, offer.getDescription(), desc);
        replace(sb, offer.getPrice().toString(), price);
        replace(sb, offer.getNotes(), notes);
    }


    public void replace(StringBuilder sb, String val, String replaceItem) {
        sb.replace(sb.indexOf(replaceItem), sb.indexOf(replaceItem) + replaceItem.length(), val);
    }

    private String getImageUrl(String html) {
        Map<String, String> headers = new HashMap<>();
        String credentials = Base64.getEncoder().encodeToString((USER_ID + ":" + API_KEY).getBytes(StandardCharsets.UTF_8));
        headers.put("Authorization", "Basic " + credentials);
        HttpRequestUtil multipart = null;
        try {
            multipart = new HttpRequestUtil(converterUrl, "utf-8", headers);

            multipart.addFormField("html", html);
            multipart.addFormField("viewport_width", templateWidth);
            multipart.addFormField("viewport_height", templateHeight);
            return multipart.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImage(URL url) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (InputStream stream = url.openStream()) {
            byte[] buffer = new byte[4096];
            while (true) {
                int bytesRead = stream.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                output.write(buffer, 0, bytesRead);
            }
        }

        return output.toByteArray();
    }

}