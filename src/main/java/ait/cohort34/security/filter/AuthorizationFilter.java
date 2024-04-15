package ait.cohort34.security.filter;

import ait.cohort34.accounting.dao.UserAccountRepository;
import ait.cohort34.accounting.dto.exceptions.IncorrectRoleException;
import ait.cohort34.accounting.dto.exceptions.UserNotFoundException;
import ait.cohort34.accounting.model.Role;
import ait.cohort34.accounting.model.UserAccount;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Order(20)
public class AuthorizationFilter implements Filter {
    final UserAccountRepository userAccountRepository;
    private static final Role ADMINISTRATOR_ROLE = Role.ADMINISTRATOR;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        Pattern pattern = Pattern.compile("^/user/.+?/role/Administrator$");
        Matcher matcher = pattern.matcher(request.getRequestURI());
        boolean isUserRoleEndpoint = matcher.matches();

        if (isUserRoleEndpoint) {
            try {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || authHeader.isBlank()) {
                    response.setStatus(401);
                    return;
                }
                String base64Credentials = authHeader.substring("Basic".length()).trim();
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String[] credentials = new String(credDecoded, StandardCharsets.UTF_8).split(":");
                UserAccount userAccount = userAccountRepository.findById(credentials[0]).orElseThrow(UserNotFoundException::new);

                if (!userAccount.getPassword().equals(credentials[1])) {
                    throw new IncorrectRoleException();
                }

                if (userAccount.getRoles().stream().noneMatch(role -> role.equals(ADMINISTRATOR_ROLE))) {
                    throw new IncorrectRoleException();
                }
            } catch (Exception e) {
                response.setStatus(403);
                return;
            }
        }
        chain.doFilter(request, response);
    }

}

