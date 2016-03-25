package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dbService.Cmds;
import dbService.DBService;
import dbService.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class PingPongServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String json = "";
        json = br.readLine();

        ObjectMapper mapper = new ObjectMapper();
        Cmd cmd = mapper.readValue(json, Cmd.class);

        int iUserId = cmd.getUserId();
        User user;
        try {
            DBService.getDbService().saveCmd(Cmds.PING, iUserId);
            user = DBService.getDbService().getUser(iUserId);

            resp.setContentType("text/html;charset=utf-8");
            mapper.writeValue(resp.getOutputStream(), user.getCmdReceived());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
