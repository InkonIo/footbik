package guis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        addMenuItem("Профиль", "/Users/inkonio/Desktop/Utilities/footbik/icons/user.png");
        addMenuItem("Новости футбола", "/Users/inkonio/Desktop/Utilities/footbik/icons/newspaper-open.png");
        addMenuItem("Мой клуб", "/Users/inkonio/Desktop/Utilities/footbik/icons/football-player.png");
        addMenuItem("Следим за игроками", "/Users/inkonio/Desktop/Utilities/footbik/icons/football.png");
        addMenuItem("Цены и трансферы", "/Users/inkonio/Desktop/Utilities/footbik/icons/usd-circle.png");
        addMenuItem("Календарь матчей", "/Users/inkonio/Desktop/Utilities/footbik/icons/calendar.png");

        add(sidePanel, BorderLayout.WEST);

        // Наведение на панель расширяет её
        sidePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                expandPanel();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                collapsePanel();
            }
        });

        setVisible(true);
    }

    private void addMenuItem(String title, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        label.setVisible(false); // Изначально скрываем

        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(60, 50));
        button.setBorderPainted(false);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setLayout(new BorderLayout());

        button.add(label, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                expandPanel();
                label.setForeground(Color.YELLOW); // Подсветка при наведении
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.WHITE); // Возвращаем стандартный цвет
            }
        });

        button.addActionListener(e -> showContent(title));
        sidePanel.add(button);
    }

    private void expandPanel() {
        if (!expanded) {
            sidePanel.setPreferredSize(new Dimension(200, getHeight()));

            // Показываем текст на всех кнопках
            for (Component comp : sidePanel.getComponents()) {
                if (comp instanceof JButton) {
                    for (Component child : ((JButton) comp).getComponents()) {
                        if (child instanceof JLabel) {
                            child.setVisible(true);
                        }
                    }
                }
            }

            sidePanel.revalidate();
            expanded = true;
        }
    }

    private void collapsePanel() {
        if (expanded) {
            sidePanel.setPreferredSize(new Dimension(60, getHeight()));

            // Скрываем текст на всех кнопках
            for (Component comp : sidePanel.getComponents()) {
                if (comp instanceof JButton) {
                    for (Component child : ((JButton) comp).getComponents()) {
                        if (child instanceof JLabel) {
                            child.setVisible(false);
                        }
                    }
                }
            }

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
