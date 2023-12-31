package ua.cruise.springcruise.util;

import org.mapstruct.Mapper;
import ua.cruise.springcruise.dto.*;
import ua.cruise.springcruise.entity.*;

/**
 * Interface that contains methods to help transform from DTO object into entity and vice-versa
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Mapper
 */

@Mapper (componentModel = "spring")
public interface EntityMapper {
    CruiseDTO cruiseToDTO(Cruise cruise);
    LinerDTO linerToDTO(Liner liner);
    RouteDTO routeToDTO(Route route);
    RoutePointDTO routePointToDTO(RoutePoint routePoint);
    TicketDTO ticketToDTO(Ticket ticket);
    UserDTO userToDTO(User user);

    Cruise dtoToCruise(CruiseDTO cruiseDTO);
    Liner dtoToLiner(LinerDTO linerDTO);
    Route dtoToRoute(RouteDTO routeDTO);
    RoutePoint dtoToRoutePoint(RoutePointDTO routePointDTO);
    Ticket dtoToTicket(TicketDTO ticketDTO);
    User dtoToUser(UserDTO userDTO);

}
