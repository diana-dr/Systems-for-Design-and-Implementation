package socket.server.Domain;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Book extends BaseEntity<Long> {
    private String category;
    private String title;
    private String author;
    private String releaseDate;
    private int price;

    public Book(){}

    public Book(String category, String title, String author, String releaseDate, int price) {
        this.category = category;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public Book(String values) {
        JSONTokener tokener = new JSONTokener(values);
        JSONObject object = new JSONObject(tokener);

        Long ID = object.getLong("id");
        this.setId(ID);

        this.category = object.getString("category");
        this.title = object.getString("title");
        this.author = object.getString("author");
        this.releaseDate = object.getString("date");
        this.price = object.getInt("price");
    }

    public Book(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getLong("id"));
        this.category = resultSet.getString("category");
        this.title = resultSet.getString("title");
        this.author = resultSet.getString("author");
        this.price = resultSet.getInt("price");
        this.releaseDate = resultSet.getString("releaseDate");
    }

    public Book getEntity(){return this;}

    public String getCategory(){return this.category;}

    public void setCategory(String category){this.category = category;}

    public Long getId() {
        return super.getId();
    }

    public void setId(Long aLong) {
        super.setId(aLong);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getReleaseDate() {

        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {

        this.releaseDate = releaseDate;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Release Date: " + releaseDate + "\n" +
                "Price:" + price + "\n" +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book books = (Book) o;
        return Objects.equals(title, books.title) &&
                Objects.equals(author, books.author) &&
                Objects.equals(releaseDate, books.releaseDate) &&
                price == books.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),title, author, releaseDate, price);
    }

    @Override
    public JSONObject getJson() {
        JSONObject object = new JSONObject();
        object.put("category", this.category);
        object.put("author", this.author);
        object.put("title", this.title);
        object.put("date", this.releaseDate);
        object.put("price", this.price);
        object.put("id", this.getId());

        return object;
    }
}
