package com.couriersync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.couriersync.entity.Usuario;
import com.couriersync.repository.UsuarioRepository;
import com.couriersync.service.AuthService;
import com.couriersync.service.SignUpService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;

import com.couriersync.dto.UsuarioLoginDTO;
import com.couriersync.dto.UsuarioRegistroDTO;


@RestController
@CrossOrigin(origins = "http://localhost:8080", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;
    private final SignUpService signUpService;

    @Autowired
    public AuthController(AuthService authService, UsuarioRepository usuarioRepository, SignUpService signUpService) {
        this.authService = authService;
        this.signUpService = signUpService;
    }

    @GetMapping("/user")
    public Usuario getMethodName(@RequestParam String cedula) {
        return authService.findByCedula(cedula);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UsuarioLoginDTO usuarioLoginDTO ,
        HttpServletRequest request) {
        boolean success = authService.authenticate( usuarioLoginDTO.getUsername(),
            usuarioLoginDTO.getContraseña(),
            usuarioLoginDTO.getRol());
       
         if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Crear o recuperar la sesión y guardar info del usuario
        HttpSession session = request.getSession(true); // crea sesión si no existe
        session.setAttribute("Username", usuarioLoginDTO.getUsername());
        session.setAttribute("rol", usuarioLoginDTO.getRol());
        session.setMaxInactiveInterval(30 * 60); // 30 minutos
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO usuarioDTO) {
        signUpService.registrarUsuario(usuarioDTO);
        return ResponseEntity.ok("Usuario creado con éxito");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // no crear si no existe
        if (session != null) {
            session.invalidate();
        }

        // Elimina cookie JSESSIONID del cliente (sirve para limpiar en algunos navegadores/clients)
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        // cookie.setHttpOnly(true); // opcional
        response.addCookie(cookie);

        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }
}
