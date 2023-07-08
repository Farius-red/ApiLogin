package juliaosystem.usuarios.api.mappers;

import juliaosystem.usuarios.api.dto.PhoneDTO;
import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.entitis.Phone;
import juliaosystem.usuarios.infraestructure.entitis.User;
import juliaosystem.usuarios.utils.jtw.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserMapper implements PlantillaMapers<User, RegisterUserDTO> {

    @Autowired
    PhoneMapper phoneMapper;
    @Override
    public List<RegisterUserDTO> getListDTO(List<User> t) {
        List<RegisterUserDTO> registerUserDTOList = new ArrayList<>();
        t.stream().forEach(user -> registerUserDTOList.add(getDTO(user)));
        return registerUserDTOList;
    }

    @Override
    public RegisterUserDTO getDTO(User user) {
        return RegisterUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .phones(phoneMapper.getListDTO(user.getPhones()))
                .build();
    }

    @Override
    public List<User> getListEntyti(List<RegisterUserDTO> d) {
        List<User>userList = new ArrayList<>();
        d.stream().forEach(registerUserDTO -> userList.add(getEntyti(registerUserDTO)));
        return userList;
    }

    public List<Phone> getPhoneEntity(RegisterUserDTO registerUserDTO) {
        List<Phone> phoneList = new ArrayList<>();
        registerUserDTO.getPhones().forEach(phoneDTO -> phoneList.add(Phone.builder()
                .citycode(phoneDTO.getCityCode())
                .countrycode(phoneDTO.getCountryCode())
                .number(phoneDTO.getNumber())
                .build()));
        return phoneList;
    }
    @Override
    public User getEntyti(RegisterUserDTO registerUserDTO) {
        return User.builder()
                .active(true)
                .email(registerUserDTO.getEmail())
                .name(registerUserDTO.getName())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .password(PasswordEncoderUtil.encodePassword(registerUserDTO.getPassword()))
                .build();
    }
}
