package modell;
import java.util.Objects;

public class categories {
    private int idCategories;
    private String name;
    private String description;


    public categories() {
    }

    public categories(int idCategories, String name, String description) {
        this.idCategories = idCategories;
        this.name = name;
        this.description = description;
    }

    public int getIdCategories() {
        return this.idCategories;
    }

    public void setIdCategories(int idCategories) {
        this.idCategories = idCategories;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public categories idCategories(int idCategories) {
        setIdCategories(idCategories);
        return this;
    }

    public categories name(String name) {
        setName(name);
        return this;
    }

    public categories description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof categories)) {
            return false;
        }
        categories categories = (categories) o;
        return idCategories == categories.idCategories && Objects.equals(name, categories.name) && Objects.equals(description, categories.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategories, name, description);
    }

    @Override
    public String toString() {
        return "{" +
            " idCategories='" + getIdCategories() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
    
}
