package com.example.api.controller;

import ong.users.Users;
import com.example.api.service.UsuarioClientService;
import org.springframework.web.bind.annotation.*;
import com.example.api.dto.LoginResponseDTO;
import com.example.api.dto.CreateUserResponseDTO;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioClientService usuarioClientService;

    public UsuarioController(UsuarioClientService usuarioClientService) {
        this.usuarioClientService = usuarioClientService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestParam String login, @RequestParam String password) {
        Users.LoginResponse response = usuarioClientService.login(login, password);
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setOk(response.getOk());
        dto.setMessage(response.getMessage());
        if (response.hasUser()) {
            dto.setUsername(response.getUser().getUsername());
            dto.setName(response.getUser().getName());
            dto.setLastname(response.getUser().getLastname());
            dto.setEmail(response.getUser().getEmail());
            dto.setRole(response.getUser().getRole().name());
        }
        return dto;
    }

    @PostMapping("/crear")
    public CreateUserResponseDTO crear(@RequestParam String actor,
                                       @RequestParam String username,
                                       @RequestParam String nombre,
                                       @RequestParam String apellido,
                                       @RequestParam String email,
                                       @RequestParam Users.Role rol) {
        Users.CreateUserResponse response = usuarioClientService.crearUsuario(actor, username, nombre, apellido, email, rol);
        CreateUserResponseDTO dto = new CreateUserResponseDTO();
        dto.setUsername(response.getUser().getUsername());
        dto.setEmail(response.getUser().getEmail());
        dto.setPlainPassword(response.getPlainPassword());
        return dto;
    }
}
