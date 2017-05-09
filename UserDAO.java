package Final_project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

public class UserDAO implements DAO<User> {

    private static UserDAO userDAO;
    private List<User> userDataBase = new ArrayList<>();
    private File file;

    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    private boolean validUser(User user) {
        boolean notValid = false;
        if (user.getName() == null || user.getName().equals("") || user.getPassword() == null || user.getPassword().equals("")) {
            notValid = true;
        }
        return notValid;
    }

    private UserDAO() {
        try {
            file = new File("src/UserDataBase.txt");
            if (file.createNewFile()) {
                System.out.println("DataBase for users is create");
            }
        } catch (IOException e) {
            System.out.println("DataBase for users cannot created!!!");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null) {
                System.out.println("DataBase for users is empty");
            } else {
                Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()));
                stream.forEach(line -> {
                    String fields[] = line.split(" ");
                    if (fields.length != 3)
                        throw new RuntimeException("DataBase of Users is incorrect!!!");
                    userDataBase.add(new User(Long.parseLong(fields[0]), fields[1], fields[2]));
                });
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("DataBase of Users is incorrect!!!");
        }
    }

    private boolean writeToFile(File file, List<User> users) {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                br.write(user.getId() + " ");
                br.write(user.getName() + " ");
                br.write(user.getPassword() + " ");
//                writeToFile(file, userDataBase);
            }
        } catch (IOException e) {
            System.err.printf("Cannot save user to DataBase");
            return false;
        }
        return true;
    }

    @Override
    public boolean add(User user) {
        try {
            if (validUser(user)) {
                System.out.println("All fields must be filled!!!");
                return false;
            } else {
                if (userDataBase.stream().anyMatch(user1 -> user1.getId() == user.getId() ||
                        Objects.equals(user.getName().toLowerCase(), user1.getName().toLowerCase()))) {
                    System.out.println("User already exist!!!");
                    return false;
                } else {
                    userDataBase.add(user);
                    writeToFile(file, userDataBase);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Please type incorrect data!!!");
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(User user) {
        if (userDataBase.contains(user)) {
            try {
                RoomDAO roomDAO = RoomDAO.getRoomDAO();
                roomDAO.getBase().forEach(room -> {
                    if (room.getUserReserved() != null && room.getUserReserved().equals(user)) {
                        room.setUserReserved(null);
                    }
                });
                roomDAO.writeToFile(roomDAO.getFile(), roomDAO.getBase());
                userDataBase.remove(user);
                writeToFile(file, userDataBase);
                return true;
            } catch (NullPointerException e) {
                System.err.printf("DataBase is empty");
            }
        }
        return false;
    }

    @Override
    public boolean edit(User user) {
        try {
            User editUser = userDataBase.stream().filter(u -> u.getId() == user.getId()).findAny().get();
            if (validUser(user)) {
                System.err.printf("Fields User and Password must be filled!!!");
                return false;
            } else {
                editUser.setName(user.getName());
                editUser.setPassword(user.getPassword());
                writeToFile(file, userDataBase);
            }
        } catch (NoSuchElementException e) {
            System.err.println("User with this Id is absent n DataBase");
            return false;
        } catch (NullPointerException e) {
            System.err.println("Please type incorrect User name");
            return false;
        }
        return true;
    }

    @Override
    public List<User> getBase() {
        return userDataBase;
    }
}
