package ua.cruise.springcruise.util;

import org.mapstruct.Mapper;
import ua.cruise.springcruise.dto.*;
import ua.cruise.springcruise.entity.*;

import java.util.List;

@Mapper (componentModel = "spring")
public interface EntityMapper {
    CruiseDTO cruiseToDTO(Cruise cruise);
    LinerDTO linerToDTO(Liner liner);
    RouteDTO routeToDTO(Route route);
    RoutePointDTO routePointToDTO(RoutePoint routePoint);
    List<RoutePointDTO> routePointListToDTO(List<RoutePoint> routePointList);
    TicketDTO ticketToDTO(Ticket ticket);
    UserDTO userToDTO(User user);

    Cruise dtoToCruise(CruiseDTO cruiseDTO);
    Liner dtoToLiner(LinerDTO linerDTO);
    Route dtoToRoute(RouteDTO routeDTO);
    RoutePoint dtoToRoutePoint(RoutePointDTO routePointDTO);
    List<RoutePoint> dtoToRoutePointList(List<RoutePointDTO> routePointDTOList);
    Ticket dtoToTicket(TicketDTO ticketDTO);
    User dtoToUser(UserDTO userDTO);

}
