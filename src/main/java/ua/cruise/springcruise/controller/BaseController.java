package ua.cruise.springcruise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public interface BaseController {
    default void checkModifiedObjectsConflict(Map<Long, String> map, Long id) throws ResponseStatusException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login;
        if (principal instanceof UserDetails) {
            login = ((UserDetails) principal).getUsername();
        } else {
            login = principal.toString();
        }

        if (map.containsKey(id)) {
            if (!map.get(id).equals(login))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Object modifing conflict [object_id=" + id + ", login=" + login + "]");
        } else {
            map.put(id, login);
        }
    }
}
