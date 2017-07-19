package gleb;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@WebFilter
public class UserIdFilter implements Filter {

    public static final String USERID = "userid";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getCookies() == null || Arrays.stream(req.getCookies()).noneMatch(c -> c.getName().equals(USERID))) {
            resp.addCookie(new Cookie(USERID, UUID.randomUUID().toString()));
        }

        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {

    }
}
