package PrimeNet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCreateUser {


    public Button CreateNewUser;
    public TextField CreateUserUsername;
    public PasswordField CreateUserPassword;

    @FXML
    private void CreateNewUser(ActionEvent e) throws IOException {

        StringBuilder sbU = new StringBuilder();
        StringBuilder sbP = new StringBuilder();

        sbU.append(CreateUserUsername.getText().toString());
        sbP.append(CreateUserPassword.getText().toString());
        File Username = new File("Username.txt");
        File Password = new File("Password.txt");
        FileWriter U = new FileWriter(Username);
        FileWriter P = new FileWriter(Password);
        U.write(sbU.toString());
        P.write(sbP.toString());
        P.close();
        U.close();
    }
}
