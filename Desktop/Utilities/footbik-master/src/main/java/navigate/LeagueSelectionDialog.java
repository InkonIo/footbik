package navigate;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        leagueDropdown.addActionListener(e -> updateLeagueLogo());

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(leagueLogoLabel, gbc);

        gbc.gridx = 1;
        topPanel.add(leagueDropdown, gbc);

        clubLogoLabel = new JLabel();
        clubLogoLabel.setPreferredSize(new Dimension(100, 100));

        clubDropdown = new JComboBox<>(new String[]{"Выберите лигу"});
        styleDropdown(clubDropdown);
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

            if (selectedClub != null && !selectedClub.equals("Выберите лигу")) {
                dispose();
                new profileGUI(selectedLeague, selectedClub);
            } else {
                JOptionPane.showMessageDialog(this, "Выберите клуб!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(confirmButton);

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateLeagueLogo();
        setVisible(true);
    }

    private void updateLeagueLogo() {
        String selectedLeague = (String) leagueDropdown.getSelectedItem();
        clubDropdown.setModel(new DefaultComboBoxModel<>(leagues.get(selectedLeague)));

        String logoURL = leagueLogos.get(selectedLeague);
        if (logoURL != null) {
            leagueLogoLabel.setIcon(loadImageFromURL(logoURL, 100, 100));
        }
    }

    private void updateClubLogo() {
        String selectedClub = (String) clubDropdown.getSelectedItem();
        if (selectedClub != null && !selectedClub.equals("Выберите лигу")) {
            String logoURL = fetchClubLogoURL(selectedClub);
            clubLogoLabel.setIcon(logoURL != null ? loadImageFromURL(logoURL, 100, 100) : null);
        }
    }

    private String fetchClubLogoURL(String clubName) {
        String apiUrl = "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=" + clubName.replace(" ", "%20");
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(jsonResponse.toString());
            JSONArray teams = jsonObject.optJSONArray("teams");

            return (teams != null && teams.length() > 0) ? teams.getJSONObject(0).optString("strBadge", null) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ImageIcon loadImageFromURL(String url, int width, int height) {
        try {
            Image image = ImageIO.read(new URL(url));
            return image != null ? new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)) : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

        leagueLogos.put("Premier League (Англия)", "https://upload.wikimedia.org/wikipedia/en/f/f2/Premier_League_Logo.svg");
        leagueLogos.put("La Liga (Испания)", "https://upload.wikimedia.org/wikipedia/en/2/2e/LaLiga_logo_%282023%29.svg");
        leagueLogos.put("Bundesliga (Германия)", "https://upload.wikimedia.org/wikipedia/en/d/df/Bundesliga_logo_%282017%29.svg");
        leagueLogos.put("Serie A (Италия)", "https://upload.wikimedia.org/wikipedia/en/e/eb/Serie_A_logo_%282019%29.svg");
        leagueLogos.put("Ligue 1 (Франция)", "https://upload.wikimedia.org/wikipedia/en/b/ba/Ligue1_UberEats_logo.svg");
    }
}

// добавить лигу картинку