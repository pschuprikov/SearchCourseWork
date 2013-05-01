package ru.kirillova.search.database;

import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.database.datatypes.ProblemRawData;

interface ContentParser {
    ParsedProblem parseContent(ProblemRawData problem);
}
