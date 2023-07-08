package juliaosystem.usuarios.api.mappers;

import juliaosystem.usuarios.api.dto.PhoneDTO;
import juliaosystem.usuarios.infraestructure.entitis.Phone;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class PhoneMapper implements PlantillaMapers<Phone, PhoneDTO> {
    @Override
    public List<PhoneDTO> getListDTO(List<Phone> t) {
        List<PhoneDTO> phoneDTOListList = new ArrayList<>();
        t.forEach(phone-> phoneDTOListList.add(getDTO(phone)));
        return phoneDTOListList;
    }

    @Override
    public PhoneDTO getDTO(Phone phone) {
        return PhoneDTO.builder()
                .cityCode(phone.getCitycode())
                .countryCode(phone.getCountrycode())
                .number(phone.getNumber())
                .build();
    }

    @Override
    public List<Phone> getListEntyti(List<PhoneDTO> d) {
         List<Phone> phoneList = new ArrayList<>();
        d.forEach(phoneDTO -> phoneList.add(getEntyti(phoneDTO)));
        return phoneList;
    }

    @Override
    public Phone getEntyti(PhoneDTO phoneDTO) {
        return Phone.builder()
                .citycode(phoneDTO.getCityCode())
                .countrycode(phoneDTO.getCountryCode())
                .number(phoneDTO.getNumber())
                .build();
    }
}
