package PrimeNet;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage Login = new Stage();

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Login = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        Login.setTitle("Login for PrimeNet");
        Login.setScene(new Scene(root, 300  , 300));
        Login.show();
    }
}
