package ua.cruise.springcruise.util;

import org.springframework.stereotype.Component;
import ua.cruise.springcruise.dto.CruiseDTO;
import ua.cruise.springcruise.entity.Cruise;

@Component
public class CruiseMapper {
    public Cruise DTOtoCruise(CruiseDTO cruiseDTO){
        return Cruise.builder()
                .id(cruiseDTO.getId())
                .name(cruiseDTO.getName())
                .liner(cruiseDTO.getLiner())
                .route(cruiseDTO.getRoute())
                .startDateTime(cruiseDTO.getStartDateTime())
                .endDateTime(cruiseDTO.getEndDateTime())
                .price(cruiseDTO.getPrice())
                .description(cruiseDTO.getDescription())
                .imageName(cruiseDTO.getImageName())
                .build();
    }
}
