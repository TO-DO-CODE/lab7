package co.edu.eci.lambda.springrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class ProxyController {

    private static final String USER_AGENT = "Mozilla/5.0";

    @GetMapping("/proxy/fibonacci")
    public String getFibonacci(@RequestParam(value = "value") int n) {
        String urlA = System.getenv("MATH_SERVICE_A_URL");
        String urlB = System.getenv("MATH_SERVICE_B_URL");

        // Try Active (A)
        try {
            return callService(urlA + "/fibonacci?value=" + n);
        } catch (Exception e) {
            System.err.println("Active Service (A) failed, trying Passive (B)...");
            // Try Passive (B)
            try {
                return callService(urlB + "/fibonacci?value=" + n);
            } catch (Exception ex) {
                return "{\"error\": \"Both services are down\"}";
            }
        }
    }

    private String callService(String urlString) throws Exception {
        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setConnectTimeout(2000); 

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new Exception("GET request failed with code: " + responseCode);
        }
    }
}
