package PrimeNet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ControllerCreateUser {


    public Button CreateNewUser;
    public TextField CreateUserUsername;
    public PasswordField CreateUserPassword;

    PasswordAuthentication auth = new PasswordAuthentication(20);

    @FXML
    private void CreateNewUser(ActionEvent e) throws IOException {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get("passwords.txt"));
        } catch (IOException e1) { }

        boolean written = false;
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("passwords.txt"))) {
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length != 2) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }

                String username = parts[0];
                String password = parts[1];

                if (username.equalsIgnoreCase(CreateUserUsername.getText())) {
                    writer.write(username + ":" + auth.hash(CreateUserPassword.getText().toCharArray()));
                    writer.newLine();
                    written = true;
                    return;
                }
            }

            writer.write(CreateUserUsername.getText() + ":" + auth.hash(CreateUserPassword.getText().toCharArray()));
            writer.newLine();
        } catch (IOException ioException) { }
    }
}
