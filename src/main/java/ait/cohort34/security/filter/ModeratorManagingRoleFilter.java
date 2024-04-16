package ait.cohort34.security.filter;

import ait.cohort34.accounting.model.Role;
import ait.cohort34.security.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(30)
public class ModeratorManagingRoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())){
            User principal = (User) request.getUserPrincipal();

            if(!principal.getRoles().contains(Role.MODERATOR.name())){
                response.sendError(403, "You are not allowed to access this resource");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return (path.matches("/forum/post/\\w+") && method.equals("DELETE"));
    }
}
