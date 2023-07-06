package juliaosystem.usuarios.infraestructure.services.secundary;

import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.entitis.User;
import juliaosystem.usuarios.utils.PlantillaResponse;

import java.util.Optional;

public interface UserServiceInter {
    Optional<PlantillaResponse <RegisterUserDTO>> findByEmail(String email);
}
