package juliaosystem.usuarios.infraestructure.adapters.third;

import juliaosystem.usuarios.api.dto.RegisterUserDTO;
import juliaosystem.usuarios.infraestructure.entitis.User;
import juliaosystem.usuarios.utils.MensajesRespuesta;
import juliaosystem.usuarios.utils.PlantillaResponse;
import juliaosystem.usuarios.utils.enums.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserResponses<E> {

    public  PlantillaResponse<E> buildResponse(int tipoRespuesta, E e
                                                            ) {
        ResponseType responseType = ResponseType.fromCode(tipoRespuesta);
        if (responseType != null) {
            return PlantillaResponse.<E>builder()
                    .message(responseType.getMessage())
                    .data(e)
                    .isRta(responseType.isRta())
                    .httpStatus(responseType.getHttpStatus())
                    .build();
        }

        throw new IllegalArgumentException("Tipo de respuesta no válido: " + tipoRespuesta);
    }
}
