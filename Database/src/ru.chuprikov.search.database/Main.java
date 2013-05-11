package ru.chuprikov.search.database;

import java.io.File;

class Main {
    public static void main(String[] args) {
        if (args.length == 0)
            System.err.println("Usage: command [arg]");
        else {
            try (SearchDatabase searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"))) {
                switch(args[0]) {
                    case "truncate" : {
                        for (int i = 1; i < args.length; i++) {
                            switch (args[i]) {
                                case "term" : searchDB.truncateTermDB(); break;
                                case "parsed" : searchDB.truncateParsedDB(); break;
                                case "document" : searchDB.truncateDocumentDB(); break;
                                case "fetched" : searchDB.truncateFetchedDB(); break;
                                case "index" : searchDB.truncateIndexDB(); break;
                                case "bigramm" : searchDB.truncateBigrammDB(); break;
                                case "seqs" : searchDB.truncateSequences(); break;
                                default:
                                    System.err.println("Unknown database"); break;
                            }
                        }
                    } break;
                    default:
                        System.err.println("Unknown command");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
