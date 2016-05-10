package objects;

/**
 * Created by nickj_000 on 3/16/2016.
 */
public class Interest {

    private String name;
    private String Description;
    private String Category;

    public Interest(String name, String description, String category) {
        this.name = name;
        Description = description;
        Category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        if (!name.equals(interest.name)) return false;
        if (!Description.equals(interest.Description)) return false;
        return Category.equals(interest.Category);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Description.hashCode();
        result = 31 * result + Category.hashCode();
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
