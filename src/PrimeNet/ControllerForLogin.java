package PrimeNet;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    //read created Userdata +login with username and password
    public void Login(javafx.event.ActionEvent event) throws Exception {
        try {
            FileReader UsernameFile = new FileReader("UserName.txt");
            FileReader PasswordFile = new FileReader("Password.txt");
            BufferedReader UsernameReader = new BufferedReader(UsernameFile);
            BufferedReader PasswordReader = new BufferedReader(PasswordFile);
            String Usernameline = UsernameReader.readLine();
            String Passwordline = PasswordReader.readLine();
            String text1 = "";
            String text2 = "";

            while (Usernameline != null) {

                text1 += Usernameline;
                Usernameline = UsernameReader.readLine();
            }
            while (Passwordline !=null){

                text2 += Passwordline;
                Passwordline = PasswordReader.readLine();

            }


            if (TextUserName.getText().equals(text1) && TextPassword.getText().equals(text2)) {

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
            } else {
                LabelStatus.setText("! falsche Angaben !");
            }
        } catch (IOException e) {
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






