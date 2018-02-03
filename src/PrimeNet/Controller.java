package PrimeNet;

import PrimeNet.movies.Results;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DefaultStringConverter;
import javafx.scene.image.ImageView;


public class Controller{
    @FXML
    public ProgressIndicator progressbar;
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
    ComboBox<String> yearComboBox = new ComboBox<>();
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

    @FXML
    ImageView previewPane = new ImageView();

    public void setUpEverything(){
        fillCategoriesComboBox();
        fillYearComboBox();
        setUpTableOfFilm();
    }

    public void fillCategoriesComboBox(){
        categoriesComboBox.getItems().addAll("Action","Abenteuer", "Animation", "Drama", "Familie", "Fantasie",
                "Historie", "Horror", "Kriegsfilm", "Krimi", "Komödie", "Liebesfilm", "Science Fiction");
    }

    public void fillYearComboBox(){
        yearComboBox.getItems().addAll("Alle", "2015","2016","2017");
    }

    public void setUpTableOfFilm() {
        filmTable.setEditable(true);

        //titleColumn
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //yearColumn
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        //rememberColumn
        rememberColumn.setCellValueFactory(new PropertyValueFactory<>("remember"));
        rememberColumn.setCellFactory(CheckBoxTableCell.forTableColumn(rememberColumn));
        rememberColumn.setEditable(true);

        //favouriteColumn
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        favouriteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(favouriteColumn));
        favouriteColumn.setEditable(true);

        //rateColumn
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(),rate));

        filmTable.getColumns().addAll(favouriteColumn, titleColumn, yearColumn, rememberColumn, rateColumn);

        filmTable.setOnMouseClicked((event) -> {
            Film film = filmTable.getSelectionModel().getSelectedItem();
            if (film == null) {
                previewPane.setImage(null);
                return;
            }

            previewPane.setImage(film.getPoster());
        });
    }

    //if enter is pressed table of film will be filled with new content
    public void onEnter() {
        progressbar.setVisible(true);
        new Thread(() -> {
            originalFilms = getFilm();

            ObservableList<Film> films = FXCollections.observableArrayList();
            films.addAll(originalFilms);
            filmTable.setItems(films);
        }).start();
    }

    public ObservableList<Film> getFilm() {
        ObservableList<Film> films = FXCollections.observableArrayList();
        Results r = MovieDatabase.getMoviesByName(searchField.getText());
        r.getMovies()
                .stream()
                .map(movie -> {
                    return new Film(false, movie.getTitle(), movie.getReleaseYear(), false, "", MovieDatabase.getPoster(movie));
                })
                .forEach(films::add);
        progressbar.setVisible(false);
        return films;
    }

    public void clickYearComboBox(){
        filterTableView(yearComboBox.getValue());
    }

    //filter tableView list according to selected year
    public void filterTableView(String yearOfComboBox){
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





