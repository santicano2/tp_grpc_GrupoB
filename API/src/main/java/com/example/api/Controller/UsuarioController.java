package com.example.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.CreateUserResponseDTO;
import com.example.api.dto.LoginResponseDTO;
import com.example.api.dto.UserDTO;
import com.example.api.service.UsuarioClientService;

import ong.users.Users;

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
                                    @RequestParam (required = false) String phone,
                                    @RequestParam Users.Role rol) {
        Users.CreateUserResponse response = usuarioClientService.crearUsuario(actor, username, nombre, apellido, email, phone, rol);
        CreateUserResponseDTO dto = new CreateUserResponseDTO();
        dto.setUsername(response.getUser().getUsername());
        dto.setEmail(response.getUser().getEmail());
        dto.setPlainPassword(response.getPlainPassword());
        return dto;
    }


        @PutMapping("/modificar/{id}")
    public ResponseEntity<UserDTO> modificar(@PathVariable int id, @RequestBody UserDTO userDto,  @RequestParam String actor) {
        try {
            Users.User updatedUser = usuarioClientService.modificarUsuario(userDto, actor);
            return new ResponseEntity<>(mapToDTO(updatedUser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @DeleteMapping("/baja/{username}")
    public ResponseEntity<Void> darDeBaja(@PathVariable String username,  @RequestParam String actor) {
        try {
            usuarioClientService.bajaUsuario(username, actor);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private UserDTO mapToDTO(Users.User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
