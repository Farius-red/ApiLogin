package juliaosystem.usuarios.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import juliaosystem.usuarios.api.dto.LoginDTO;
import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.services.primary.UserService;
import juliaosystem.usuarios.utils.PlantillaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@Tag(name = "usuarios" , description = "Endpoints relacionados con el manejo de usuarios")
public class UserController {

    @Autowired
    UserService userService;
    @Operation(summary = "registrar un usuario", description = "Permite agregar Registrar nuevos usuarios")

    @PostMapping("/add")
    public ResponseEntity<PlantillaResponse<RegisterUserDTO>> add (@RequestBody RegisterUserDTO registerUserDTO ) {
        PlantillaResponse<RegisterUserDTO> response = userService.add(registerUserDTO).orElseThrow(NoSuchElementException::new);
        return new ResponseEntity<>(response,response.getHttpStatus());
    }

    @Operation(summary = "Logear usuarios", description = "Permite iniciar sesion ")

    @PostMapping("/login")
    public ResponseEntity<PlantillaResponse<RegisterUserDTO>> login (@RequestBody LoginDTO loginDTO ) {
        PlantillaResponse<RegisterUserDTO> response = userService.login(loginDTO).orElseThrow(NoSuchElementException::new);
        return new ResponseEntity<>(response,response.getHttpStatus());
    }


}
