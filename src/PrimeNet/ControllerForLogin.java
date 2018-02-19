package PrimeNet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;

public class ControllerForLogin {

    @FXML
    public Label LabelStatus;
    @FXML
    public PasswordField TextPassword;
    @FXML
    public TextField TextUserName;

    //login with username and password
    public void Login(javafx.event.ActionEvent event) throws Exception {

        if (TextUserName.getText().equals("") && TextPassword.getText().equals("")) {

            Parent root = FXMLLoader.load(getClass().getResource("FxmlFiles/MainWindow.fxml"));
            Stage PrimeNet = new Stage();
            PrimeNet.setTitle("PrimeNet");
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            PrimeNet.setResizable(false);
            //PrimeNet.setScene(new Scene(root, screen.width, screen.height));
            PrimeNet.setScene(new Scene(root, screen.width / 1.5, screen.height / 1.1 - 65));
            //PrimeNet.setScene(new Scene(root,800,600));
            PrimeNet.show();
            Main.Login.close();
        } else {
            LabelStatus.setText("! falsche Angaben !");
        }
    }

    //function of the reset button in login menu
    public void Reset(javafx.event.ActionEvent event) {
        TextUserName.setText("");
        TextPassword.setText("");
        LabelStatus.setText("Nochmal eingeben");
    }


}





