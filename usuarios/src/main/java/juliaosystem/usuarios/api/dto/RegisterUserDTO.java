package juliaosystem.usuarios.api.dto;

import juliaosystem.usuarios.infraestructure.entitis.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterUserDTO {
        private UUID id;
        private String name;
        private String email;
        private String password;
        private List<PhoneDTO> phones;
        private String token;
}
