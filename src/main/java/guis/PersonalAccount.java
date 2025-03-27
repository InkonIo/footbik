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
        addMenuItem("Профиль", "D:\\JustFol\\footbik\\icons\\user.png");
        addMenuItem("Новости футбола", "D:\\JustFol\\footbik\\icons\\newspaper-open.png");
        addMenuItem("Мой клуб", "D:\\JustFol\\footbik\\icons\\football.png");
        addMenuItem("Следим за игроками", "D:\\JustFol\\footbik\\icons\\football-player.png");
        addMenuItem("Цены и трансферы", "D:\\JustFol\\footbik\\icons\\usd-circle.png");
        addMenuItem("Календарь матчей", "D:\\JustFol\\footbik\\icons\\calendar.png");

        add(sidePanel, BorderLayout.WEST);

        // Наведение на боковую панель
        sidePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                expandPanel();
            }
        });

        // Глобальный обработчик движения мыши
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (expanded && e.getX() > 200) { // Если панель раскрыта и курсор ушел вправо от нее
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

    private void expandPanel() {
        if (!expanded) {
            sidePanel.setPreferredSize(new Dimension(200, getHeight()));

            for (Component comp : sidePanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    for (Component child : panel.getComponents()) {
                        if (child instanceof JLabel textLabel && !((JLabel) panel.getComponent(0)).getIcon().equals(textLabel.getIcon())) {
                            textLabel.setVisible(true);
                        }
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
                    for (Component child : panel.getComponents()) {
                        if (child instanceof JLabel textLabel && !((JLabel) panel.getComponent(0)).getIcon().equals(textLabel.getIcon())) {
                            textLabel.setVisible(false);
                        }
                    }
                }
            }

            sidePanel.revalidate();
            sidePanel.repaint();
            expanded = false;
        }
    }

    private void showContent(String section) {
        // Здесь будет логика отображения контента
    }

    public static void main(String[] args) {
        new PersonalAccount();
    }
}
