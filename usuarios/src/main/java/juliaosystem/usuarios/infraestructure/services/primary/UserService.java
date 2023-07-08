package juliaosystem.usuarios.infraestructure.services.primary;

import juliaosystem.usuarios.api.dto.LoginDTO;
import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.adapters.primary.UserImpl;
import juliaosystem.usuarios.utils.PlantillaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserImpl userImpl;
   public  Optional<PlantillaResponse<RegisterUserDTO>> add(RegisterUserDTO registerUserDTO){
        return userImpl.registro(registerUserDTO);
    }

    public  Optional<PlantillaResponse<RegisterUserDTO>> login(LoginDTO loginDTO){
        return userImpl.login(loginDTO);
    }

}
