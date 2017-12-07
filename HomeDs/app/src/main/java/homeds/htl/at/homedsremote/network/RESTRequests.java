package homeds.htl.at.homedsremote.network;

import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import homeds.htl.at.homedsremote.entity.AccessData;

/**
 * Created by Felix on 30.11.2017.
 */

public class RESTRequests {

    AccessData accessData = new AccessData("","http://172.18.199.120","vwmpjcZ6wrYmNShhOXVQsW54N8PJulK6AEhfRVeF","Z93XJGgZli3yMA0gYdtJjt5qYPEi2zdMMeddzsYbMMWj5AetOuMIfNAn4kAcNtY1GoYSt9dKjfSVIGRGFHnYbf9GEcrgVibliZRNVCPZyvH1cgDJT8vJywAhoWKGQG2wSjOnViXGIqwuOQTi4ojPgX1ZHK4m6sgpbx1micAkY6e7L7xLly7h2gKEHScXOEIhfF9jAmMFxvK1fqQv9o6vsTJCNsEbfRiEKQYYSzkCqfIya9YFWmTAfykGgGsrj0");


    public boolean authenticate() throws IOException, JSONException {
        //fix me workarround needed (other klass)
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);


        HttpURLConnection session = (HttpURLConnection) new URL(accessData.getBASEUrl()+"/api/authorize/access_token").openConnection();
        //session.setDoOutput(true);
        session.setRequestProperty("Accept","application/json");
        session.setRequestMethod("POST");
        session.connect();

        JSONObject accessObject = new JSONObject();
        accessObject.put("client_id",accessData.getClientId());
        accessObject.put("client_secret",accessData.getClientSecret());
        accessObject.put("grant_type","client_credentials");

        OutputStream autentication = session.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(autentication,"UTF-8"));
        writer.write(accessObject.toString());
        writer.close();
        autentication.close();
        int status = session.getResponseCode();
        session.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(session.getInputStream(),"UTF-8"));
        StringBuilder sb = new StringBuilder();


        String rows = reader.readLine();
        while (rows != null){
            sb.append(reader.readLine());
            rows = reader.readLine();

        }
        JSONObject res = new JSONObject(sb.toString());


        if (res.has("access_token")){
            accessData.setAccess_token(res.getString("access_token"));
            return true;
        }
        return false;
    }
}
