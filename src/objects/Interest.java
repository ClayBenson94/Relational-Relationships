package objects;

/**
 * A class to represent an interest
 */
public class Interest {

    private String name;
    private String Description;
    private String Category;

    /**
     * Construct an interest
     *
     * @param name - The name of the interest
     * @param description - The description of the interest
     * @param category - The category of the interest
     */
    public Interest(String name, String description, String category) {
        this.name = name;
        Description = description;
        Category = category;
    }

    /**
     * Overridden method to see if 2 interest objects are equal
     *
     * @param o - The other interest object to check for equality
     * @return Whether or not the 2 objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        if (!name.equals(interest.name)) return false;
        if (!Description.equals(interest.Description)) return false;
        return Category.equals(interest.Category);

    }

    /**
     * Returns the hashcode of the given interest object
     * @return The hashcode of the interest object
     */
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
