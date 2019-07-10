package at.favre.app.personspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class PersonDaoImpl implements PersonDao {

    private static final Logger LOG = LoggerFactory.getLogger(PersonDaoImpl.class);

    private final DataSource dataSource;

    @Autowired
    public PersonDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Person p) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement pstmt = con
                    .prepareStatement("INSERT INTO PERSON (id, name, age) VALUES ( ? , ? , ? )");
            pstmt.setLong(1, p.getId() == 0 ? new Random().nextLong() : p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getAge());
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            LOG.error("could not insert ("+p.getId()+")", e);
        }
    }

    @Override
    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM PERSON LIMIT 1500");
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
            con.setAutoCommit(false);
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
