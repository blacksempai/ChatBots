package myapp;

import myapp.model.Owner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RootServlet extends HttpServlet {
    Owner owner = Owner.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isValid(req)){
            Owner owner = Owner.getInstance();
            owner.setId(getOwnerId(req,resp));
            resp.getWriter().println("Everything is cool");
        }
        else
        resp.getWriter().println("You are not Admin");
    }

    private boolean isValid(HttpServletRequest req){
        if(req.getParameter("login")==null||req.getParameter("password")==null)
            return false;
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String adminLogin = (String) getServletContext().getAttribute("admin_login");
        String adminPassword = (String) getServletContext().getAttribute("admin_password");
        return login.equals(adminLogin) && password.equals(adminPassword);
    }

    private long getOwnerId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long ownerId;
        try {
            ownerId = Long.parseLong(req.getParameter("owner_id"));
        } catch (NumberFormatException e) {
            resp.getWriter().println("Owner id should be long type");
            ownerId = owner.getId();
        }
        return ownerId;
    }
}
