package Final_project;

public class Room {
    private long id;
    private int persons;
    private int price;
    private Hotel hotel;
    private User userReserved;

    public Room(long id, int persons, int price, Hotel hotel, User userReserved) {
        this.id = id;
        this.persons = persons;
        this.price = price;
        this.hotel = hotel;
        this.userReserved = userReserved;
    }

    public Room(long id, int persons, int price, Hotel hotel) {
        this.id = id;
        this.persons = persons;
        this.price = price;
        this.hotel = hotel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public User getUserReserved() {
        return userReserved;
    }

    public void setUserReserved(User userReserved) {
        this.userReserved = userReserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (id != room.id) return false;
        if (persons != room.persons) return false;
        if (price != room.price) return false;
        return hotel != null ? hotel.equals(room.hotel) : room.hotel == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + persons;
        result = 31 * result + price;
        result = 31 * result + (hotel != null ? hotel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", persons=" + persons +
                ", price=" + price +
                ", hotel=" + hotel +
                '}';
    }
}
