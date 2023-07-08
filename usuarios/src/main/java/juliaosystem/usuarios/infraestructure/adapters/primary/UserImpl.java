package juliaosystem.usuarios.infraestructure.adapters.primary;

import juliaosystem.usuarios.api.dto.LoginDTO;
import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.adapters.third.UserResponses;
import juliaosystem.usuarios.infraestructure.services.secundary.UserServiceInter;
import juliaosystem.usuarios.utils.MensajesRespuesta;
import juliaosystem.usuarios.utils.PlantillaResponse;
import juliaosystem.usuarios.utils.enums.PasswordValidationPattern;
import juliaosystem.usuarios.utils.enums.ResponseType;
import juliaosystem.usuarios.utils.jtw.JwtTokenUtil;
import juliaosystem.usuarios.utils.jtw.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;


import java.util.ArrayList;
import java.util.Optional;
@Service
public class UserImpl {
    @Autowired
    UserServiceInter userServiceInter;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserResponses userResponses;

    public Optional<PlantillaResponse<RegisterUserDTO>> login(LoginDTO loginDTO){
        return Optional.ofNullable(userServiceInter.findByEmail(loginDTO.getEmail()))
                .map(user -> {
                    if (PasswordEncoderUtil.matches(loginDTO.getPassword(), user.get().getData().getPassword())) {
                        UserDetails userDetails =  new User(loginDTO.getEmail(), loginDTO.getPassword(), new ArrayList<>());
                        String token = jwtTokenUtil.generateToken(userDetails);
                        user.get().getData().setToken(token);
                        return userResponses.buildResponse(ResponseType.USER_lOGEADO.getCode(), user.get().getData());
                    }else{
                        return userResponses.buildResponse(ResponseType.EMAIL_NOT_FOUD.getCode(), RegisterUserDTO.builder().build());

                }});

    }


    public Optional<PlantillaResponse<RegisterUserDTO>> registro(RegisterUserDTO registerUserDTO){
        return userServiceInter.add(registerUserDTO);
    }
}
