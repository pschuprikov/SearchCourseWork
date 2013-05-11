package ru.chuprikov.search.web;


import ru.chuprikov.search.datatypes.Term;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebTermDB {

    @WebMethod
    Term get(String term) throws Exception;

    @WebMethod
    Term getFirstTerm() throws Exception;

    @WebMethod
    Term[] getNextTerms(String term, int length);
}
