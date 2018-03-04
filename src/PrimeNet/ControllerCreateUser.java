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

    @FXML
    public Button CreateNewUser;

    @FXML
    public TextField CreateUserUsername;

    @FXML
    public TextField CreateUserAPIkey;

    @FXML
    public PasswordField CreateUserPassword;

    PasswordAuthentication auth = new PasswordAuthentication(18);

    //creating a new user with his own password
    @FXML
    private void CreateNewUser(ActionEvent e) throws IOException {
        List<String> lines = new ArrayList<>();
        List<String> oldusers = new ArrayList<>();
        try {
            oldusers = lines = Files.readAllLines(Paths.get("passwords.txt"));
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
                    for (String olduser : oldusers){
                        String string = String.valueOf(oldusers);
                        writer.write(string.replaceAll("\\[+\\]+", ""));
                        writer.newLine();
                    }
                    writer.write(username + ":"
                            + auth.hash(CreateUserPassword.getText().toCharArray()));
                    writer.newLine();
                    written = true;
                    break;
                }
            }

            for (String olduser : oldusers){
                String string = String.valueOf(oldusers);
                writer.write(string.replaceAll("\\[+\\]+", ""));
                writer.newLine();
            }
            writer.write(CreateUserUsername.getText() + ":"
                    + auth.hash(CreateUserPassword.getText().toCharArray()));
            writer.newLine();
        } catch (IOException ioException) { }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("config.properties"))) {
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length != 2) {
                    writer.write(line);
                    writer.newLine();
                    break;
                }

                String username = parts[0];
                String apikey = parts[1];

                if (username.equalsIgnoreCase(CreateUserUsername.getText())) {
                    writer.write("api.key = " + CreateUserAPIkey.getText());
                    writer.newLine();
                    written = true;
                    return;
                }
            }

            writer.write("api.key = " + CreateUserAPIkey.getText());
            writer.newLine();
        } catch (IOException ioException) { }

        CreateNewUser.getScene().getWindow().hide();
    }
}
