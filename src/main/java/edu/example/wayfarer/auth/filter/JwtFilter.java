package edu.example.wayfarer.auth.filter;

import edu.example.wayfarer.apiPayload.code.status.ErrorStatus;
import edu.example.wayfarer.apiPayload.exception.handler.AuthHandler;
import edu.example.wayfarer.auth.constant.SecurityConstants;
import edu.example.wayfarer.auth.userdetails.PrincipalDetailsService;
import edu.example.wayfarer.auth.util.JwtUtil;
import edu.example.wayfarer.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PrincipalDetailsService principalDetailsService;
    private final AuthService authService; // 액세스 갱신을 위해
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldSkip = Arrays.stream(SecurityConstants.allowedUrls)
                        .anyMatch(pattern -> antPathMatcher.match(pattern, path));

        log.info("Should skip JwtFilter for path {}: {}", path, shouldSkip); // 추가된 로그
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtil.resolveAccessToken(request);//헤더나 쿠키에서 액세스 토큰 가져오기

            if (accessToken != null && jwtUtil.isAccessTokenValid(accessToken)) { //액세스 토큰이 존재하고 유효하다면
                String email = jwtUtil.getEmail(accessToken); //email 가져오기
                UserDetails userDetails = principalDetailsService.loadUserByUsername(email); //유저 디테일 가져오기
                log.info(userDetails.toString());

                if (userDetails != null) {//
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, "", userDetails.getAuthorities());//사용자정보,비밀번호,권한 -> OAuth때문에 비밀번호는 사용되지 않음
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new AuthHandler(ErrorStatus._NOT_FOUND_MEMBER);
                }
            } else {
                log.warn("Access token is invalid or not present");
            }
        } catch (AuthHandler e) {
            handleAuthError(e, response);
            return;
        }

        filterChain.doFilter(request, response);//다음 필터로 요청 전달 ->마지막 필터였으면 컨트롤러로
    }

    private void handleAuthError(AuthHandler e, HttpServletResponse response) throws IOException {
        if (ErrorStatus._AUTH_EXPIRE_TOKEN.equals(e.getErrorReasonHttpStatus().getCode())) {
            log.error("Access Token expired: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized - Access Token expired");
        } else {
            log.error("Authentication Error: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized");
        }
    }

    /**
     * 쿠키에서 Access Token을 추출하는 메서드 추가
     */
    private String resolveAccessTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            log.warn("No cookies found in the request.");
            return null;
        }
        for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
            log.info("Checking cookie: {} with value: {}", cookie.getName(), cookie.getValue());
            if ("accessToken".equals(cookie.getName())) {
                log.info("Access Token found in cookies: {}", cookie.getValue());
                return cookie.getValue();
            }
        }
        log.warn("Access Token cookie not found.");
        return null;
    }

}
