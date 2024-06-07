import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TimerApp extends Frame {
    private Label timeLabel;
    private TextField inputField;
    private Button addButton;
    private Timer timer;
    private int remainingTime;
    private String userId;
    private Firestore firestore;

    public TimerApp(String userId, int remainingTime, Firestore firestore) {
        this.userId = userId;
        this.remainingTime = remainingTime;
        this.firestore = firestore;

        setTitle("Timer");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        timeLabel = new Label("00:00:00", Label.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        add(timeLabel);

        Label inputLabel = new Label("Add Time (sec):");
        add(inputLabel);

        inputField = new TextField();
        add(inputField);

        addButton = new Button("Add Time");
        addButton.addActionListener(this::addTime);
        add(addButton);

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

    private void addTime(ActionEvent e) {
        try {
            int timeToAdd = Integer.parseInt(inputField.getText());
            remainingTime += timeToAdd;
            updateTimeLabel();
            inputField.setText("");

            if (remainingTime > 0 && (timer == null || timerHasStopped())) {
                startTimer();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
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
}
