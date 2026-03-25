package co.edu.eci.lambda.springrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class ProxyController {

    private static final String USER_AGENT = "Mozilla/5.0";

    @GetMapping("/proxy/fibonacci")
    public String getFibonacci(@RequestParam(value = "value") String n) {
        String urlA = System.getenv("MATH_SERVICE_A_URL");
        String urlB = System.getenv("MATH_SERVICE_B_URL");

        // Logs para ver qué está intentando el proxi
        System.out.println("Intentando conectar con A: " + urlA);
        System.out.println("Intentando conectar con B: " + urlB);

        try {
            return callHttpService(urlA + "/fibonacci?value=" + n);
        } catch (Exception e) {
            System.err.println("Error en A: " + e.getMessage());
            try {
                return callHttpService(urlB + "/fibonacci?value=" + n);
            } catch (Exception ex) {
                System.err.println("Error en B: " + ex.getMessage());
                return "{\"error\":\"Ambos servicios caídos. Detalle: " + ex.getMessage() + "\"}";
            }
        }
    }

    private String callHttpService(String urlToCall) throws IOException {
        URL obj = new URL(urlToCall);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setConnectTimeout(3000); // 3 segundos de espera

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("HTTP Error: " + responseCode);
        }
    }
}
