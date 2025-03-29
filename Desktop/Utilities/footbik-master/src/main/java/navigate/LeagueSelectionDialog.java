package navigate;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class LeagueSelectionDialog extends JDialog {
    private JComboBox<String> leagueDropdown;
    private JComboBox<String> clubDropdown;
    private HashMap<String, String[]> leagues;
    private HashMap<String, String> leagueLogos;
    private JLabel leagueLogoLabel;
    private JLabel clubLogoLabel;
    private HashMap<String, String> clubLogos;

    public LeagueSelectionDialog(JFrame parent) {
        super(parent, "Выберите лигу и клуб", true);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(230, 230, 250));

        leagues = new HashMap<>();
        leagueLogos = new HashMap<>();
        initializeLeagues();

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        leagueLogoLabel = new JLabel();
        leagueLogoLabel.setPreferredSize(new Dimension(100, 100));

        leagueDropdown = new JComboBox<>(leagues.keySet().toArray(new String[0]));
        styleDropdown(leagueDropdown);
        leagueDropdown.addActionListener(e -> updateLeagueAndClub());

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(leagueLogoLabel, gbc);

        gbc.gridx = 1;
        topPanel.add(leagueDropdown, gbc);

        clubLogoLabel = new JLabel();
        clubLogoLabel.setPreferredSize(new Dimension(100, 100));

        clubDropdown = new JComboBox<>();
        styleDropdown(clubDropdown);
        updateClubDropdown("Выберите лигу"); // Устанавливаем placeholder
        clubDropdown.addActionListener(e -> updateClubLogo());

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(clubLogoLabel, gbc);

        gbc.gridx = 1;
        topPanel.add(clubDropdown, gbc);

        JButton confirmButton = new JButton("Подтвердить");
        styleButton(confirmButton);
        confirmButton.addActionListener(e -> {
            String selectedLeague = (String) leagueDropdown.getSelectedItem();
            String selectedClub = (String) clubDropdown.getSelectedItem();

            if (selectedClub != null && !selectedClub.equals("Выберите клуб")) {
                dispose();
                new profileGUI(selectedLeague, selectedClub);
            } else {
                JOptionPane.showMessageDialog(this, "Выберите клуб!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.GRAY);
        bottomPanel.add(confirmButton);

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateLeagueAndClub();
        setVisible(true);
    }

    private void updateLeagueAndClub() {
        String selectedLeague = (String) leagueDropdown.getSelectedItem();
        updateClubDropdown(selectedLeague);

        String logoURL = leagueLogos.get(selectedLeague);
        if (logoURL != null) {
            leagueLogoLabel.setIcon(loadImageFromURL(logoURL));
        }

        // Выбираем первый клуб по умолчанию
        if (clubDropdown.getItemCount() > 1) {
            clubDropdown.setSelectedIndex(1);
            updateClubLogo();
        }
    }

    private void updateClubDropdown(String league) {
        clubDropdown.removeAllItems();
        clubDropdown.addItem("Выберите клуб");

        if (leagues.containsKey(league)) {
            for (String club : leagues.get(league)) {
                clubDropdown.addItem(club);
            }
        }
    }

    private void updateClubLogo() {
        String selectedClub = (String) clubDropdown.getSelectedItem();
        if (selectedClub != null && !selectedClub.equals("Выберите клуб")) {
            // Проверяем, есть ли локальный логотип
            if (clubLogos.containsKey(selectedClub)) {
                clubLogoLabel.setIcon(loadImageFromURL(clubLogos.get(selectedClub)));
            } else {
                String logoURL = fetchClubLogoURL(selectedClub);
                if (logoURL != null && !logoURL.isEmpty()) {
                    clubLogoLabel.setIcon(loadImageFromURL(logoURL));
                } else {
                    System.err.println("Ошибка: логотип не найден для клуба " + selectedClub);
                    clubLogoLabel.setIcon(null);
                }
            }
        } else {
            clubLogoLabel.setIcon(null);
        }
    }

    private String fetchClubLogoURL(String clubName) {
        if (clubName == null || clubName.trim().isEmpty()) {
            System.err.println("Ошибка: название клуба не задано.");
            return null;
        }

        String apiUrl = "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=" + clubName.replace(" ", "%20");
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Ошибка: API вернуло код " + responseCode);
                return null;
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(jsonResponse.toString());
            JSONArray teams = jsonObject.optJSONArray("teams");

            if (teams == null || teams.length() == 0) {
                System.err.println("Ошибка: команда '" + clubName + "' не найдена в API.");
                return null;
            }

            String logoURL = teams.getJSONObject(0).optString("strBadge", null);
            if (logoURL == null || logoURL.isEmpty()) {
                System.err.println("Ошибка: логотип для команды '" + clubName + "' не найден.");
            }

            return logoURL;
        } catch (Exception e) {
            System.err.println("Ошибка при запросе к API: " + e.getMessage());
            return null;
        }
    }


    private ImageIcon loadImageFromURL(String path) {
        if (path == null || path.isEmpty()) {
            System.err.println("Ошибка: path равно null или пусто.");
            return new ImageIcon(); // Возвращаем пустую иконку
        }

        try {
            if (path.startsWith("http")) {
                URL url = new URL(path);
                Image img = ImageIO.read(url);
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            } else {
                File file = new File(path);
                if (file.exists()) {
                    Image img = ImageIO.read(file);
                    Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImg);
                } else {
                    System.err.println("Файл не найден: " + path);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
        }
        return new ImageIcon();
    }

    private void styleDropdown(JComboBox<String> dropdown) {
        dropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        dropdown.setBackground(Color.WHITE);
        dropdown.setForeground(Color.BLACK);
        dropdown.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(60, 120, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 100, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 120, 200));
            }
        });
    }

    private void initializeLeagues() {
        leagues.put("Premier League (Англия)", new String[]{"Arsenal", "Manchester City", "Liverpool", "Chelsea", "Manchester United"});
        leagues.put("La Liga (Испания)", new String[]{"Real Madrid", "Barcelona", "Atletico Madrid"});
        leagues.put("Bundesliga (Германия)", new String[]{"Bayern Munich", "Borussia Dortmund", "RB Leipzig"});
        leagues.put("Serie A (Италия)", new String[]{"Juventus", "Inter Milan", "AC Milan"});
        leagues.put("Ligue 1 (Франция)", new String[]{"PSG", "Marseille", "Lyon"});

        leagueLogos.put("Premier League (Англия)", "/Users/inkonio/Desktop/Utilities/footbik-master/ImagesAll/League/Epl.png");
        leagueLogos.put("La Liga (Испания)", "/Users/inkonio/Desktop/Utilities/footbik-master/ImagesAll/League/laliga.png");
        leagueLogos.put("Bundesliga (Германия)", "/Users/inkonio/Desktop/Utilities/footbik-master/ImagesAll/League/bundesliga.png");
        leagueLogos.put("Serie A (Италия)", "/Users/inkonio/Desktop/Utilities/footbik-master/ImagesAll/League/serieA.png");
        leagueLogos.put("Ligue 1 (Франция)", "/Users/inkonio/Desktop/Utilities/footbik-master/ImagesAll/League/ligueOne.png");

        // Добавляем логотип только для PSG
        clubLogos = new HashMap<>();
        clubLogos.put("PSG", "/Users/inkonio/Desktop/Utilities/footbik-master/ImagesAll/clubs/psg.png");
    }
}
