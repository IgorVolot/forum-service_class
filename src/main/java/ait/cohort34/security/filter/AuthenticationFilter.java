package ait.cohort34.security.filter;

import ait.cohort34.accounting.dao.UserAccountRepository;
import ait.cohort34.accounting.model.Role;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.security.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

    final UserAccountRepository userAccountRepository;
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            try {
                String[] credentials = getCredentials(request.getHeader("Authorization"));
                UserAccount userAccount = userAccountRepository.findById(credentials[0]).orElseThrow(RuntimeException::new);
                if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
                    throw new RuntimeException();
                }
                Set<String> roles = userAccount.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet());
                request = new WrappedRequest(request, userAccount.getLogin(), roles);
            } catch (Exception e) {
                response.sendError(401, "Unauthorized");
                return;
            }

        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
//        if ("/account/register".equalsIgnoreCase(path)) {
//            return false;
//        }
//        if (path.startsWith("/forum/posts/")) {
//            return false;
//        }
////        String [] pathParts = path.split("/");
//        return true;
        return !(
                (HttpMethod.POST.matches(method) && path.matches("/account/register"))
                || path.matches("/forum/posts/\\w+(/\\w+)?")
                );
    }


//    private String[] getCredentials(String authorization) {
//        String token = authorization.split(" ")[1];
//        String decode = new String(Base64.getDecoder().decode(token));
//        return decode.split(":");
//    }

    private String[] getCredentials(String authorization) {
        if (authorization == null || !authorization.contains(" ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = authorization.split(" ")[1];
        try {
            String decode = new String(Base64.getDecoder().decode(token));
            return decode.split(":");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to decode credentials");
        }
    }

    private class WrappedRequest extends HttpServletRequestWrapper {
        private String login;
        private Set<String> roles;

        public WrappedRequest(HttpServletRequest request, String login, Set<String> roles) {
            super(request);
            this.login = login;
            this.roles = roles;
        }

        @Override
        public Principal getUserPrincipal() {
            return new User(login, roles);
        }
    }
}
