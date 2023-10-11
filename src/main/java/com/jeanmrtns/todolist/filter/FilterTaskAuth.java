package com.jeanmrtns.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jeanmrtns.todolist.user.IUserRepository;
import com.jeanmrtns.todolist.user.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    IUserRepository userRepository;

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authorization = request.getHeader("Authorization");
        var base64Authorization = authorization.substring("Basic".length()).trim();

        byte[] authDecoded = Base64.getDecoder().decode(base64Authorization);

        String authString = new String(authDecoded);
        String[] credentials = authString.split(":");

        String email = credentials[0];
        String password = credentials[1];
        
        User userExists = this.userRepository.findByEmail(email);

        if (userExists == null) {
            response.sendError(401, "Invalid credentials");
        } else {
            var passwordMatches = BCrypt.verifyer().verify(password.toCharArray(), userExists.getPassword());

            if (!passwordMatches.verified) {
                response.sendError(401, "Invalid credentials");
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
    
}
