package xyz.cyanin.securityapp.core.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import xyz.cyanin.securityapp.core.auth.MyUserDetails;
import xyz.cyanin.securityapp.model.User;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String prefixJwt = request.getHeader(MyJwtProvider.HEADER);
        if(prefixJwt != null){
            String jwt = prefixJwt.replace(MyJwtProvider.TOKEN_PREFIX, "");
            try {
                DecodedJWT decodedJWT = MyJwtProvider.verify(jwt);
                Long id = decodedJWT.getClaim("id").asLong();
                String role = decodedJWT.getClaim("role").asString();

                User user = User.builder().id(id).role(role).build();
                MyUserDetails myUserDetails = new MyUserDetails(user);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                myUserDetails,
                                myUserDetails.getPassword(),
                                myUserDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (SignatureVerificationException sve){
                chain.doFilter(request, response);
            }catch (TokenExpiredException tee){
                chain.doFilter(request, response);
            }
        }
        chain.doFilter(request, response);
    }
}