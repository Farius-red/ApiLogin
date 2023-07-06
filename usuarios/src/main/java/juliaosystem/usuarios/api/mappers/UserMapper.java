package juliaosystem.usuarios.api.mappers;

import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.entitis.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserMapper implements PlantillaMapers<User, RegisterUserDTO> {
    @Override
    public List<RegisterUserDTO> getListDTO(List<User> t) {
        List<RegisterUserDTO> registerUserDTOList = new ArrayList<>();
        t.stream().forEach(user -> registerUserDTOList.add(getDTO(user)));
        return registerUserDTOList;
    }

    @Override
    public RegisterUserDTO getDTO(User user) {
        return RegisterUserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .phones(user.getPhones())
                .build();
    }

    @Override
    public List<User> getListEntyti(List<RegisterUserDTO> d) {
        List<User>userList = new ArrayList<>();
        d.stream().forEach(registerUserDTO -> userList.add(getEntyti(registerUserDTO)));
        return userList;
    }

    @Override
    public User getEntyti(RegisterUserDTO registerUserDTO) {
        return User.builder()
                .active(true)
                .email(registerUserDTO.getEmail())
                .name(registerUserDTO.getName())
                .password(registerUserDTO.getPassword())
                .phones(registerUserDTO.getPhones())
                .build();
    }
}
