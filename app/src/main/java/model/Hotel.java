package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hotel {
    private String name;
    private List<Room> rooms;

    public Hotel(int numberOfRooms, String name) {
        if (numberOfRooms <= 0 || numberOfRooms % 2 != 0) {
            throw new IllegalArgumentException(
                "The number of rooms must be positive and even."
            );
        }

        rooms = new ArrayList<>();
        this.name = name;
        int half = numberOfRooms / 2;

        List<Boolean> roomTypes = new ArrayList<>();

        for (int i = 0; i < half; i++) {
            roomTypes.add(true);  // SimpleRoom
            roomTypes.add(false); // BusinessClassRoom
        }

        Collections.shuffle(roomTypes);

        for (int i = 0; i < numberOfRooms; i++) {
            if (roomTypes.get(i)) {
                rooms.add(new SimpleRoom());
            } else {
                rooms.add(new BusinessClassRoom());
            }
        }
    }

    // Register a guest in the first available room.
    // Returns the assigned room, or null if no room is available.
    public Room registerGuest(String name, String surname) {
        for (Room room : rooms) {
            if (room.isAvailable()) {
                Guest guest = new Guest(name, surname);
                room.checkIn(guest);
                return room;
            }
        }

        return null;
    }

    // Check out the guest from a room.
    // Returns true if checkout succeeds, false otherwise.
    public boolean checkOut(int roomNumber) {
        Room room = getRoom(roomNumber);

        if (room == null || room.isAvailable()) {
            return false;
        }

        room.checkOut();
        return true;
    }

    // Return all currently occupied rooms.
    public List<Room> getOccupiedRooms() {
        List<Room> occupiedRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (!room.isAvailable()) {
                occupiedRooms.add(room);
            }
        }

        return occupiedRooms;
    }

    // Return all currently available rooms.
    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }



    // Return all rooms sorted by occupancy count, MAX to MIN.
    // Ties are broken by room number in ascending order.
    public List<Room> getOccupancyReport() {
        List<Room> report = new ArrayList<>(rooms);

        report.sort(
            Comparator.comparingInt(Room::getOccupancyCount)
                .reversed()
                .thenComparingInt(Room::getId)
        );

        return report;
    }

    // Find a room by its number.
    public Room getRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getId() == roomNumber) {
                return room;
            }
        }

        return null;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public int getNumberOfRooms() {
        return rooms.size();
    }
    public String getName() {
        return name;
    }

    public void addRoomPairs(int numberOfPairs) {
        if (numberOfPairs <= 0) {
            throw new IllegalArgumentException(
                "The number of room pairs must be positive."
            );
        }

        for (int i = 0; i < numberOfPairs; i++) {
            // Randomize the order within each pair.
            if (Math.random() < 0.5) {
                rooms.add(new SimpleRoom());
                rooms.add(new BusinessClassRoom());
            } else {
                rooms.add(new BusinessClassRoom());
                rooms.add(new SimpleRoom());
            }
        }
    }

    public float getBasePrice() {
        return Room.basePrice;
    }

    public void setBasePrice(float basePrice) {
        if (!Float.isFinite(basePrice) || basePrice <= 0) {
            throw new IllegalArgumentException(
                "Base price must be a positive, finite number."
            );
        }

        Room.basePrice = basePrice;
    }
}