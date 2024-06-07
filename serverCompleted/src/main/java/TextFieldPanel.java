import java.awt.*;

class TextFieldPanel extends Panel {
    private TextField idField;
    private TextField passwordField;

    public TextFieldPanel() {
        setLayout(new GridLayout(3, 2, 10, 10));
        add(new Label("ID:"));
        idField = new TextField();
        add(idField);

        add(new Label("Password:"));
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        add(passwordField);
    }

    public String getIdFieldText() {
        return idField.getText();
    }

    public String getPasswordFieldText() {
        return passwordField.getText();
    }

    public void clearFields() {
        idField.setText("");
        passwordField.setText("");
    }

    public TextField getIdField() {
        return idField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }
}
