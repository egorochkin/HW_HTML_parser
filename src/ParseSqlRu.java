public class ParseSqlRu implements ParseGetter{
    @Override
    public Parser getParser() {
        return new SQLRuParser();
    }
}
