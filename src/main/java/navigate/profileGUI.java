package navigate;

import guis.PersonalAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class profileGUI extends JFrame {
    public profileGUI() {
        setTitle("Профиль");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Добро пожаловать в профиль!");
        add(label);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Закрываем текущее окно
                new PersonalAccount(); // Открываем личный кабинет
            }
        });

        add(backButton);
        setVisible(true);
    }
}
