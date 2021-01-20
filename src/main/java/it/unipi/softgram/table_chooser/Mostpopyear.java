package table_chooser;

public class Mostpopyear {
    private String _id;
    private String name;
    private String category;
    private String released;


    public Mostpopyear(String _id, String name, String category, String released) {
        this._id = _id;
        this.name = name;
        this.category = category;
        this.released = released;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }
}
