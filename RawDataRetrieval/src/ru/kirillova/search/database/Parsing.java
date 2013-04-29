package ru.kirillova.search.database;

import ru.chuprikov.search.database.*;
import ru.chuprikov.search.gather.ProblemRawData;

import java.io.File;

public class Parsing {
    public static void main(String[] args) {
        MultiSiteContentParser parser = new MultiSiteContentParser();
        try (
            SearchDatabase searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
            FetchedDB fetchDB = searchDB.openFetchDB();
            ParsedDB parsedDB = searchDB.openParseDB();
            CloseableIterator<ProblemRawData> it = fetchDB.openIterator();
        ) {
            while (it.hasNext()) {
                parsedDB.saveParsed(parser.parseContent(it.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
