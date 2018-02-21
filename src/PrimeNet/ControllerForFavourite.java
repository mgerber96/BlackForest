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

    private boolean doubleClick = false;

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
        HelperMethods.writeInFile("Favoriten.txt", filmTitle, filmYear);
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
                        doubleClick = true;
                        Controller.getFavouriteWindow().close();
                        Controller.setDoubleClickInFavouriteOrBookmarksWindow(
                                favouriteTable.getSelectionModel().getSelectedItem().getTitle(),
                                favouriteTable.getSelectionModel().getSelectedItem().getYear()
                        );
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
                        Controller.getBookmarksWindow().close();
                        Controller.setDoubleClickInFavouriteOrBookmarksWindow(
                                bookmarksTable.getSelectionModel().getSelectedItem().getTitle(),
                                bookmarksTable.getSelectionModel().getSelectedItem().getYear()
                        );
                    }
                }
            }
        });
    }

    public void readLinesFromFavourite(String fileName){
        HelperMethods.readLinesFromFile(fileName, allFilmsInFavourite);
    }

    public void readLinesFromBookmarks(String fileName){
        HelperMethods.readLinesFromFile(fileName, allFilmsInBookmarks);
    }

    //Before finally closing the window
    //Favourite.txt will be changed according to latest ObservableList allFilmsInFavourite
    public void closingFavouriteWindowAction(Stage stage) {
        stage.setOnHiding(event ->{
            overwriteFavourite();
            if(!doubleClick){
                Controller.setWindowCloseAction();
            }
        });
    }

    public void overwriteFavourite() {
        HelperMethods.overwriteFile("Favoriten.txt", allFilmsInFavourite);
    }

    //Bookmarks.txt will be changed acoording to latest ObservableList allFilmsInBookmarks
    public void closingBookmarksWindowAction(Stage stage) {
        stage.setOnHiding(event -> {
            overwriteInBookmarks();
            Controller.setWindowCloseAction();
        });
    }

    public void overwriteInBookmarks(){
        HelperMethods.overwriteFile("Bookmarks.txt", allFilmsInBookmarks);
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
}

