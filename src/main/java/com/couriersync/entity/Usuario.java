package com.couriersync.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_usuarios")
public class Usuario {

    @Id
    private String cedula;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "nombres", nullable = false, unique = true)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellido;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "celular", nullable = false, unique = true)
    private String celular;

    
    @Column(name = "contraseña", nullable = false)
    @JsonIgnore
    private String contraseña;

    @Column(name = "rol", nullable = false)
    private int rol;

    @Column(name = "mfa_estado", nullable = false)
    private boolean mfaEnabled; // ¿El usuario tiene MFA activo? 
    
    @Column(name = "mfa_secreto", length = 128)
    @JsonIgnore
    private String mfaSecret;       // Clave secreta cifrada para TOTP

    // Getters y Setters generados por Lombok

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }
}
