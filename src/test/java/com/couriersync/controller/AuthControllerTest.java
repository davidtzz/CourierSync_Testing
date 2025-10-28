package com.couriersync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.couriersync.dto.UsuarioLoginDTO;
import com.couriersync.entity.Usuario;
import com.couriersync.repository.UsuarioRepository;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioRepository usuarioRepository; // Se "simula" para evitar conexión real a DB

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //Historia de usuario 1.1 Iniciar sesión

    @BeforeEach
    void setUp() {
        // Usuario válido simulado
        Usuario usuario = new Usuario();
        usuario.setUsuario("usuario1");
        usuario.setContraseña(passwordEncoder.encode("1234"));
        usuario.setRol(1);

        Mockito.when(usuarioRepository.findByUsuario("usuario1"))
                .thenReturn(usuario);
    }

    @Test
    void testInicioSesionExitoso() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setUsername("usuario1");
        loginDTO.setContraseña("1234");
        loginDTO.setRol(1);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void testInicioSesionExcepcional() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setUsername("usuario1");
        loginDTO.setContraseña(" ");
        loginDTO.setRol(1);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    void testContrasenaIncorrecta() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setUsername("usuario1");
        loginDTO.setContraseña("1235");
        loginDTO.setRol(1);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }
    @Test
    void testUsuarioInexistente() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setUsername("usuario2");
        loginDTO.setContraseña("1234");
        loginDTO.setRol(1);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }
    @Test
    void testCamposVacios() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setUsername("");
        loginDTO.setContraseña(" ");
        loginDTO.setRol(null);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}

