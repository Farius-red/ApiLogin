package juliaosystem.usuarios.infraestructure.adapters.secundary;

import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.api.mappers.UserMapper;
import juliaosystem.usuarios.infraestructure.entitis.User;
import juliaosystem.usuarios.infraestructure.repository.UserRepository;
import juliaosystem.usuarios.infraestructure.services.secundary.UserServiceInter;
import juliaosystem.usuarios.utils.EmailValidationPattern;
import juliaosystem.usuarios.utils.MensajesRespuesta;
import juliaosystem.usuarios.utils.PlantillaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAdapter   implements UserServiceInter {

    @Autowired
    UserRepository userRepository;
    @Autowired
      UserMapper userMapper;



public Optional<PlantillaResponse<RegisterUserDTO>> findByEmail(String email) {
    return Optional.ofNullable((PlantillaResponse<RegisterUserDTO>) Optional.ofNullable(userRepository.findByEmail(email))
            .map(user -> {
                if (isUserEmpty(user)) {
                    return handleFailure(email);
                } else {
                    return PlantillaResponse.<RegisterUserDTO>builder()
                            .message(MensajesRespuesta.GET.getMensaje())
                            .isRta(true)
                            .data(userMapper.getDTO(user))
                            .httpStatus(HttpStatus.OK)
                            .build();
                }
            })
            .orElseGet(() -> handleFailure(email))); // Usuario no encontrado, devuelve respuesta de error
}

    private boolean isUserEmpty(User user) {
        return user.getName() == null ;
    }


    private PlantillaResponse<User> handleFailure(String email) {
        try {
            if (!email.matches(EmailValidationPattern.VALID.getPattern())) {
                return PlantillaResponse.<User>builder()
                        .message(EmailValidationPattern.EMAIL_VALIDATION_FAIL.name())
                        .isRta(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }else{
                return PlantillaResponse.<User>builder()
                        .message(EmailValidationPattern.EMAIL_NOT_FOUD.name())
                        .isRta(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return PlantillaResponse.<User>builder()
                .message(MensajesRespuesta.FALLO.getMensaje())
                .isRta(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

}
