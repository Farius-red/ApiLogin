package juliaosystem.usuarios.infraestructure.adapters.secundary;


import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.api.mappers.PhoneMapper;
import juliaosystem.usuarios.api.mappers.UserMapper;
import juliaosystem.usuarios.infraestructure.adapters.third.UserResponses;
import juliaosystem.usuarios.infraestructure.entitis.User;
import juliaosystem.usuarios.infraestructure.repository.UserRepository;
import juliaosystem.usuarios.infraestructure.services.secundary.UserServiceInter;
import juliaosystem.usuarios.utils.enums.EmailValidationPattern;
import juliaosystem.usuarios.utils.MensajesRespuesta;
import juliaosystem.usuarios.utils.PlantillaResponse;
import juliaosystem.usuarios.utils.enums.PasswordValidationPattern;
import juliaosystem.usuarios.utils.enums.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserAdapter   implements UserServiceInter {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PhoneMapper phoneMapper;

    @Autowired
    UserResponses userResponses;



public Optional<PlantillaResponse<RegisterUserDTO>> findByEmail(String email) {

    if (!validarEmail(email)) {
        return Optional.of(userResponses.buildResponse(ResponseType.EMAIL_VALIDATION_FAIL.getCode(), RegisterUserDTO.builder().build()));
    }
    return Optional.ofNullable((PlantillaResponse<RegisterUserDTO>) Optional.ofNullable(userRepository.findByEmail(email))
            .map(user -> {
                return (isUserEmpty(user))
                        ? userResponses.buildResponse(ResponseType.EMAIL_NOT_FOUD.getCode(), RegisterUserDTO.builder().build())
                        : userResponses.buildResponse(ResponseType.USER_ISFOUND.getCode(), userMapper.getDTO(user));

            })
            .orElseGet(() -> userResponses.buildResponse(ResponseType.EMAIL_NOT_FOUD.getCode(), RegisterUserDTO.builder().build())));
}


    private PlantillaResponse<RegisterUserDTO> buildRegisterUserResponse(User user) {
        RegisterUserDTO userDTO = userMapper.getDTO(user);
        userDTO.setPhones(phoneMapper.getListDTO(user.getPhones()));
        return userResponses.buildResponse(ResponseType.CREATED.getCode(), userDTO );
    }
    @Override
    public Optional<PlantillaResponse<RegisterUserDTO>> add(RegisterUserDTO registerUserDTO) {
     if(validarPassword(registerUserDTO)) {
         Optional<PlantillaResponse<RegisterUserDTO>> register = findByEmail(registerUserDTO.getEmail());
         return verificarReponseAdd(registerUserDTO, register);
     } else {
         return Optional.ofNullable(userResponses.buildResponse(ResponseType.PASSWORD_VALIDATION_FAIL.getCode(), RegisterUserDTO.builder().build()));
     }
    }


    private boolean validarEmail(String email){
      return email.matches(EmailValidationPattern.VALID.getPattern());
    }
    private boolean validarPassword(RegisterUserDTO registerUserDTO){
        return registerUserDTO.getPassword().matches(PasswordValidationPattern.VALID.getPattern());
    }
    private Optional<PlantillaResponse<RegisterUserDTO>>  verificarReponseAdd( RegisterUserDTO registerUserDTO, Optional<PlantillaResponse<RegisterUserDTO>> register){
        if (!register.get().isRta() && register.get().getMessage() != MensajesRespuesta.USER_ISFOUND.getMensaje() ) {
            User user = userMapper.getEntyti(registerUserDTO);
            user.setPhones(phoneMapper.getListEntyti(registerUserDTO.getPhones()));
            User userSave = userRepository.save(user);
            if (!userSave.getEmail().isEmpty())
                return Optional.ofNullable(buildRegisterUserResponse(userSave));
        }
        if (register.get().getMessage().equals(MensajesRespuesta.USER_ISFOUND.getMensaje()))
          return Optional.ofNullable(userResponses.buildResponse(ResponseType.USER_ISFOUND.getCode(), RegisterUserDTO.builder().build()));

    return  register;
    }
    private boolean isUserEmpty(User user) {
        return user.getName() == null ;
    }




}
