package juliaosystem.usuarios.infraestructure.adapters.secundary;

import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.api.mappers.UserMapper;
import juliaosystem.usuarios.infraestructure.entitis.User;
import juliaosystem.usuarios.infraestructure.repository.UserRepository;
import juliaosystem.usuarios.infraestructure.services.secundary.UserServiceInter;
import juliaosystem.usuarios.utils.EmailValidationPattern;
import juliaosystem.usuarios.utils.MensajesRespuesta;
import juliaosystem.usuarios.utils.PlantillaResponse;
import juliaosystem.usuarios.utils.jtw.JwtTokenUtil;
import juliaosystem.usuarios.utils.jtw.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
            .orElseGet(() -> handleFailure(email)));
}
    private String generateJwtToken(UserDetails userDetails) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        return jwtTokenUtil.generateToken(userDetails);
    }

    private PlantillaResponse<RegisterUserDTO> buildRegisterUserResponse(User user) {
        RegisterUserDTO userDTO = userMapper.getDTO(user);

        return PlantillaResponse.<RegisterUserDTO>builder()
                .message(MensajesRespuesta.CREADO.getMensaje())
                .data(userDTO)
                .isRta(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }
    @Override
    public Optional<PlantillaResponse<RegisterUserDTO>> add(RegisterUserDTO registerUserDTO) {
        Optional<PlantillaResponse<RegisterUserDTO>> register = findByEmail(registerUserDTO.getEmail());
        if (!register.isPresent()) {
            User user = User.builder()
                    .name(registerUserDTO.getName())
                    .email(registerUserDTO.getEmail())
                    .password(PasswordEncoderUtil.encodePassword(registerUserDTO.getPassword()))
                    .build();
            User userSave = userRepository.save(user);

            if (!userSave.getEmail().isEmpty()) {
                return Optional.ofNullable(buildRegisterUserResponse(userSave));
            }
        }
        return Optional.empty();
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
