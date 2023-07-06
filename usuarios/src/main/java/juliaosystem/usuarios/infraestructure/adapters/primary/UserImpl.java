package juliaosystem.usuarios.infraestructure.adapters.primary;

import juliaosystem.usuarios.api.dto.LoginDTO;
import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.services.secundary.UserServiceInter;
import juliaosystem.usuarios.utils.MensajesRespuesta;
import juliaosystem.usuarios.utils.PlantillaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserImpl {
    @Autowired
    UserServiceInter userServiceInter;

    public Optional<PlantillaResponse<RegisterUserDTO>> login(LoginDTO loginDTO){
        return Optional.ofNullable(userServiceInter.findByEmail(loginDTO.getEmail()))
                .map(user -> {

                        return PlantillaResponse.<RegisterUserDTO>builder()
                                .message(MensajesRespuesta.GET.getMensaje())
                                .isRta(true)
                                .data(user.get().getData())
                                .httpStatus(HttpStatus.OK)
                                .build();

                });

    }

    public Optional<PlantillaResponse<RegisterUserDTO>> registro(RegisterUserDTO registerUserDTO){
        return userServiceInter.add(registerUserDTO);
    }
}
