package at.favre.server.person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {

    void create(Person p);

    List<Person> findAll();

    Optional<Person> findById(long id);

    void delete(long id);
}
