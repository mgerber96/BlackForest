package PrimeNet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ControllerForLogin {

    @FXML
    public Label LabelStatus;
    @FXML
    public PasswordField TextPassword;
    @FXML
    public TextField TextUserName;
    @FXML
    public Button createUserButton;

    public boolean enter = false;

    PasswordAuthentication auth = new PasswordAuthentication(18);

    //read created Userdata + login with username and password
    public void Login(javafx.event.ActionEvent event) throws Exception {
           try {
               FileReader passwords = new FileReader("passwords.txt");
               BufferedReader PasswordReader = new BufferedReader(passwords);
               String line = null;
               while (true) {
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
                       generateUserProfile(username);
                       setMainStageAndCloseLoginWindow();
                       return;
                   }
               }
           } catch (IOException e) {
               LabelStatus.setText("! falsche Angaben !");
               e.printStackTrace();
           }
    }

    //if login is successful this program will only used txt.file in this format
    //"usernameFilename" all existing Filenames have got as prefix a username
    public void generateUserProfile(String username){
        //Create directory: Profile/Username/
        File file = new File("Profile/" + username + "/");
        if(!file.exists())
            file.mkdir();

        String userDirectiory = "Profile/" + username + "/";
        //add directory as a prefix for username so that the program knows in which directory .txt file lies
        username = userDirectiory + username;
        System.out.println(username);
        Controller.setUsername(username);
        HelperMethods.createAFile(username + "Favoriten.txt");
        HelperMethods.createAFile(username + "copyOfFavoriten.txt");
        HelperMethods.createAFile(username + "Bookmarks.txt");
        HelperMethods.createAFile(username + "copyOfBookmarks.txt");
        HelperMethods.createAFile(username + "SearchHistory.txt");
        HelperMethods.createAFile(username + "copyOfSearchHistory.txt");
    }

    public void setMainStageAndCloseLoginWindow(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainWindow.fxml"));
            Stage PrimeNet = new Stage();
            PrimeNet.setTitle("PrimeNet");
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            PrimeNet.setScene(new Scene(root, screen.width / 1.5, screen.height / 1.1 - 65));
            PrimeNet.show();
            Main.Login.close();
        } catch (IOException e){ e.printStackTrace(); }
    }

    //function of the reset button in login menu
    //current username and password field will be emptied
    public void Reset (javafx.event.ActionEvent event){
        TextUserName.setText("");
        TextPassword.setText("");
        LabelStatus.setText("Bitte versuchen Sie es nochmal");
    }

    public void openCreateUser (javafx.event.ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/CreateUserWindow.fxml"));
        Stage CreateUser = new Stage();
        CreateUser.setTitle("Create_User");
        CreateUser.setScene(new Scene(root, 300, 250));
        CreateUser.show();
    }
}