package juliaosystem.usuarios.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlantillaResponse<E>{
    private boolean isRta;
    private String message;
    private HttpStatus httpStatus;
    private E data;
    private List<E> dataList;
}
