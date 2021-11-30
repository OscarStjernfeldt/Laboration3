import java.sql.*;
import java.util.Scanner;

public class Labb3 {
    private static final Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        Labb3 labb = new Labb3();

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306",
                "darkend", "1234");

        labb.createDatabase();
        labb.createTable();

        labb.menu();
    }

    private void createDatabase() throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP DATABASE IF EXISTS laboration3;");
        statement.executeUpdate("CREATE DATABASE laboration3;");
        statement.executeUpdate("USE laboration3;");
    }

    private void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS artist");
        statement.executeUpdate("CREATE TABLE artist (id SMALLINT AUTO_INCREMENT, first_name VARCHAR(50), last_name VARCHAR(50), age TINYINT UNSIGNED, PRIMARY KEY (id));");
    }

    private String addInputs() throws SQLException {
        System.out.print("Enter a first name: ");
        String firstName = getInput();
        System.out.print("Enter a last name: ");
        String lastName = getInput();
        while (true) {
            try {
                System.out.print("Enter an age: ");
                int age = Integer.parseInt(getInput());
                return "Rows affected: " + add(firstName, lastName, age);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid age");
            }
        }
    }

    private int add(String firstName, String lastName, int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO laboration3.artist (first_name, last_name, age) VALUES (?, ?, ?);");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setInt(3, age);

        return statement.executeUpdate();
    }

    private String deleteInput() throws SQLException {
        while (true) {
            try {
                System.out.println("Enter an ID: ");
                int id = Integer.parseInt(getInput());
                return "Rows affected: " + delete(id);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid ID");
            }
        }
    }

    private int delete(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM laboration3.artist WHERE id = ?;");
        statement.setInt(1, id);
        return statement.executeUpdate();
    }

    private String updateInputs() throws SQLException {
        while (true) {
            try {
                System.out.print("Please enter the ID of the artist: ");
                int id = Integer.parseInt(getInput());
                System.out.print("What is their age: ");
                int age = Integer.parseInt(getInput());
                return "Rows affected: " + update(id, age);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    private int update(int id, int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE laboration3.artist SET age = ? WHERE id = ?");
        statement.setInt(1, age);
        statement.setInt(2, id);
        return statement.executeUpdate();
    }

    private void printShowAll() throws SQLException {
        ResultSet result = showAll();
        while (result.next()) {
            System.out.println("Artist ID: " + result.getString("id") +
                    ", First name: " + result.getString("first_name") +
                    ", Last name: " + result.getString("last_name") +
                    ", Age: " + result.getString("age"));
        }
    }

    private ResultSet showAll() throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM laboration3.artist");
    }

    private int findByIDInput() {
        while (true)
            try {
                System.out.print("Please enter the ID of the artist: ");
                return Integer.parseInt(getInput());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
    }

    private void printFindByID() throws SQLException {
        ResultSet result = findByID(findByIDInput());
        while (result.next()) {
            System.out.println("Artist ID: " + result.getString("id") +
                    ", First name: " + result.getString("first_name") +
                    ", Last name: " + result.getString("last_name") +
                    ", Age: " + result.getString("age"));
        }
    }

    private ResultSet findByID(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM laboration3.artist WHERE id = ?");
        statement.setInt(1, id);

        return statement.executeQuery();
    }

    private int findByAgeInput() {
        while (true)
            try {
                System.out.print("How old is the artist: ");
                return Integer.parseInt(getInput());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
    }

    private void printFindByAge() throws SQLException {
        ResultSet result = findByAge(findByAgeInput());
        while (result.next()) {
            System.out.println("Artist ID: " + result.getString("id") +
                    ", First name: " + result.getString("first_name") +
                    ", Last name: " + result.getString("last_name") +
                    ", Age: " + result.getString("age"));
        }
    }

    private ResultSet findByAge(int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM laboration3.artist WHERE age = ?");
        statement.setInt(1, age);
        return statement.executeQuery();
    }

    private void findByNameInputs() throws SQLException {
        System.out.println("Enter the first name of the artist");
        String firstName = getInput();
        System.out.println("Enter the last name of the artist");
        String lastName = getInput();
        printFindByName(firstName, lastName);
    }

    private void printFindByName(String firstName, String lastName) throws SQLException {
        ResultSet result = findByName(firstName, lastName);
        while (result.next()) {
            System.out.println("Artist ID: " + result.getString("id") +
                    ", First name: " + result.getString("first_name") +
                    ", Last name: " + result.getString("last_name") +
                    ", Age: " + result.getString("age"));
        }
    }

    private ResultSet findByName(String first_name, String last_name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM laboration3.artist WHERE first_name = ? AND last_name = ?;");
        statement.setString(1, first_name);
        statement.setString(2, last_name);
        return statement.executeQuery();
    }

    private void menu() throws SQLException {
        while (true) {
            printOptions();
            switch (getInput().toLowerCase()) {
                case "add" -> System.out.println(addInputs());
                case "delete" -> System.out.println(deleteInput());
                case "update" -> System.out.println(updateInputs());
                case "showall" -> printShowAll();
                case "findbyid" -> printFindByID();
                case "findbyage" -> printFindByAge();
                case "findbyname" -> findByNameInputs();
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    private void printOptions() {
        System.out.println("What would you like to do?");
        System.out.println("Options are: Add, Delete, Update, ShowAll, FindByID, FindByAge, FindByName and Exit");
    }

    private String getInput() {
        return scanner.nextLine();
    }
}
