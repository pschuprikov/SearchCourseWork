package ru.kirillova.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

interface ContentParser {
    ParsedProblem parseContent(ProblemRawData problem);
}
