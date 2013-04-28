package ru.kirillova.search.database;

import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.ParsedDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.gather.ProblemRawData;

import java.io.File;

public class Parsing {
    public static void main(String[] args) {
        MultiSiteContentParser parser = new MultiSiteContentParser();
        try (
            SearchDatabase searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
            FetchedDB fetchDB = searchDB.openFetchDB();
            ParsedDB parsedDB = searchDB.openParseDB();
        ) {
            for (ProblemRawData raw : fetchDB) {
                parsedDB.saveParsed(parser.parseContent(raw));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
