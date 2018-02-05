package PrimeNet;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;

public class ControllerForFavourite {
    @FXML
    TableView <Film> favouriteTable = new TableView();
    @FXML
    TableColumn <Film, String> favouriteTitleColumn = new TableColumn<>();
    @FXML
    TableColumn <Film, Integer> favouriteYearColumn = new TableColumn<>();

    ObservableList<Film> allFilms = FXCollections.observableArrayList();

    File file;
    FileWriter writer;

    @FXML
    private void initialize(){
        setUpTable();
        readLinesFromFile("Favoriten.txt");
        closingWindowAction(Controller.favouriteWindow);
    }

    //Before finally closing the window
    //Favourite.txt will be changed according to latest ObservableList allFilms
    private void closingWindowAction(Stage stage){
        stage.setOnHiding(event -> writeInFavourite(allFilms));
    }

    public void writeInFavourite(ObservableList<Film> allFilms){
        file = new File ("Favoriten.txt");
        try{
            writer = new FileWriter(file);
            for(Film f : allFilms){
                writer.write(f.getTitle());
                writer.write("\t");
                writer.write(String.valueOf(f.getYear()));
                writer.write(System.getProperty("line.separator"));
            }
            writer.flush();
        }catch (IOException e) { e.printStackTrace(); }
    }

    //Action by pressing the delete button
    public void deleteFilm(){
        ObservableList<Film> productSelected;
        productSelected = favouriteTable.getSelectionModel().getSelectedItems();
        productSelected.forEach(allFilms::remove);
    }

    private void readLinesFromFile(String datName) {
        File file = new File(datName);

        if (!file.canRead() || !file.isFile())
            System.exit(0);

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(datName));
            String line = null;
            String[] word;

            //read lines by lines and add the films to favouriteTableView
            while ((line = in.readLine()) != null) {

                //Strings in these lines are separated by a tab, we will get each of them and create a instance of film
                //then we will add it to a new list which we will later use to generate our list in favouriteTableView
                word = line.split("\t");
                allFilms.add(getFavourite(word[0], Integer.parseInt(word[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) { }
            }
        }
    }

    private void setUpTable (){
        //favouriteTitleColumn
        favouriteTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //favouriteYearColumn
        favouriteYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        favouriteTable.setItems(allFilms);

        //if you do a double click the selected film will be shown in the main window
        favouriteTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("Double click");
                    }
                }
            }
        });
    }


    public Film getFavourite(String title, int year){
        Film film = new Film(title, year);
        return film;
    }



}

