package guis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PersonalAccount extends JFrame {
    private JPanel sidePanel;
    private boolean expanded = false;

    public PersonalAccount() {
        setTitle("Личный кабинет");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Боковая панель
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(60, getHeight()));
        sidePanel.setBackground(Color.DARK_GRAY);

        // Добавляем кнопки
        addMenuItem("Профиль", "user_icon.png");
        addMenuItem("Новости футбола", "news_icon.png");
        addMenuItem("Мой клуб", "club_icon.png");
        addMenuItem("Следим за игроками", "players_icon.png");
        addMenuItem("Цены и трансферы", "transfers_icon.png");
        addMenuItem("Календарь матчей", "calendar_icon.png");

        add(sidePanel, BorderLayout.WEST);
        setVisible(true);
    }

    private void addMenuItem(String title, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(title, icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setBorderPainted(false);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                expandPanel();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                collapsePanel();
            }
        });

        button.addActionListener(e -> showContent(title));
        sidePanel.add(button);
    }

    private void expandPanel() {
        if (!expanded) {
            sidePanel.setPreferredSize(new Dimension(200, getHeight()));
            sidePanel.revalidate();
            expanded = true;
        }
    }

    private void collapsePanel() {
        if (expanded) {
            sidePanel.setPreferredSize(new Dimension(60, getHeight()));
            sidePanel.revalidate();
            expanded = false;
        }
    }

    private void showContent(String section) {

    }

    public static void main(String[] args) {
        new PersonalAccount();
    }
}
