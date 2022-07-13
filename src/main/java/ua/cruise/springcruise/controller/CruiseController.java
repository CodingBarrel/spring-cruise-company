package ua.cruise.springcruise.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.util.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * An controller class that controls authorized users' requests related to cruise (such as view all cruises) and redirects
 * them to requested services. Controls view layer.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Controller
 */

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/cruise")
public class CruiseController {
    private final CruiseService cruiseService;

    @GetMapping("")
    public String readAll(@RequestParam(value = "dateTimeSign", required = false) String dateTimeSignAsString,
                          @RequestParam(value = "startDateTime", required = false) String dateTimeAsString,
                          @RequestParam(value = "durationSign", required = false) String durationSignAsString,
                          @RequestParam(value = "duration", required = false) Integer duration,
                          @RequestParam(value = "durationType", required = false) String durationTypeAsString,
                          @RequestParam(value = "page", required = false) Integer pageId,
                          @RequestParam(value = "perPage", required = false) Integer countPerPage,
                          Model model) {
        Constants.equalitySign dateTimeSign;
        try {
            dateTimeSign = Constants.equalitySign.valueOf(dateTimeSignAsString);
        } catch (IllegalArgumentException | NullPointerException ex) {
            dateTimeSign = null;
        }
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeAsString).truncatedTo(ChronoUnit.SECONDS);
        }catch (DateTimeParseException | NullPointerException ex){
            dateTime = null;
        }
        Constants.equalitySign durationSign;
        try {
            durationSign = Constants.equalitySign.valueOf(durationSignAsString);
        } catch (IllegalArgumentException | NullPointerException ex) {
            durationSign = null;
        }
        Constants.durationType durationType;
        if (duration == null)
            duration = 0;
        int durationAsDays = 0;
        try {
            durationType = Constants.durationType.valueOf(durationTypeAsString);
            if (durationType.equals(Constants.durationType.WEEKS))
                durationAsDays = duration * 7;
            else if (durationType.equals(Constants.durationType.MONTHS))
                durationAsDays = duration * 30;
            else
                durationAsDays = duration;
        } catch (IllegalArgumentException | NullPointerException ex) {
            durationType = null;
        }
        if (pageId == null)
            pageId = 1;
        if (countPerPage == null)
            countPerPage = 5;
        Page<Cruise> cruisePage = cruiseService.findByStartDateTimeAndDuration(dateTimeSign, dateTime, durationSign, durationAsDays, pageId-1, countPerPage);
        model.addAttribute("cruiseList", cruisePage);
        model.addAttribute("dateTimeSign", dateTimeSign);
        model.addAttribute("dateTime", dateTime);
        model.addAttribute("durationSign", durationSign);
        model.addAttribute("duration", duration);
        model.addAttribute("durationType", durationType);
        model.addAttribute("signList", Constants.equalitySign.values());
        model.addAttribute("durationTypeList", Constants.durationType.values());
        model.addAttribute("page", pageId);
        model.addAttribute("perPage", countPerPage);
        return "cruise/readAll";
    }

    @GetMapping("/{id}")
    public String readById(@PathVariable Long id, Model model) {
        Cruise foundCruise = cruiseService.findById(id);
        model.addAttribute("cruise", foundCruise);
        return "cruise/readById";
    }
}
