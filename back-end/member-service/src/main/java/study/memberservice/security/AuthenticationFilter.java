package study.memberservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.memberservice.dto.LoginForm;
import study.memberservice.dto.MemberDto;
import study.memberservice.messagequeue.KafkaProducer;
import study.memberservice.messagequeue.MemberMessage;
import study.memberservice.service.MemberService;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment env;
    private final MemberService memberService;
    private final KafkaProducer producer;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            LoginForm cred = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            cred.getEmail(),
                            cred.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        SecretKey secretKey = Keys.hmacShaKeyFor(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8));
        String username = ((User) auth.getPrincipal()).getUsername();
        MemberDto userDetails = memberService.getMemberByEmail(username);

        String token = Jwts.builder()
                .setSubject(userDetails.getMemberId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(secretKey)
                .compact();

        memberService.updateLastLoginAt(userDetails.getMemberId());
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(userDetails.getMemberId());

        log.info("Produce message topic: member-member-login, message: {}", memberMessage);
        producer.sendId("member-member-login", memberMessage);

        response.addHeader("token", token);
        response.addHeader("memberId", userDetails.getMemberId());
        response.addHeader("memberName", userDetails.getName());
    }
}
