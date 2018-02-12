package PrimeNet.movies;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.stream.Collectors;

public class Posters {

    @SerializedName("posters")
    private List<String> posterURLs;

    public Posters() {
    }

    public Posters(List<String> urls) {
        this.posterURLs = urls;
    }

    public List<String> getPosterURLs() {
        return posterURLs;
    }

    public void setPosterURLs(List<String> posterURLs) {
        this.posterURLs = posterURLs;
    }

    @Override
    public String toString() {
        return posterURLs.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
}
