package gleb.web;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Component
public class UserIdFilter implements Filter {

    public static final String USERID = "userid";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();

        if (cookies == null || Arrays.stream(cookies).noneMatch(c -> c.getName().equals(USERID))) {
            ((HttpServletResponse) response).addCookie(new Cookie(USERID, UUID.randomUUID().toString()));
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
