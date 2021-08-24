package com.example.demothymeleaf.controller;

import com.example.demothymeleaf.form.PersonForm;
import com.example.demothymeleaf.form.PersonFormId;
import com.example.demothymeleaf.form.PersonFormUpdate;
import com.example.demothymeleaf.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    private static List<Person> persons = new ArrayList<Person>();
    static{
        persons.add(new Person(1, "Bill", "Gates"));
        persons.add(new Person(2, "Steve", "Jobs"));
    }

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @Value("${error.messageId}")
    private String errorMessageId;

    @GetMapping("/")
    public String showLogin(Model model){
        PersonFormId personFormId = new PersonFormId();
        model.addAttribute("personFormId", personFormId);
        return "login";
    }

    @PostMapping("/")
    public String login(Model model, PersonFormId personFormId){
        int id = personFormId.getId();
        for(Person p : persons)
            if(p.getId() == id){
                model.addAttribute("message", message);
                return "index";
            }

        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("message", message);
        return "index";
    }

    //@GetMapping("/personList")
    @RequestMapping(value = { "/personList" }, method = RequestMethod.GET)
    public String personList(Model model){
        model.addAttribute("persons", persons);
        return "personList";
    }

    @GetMapping("/addPerson")
    public String showAddPersonPage(Model model){

        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);

        return "addPerson";
    }

    @PostMapping("/addPerson")
    public String savePerson(Model model,
                             @ModelAttribute PersonForm personForm){

        int id = personForm.getId();
        String firstName = personForm.getFirstName();
        String lastName = personForm.getLastName();

        if(firstName != null && firstName.length() > 0
                && lastName != null && lastName.length() > 0){
            Person newPerson = new Person(id ,firstName, lastName);
            persons.add(newPerson);

            return "redirect:/personList";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "addPerson";
    }

    @GetMapping("/deletePerson")
    public String showDeletePersonPage(Model model){

        PersonFormId personFormId = new PersonFormId();
        model.addAttribute("personFormId", personFormId);

        return "deletePerson";
    }

    @PostMapping("/deletePerson")
    public String DeletePerson(Model model, @ModelAttribute PersonFormId personFormId){
        int id = personFormId.getId();

        if(persons.contains(id)){
            persons.removeIf(t -> t.getId() == id);
            return "redirect:/personList";
        }

        model.addAttribute("errorMessageId", errorMessageId);
        return "deletePerson";
    }

    @GetMapping("/updatePerson")
    public String showUpdatePersonPage(Model model){
        PersonFormUpdate personFormUpdate = new PersonFormUpdate();
        model.addAttribute("personFormUpdate", personFormUpdate);

        return "updatePerson";
    }

    @PostMapping("/updatePerson")
    public String UpdatePerson(Model model, @ModelAttribute PersonFormUpdate personFormUpdate){
        int id = personFormUpdate.getId();
        String firstName = personFormUpdate.getFirstName();
        String lastName = personFormUpdate.getLastName();

        for(Person p : persons){
            if(p.getId() == id){
                p.setFirstName(firstName);
                p.setLastName(lastName);
                return "redirect:/personList";
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        return "updatePerson";
    }

}
