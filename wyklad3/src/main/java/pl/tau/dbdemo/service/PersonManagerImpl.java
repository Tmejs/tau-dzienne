package pl.tau.dbdemo.service;

import pl.tau.dbdemo.domain.Person;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PersonManagerImpl implements PersonManager {

    private Connection connection;

    private PreparedStatement addPersonStmt;
    private PreparedStatement getAllPersonsStmt;

    public PersonManagerImpl(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isDatabaseReady()) {
            createTables();
        }
        setConnection(connection);
    }

    public PersonManagerImpl() throws SQLException {
    }

    public void createTables() throws SQLException {
        connection.createStatement().executeUpdate(
            "CREATE TABLE "
                + "Person(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
                "name varchar(20) NOT NULL, " + "yob integer)");
    }

    public boolean isDatabaseReady() {
        try {
            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if ("Person".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }
            return tableExists;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int addPerson(Person person) {
        int count = 0;
        try {
            addPersonStmt.setString(1, person.getName());
            addPersonStmt.setInt(2, person.getYob());
            count = addPersonStmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new LinkedList<>();
        try {
            ResultSet rs = getAllPersonsStmt.executeQuery();
            
            while (rs.next()) {
                Person p = new Person();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setYob(rs.getInt("yob"));
                persons.add(p);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return persons;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) throws SQLException {
        this.connection = connection;
        addPersonStmt = connection.
            prepareStatement
            ("INSERT INTO Person (name, yob) VALUES (?, ?)");
        getAllPersonsStmt = connection.
        prepareStatement("SELECT id, name, yob FROM Person");
    }


}
