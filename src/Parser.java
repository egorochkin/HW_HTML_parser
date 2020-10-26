import java.io.IOException;
import java.util.Set;
public interface Parser {
    Set<Vacancy> parse(String keyWord, int period) throws IOException;
}
