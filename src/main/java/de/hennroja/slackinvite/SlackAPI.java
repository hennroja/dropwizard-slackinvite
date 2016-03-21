package de.hennroja.slackinvite;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hennroja on 20/03/16.
 */
public class SlackAPI {

    final static Logger logger = LoggerFactory.getLogger(SlackAPI.class);
    private final String token;

    public SlackAPI(String token) {
        this.token = token;
    }


    public void sendInvite(String email, String firstname) {


        try {

            String body = "email=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    "token=" + token + "&" +
                    "pretty=" + "1" + "&" +
                    "set_active=" + URLEncoder.encode("false", "UTF-8") + "&" +
                    "_attempts=" + "1" + "&" +
                    "first_name=" + URLEncoder.encode(firstname, "UTF-8");


            URL url = new URL("https://slack.com/api/users.admin.invite?" + body);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            for (String line; (line = reader.readLine()) != null; ) {
                logger.debug(line);
            }

            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean testAuth(){
        boolean result = false;
        try {

            String body = "token=" + token + "&" + "pretty=" + "1" + "&";


            URL url = new URL("https://slack.com/api/auth.test?" + body);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder sb = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                sb.append(line);
                logger.debug(line);
            }
            String resp = sb.toString();
            if(resp.contains("\"ok\": true,")){
                result = true;
            }

            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
