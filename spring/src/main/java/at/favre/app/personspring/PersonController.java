package at.favre.app.personspring;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    private final PersonDao personDao;

    public PersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping(value = "/person/{id}", produces = "application/json")
    public Person getById(@PathVariable("id") Long id) {
        Optional<Person> p = personDao.findById(id);

        if (p.isPresent()) {
            return p.get();
        }


        throw new NotFoundException();
    }

    @GetMapping(value = "/person", produces = "application/json")
    public List<Person> getAll() {
        return personDao.findAll();
    }

    @PostMapping(value = "/person")
    public void create(@RequestBody Person p) {
        personDao.create(p);
    }
}
