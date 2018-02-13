package PrimeNet;

import PrimeNet.movies.Results;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Controller{
    public static Stage favouriteWindow = new Stage();
    @FXML
    public ProgressIndicator progressbar;
    @FXML
    Button favouriteButton = new Button();
    @FXML
    ComboBox<String> categoriesComboBox;
    @FXML
    TextField searchField = new TextField();
    @FXML
    TableView<Film> filmTable = new TableView<>();
    @FXML
    TableColumn<Film, String> rateColumn = new TableColumn<>("Bewerten");
    @FXML
    TableColumn<Film, Boolean> favouriteColumn = new TableColumn<>("Favorit");
    @FXML
    TableColumn<Film, String> titleColumn = new TableColumn<>("Film");
    @FXML
    TableColumn<Film, Integer> yearColumn = new TableColumn<>("Jahr");
    @FXML
    TableColumn<Film, Boolean> rememberColumn = new TableColumn<>("Merken");
    @FXML
    TableColumn<Film, String> categoriesColumn = new TableColumn<>("Kategorien");
    @FXML
    ComboBox<String> yearComboBox = new ComboBox<>();
    File file;
    FileWriter writer;
    @FXML
    ImageView previewPane = new ImageView();
    @FXML
    Label previewTitle = new Label();
    @FXML
    Label previewDate = new Label();
    @FXML
    Label previewRate = new Label();
    @FXML
    Label previewOverview = new Label();
    @FXML
    private ObservableList<String> rate = FXCollections.observableArrayList();
    private ObservableList<Film> originalFilms = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        setUpEverything();
        rate.addAll("Like", "Dislike");
        progressbar.setProgress(-1.0f);
        progressbar.setVisible(false);
    }

    public void setUpEverything(){
        fillCategoriesComboBox();
        fillYearComboBox();
        setUpTableOfFilm();
    }

    public void fillCategoriesComboBox(){
        categoriesComboBox.getItems().add("Alle");
        categoriesComboBox.getItems().addAll( "Abenteuer", "Action", "Animation", "Dokumentarfilm",
                "Drama", "Familie", "Fantasy", "Historie", "Horror", "Komödie", "Kriegsfilm", "Krimi",
                "Liebesfilm", "Musik", "Mystery", "Science", "Fiction", "TV-Film", "Thriller", "Western");
    }

    public void fillYearComboBox(){
        yearComboBox.getItems().add("Alle");
        for (int n = 1950; n <= 2018; n++){
            yearComboBox.getItems().add(String.valueOf(n));
        }
    }

    public void setUpTableOfFilm() {
        filmTable.setEditable(true);

        //titleColumn
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);

        //yearColumn
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearColumn.setPrefWidth(45);

        //rememberColumn
        rememberColumn.setEditable(true);
        rememberColumn.setPrefWidth(60);
        rememberColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        rememberColumn.setCellValueFactory(new PropertyValueFactory<>("remember"));

        //favouriteColumn
        favouriteColumn.setEditable(true);
        favouriteColumn.setPrefWidth(60);
        favouriteColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        favouriteColumn.setCellValueFactory(cellData -> {
            Film cellValue = cellData.getValue();
            BooleanProperty property = cellValue.favouriteProperty();

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> {
               cellValue.setFavourite(newValue);
               writeInFavourite(cellValue.getTitle(), String.valueOf(cellValue.getYear()));
            });

            return property;
        });

        //rateColumn
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(),rate));
        rateColumn.setPrefWidth(75);

        //categoriesColumn
        categoriesColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));

        filmTable.getColumns().addAll(favouriteColumn, titleColumn, yearColumn, rememberColumn, rateColumn, categoriesColumn);

        filmTable.setOnMouseClicked((event) -> {
            Film film = filmTable.getSelectionModel().getSelectedItem();
            if (film == null) {
                previewPane.setImage(null);
                previewTitle.setText("");
                previewDate.setText("");
                previewRate.setText("");
                previewOverview.setText("");
                return;
            }
            previewPane.setImage(film.getPoster());
            previewTitle.setText(film.getTitle());
            String year = Integer.toString(film.getYear());
            previewDate.setText("(" + year + ")");
            previewRate.setText(film.getRate());
            previewOverview.setText(film.getOverview());
        });
    }

    //write in favouriteFile to save favourite
    public void writeInFavourite(String filmTitle, String filmYear){
        file = new File ("Favoriten.txt");
        try{
            writer = new FileWriter(file, true);
            writer.write(filmTitle);
            writer.write("\t");
            writer.write(filmYear);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }catch (IOException e) { e.printStackTrace(); }
    }

    //by clicking the button "Favoriten" a new window will be opened
    public void clickFavourite() throws IOException{
        Parent root =  FXMLLoader.load(getClass().getResource("Favourite.fxml"));
        try{
            favouriteWindow.setTitle("Favoriten");
            favouriteWindow.setResizable(false);
            favouriteWindow.setScene(new Scene(root,600, 400));
            favouriteWindow.show();
        }catch (IllegalStateException e){
            favouriteWindow.show();
        }
    }

    //if enter is pressed table of film will be filled with new content
    public void onEnter() {
        progressbar.setVisible(true);
        new Thread(() -> {
            originalFilms = getFilm();
            ObservableList<Film> films = FXCollections.observableArrayList();
            films.addAll(originalFilms);
            filmTable.setItems(films);
            filterTableViewAccToYears(yearComboBox.getValue());
        }).start();
    }

    public ObservableList<Film> getFilm() {
        ObservableList<Film> films = FXCollections.observableArrayList();
        String pattern = "\\s+";
        String searchCorrection = searchField.getText().replaceAll(pattern, "+");
        Results r = MovieDatabase.getMoviesByName(searchCorrection);
        r.getMovies()
                .stream()
                .map(movie -> {
                    return new Film(false, movie.getTitle(), movie.getReleaseYear(),
                            movie.getOverview(), false, "", MovieDatabase.getPoster(movie),
                            movie.getCategories()) ;
                })
                .forEach(films::add);
        progressbar.setVisible(false);
        return films;
    }

    public void clickYearComboBox(){
        filterTableViewAccToYears(yearComboBox.getValue());
    }

    public void clickCategoriesComboBox() {
        filterTableViewAccToCategories(categoriesComboBox.getValue());
    }
    //filter tableView list according to selected year
    public void filterTableViewAccToYears(String yearOfComboBox){
        ObservableList<Film> selectedYearFilms;
        selectedYearFilms = FXCollections.observableArrayList();
        try {
            int year = Integer.parseInt(yearOfComboBox);
            for (Film s : originalFilms){
                if (s.getYear() == year){
                    selectedYearFilms.add(s);
                }
            }
        } catch (NumberFormatException e) {
            selectedYearFilms = originalFilms;
        }

        filmTable.setItems(selectedYearFilms);
    }

    //filter tableView list according to selected Categorie
    public void filterTableViewAccToCategories(String categorieOfComboBox) {
        ObservableList<Film> selectedCategorieFilms;
        selectedCategorieFilms = FXCollections.observableArrayList();
        try {
            String categorie = categorieOfComboBox;
            for (Film s : originalFilms){
                if (s.getCategories().equals(categorie)){
                    selectedCategorieFilms.add(s);
                }
            }

        } catch (Exception e) {
            selectedCategorieFilms = originalFilms;
        }

        filmTable.setItems(selectedCategorieFilms);
    }


        /*
        AutoCompletionBinding<MovieData> autoCompletionBinding =
                TextFields.bindAutoCompletion(searchField,
                        suggestionRequest -> MovieData. );
        autoCompletionBinding.setOnAutoCompleted(event -> {
            cities.add(event.getCompletion());
            searchField.clear();
            saveCitiesToDisk();
        });*/


}





