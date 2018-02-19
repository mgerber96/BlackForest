package PrimeNet.movies;

import com.google.gson.annotations.SerializedName;

import javax.naming.Name;
import java.util.List;

public class Titles {

    @SerializedName("title")
    private List<Name> title;

    public Titles() {
    }

    public Titles(List<Name> title) {
        this.title = title;
    }

    public void setNames(List<Name> title) {
        this.title = title;
    }

    public List<Name> getNames() {
        return title;
    }
}
