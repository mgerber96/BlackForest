package PrimeNet.movies;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Movie {

    private static Map<Integer, String> categories = new HashMap<>();

    static {
        categories.put(12, "Abenteuer");
        categories.put(28, "Action");
        categories.put(16, "Animation");
        categories.put(99, "Dokumentarfilm");
        categories.put(18, "Drama");
        categories.put(10751, "Familie");
        categories.put(14, "Fantasy");
        categories.put(36, "Historie");
        categories.put(27, "Horror");
        categories.put(35, "Komödie");
        categories.put(10752, "Kriegsfilm");
        categories.put(80, "Krimi");
        categories.put(10749, "Liebesfilm");
        categories.put(10402, "Musik");
        categories.put(9648, "Mystery");
        categories.put(878, "Science Fiction");
        categories.put(10770, "TV-Film");
        categories.put(53, "Thriller");
        categories.put(37, "Western");
    }

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("id")
    private String id;

    @SerializedName("genre_ids")
    private int[] genres;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    public int getReleaseYear() {
        if (releaseDate == null) {
            return 0;
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(releaseDate);
            LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
            return localDate.getYear();
        } catch (ParseException parseException) {
            return 0;
        }
    }

    public String getCategories() {
        return Arrays.stream(genres).mapToObj(genre -> categories.getOrDefault(genre, "nicht bekannt"))
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return String.format("%s: %s, %s", id, title, overview);
    }
}

/* Example JSON
{"page":1,
"total_results":1,
"total_pages":1,
"results":[{"vote_count":4943,
"id":497,
"video":false,
"vote_average":8.3,
"title":"The Green Mile",
"popularity":32.818983,
"posterPath":"\/3yJUlOtVa09CYJocwBU8eAryja0.jpg",
"original_language":"en",
"original_title":"The Green Mile",
"genre_ids":[14,18,80],
"backdrop_path":"\/Rlt20sEbOQKPVjia7lUilFm49W.jpg",
"adult":false,
"overview":"A supernatural tale set on death row in a Southern prison, where gentle giant John Coffey possesses
the mysterious power to heal people'getMoviesByName ailments. When the cellblock'getMoviesByName head guard, Paul Edgecomb, recognizes Coffey'getMoviesByName
miraculous gift, he tries desperately to help stave off the condemned man'getMoviesByName execution.",
"releaseDate":"1999-12-10"}]}
*/
