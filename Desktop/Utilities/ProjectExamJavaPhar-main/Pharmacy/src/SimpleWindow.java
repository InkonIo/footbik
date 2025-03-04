import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SimpleWindow extends JFrame {
    private String userEmail;
    private ArrayList<String> selectedMedicines;
    private JPanel medicinePanel;
    private JScrollPane medicineScrollPane;
    private JTextField searchField;
    private List<JPanel> medicineCards;

    public SimpleWindow(String userEmail, ArrayList<String> selectedMedicines) {
        this.userEmail = userEmail;
        this.selectedMedicines = selectedMedicines;
        this.medicineCards = new ArrayList<>();

        setTitle("Простуда");
        setBounds(100, 100, 650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(138, 209, 206));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField("Поиск...");
        searchField.setForeground(Color.GRAY);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterMedicines(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterMedicines(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterMedicines(); }
        });

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Поиск...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Поиск...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);

        medicinePanel = new JPanel(new GridLayout(0, 1, 10, 10));
        medicinePanel.setBackground(new Color(138, 209, 206));
        medicinePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addMedicineButton("Цитрамон", "Обезболивающее и жаропонижающее.", "/images/citramon.png", CitramonWindow.class);
        addMedicineButton("Парацетамол", "Снижает температуру и боль.", "/images/paracetomo.png", ParacetamolWindow.class);
        addMedicineButton("Ибупрофен", "Противовоспалительное средство.", "/images/ibuprofen.png", IbuprofenWindow.class);

        medicineScrollPane = new JScrollPane(medicinePanel);
        medicineScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        medicineScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(138, 209, 206));

        JButton backButton = createStyledButton("Назад", new Color(0, 123, 167), Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new MedicineSelectionWindow(userEmail, selectedMedicines);
        });

        JButton basketButton = createStyledButton("Корзина", new Color(0, 123, 167), Color.WHITE);
        basketButton.addActionListener(e -> {
            dispose();
            new Basket(userEmail, selectedMedicines);
        });

        buttonPanel.add(backButton);
        buttonPanel.add(basketButton);

        container.add(searchPanel, BorderLayout.NORTH);
        container.add(medicineScrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Запускаем таймер для обновления цен каждые 5 секунд
        Timer timer = new Timer(5000, e -> updatePrices());
        timer.start();
    }

    private void addMedicineButton(String medicineName, String description, String imagePath, Class<?> medicineWindowClass) {
        JPanel medicineCard = new JPanel(new BorderLayout());
        medicineCard.setBackground(new Color(200, 230, 229));
        medicineCard.setBorder(BorderFactory.createLineBorder(new Color(0, 90, 140), 2));
        medicineCard.setPreferredSize(new Dimension(300, 100));

        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel nameLabel = new JLabel(medicineName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        double price = MedicineDatabase.getPrice(medicineName);
        JLabel priceLabel = new JLabel("Цена: " + price + " KZT");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(new Color(200, 230, 229));
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(priceLabel, BorderLayout.SOUTH);

        JButton selectButton = createStyledButton("Выбрать", new Color(0, 123, 167), Color.WHITE);
        selectButton.addActionListener(e -> openMedicineWindow(medicineWindowClass));

        medicineCard.add(imageLabel, BorderLayout.WEST);
        medicineCard.add(textPanel, BorderLayout.CENTER);
        medicineCard.add(selectButton, BorderLayout.EAST);

        medicineCards.add(medicineCard);
        medicinePanel.add(medicineCard);
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    private void openMedicineWindow(Class<?> medicineWindowClass) {
        try {
            medicineWindowClass.getDeclaredConstructor(String.class, ArrayList.class).newInstance(userEmail, selectedMedicines);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void filterMedicines() {
        String query = searchField.getText().trim().toLowerCase();
        medicinePanel.removeAll();

        for (JPanel card : medicineCards) {
            JLabel nameLabel = (JLabel) ((JPanel) card.getComponent(1)).getComponent(0);
            if (nameLabel.getText().toLowerCase().contains(query)) {
                medicinePanel.add(card);
            }
        }
        medicinePanel.revalidate();
        medicinePanel.repaint();
    }

    public void updatePrices() {
        for (JPanel card : medicineCards) {
            JPanel textPanel = (JPanel) card.getComponent(1);
            JLabel nameLabel = (JLabel) textPanel.getComponent(0);
            JLabel priceLabel = (JLabel) textPanel.getComponent(1);

            String medicineName = nameLabel.getText();
            double newPrice = MedicineDatabase.getPrice(medicineName);
            priceLabel.setText("Цена: " + newPrice + " KZT");
        }
        medicinePanel.revalidate();
        medicinePanel.repaint();
    }
}
