package PrimeNet;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class ControllerForFavourite {
    @FXML
    TableView<Film> favouriteTable = new TableView();
    @FXML
    TableColumn<Film, String> favouriteTitleColumn = new TableColumn<>();
    @FXML
    TableColumn<Film, Integer> favouriteYearColumn = new TableColumn<>();
    @FXML
    TableView<Film> bookmarksTable = new TableView();
    @FXML
    TableColumn<Film, String> bookmarksTitleColumn = new TableColumn<>();
    @FXML
    TableColumn<Film, Integer> bookmarksYearColumn = new TableColumn<>();
    @FXML
    TableColumn<Film, Boolean> bookmarksFavouriteColumn = new TableColumn<>();

    ObservableList<Film> allFilmsInFavourite = FXCollections.observableArrayList();

    ObservableList<Film> allFilmsInBookmarks = FXCollections.observableArrayList();

    File file;
    FileWriter writer;

    @FXML
    private void initialize() {
        setUpTableForFavourite();
        readLinesFromFavourite("Favoriten.txt");
        closingFavouriteWindowAction(Controller.getFavouriteWindow());

        setUpTableForBookmarks();
        readLinesFromBookmarks("Bookmarks.txt");
        setUpFavouriteColumnInBookmarks();
        closingBookmarksWindowAction(Controller.getBookmarksWindow());
    }

    public void setUpFavouriteColumnInBookmarks(){
        for (Film filmInBookmarks : allFilmsInBookmarks) {
             filmInBookmarks.favouriteProperty().addListener((observableValue, oldValue, newValue) -> {
                 if (newValue && !oldValue) {
                     writeInFavouriteFile(filmInBookmarks.getTitle(), String.valueOf(filmInBookmarks.getYear()));
                     try{
                         List<Film> productSelected = new ArrayList<>();
                         productSelected.add(filmInBookmarks);
                         productSelected.forEach(allFilmsInBookmarks::remove);
                     } catch (NoSuchElementException e){Controller.getBookmarksWindow().close();}
                 }
             });
        }
    }

    public void writeInFavouriteFile(String filmTitle, String filmYear){
        writeInFile("Favoriten.txt", filmTitle, filmYear);
    }

    public void writeInFile(String pathname, String filmTitle, String filmYear){
        file = new File (pathname);
        try{
            writer = new FileWriter(file, true);
            writer.write(filmTitle + "\t" + filmYear);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }catch (IOException e) { e.printStackTrace(); }
    }

    public void setUpTableForFavourite() {
        //favouriteTitleColumn
        favouriteTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //favouriteYearColumn
        favouriteYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        favouriteTable.setItems(allFilmsInFavourite);

        //if you do a double click the selected film will be shown in the main window
        favouriteTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double click");
                    }
                }
            }
        });
    }

    public void setUpTableForBookmarks(){
        bookmarksTable.setEditable(true);

        //bookmarksTitleColumn
        bookmarksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //bookmarksYearColumn
        bookmarksYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        //bookmarksFavouriteColumn
        bookmarksFavouriteColumn.setEditable(true);
        bookmarksFavouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        bookmarksFavouriteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(bookmarksFavouriteColumn));

        bookmarksTable.setItems(allFilmsInBookmarks);

        //if you do a double click the selected film will be shown in the main window
        bookmarksTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double click");
                    }
                }
            }
        });
    }

    public void readLinesFromFavourite(String fileName){
        readLinesFromFile(fileName, allFilmsInFavourite);
    }

    public void readLinesFromBookmarks(String fileName){
        readLinesFromFile(fileName, allFilmsInBookmarks);
    }
    private void readLinesFromFile(String datName, ObservableList<Film> allFilms) {
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
                allFilms.add(makeFilm(word[0], Integer.parseInt(word[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public Film makeFilm(String title, int year) {
        Film film = new Film(title, year);
        return film;
    }

    //Before finally closing the window
    //Favourite.txt will be changed according to latest ObservableList allFilmsInFavourite
    public void closingFavouriteWindowAction(Stage stage) {
        stage.setOnHiding(event ->{
            overwriteFavourite();
            Controller.setSimpleBooleanProperty();
        });
    }

    //Bookmarks.txt will be changed acoording to latest ObservableList allFilmsInBookmarks
    public void closingBookmarksWindowAction(Stage stage) {
        stage.setOnHiding(event -> {
            overwriteInBookmarks();
            Controller.setSimpleBooleanProperty();
        });
    }

    //Action by pressing the delete button in favourite table
    public void deleteFilmInFavourite() {
        try{
            ObservableList<Film> productSelected;
            productSelected = favouriteTable.getSelectionModel().getSelectedItems();
            productSelected.forEach(allFilmsInFavourite::remove);
        } catch (NoSuchElementException e){Controller.getFavouriteWindow().close();}
    }

    //Action by pressing the delete button in bookmarks table
    public void deleteFilmInBookmarks() {
        try{
            ObservableList<Film> productSelected;
            productSelected = bookmarksTable.getSelectionModel().getSelectedItems();
            productSelected.forEach(allFilmsInBookmarks::remove);
        } catch (NoSuchElementException e){Controller.getBookmarksWindow().close();}
    }

    public void overwriteFavourite() {
        overwriteFile("Favoriten.txt", allFilmsInFavourite);
    }

    public void overwriteInBookmarks(){
        overwriteFile("Bookmarks.txt", allFilmsInBookmarks);
    }

    public void overwriteFile(String pathname, ObservableList<Film> allFilms){
        file = new File(pathname);
        try {
            writer = new FileWriter(file);
            for (Film f : allFilms) {
                writer.write(f.getTitle());
                writer.write("\t");
                writer.write(String.valueOf(f.getYear()));
                writer.write(System.getProperty("line.separator"));
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    //
    //
    //


}

