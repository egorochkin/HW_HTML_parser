import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Main {
    //В этом методе кладем все имеющиеся конкретные фабрики в HashMap с ключом
    private Map<String, ParseGetter> makeInit(){
            Map<String, ParseGetter> fabricMap = new HashMap<>();
            fabricMap.put("sql.ru", new ParseSqlRu());
            return fabricMap;
    }

    public static void main(String[] args) throws IllegalAccessException {
        Main app = new Main();
        //Получаем HashMap содержащий в себе ключи в строковом формате (сайты)
        //и объекты-фабрики для порождения парсеров. Пока фабрика только одна,
        //но если надо будет расширять - это будет не сложно.
        Map<String, ParseGetter> fabricMap = app.makeInit();
        //Дальше парсим аргументы командной строки, в формате -<ключ>=<значение>
        //запускать так:
        //-source=<откуда брать вакансии> -file=<имя файла> -key=<ключевое слово> -period=<кол-во дней>
        Map<String, String> parameters = new LinkedHashMap<>();
        for (String arg : args) {
            String[] parts = arg.split("=", 2);
            String key = parts[0];
            if (key.startsWith("--")) {
                key = key.substring(2);
            } else {
                key = key.substring(1);
            }
            String value = parts[1];

            parameters.put(key, value);
        }
        //Проверяем что передан поддерживаемый параметр
        if ( !fabricMap.containsKey(parameters.get("source"))) throw new IllegalArgumentException("source");
        Parser vacParser = fabricMap.get(parameters.get("source")).getParser();
        Writer writer = new Writer(parameters.get("file"));
        if (parameters.size() == 4 && parameters.get("period").chars().allMatch(Character::isDigit)) try {
            writer.writeToFile(vacParser.parse(parameters.get("key"),
                    Integer.parseInt(parameters.get("period"))));
        } catch (IOException e) {
            e.printStackTrace();
        }else throw new IllegalArgumentException();

    }

}
