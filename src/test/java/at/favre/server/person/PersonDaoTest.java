package at.favre.server.person;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class PersonDaoTest {

    @Inject
    private PersonDao personDao;

    @Test
    void findById() {
        Person original = new Person(6984684, "pat", 19);
        personDao.create(original);
        Person p = personDao.findById(6984684).get();

        assertEquals(original, p);
    }
}
