package navigate;

import guis.PersonalAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class profileGUI extends JFrame {
    private JLabel profilePicLabel;
    private ImageIcon defaultProfileIcon;
    private JLabel favoriteClubLabel;
    private String favoriteLeague;
    private String favoriteClub;

    public profileGUI(String league, String club) {
        this.favoriteLeague = league;
        this.favoriteClub = club;

        setTitle("Профиль");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // **Верхняя шапка**
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(30, 30, 30));
        headerPanel.setPreferredSize(new Dimension(600, 120));

        // Фото профиля
        defaultProfileIcon = new ImageIcon("D:\\JustFol\\footbik\\icons\\profile.png");
        profilePicLabel = new JLabel(scaleImage(defaultProfileIcon, 80, 80));

        // Кнопка для смены фото
        JButton changePhotoButton = new JButton("Выбрать фото");
        changePhotoButton.addActionListener(this::chooseProfilePicture);

        // Поле для ввода никнейма
        JTextField nicknameField = new JTextField("Ваш никнейм", 15);
        nicknameField.setFont(new Font("Arial", Font.BOLD, 14));

        headerPanel.add(profilePicLabel);
        headerPanel.add(nicknameField);
        headerPanel.add(changePhotoButton);
        add(headerPanel, BorderLayout.NORTH);

        // **Центральный контент**
        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Добро пожаловать в профиль!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        favoriteClubLabel = new JLabel("Любимый клуб: " + favoriteClub + " (" + favoriteLeague + ")", SwingConstants.CENTER);
        favoriteClubLabel.setFont(new Font("Arial", Font.BOLD, 16));

        centerPanel.add(welcomeLabel, BorderLayout.NORTH);
        centerPanel.add(favoriteClubLabel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // **Кнопка "Назад"**
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            dispose();
            new PersonalAccount();
        });

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // **Метод для выбора фото**
    private void chooseProfilePicture(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon newProfileIcon = new ImageIcon(selectedFile.getAbsolutePath());
            profilePicLabel.setIcon(scaleImage(newProfileIcon, 80, 80));
        }
    }

    // **Масштабирование изображения**
    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}
