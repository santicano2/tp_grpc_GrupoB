package com.example.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/listar")
    public List<UserDTO> listar() {
        try {
            Users.UserList userList = usuarioClientService.listarUsuarios();
            List<UserDTO> dtos = new ArrayList<>();
            
            for (Users.User user : userList.getUsersList()) {
                dtos.add(mapToDTO(user));
            }
            
            return dtos;
        } catch (Exception e) {
            // si hay error con el servidor gRPC, log del error y devolver lista vacía
            System.err.println("Error al obtener usuarios del servidor gRPC: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


        @PutMapping("/modificar/{id}")
    public ResponseEntity<UserDTO> modificar(@PathVariable int id, @RequestBody UserDTO userDto,  @RequestParam String actor) {
        try {
            System.out.println("=== MODIFICAR USUARIO ===");
            System.out.println("ID from path: " + id);
            System.out.println("Actor: " + actor);
            System.out.println("UserDTO received: " + userDto);
            
            userDto.setId(id); // asegurar que el UserDTO tenga el ID correcto
            System.out.println("UserDTO after setting ID: " + userDto);
            
            Users.User updatedUser = usuarioClientService.modificarUsuario(userDto, actor);
            System.out.println("User updated successfully: " + updatedUser);
            return new ResponseEntity<>(mapToDTO(updatedUser), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("ERROR in modificar: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @DeleteMapping("/baja/{id}")
    public ResponseEntity<?> darDeBaja(@PathVariable int id, @RequestParam String actor) {
        System.out.println("=== DEACTIVATE USER REQUEST ===");
        System.out.println("ID: " + id);
        System.out.println("Actor: " + actor);
        try {
            System.out.println("Calling bajaUsuario service...");
            Users.User user = usuarioClientService.bajaUsuario(id, actor);
            System.out.println("DeactivateUser successful, user: " + user);
            return ResponseEntity.ok(Map.of(
                "message", "Usuario dado de baja correctamente",
                "user", mapToDTO(user)
            ));
        } catch (io.grpc.StatusRuntimeException e) {
            System.out.println("gRPC error in deactivateUser: " + e.getStatus());
            System.out.println("Error description: " + e.getStatus().getDescription());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getStatus().getDescription()));
        } catch (Exception e) {
            System.out.println("Unexpected error in deactivateUser: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }


    private UserDTO mapToDTO(Users.User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setActive(user.getActive());
        return dto;
    }
}
