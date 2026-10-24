package view;

import model.Guest;
import model.Hotel;
import model.Room;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HotelView extends JFrame {

    public interface Actions {
        Room registerGuest(String name, String surname);
        boolean checkOut(int roomNumber);
        void addRoomPairs(int numberOfPairs);
        void changeBasePrice(float newBasePrice);
    }

    private final Hotel hotel;
    private final Actions actions;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pages = new JPanel(cardLayout);

    private JPanel roomGrid;
    private JComboBox<String> filterBox;
    private JToggleButton statsToggle;

    private final NumberFormat currency =
        NumberFormat.getCurrencyInstance(Locale.FRANCE);

    public HotelView(Hotel hotel, Actions actions) {
        this.hotel = hotel;
        this.actions = actions;

        setTitle(hotel.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setSize(1050, 750);
        setLocationRelativeTo(null);

        pages.add(createHomePage(), "HOME");
        pages.add(createRoomsPage(), "ROOMS");

        setContentPane(pages);
        refreshRoomGrid();
    }

    // --------------------------------------------------
    // Landing page
    // --------------------------------------------------

    private JPanel createHomePage() {
        JPanel page = new JPanel(new BorderLayout(20, 20));
        page.setBorder(
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        );

        JLabel title = new JLabel(
            hotel.getName(),
            SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 32));

        JLabel subtitle = new JLabel(
            "Hotel management",
            SwingConstants.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 17));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 8));
        header.add(title);
        header.add(subtitle);

        JButton registerButton = createMenuButton("Register a guest");
        JButton checkoutButton = createMenuButton("Check out a guest");
        JButton roomsButton = createMenuButton("View rooms");
        JButton addRoomsButton = createMenuButton("Add rooms");
        JButton priceButton = createMenuButton("Change base price");

        registerButton.addActionListener(
            event -> showRegisterDialog()
        );

        checkoutButton.addActionListener(
            event -> showCheckoutDialog()
        );

        roomsButton.addActionListener(event -> {
            refreshRoomGrid();
            cardLayout.show(pages, "ROOMS");
        });
        addRoomsButton.addActionListener(
            event -> showAddRoomsDialog()
        );

        priceButton.addActionListener(
            event -> showChangeBasePriceDialog()
        );

        JPanel menu = new JPanel();
        menu.setLayout(
            new BoxLayout(menu, BoxLayout.Y_AXIS)
        );

        menu.add(registerButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(checkoutButton);
        menu.add(Box.createVerticalStrut(15));
        menu.add(roomsButton);
        menu.add(addRoomsButton);
        menu.add(Box.createVerticalStrut(15));

        menu.add(priceButton);
        menu.add(Box.createVerticalStrut(15));
        page.add(header, BorderLayout.NORTH);
        page.add(menu, BorderLayout.CENTER);

        return page;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(320, 55));
        button.setPreferredSize(new Dimension(320, 55));
        button.setFocusable(false);
        return button;
    }

    // --------------------------------------------------
    // Room grid page
    // --------------------------------------------------

    private JPanel createRoomsPage() {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JButton backButton = new JButton("← Home");
        backButton.addActionListener(
            event -> cardLayout.show(pages, "HOME")
        );

        JLabel title = new JLabel(
            "Rooms",
            SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel controls = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        filterBox = new JComboBox<>(new String[] {
            "All rooms",
            "Available only",
            "Occupied only"
        });

        statsToggle = new JToggleButton("Show stats");

        filterBox.addActionListener(
            event -> refreshRoomGrid()
        );

        statsToggle.addActionListener(event -> {
            statsToggle.setText(
                statsToggle.isSelected()
                    ? "Show room details"
                    : "Show stats"
            );
            refreshRoomGrid();
        });

        controls.add(new JLabel("Filter:"));
        controls.add(filterBox);
        controls.add(statsToggle);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(backButton, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        top.add(controls, BorderLayout.EAST);

        roomGrid = new JPanel();
        roomGrid.setBackground(Color.WHITE);
        roomGrid.setBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        JScrollPane scrollPane = new JScrollPane(roomGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JLabel legend = new JLabel(
            "Green = available     Red = occupied     "
                + "Select a room for details",
            SwingConstants.CENTER
        );

        page.add(top, BorderLayout.NORTH);
        page.add(scrollPane, BorderLayout.CENTER);
        page.add(legend, BorderLayout.SOUTH);

        return page;
    }

    private void showAddRoomsDialog() {
        String input = JOptionPane.showInputDialog(
            this,
            "How many room pairs would you like to add?\n"
                + "Each pair contains one standard room "
                + "and one business room.",
            "Add rooms",
            JOptionPane.QUESTION_MESSAGE
        );

        if (input == null) {
            return; // User cancelled.
        }

        try {
            int numberOfPairs = Integer.parseInt(input.trim());

            if (numberOfPairs <= 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a positive integer.",
                    "Invalid number",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            actions.addRoomPairs(numberOfPairs);
            refreshRoomGrid();

            JOptionPane.showMessageDialog(
                this,
                numberOfPairs + " pairs added successfully ("
                    + (numberOfPairs * 2) + " rooms).",
                "Rooms added",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid whole number.",
                "Invalid input",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showChangeBasePriceDialog() {
        String input = JOptionPane.showInputDialog(
            this,
            "Enter the new standard room price (€):",
            hotel.getBasePrice()
        );

        if (input == null) {
            return;
        }

        try {
            float newPrice = Float.parseFloat(
                input.trim().replace(',', '.')
            );

            actions.changeBasePrice(newPrice);

            refreshRoomGrid();

            JOptionPane.showMessageDialog(
                this,
                "Base price updated successfully.",
                "Price updated",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid positive price.",
                "Invalid price",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void refreshRoomGrid() {
        roomGrid.removeAll();

        List<Room> visibleRooms = statsToggle.isSelected()
        ? hotel.getOccupancyReport()
        : hotel.getRooms();
        String filter = (String) filterBox.getSelectedItem();

        for (int i = visibleRooms.size() - 1; i >= 0; i--) {
            Room room = visibleRooms.get(i);

            if ("Available only".equals(filter)
                    && !room.isAvailable()) {
                visibleRooms.remove(i);
            } else if ("Occupied only".equals(filter)
                    && room.isAvailable()) {
                visibleRooms.remove(i);
            }
        }

        int columns = Math.max(
            1,
            (int) Math.ceil(
                Math.sqrt(Math.max(1, visibleRooms.size()))
            )
        );

        roomGrid.setLayout(
            new GridLayout(0, columns, 12, 12)
        );

        if (visibleRooms.isEmpty()) {
            roomGrid.add(
                new JLabel("No rooms match this filter.")
            );
        } else {
            for (Room room : visibleRooms) {
                roomGrid.add(createRoomButton(room));
            }
        }

        roomGrid.revalidate();
        roomGrid.repaint();
    }

    private JButton createRoomButton(Room room) {
        JButton button = new JButton();

        String roomType = room.getClass().getSimpleName()
            .equals("BusinessClassRoom")
                ? "Business"
                : "Standard";

        if (statsToggle.isSelected()) {
            button.setText(
                "<html><center>"
                    + "<b>Room " + room.getId() + "</b><br>"
                    + "Guests: " + room.getOccupancyCount() + "<br>"
                    + "Profit: " + currency.format(room.getProfit())
                    + "</center></html>"
            );
        } else {
            button.setText(
                "<html><center>"
                    + "<b>Room " + room.getId() + "</b><br>"
                    + roomType + "<br>"
                    + currency.format(room.getPrice())
                    + "</center></html>"
            );
        }

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(
            BorderFactory.createLineBorder(
                new Color(190, 190, 190),
                1
            )
        );
        button.setPreferredSize(new Dimension(170, 120));

        if (room.isAvailable()) {
            button.setBackground(new Color(218, 242, 221));
        } else {
            button.setBackground(new Color(250, 218, 218));

            Guest guest = room.getCurrentGuest();
            button.setToolTipText(
                "Occupied by: "
                    + guest.getName() + " " + guest.getSurname()
            );
        }

        if (room.isAvailable()) {
            button.setToolTipText("Room is available");
        }

        button.addActionListener(
            event -> showRoomDetails(room)
        );

        return button;
    }

    // --------------------------------------------------
    // Room details and guest history
    // --------------------------------------------------

    private void showRoomDetails(Room room) {
        String roomType = room.getClass().getSimpleName()
            .equals("BusinessClassRoom")
                ? "Business class"
                : "Standard";

        StringBuilder details = new StringBuilder();

        details.append("Room number: ")
            .append(room.getId()).append('\n');

        details.append("Room type: ")
            .append(roomType).append('\n');

        details.append("Price: ")
            .append(currency.format(room.getPrice())).append('\n');

        details.append("Profit: ")
            .append(currency.format(room.getProfit())).append('\n');

        details.append("Number of stays: ")
            .append(room.getOccupancyCount()).append('\n');

        details.append("Status: ")
            .append(room.isAvailable() ? "Free" : "Occupied")
            .append('\n');

        if (!room.isAvailable()) {
            Guest currentGuest = room.getCurrentGuest();

            details.append("Current guest: ")
                .append(currentGuest.getName()).append(' ')
                .append(currentGuest.getSurname()).append('\n');
        }

        details.append("\nGuest history (oldest first):\n");

        List<Guest> history = room.getGuestHistory();

        if (history.isEmpty()) {
            details.append("No guests have stayed in this room.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                Guest guest = history.get(i);

                details.append(i + 1).append(". ")
                    .append(guest.getName()).append(' ')
                    .append(guest.getSurname()).append('\n');
            }
        }

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setCaretPosition(0);
        textArea.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(430, 360));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Room " + room.getId(),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // --------------------------------------------------
    // Guest registration
    // --------------------------------------------------

    private void showRegisterDialog() {
        JTextField nameField = new JTextField(18);
        JTextField surnameField = new JTextField(18);

        JPanel form = new JPanel(
            new GridLayout(2, 2, 8, 8)
        );

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Surname:"));
        form.add(surnameField);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Register a guest",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Both name and surname are required.",
                "Invalid input",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Room room = actions.registerGuest(name, surname);

        if (room == null) {
            JOptionPane.showMessageDialog(
                this,
                "Registration failed: no rooms are available.",
                "Hotel full",
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                this,
                name + " " + surname
                    + " was registered in room " + room.getId() + ".",
                "Registration successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        refreshRoomGrid();
    }

    // --------------------------------------------------
    // Check-out
    // --------------------------------------------------

    private void showCheckoutDialog() {
        List<Room> occupiedRooms = hotel.getOccupiedRooms();

        if (occupiedRooms.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "There are no occupied rooms.",
                "Check-out",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JComboBox<Room> roomChooser = new JComboBox<>(
            occupiedRooms.toArray(new Room[0])
        );

        roomChooser.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                Component component =
                    super.getListCellRendererComponent(
                        list, value, index,
                        isSelected, cellHasFocus
                    );

                if (component instanceof JLabel
                        && value instanceof Room) {
                    JLabel label = (JLabel) component;
                    Room room = (Room) value;
                    Guest guest = room.getCurrentGuest();

                    label.setText(
                        "Room " + room.getId() + " — "
                            + guest.getName() + " "
                            + guest.getSurname()
                    );
                }

                return component;
            }
        });

        JPanel form = new JPanel(new BorderLayout(8, 8));
        form.add(
            new JLabel("Select the room to check out:"),
            BorderLayout.NORTH
        );
        form.add(roomChooser, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Check out a guest",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Room selectedRoom = (Room) roomChooser.getSelectedItem();

        if (selectedRoom == null) {
            return;
        }

        boolean success = actions.checkOut(selectedRoom.getId());

        JOptionPane.showMessageDialog(
            this,
            success
                ? "Check-out successful for room "
                    + selectedRoom.getId() + "."
                : "Check-out failed.",
            "Check-out",
            success
                ? JOptionPane.INFORMATION_MESSAGE
                : JOptionPane.WARNING_MESSAGE
        );

        refreshRoomGrid();
    }
}