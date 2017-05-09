package Final_project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class HotelDAO implements DAO<Hotel> {

    private static HotelDAO hotelDAO;
    private List<Hotel> hotelList = new ArrayList<>();
    private File file;

    public static HotelDAO getHotelDAO() {
        if (getHotelDAO() == null) {
            hotelDAO = new HotelDAO();
        }
        return hotelDAO;
    }

    private HotelDAO() {
        try {
            file = new File("src/HotelDBForProject.txt");
            if (file.createNewFile()) {
                System.out.println("Create new File");
            }
        } catch (IOException e) {
            System.out.println("File has not created !!!");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null) {
                System.out.println("HotelDB is empty !!!");
            } else {
                Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()));
                {
                    stream.forEach(x -> {
                        String fields[] = x.split("/");
                        if (fields.length != 3) {
                            throw new RuntimeException("HotelDB is incorrect");
                        }
                        if (hotelList.stream().anyMatch(hotel -> hotel.getId() == Long.parseLong(fields[0]))) {
                            throw new RuntimeException("HotelDB is incorrect");
                        }
                        hotelList.add(new Hotel(Long.parseLong(fields[0]), fields[1], fields[2]));
                    });
                    stream.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("HotelDB is incorrect");
        }
    }

    private boolean validHotel(Hotel hotel) {
        boolean notValid = false;
        if ((hotel.getId() == 0) || (hotel.getCity() == null || hotel.getCity().equals("")) || (hotel.getName() == null
                || hotel.getName().equals(""))) {
            notValid = true;
        }
        return notValid;
    }


    @Override
    public boolean add(Hotel hotel) {
        try {
            if (validHotel(hotel)) {
                System.out.println("Data fields must be filled");
                return false;
            } else {
                if (hotelList.stream().anyMatch(hotel1 -> hotel.getId() == hotel.getId())) {
                    System.out.println("Hotel with same Id is already exist");
                    return false;
                }
                if (hotelList.stream().anyMatch(hotel1 -> hotel.getCity().toLowerCase().equals(hotel.getCity().toLowerCase())
                        && hotel.getName().toLowerCase().equals(hotel.getName().toLowerCase()))) {
                    System.out.println("Hotel is already added");
                }
                hotelList.add(hotel);
                writeToFile(file, hotelList);
            }
        } catch (NullPointerException e) {
            System.err.println("Incorrect information");
            return false;
        }
        return true;
    }

    private boolean writeToFile(File file, List<Hotel> hotelList) {
        StringBuilder sb = new StringBuilder();
        hotelList.stream().forEach(hotel -> sb.append(hotel.getId() + "-" + hotel.getName() + "-" + hotel.getCity()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.append(sb);
            bw.flush();
        } catch (IOException e) {
            System.err.println("Disable!!! Data cannot save to DataBase");
        }
        return true;
    }

    @Override
    public boolean delete(Hotel hotel) {
        if (!hotelList.contains(hotel)) {
            System.out.println("Hotel is not present in DataBase");
            return false;
        } else {
            try {
                if (hotelList.stream().anyMatch(hotel1 -> hotel1.getId() == hotel.getId())) {
                    RoomDAO roomDAO = RoomDAO.getRoomDAO();
                    for (int i = roomDAO.getBase().size() - 1; i >= 0; i--) {
                        if (roomDAO.getBase().get(i).getId() == hotel.getId()) {
                            roomDAO.delete(roomDAO.getBase().get(i));
                        }
                    }
                    hotelList.remove(hotel);
                    writeToFile(file, hotelList);
                }
            } catch (NullPointerException e) {
                System.err.println("Hotel is not present in DataBase");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean edit(Hotel hotel) {
        try {
            if (validHotel(hotel)) {
                System.out.println("Data fields must be filled.");
                return false;
            } else {
                Hotel hotel1 = hotelDAO.getBase().stream().filter(
                        hotel2 -> hotel.getId() == hotel.getId()).findAny().get();

                hotel1.setCity(hotel.getCity());
                hotel1.setName(hotel.getName());
                writeToFile(file, hotelList);
            }
        } catch (NoSuchElementException e) {
            System.err.println("Hotel with this ID is not present in DataBase");
        } catch (NullPointerException e) {
            System.err.println("Please write correct information");
            return false;
        }
        return true;
    }

    @Override
    public List<Hotel> getBase() {
        return hotelList;
    }
}
