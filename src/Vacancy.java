import lombok.*;
@Data
public class Vacancy {
    @NonNull private String linkToSource;
    @NonNull private String vacancyHead;
    @NonNull private String vacancyText;
    @NonNull private String creationDate;

}
