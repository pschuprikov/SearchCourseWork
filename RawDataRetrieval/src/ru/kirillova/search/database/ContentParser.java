package ru.kirillova.search.database;

import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemRawData;

interface ContentParser {
    ParsedProblem parseContent(ProblemRawData problem);
}
