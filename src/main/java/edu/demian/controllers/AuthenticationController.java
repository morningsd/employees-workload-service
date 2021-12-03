package edu.demian.controllers;

import edu.demian.dto.auth.AuthenticationRequestDTO;
import edu.demian.dto.auth.AuthenticationResponseDTO;
import edu.demian.exceptions.JwtAuthenticationException;
import edu.demian.security.jwt.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  public AuthenticationController(
      AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponseDTO> login(
      @Valid @RequestBody AuthenticationRequestDTO requestDTO) {
    try {
      String email = requestDTO.getEmail();
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, requestDTO.getPassword()));
      String token = tokenProvider.createToken(email);
      return new ResponseEntity<>(new AuthenticationResponseDTO(email, token), HttpStatus.OK);
    } catch (AuthenticationException e) {
      throw new JwtAuthenticationException("Invalid username or password");
    }

  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
    securityContextLogoutHandler.logout(request, response, null);
    return ResponseEntity.ok().build();
  }
}
