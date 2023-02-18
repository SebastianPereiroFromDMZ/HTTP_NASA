import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.*;


public class RequestFromNASA {

    public static ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws IOException {


        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=GBkGbbfvpykZZM5lhzDnoJgdBQZeKflYfuHgm7hf");

        CloseableHttpResponse response = httpClient.execute(request);

        Information information = mapper.readValue(response.getEntity().getContent(), Information.class);

        response.close();

        String address = information.getHdurl();

        String[] words = address.split("/");
        String fileName = words[words.length - 1];

        HttpGet requestHdurl = new HttpGet(address);

        CloseableHttpResponse responseHdurl = httpClient.execute(requestHdurl);

        OutputStream out = new FileOutputStream(fileName);

        byte[] in = responseHdurl.getEntity().getContent().readAllBytes();

        out.write(in);

        responseHdurl.close();
        out.close();
        httpClient.close();
    }
}
