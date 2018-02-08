package br.edu.ifpb;

import java.io.IOException;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ricardo Job
 */
//http://localhost:8080/pos-oauth/dropbox
@WebServlet(name = "Dropbox", urlPatterns = {"/dropbox"})
public class Dropbox extends HttpServlet {
//https://www.dropbox.com/oauth2/authorize?client_id=oxadjhz6yxnjgur&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fpos-oauth%2Fdropbox&response_type=code

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
//        System.out.println("code = " + code);
        Client newBuilder = ClientBuilder.newBuilder().build();
        WebTarget target = newBuilder.target("https://api.dropboxapi.com/oauth2/token");
        Form form = new Form("client_id", "oxadjhz6yxnjgur")
                .param("client_secret", "f9bxyqds8bjy2go")
                .param("grant_type", "authorization_code")
                .param("redirect_uri", "http://localhost:8080/pos-oauth/dropbox")
                .param("code", code);

        Response post = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(form));
        JsonObject readObject = post.readEntity(JsonObject.class);

//        String token = post.readEntity(String.class);
//        System.out.println("token = " + token);
//        JsonObject readObject = Json.createReader(new StringReader(token))
//                .readObject();
        
        String access_token = readObject.getString("access_token");
//        System.out.println("access_token = " + access_token);

        String files_folder = "https://api.dropboxapi.com/2/files/list_folder";
        //Bearer LIFwUpPTwZUAAAAAAAACJexVp9JF_9bhA6huOA63Cb-ZQLOsrRqC4FOJMEjDndK2

//        {
//    "path": "/aplicativos",
//    "recursive": false,
//    "include_media_info": false,
//    "include_deleted": false,
//    "include_has_explicit_shared_members": false,
//    "include_mounted_folders": true
//}
        JsonObject json = Json.createObjectBuilder()
                .add("path", "/aplicativos")
                .add("recursive", false)
                .add("include_media_info", false)
                .add("include_deleted", false)
                .add("include_has_explicit_shared_members", false)
                .add("include_mounted_folders", true)
                .build();
        
        Entity<JsonObject> param = Entity.json(json);
        WebTarget filesTarget = newBuilder.target(files_folder);
        Response arquivos = filesTarget.request()
                .header("Authorization", "Bearer " + access_token)
                .accept(MediaType.APPLICATION_JSON)
                .post(param);

        String resposta = arquivos.readEntity(String.class);
        System.out.println("resposta = " + resposta);

    }

}
