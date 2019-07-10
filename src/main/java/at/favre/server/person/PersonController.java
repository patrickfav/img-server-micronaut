package at.favre.server.person;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller("/person")
public class PersonController {

    private final PersonDao personDao;

    public PersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<Person> getById(@Parameter("id") long id) {
        Optional<Person> p = personDao.findById(id);

        if (p.isPresent()) {
            return HttpResponse.ok(p.get());
        }


        throw new NotFoundException();
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<List<Person>> getAll() {
        return HttpResponse.ok(personDao.findAll());
    }

    @Post
    public void create(@Body Person p) {
        personDao.create(p);
    }
}
