package Final_project;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Controller {
    public User UserRegistration(User user, long userId) {
        try {
            User newUserToRegistration = UserDAO.getUserDAO().getBase().stream().filter(user1 -> user1.getId() == userId).findFirst().get();
            System.err.printf("Cancel!!! User with the same Id is already exist in UserBase");

            if (newUserToRegistration != null) {
                user = new User(user.getId(), user.getName(), user.getPassword());
                System.out.println("User was successfully registered");
            }
        } catch (NoSuchElementException | NullPointerException e) {
            System.err.printf("Cancel!!! This User is already exist in UserBase");
        }
        return user;
    }

    public boolean editUser(User user) {
        return UserDAO.getUserDAO().edit(user);
    }

    public boolean deleteUser(User user) {
        return UserDAO.getUserDAO().delete(user);
    }

    public List<Hotel> findHotelByName(String name) {
        List<Hotel> hotelList = HotelDAO.getHotelDAO().getBase().stream().filter(hotel ->
                hotel.getName().toLowerCase().equals(name.toLowerCase())).collect(Collectors.toList());
        if (hotelList.isEmpty()) {
            System.out.println("Hotel not found");
        }
        return hotelList;
    }

    public Hotel addHotel(Hotel hotel, long hotelId) {
        try {
            Hotel newHotelToAdd = HotelDAO.getHotelDAO().getBase().stream().filter(hotel1 -> hotel1.getId() == hotelId).findFirst().get();
            System.out.println("Cancel!!! This Hotel is already exist in HotelBase");

            if (newHotelToAdd != null) {
                hotel = new Hotel(hotel.getName(), hotel.getCity(), hotel.getId());
                System.out.println("Hotel was successfully added");
            }
        } catch (NoSuchElementException | NullPointerException e) {
            System.out.println("Cancel!!! This Hotel is already exist in HotelBase");
        }
        return hotel;
    }

    public boolean deleteHotel(Hotel hotel) {
        return HotelDAO.getHotelDAO().delete(hotel);
    }


    public boolean editHotel(Hotel hotel) {
        return HotelDAO.getHotelDAO().edit(hotel);
    }

    public List<Hotel> findHotelByCity(String city) {
        List<Hotel> hotelList = HotelDAO.getHotelDAO().getBase().stream().filter(hotel ->
                hotel.getCity().toLowerCase().equals(city.toLowerCase())).collect(Collectors.toList());
        if (hotelList.isEmpty()) {
            System.out.println("Hotel not found");
        }
        return hotelList;
    }

    public void bookingRoomOnUserName(long userId, long roomId, long hotelId) {
        try {
            User newUserForRegistration = UserDAO.getUserDAO().getBase().stream().filter(user -> user.getId() == userId).findFirst().get();
            try {
                Room newRoomForRegistration = RoomDAO.getRoomDAO().getBase().stream().filter(room -> room.getId() == roomId).findFirst().get();
                newRoomForRegistration.setUserReserved(newUserForRegistration);
                System.out.println("Room was successfully booked");
                RoomDAO.getRoomDAO().edit(newRoomForRegistration);
            } catch (NullPointerException | NoSuchElementException e) {
                System.err.printf("User is absent in UserBase");
            }
        } catch (NullPointerException | NoSuchElementException e) {
            System.err.printf("Room is absent in RoomBase");
        }
    }

    public void RoomCancelBooking(long roomId, long hotelId, long userId) {
        try {
            Room newRoomForRegistration = RoomDAO.getRoomDAO().getBase().stream().filter(room -> room.getId() == roomId).findFirst().get();
            newRoomForRegistration.setUserReserved(null);
            System.out.println("Room booking is canceled");
            RoomDAO.getRoomDAO().edit(newRoomForRegistration);
        } catch (NullPointerException | NoSuchElementException e) {
            System.err.printf("Room is absent in RoomBase");
        }
    }

    public Room findRoomByHotel(long roomId, Room room) {
        try {
            Room searchingRoom = RoomDAO.getRoomDAO().getBase().stream().filter(room1 -> room1.getId() == roomId).findAny().get();
            System.out.println("Room has been successfully found");
            room = searchingRoom;
            if (searchingRoom == null) {
                System.out.println("Room not found");
            }
        } catch (NoSuchElementException | NullPointerException e) {
            System.err.printf("Room not found");
        }
        return room;
    }


    public boolean deleteRoom(Room room) {
        return RoomDAO.getRoomDAO().delete(room);
    }


    public boolean editRoom(Room room) {
        return RoomDAO.getRoomDAO().edit(room);
    }


    public Room addRoomToHotel(long roomId, Room room) {
        try {
            Room newRoomToAdd = RoomDAO.getRoomDAO().getBase().stream().filter(room1 -> room1.getId() == roomId).findFirst().get();
            System.out.println("Cancel!!! This Room is already exist in RoomBase");

            if (newRoomToAdd != null) {
                room = new Room(roomId, room.getPersons(), room.getPrice(), room.getHotel(), room.getUserReserved());
                System.out.println("Room was successfully added");
            }
        } catch (NoSuchElementException | NullPointerException e) {
            System.out.println("Cancel!!! This Room is already exist in RoomBase");
        }
        return room;
    }
}
