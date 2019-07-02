package at.favre.server.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class PersonDaoImpl implements PersonDao {

    private static final Logger LOG = LoggerFactory.getLogger(PersonDaoImpl.class);

    private final DataSource dataSource;

    @Inject
    public PersonDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Person p) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pstmt = con
                    .prepareStatement("INSERT INTO PERSON (id, name, age) VALUES ( ? , ? , ? )");
            pstmt.setLong(1, p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getAge());
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            LOG.error("could not insert", e);
        }
    }

    @Override
    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM PERSON LIMIT 1000");
            ResultSet set = pstmt.executeQuery();

            while (set.next()) {
                list.add(convert(set));
            }
            con.commit();
        } catch (Exception e) {
            LOG.error("could not find by id", e);
        }

        return list;
    }

    @Override
    public Optional<Person> findById(long id) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM PERSON WHERE PERSON.ID = ?");
            pstmt.setLong(1, id);
            ResultSet set = pstmt.executeQuery();

            if (set.next()) {
                return Optional.of(convert(set));
            }
            con.commit();

        } catch (Exception e) {
            LOG.error("could not find by id", e);
        }

        return Optional.empty();
    }

    private Person convert(ResultSet set) throws SQLException {
        return new Person(
                set.getLong("id"),
                set.getString("name"),
                set.getInt("age"));
    }

    @Override
    public void delete(long id) {

    }
}
