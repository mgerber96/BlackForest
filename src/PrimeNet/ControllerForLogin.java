package PrimeNet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerForLogin implements Initializable {

    @FXML
    public Label LabelStatus;
    @FXML
    public PasswordField TextPassword;
    @FXML
    public TextField TextUserName;
    @FXML
    public Button createUserButton;

    PasswordAuthentication auth = new PasswordAuthentication(20);

    //read created Userdata +login with username and password
    public void Login(javafx.event.ActionEvent event) throws Exception {
        try {
            FileReader passwords = new FileReader("passwords.txt");
            BufferedReader PasswordReader = new BufferedReader(passwords);
            String line = null;
            while(true) {
                line = PasswordReader.readLine();
                if (line == null) {
                    LabelStatus.setText("! falsche Angaben !");
                    return;
                }

                String[] parts = line.split(":");
                if (parts.length != 2) {
                    continue;
                }

                String username = parts[0];
                String password = parts[1];

                if (!username.equalsIgnoreCase(TextUserName.getText())) {
                    continue;
                }

                if (auth.authenticate(TextPassword.getText().toCharArray(), password)) {
                    Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainWindow.fxml"));
                    Stage PrimeNet = new Stage();
                    PrimeNet.setTitle("PrimeNet");
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    //PrimeNet.setResizable(false);
                    //PrimeNet.setScene(new Scene(root, screen.width, screen.height));
                    PrimeNet.setScene(new Scene(root, screen.width / 1.5, screen.height / 1.1 - 65));
                    //PrimeNet.setScene(new Scene(root,800,600));
                    PrimeNet.show();
                    Main.Login.close();
                    return;
                }
            }
        } catch (IOException e) {
            LabelStatus.setText("! falsche Angaben !");
            e.printStackTrace();
        }
    }



        //function of the reset button in login menu
        public void Reset (javafx.event.ActionEvent event){
            TextUserName.setText("");
            TextPassword.setText("");
            LabelStatus.setText("Nochmal eingeben");
        }


        public void openCreateUser (javafx.event.ActionEvent event) throws Exception {

            Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/CreateUserWindow.fxml"));
            Stage CreateUser = new Stage();
            CreateUser.setTitle("Create_User");
            CreateUser.setScene(new Scene(root, 250, 150));
            CreateUser.show();

        }


        @Override
        public void initialize (URL location, ResourceBundle resources){


        }
    }






