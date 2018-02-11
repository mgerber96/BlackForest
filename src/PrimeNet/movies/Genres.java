package PrimeNet.movies;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Genres {

        @SerializedName("genre_ids")
        private List<Integer> genreIDs;

        public List<Integer> getGenreIDs() { return genreIDs; }

        public void setGenreIDs(List<Integer> genreIDs) { this.genreIDs = genreIDs; }

        private List<String> categories;

        public Genres (){
        }

        public void setCategories(List<String> categories){
            switch (genreIDs.get(0))
            {
                case 12:  categories.add(1,"Abenteuer");
                    break;
                case 28:  categories.add(2,"Action");
                    break;
                case 16:  categories.add(3,"Animation");
                    break;
                case 99:  categories.add(4,"Dokumentarfilm");
                    break;
                case 18:  categories.add(5,"Drama");
                    break;
                case 10751:  categories.add(6,"Familie");
                    break;
                case 14:  categories.add(7,"Fantasy");
                    break;
                case 36:  categories.add(8,"Historie");
                    break;
                case 27:  categories.add(9,"Horror");
                    break;
                case 35: categories.add(10,"Komödie");
                    break;
                case 10752: categories.add(11,"Kriegsfilm");
                    break;
                case 80: categories.add(12,"Krimi");
                    break;
                case 10749: categories.add(13,"Liebesfilm");
                    break;
                case 10402: categories.add(14,"Musik");
                    break;
                case 9648: categories.add(15,"Mystery");
                    break;
                case 878: categories.add(16,"Science Fiction");
                    break;
                case 10770: categories.add(17,"TV-Film");
                    break;
                case 53: categories.add(18,"Thriller");
                    break;
                case 37: categories.add(19,"Western");
                    break;
                default: categories.add(20,"nicht bekannt");
                    break;
            }
        }

        public Genres (List<String> categories) { this.categories = categories; }

}

