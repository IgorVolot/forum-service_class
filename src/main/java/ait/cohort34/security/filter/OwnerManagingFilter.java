package ait.cohort34.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(40)
public class OwnerManagingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (checkEndpoint(request.getMethod(), request.getServletPath())){
            String login = request.getUserPrincipal().getName();

            if(!login.matches(request.getRemoteUser())){
                response.sendError(401, "Unauthorized");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return ("/account/user/\\w+".equalsIgnoreCase(path)) || ("/forum/post/\\w+".equalsIgnoreCase(path)) || ("/account/password".equalsIgnoreCase(path));
    }
}
