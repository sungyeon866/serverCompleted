import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class StartingFrame extends Frame {
    public StartingFrame(Firestore firestore) {
        Frame frame = new Frame();
        frame.setSize(300, 200);
        frame.setTitle("Login");
        setLayout(new BorderLayout());

        TextFieldPanel textFieldPanel = new TextFieldPanel();
        frame.add(textFieldPanel, BorderLayout.NORTH);

        Login login = new Login(textFieldPanel, firestore);

        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        Button loginButton = new LoginButton(login, frame);
        buttonPanel.add(loginButton);

        Button signUpButton = new SignUpButton(frame, firestore);
        buttonPanel.add(signUpButton);

        frame.add(buttonPanel, BorderLayout.EAST);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.setVisible(true);
    }
}

class SignUpButton extends Button {
    public SignUpButton(Frame frame, Firestore firestore) {
        super("Sign up");
        setPreferredSize(new Dimension(100, 30));
        addActionListener(e -> {
            frame.dispose();
            new SignupAppAWT(firestore);
        });
    }
}
class LoginButton extends Button {
    public LoginButton(Login login, Frame frame) {
        super("Login");
        setPreferredSize(new Dimension(100, 30));
        addActionListener(e -> {
            boolean check = login.login();
            if(check){
                frame.dispose();
            }
            else{
                JOptionPane.showMessageDialog(null, "Login failed");
            }
        });
    }
}