package edu.example.wayfarer.auth.filter;

import edu.example.wayfarer.apiPayload.code.status.ErrorStatus;
import edu.example.wayfarer.auth.constant.SecurityConstants;
import edu.example.wayfarer.auth.util.JwtUtil;
import edu.example.wayfarer.entity.Token;
import edu.example.wayfarer.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldSkip = Arrays.stream(SecurityConstants.allowedUrls)
                .anyMatch(pattern -> antPathMatcher.match(pattern, path));

        log.info("Should skip JwtFilter for path {}: {}", path, shouldSkip);
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtUtil.resolveAccessToken(request);

        if (accessToken != null) {
            try {
                if (jwtUtil.isAccessTokenValid(accessToken)) {
                    // 유효한 Access Token인 경우, SecurityContext에 인증 정보 설정
                    String email = jwtUtil.getEmail(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(jwtUtil.createAuthentication(email));
                } else {
                    throw new ExpiredJwtException(null, null, "Access token expired");
                }
            } catch (ExpiredJwtException e) {
                log.info("[*] Access Token이 만료되었습니다. Refresh Token으로 재발급을 시도합니다.");

                String email = jwtUtil.getEmailFromExpiredToken(accessToken); // 만료된 토큰에서 이메일 추출
                if (email != null) {
                    Optional<Token> tokenOptional = tokenRepository.findByEmail(email);
                    if (tokenOptional.isPresent()) {
                        String refreshToken = tokenOptional.get().getRefreshToken();
                        String socialAccessToken = tokenOptional.get().getSocialAccessToken();
                        String provider = tokenOptional.get().getProvider();
                        if (refreshToken != null && jwtUtil.isRefreshTokenValid(refreshToken)) {
                            jwtUtil.generateAndStoreTokens(email, "ROLE_USER", socialAccessToken, provider);

                            String newAccessToken = tokenRepository.findByEmail(email).get().getAccessToken();
                            log.info("재발급된 AccessToken : {}", newAccessToken);

                            // 클라이언트에게 401 상태 코드와 새로운 AccessToken을 쿼리 파라미터로 반환
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setHeader("Content-Type", "application/json");
                            response.getWriter().write(String.format(
                                    "{\"message\":\"%s\", \"newAccessToken\":\"%s\"}",
                                    ErrorStatus._AUTH_EXPIRE_TOKEN.getMessage(),
                                    newAccessToken
                            ));
                            return; // 필터 체인을 중단하고 응답을 반환합니다.
                        } else {
                            log.warn("[*] Refresh Token이 유효하지 않습니다. 인증에 실패했습니다.");
                            redirectToLoginPage(response);
                            return;
                        }
                    } else {
                        log.warn("[*] 이메일에 해당하는 토큰을 찾을 수 없습니다.");
                        redirectToLoginPage(response);
                        return;
                    }
                } else {
                    log.warn("[*] 만료된 토큰에서 이메일을 추출하지 못했습니다.");
                    redirectToLoginPage(response);
                    return;
                }
            }
        } else {
            log.warn("[*] No Access Token found in request");
            redirectToLoginPage(response);
        }

        // 다음 필터로 넘깁니다.
        filterChain.doFilter(request, response);
    }

    private void redirectToLoginPage(HttpServletResponse response) throws IOException {
        // 기존의 모든 로그인 관련 정보가 삭제된 상태에서 로그인 페이지로 이동하도록 설정
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/auth/login"); // 로그인 페이지 URL로 리다이렉트
    }
}
