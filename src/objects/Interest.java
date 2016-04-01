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
