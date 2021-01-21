package it.unipi.softgram.table_chooser;

public class MostPopCat {
    private String _id;
    private String name;
    private String category;
    private double Avg;
    private String released;

    public MostPopCat(String _id, String name, String category, double avg, String released) {
        this._id = _id;
        this.name = name;
        this.category = category;
        this.Avg = avg;
        this.released=released;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
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

    public double getAvg() {
        return Avg;
    }

    public void setAvg(double avg) {
        Avg = avg;
    }
}
