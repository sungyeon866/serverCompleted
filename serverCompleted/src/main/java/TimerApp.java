import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TimerApp extends Frame {
    private Label timeLabel;
    private Timer timer;
    private int remainingTime;
    private String userId;
    private Firestore firestore;

    public TimerApp(String userId, int remainingTime, Firestore firestore) {
        this.userId = userId;
        this.remainingTime = remainingTime;
        this.firestore = firestore;

        setTitle("Timer");
        setSize(400, 600);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        timeLabel = new Label("00:00:00", Label.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        add(timeLabel, gbc);

        gbc.gridwidth = 1; // Reset gridwidth
        gbc.gridy = 1; // Move to the next row
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(createButtonPanel(), gbc);

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveRemainingTime));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                saveRemainingTime();
                if (timer != null) {
                    timer.cancel();
                }
                dispose();
            }
        });

        if (remainingTime > 0) {
            startTimer();
        }
        updateTimeLabel();

        setVisible(true);
    }

    private boolean timerHasStopped() {
        return remainingTime > 0 && timer == null;
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    updateTimeLabel();
                } else {
                    timer.cancel();
                    timer = null;
                    JOptionPane.showMessageDialog(null, "Time's up!");
                    Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            saveRemainingTime();
                            dispose();
                            shutDown();
                        }
                    }, 1000);
                }
            }
        }, 0, 1000);
    }

    public void shutDown() {
        try {
            Runtime.getRuntime().exec("shutdown -s -t 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTimeLabel() {
        int hours = remainingTime / 3600;
        int minutes = (remainingTime % 3600) / 60;
        int seconds = remainingTime % 60;
        timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void saveRemainingTime() {
        try {
            DocumentReference docRef = firestore.collection("users").document(userId);
            docRef.update("remainingTime", remainingTime).get();
            System.out.println("Remaining time updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to update remaining time.");
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        addButtonToPanel(panel, "35 minutes 1000 won", 0, 0, gbc);
        addButtonToPanel(panel, "1 hour 20 minutes 2000 won", 1, 0, gbc);
        addButtonToPanel(panel, "2 hours 3000 won", 0, 1, gbc);
        addButtonToPanel(panel, "2 hours 40 minutes 4000 won", 1, 1, gbc);
        addButtonToPanel(panel, "3 hours 30 minutes 5000 won", 0, 2, gbc);

        return panel;
    }

    private void addButtonToPanel(JPanel panel, String text, int x, int y, GridBagConstraints gbc) {
        JButton button = new JButton("<html>" + text.replace(" ", "<br>") + "</html>");
        button.addActionListener(e -> addPredefinedTime(text));
        button.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(button, gbc);
    }

    private void addPredefinedTime(String item) {
        try {
            int timeToAdd = 0;
            int cost = 0;
            switch (item) {
                case "35 minutes 1000 won":
                    timeToAdd = 35 * 60;
                    cost = 1000;
                    break;
                case "1 hour 20 minutes 2000 won":
                    timeToAdd = 80 * 60;
                    cost = 2000;
                    break;
                case "2 hours 3000 won":
                    timeToAdd = 120 * 60;
                    cost = 3000;
                    break;
                case "2 hours 40 minutes 4000 won":
                    timeToAdd = 160 * 60;
                    cost = 4000;
                    break;
                case "3 hours 30 minutes 5000 won":
                    timeToAdd = 210 * 60;
                    cost = 5000;
                    break;
            }

            DocumentReference docRef = firestore.collection("users").document(userId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            User user = document.toObject(User.class);

            if (user != null) {
                int currentMoney = user.getMoney();
                if (currentMoney < cost) {
                    JOptionPane.showMessageDialog(this, "Not enough money to add time.");
                    return;
                }
                docRef.update("money", currentMoney - cost).get();
                remainingTime += timeToAdd;
                updateTimeLabel();
                if (remainingTime > 0 && (timer == null || timerHasStopped())) {
                    startTimer();
                }
                JOptionPane.showMessageDialog(this, "Added " + (timeToAdd / 60) + " minutes for " + cost + " won.");
            } else {
                JOptionPane.showMessageDialog(this, "User data not found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user data");
        }
    }
}
