package Final_project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class RoomDAO implements DAO<Room>{
    private static RoomDAO roomDAO;
    private List<Room> roomDataBase = new ArrayList<>();
    private File file;

    public File getFile() {
        return file;
    }
    public static RoomDAO getRoomDAO() {
        if (roomDAO == null) {
            roomDAO = new RoomDAO();
        }
        return roomDAO;
    }

    private boolean validRoom(Room room) {
        boolean notValid = false;
        if ((room.getId() == 0) || (room.getPersons() == 0) || (room.getHotel() == null)) {
            notValid = true;
        }
        return notValid;
    }

    private RoomDAO() {
        try {
            file = new File("src/RoomDB.txt");
            if (file.createNewFile()) {
                System.out.println("New file was created");
            }
        } catch (IOException e) {
            System.err.println("File was not created!!!");
        }
        HotelDAO hotelDAO = HotelDAO.getHotelDAO();
        UserDAO userDAO = UserDAO.getUserDAO();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null) {
                System.out.println("Room is empty");
            } else {
                Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()));
                stream.forEach(line -> {
                    String fields[] = line.split("/");
                    if (roomDataBase.stream().anyMatch(room -> room.getId() == Long.parseLong(fields[0]))) {
                        throw new RuntimeException("Room DataBase is incorrect!!!");
                    }
                    if (fields.length != 5 && fields.length != 4) {
                        throw new RuntimeException("Room DataBase is incorrect!!!");
                    }
                    Hotel hotel = null;
                    User user = null;
                    for (int i = 0; i < hotelDAO.getBase().size(); i++) {
                        if (hotelDAO.getBase().get(i).getId() == Long.parseLong(fields[3])) {
                            hotel = hotelDAO.getBase().get(i);
                            break;
                        }
                    }
                    if (fields.length == 5) {
                        for (int i = 0; i < userDAO.getBase().size(); i++) {
                            if (userDAO.getBase().get(i).getId() == Long.parseLong(fields[4])) {
                                user = userDAO.getBase().get(i);
                                break;
                            }
                        }
                    }
                    if (hotel != null) {
                        roomDataBase.add(new Room(Long.parseLong(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), hotel, user));
                    }
                });
                stream.close();
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Room DataBase is incorrect!!!");
        }
    }

    @Override
    public boolean add(Room room) {
        if (!HotelDAO.getHotelDAO().getBase().contains(room.getHotel())) {
            System.out.println("Please add Hotel to the DataBase!!!");
            return false;
        }
        try {
            if (validRoom(room)) {
                System.out.println("Please fill all fields");
                return false;
            } else {
                if (roomDataBase.stream().anyMatch(room1 -> room.getId() == room.getId())) {
                    System.out.println("Romm is already exist!!!");
                    return false;
                } else {
                    roomDataBase.add(room);
                    writeToFile(file, roomDataBase);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Please fill correct information to file!!!");
            return false;
        }
        return true;
    }

    public boolean writeToFile(File file, List<Room> rooms) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Room room : rooms) {
                bw.write(room.getId() + " ");
                bw.write(room.getPersons() + " ");
                bw.write(room.getPrice() + " ");
                bw.write(String.valueOf(room.getHotel().getId()));
                if (room.getUserReserved() != null) {
                    bw.write(" " + room.getUserReserved().getId());
                }
                bw.write(System.lineSeparator());
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Cannot save to DataBase!!!");
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Room room) {
        if (!roomDataBase.contains(room)) {
            System.out.println("The room is absent in DataBase");
            return false;
        } else {
            try {
                if (roomDataBase.stream().anyMatch(room1 -> room1.getId() == room.getId())) {
                    roomDataBase.remove(room);
                    writeToFile(file, roomDataBase);
                }
            } catch (NullPointerException e) {
                System.err.println("The room is absent");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean edit(Room room) {
        try {
            Room editRoom = roomDataBase.stream().filter(room1 -> room1.getId() == room.getId()).findAny().get();
            if (validRoom(room)) {
                System.out.println("All fields must be filled!!!");
                return false;
            } else {
                editRoom.setPersons(room.getPersons());
                editRoom.setPrice(room.getPrice());
                editRoom.setHotel(room.getHotel());
                editRoom.setUserReserved(room.getUserReserved());
                writeToFile(file, roomDataBase);
            }
        } catch (NoSuchElementException e) {
            System.err.println("Room" + room.getId() + " does not exist.");
            return false;
        } catch (NullPointerException e) {
            System.err.println("Please fill correct information to file!!!");
            return false;
        }
        return true;
    }

    @Override
    public List<Room> getBase() {
        return roomDataBase;
    }
}
