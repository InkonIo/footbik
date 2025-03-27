package guis;

import navigate.profileGUI;
import guis.LoginFormGUI;


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
        addMenuItem("Профиль", "D:\\JustFol\\footbik\\icons\\user.png");
        addMenuItem("Новости футбола", "D:\\JustFol\\footbik\\icons\\newspaper-open.png");
        addMenuItem("Мой клуб", "D:\\JustFol\\footbik\\icons\\football.png");
        addMenuItem("Следим за игроками", "D:\\JustFol\\footbik\\icons\\football-player.png");
        addMenuItem("Цены и трансферы", "D:\\JustFol\\footbik\\icons\\usd-circle.png");
        addMenuItem("Календарь матчей", "D:\\JustFol\\footbik\\icons\\calendar.png");


        JButton logoutButton = new JButton("Выйти");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.RED);
        logoutButton.setFocusPainted(false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(200, 40));

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Закрываем личный кабинет
                new LoginFormGUI().setVisible(true); // Открываем форму логина
            }
        });

        sidePanel.add(Box.createVerticalGlue()); // Добавляем отступ перед кнопкой
        sidePanel.add(logoutButton);
        add(sidePanel, BorderLayout.WEST);

        setupPanel(); // Загружаем свернутую панель

        sidePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                expandPanel();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (expanded && e.getX() > 200) {
                    collapsePanel();
                }
            }
        });

        setVisible(true);
    }

    private void addMenuItem(String title, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        JLabel iconLabel = new JLabel(icon);
        JLabel textLabel = new JLabel(title);
        textLabel.setForeground(Color.WHITE);
        textLabel.setVisible(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setPreferredSize(new Dimension(200, 50));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(iconLabel);
        buttonPanel.add(textLabel);

        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                expandPanel();
                textLabel.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                textLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showContent(title);
            }
        });

        sidePanel.add(buttonPanel);
    }

    private void setupPanel() {
        sidePanel.setPreferredSize(new Dimension(60, getHeight()));

        for (Component comp : sidePanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setLayout(null);

                if (panel.getComponentCount() > 1) {
                    JLabel iconLabel = (JLabel) panel.getComponent(0);
                    JLabel textLabel = (JLabel) panel.getComponent(1);

                    iconLabel.setBounds(10, 10, 40, 40);
                    textLabel.setBounds(60, 10, 120, 40);
                    textLabel.setVisible(false);
                }
            }
        }

        sidePanel.revalidate();
        sidePanel.repaint();
        expanded = false;
    }

    private void expandPanel() {
        if (!expanded) {
            sidePanel.setPreferredSize(new Dimension(200, getHeight()));

            for (Component comp : sidePanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    panel.setLayout(null);

                    if (panel.getComponentCount() > 1) {
                        JLabel iconLabel = (JLabel) panel.getComponent(0);
                        JLabel textLabel = (JLabel) panel.getComponent(1);

                        iconLabel.setBounds(10, 10, 40, 40);
                        textLabel.setBounds(60, 10, 120, 40);
                        textLabel.setVisible(true);
                    }
                }
            }

            sidePanel.revalidate();
            sidePanel.repaint();
            expanded = true;
        }
    }

    private void collapsePanel() {
        if (expanded) {
            sidePanel.setPreferredSize(new Dimension(60, getHeight()));

            for (Component comp : sidePanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    panel.setLayout(null);

                    if (panel.getComponentCount() > 1) {
                        JLabel iconLabel = (JLabel) panel.getComponent(0);
                        JLabel textLabel = (JLabel) panel.getComponent(1);

                        iconLabel.setBounds(10, 10, 40, 40);
                        textLabel.setVisible(false);
                    }
                }
            }

            sidePanel.revalidate();
            sidePanel.repaint();
            expanded = false;
        }
    }

    private void showContent(String section) {
        if (section.equals("Профиль")) {
            dispose(); // Закрываем текущее окно
            new profileGUI(); // Открываем профиль
        }
    }

    public static void main(String[] args) {
        new PersonalAccount();
    }
}
