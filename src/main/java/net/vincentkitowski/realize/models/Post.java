package net.vincentkitowski.realize.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
public class Post extends AbstractEntity {

    private static final String START_DATE_FORMAT_PATTERN = "MM/dd/yyyy";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT
            = new SimpleDateFormat(START_DATE_FORMAT_PATTERN);

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Please enter a valid date")
    @DateTimeFormat(pattern = START_DATE_FORMAT_PATTERN)
    private Date startDate;

    private String location;


    private String status;


    public Post() {}

    public Post(@NotBlank String title,
                @NotNull String description,
                @NotNull Date startDate,
                String location, String status) {

        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("Title may not be blank");

        if (description == null || description.length() == 0)
            throw new IllegalArgumentException("Description may not be null");

        if (startDate == null)
            throw new IllegalArgumentException("Start date may not be null");

        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.location = location;
        this.location = status;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getFormattedStartDate() {
        return Post.SIMPLE_DATE_FORMAT.format(startDate);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }



    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                '}';
    }

}