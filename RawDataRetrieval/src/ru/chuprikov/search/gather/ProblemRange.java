package ru.chuprikov.search.gather;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
interface ProblemRange extends Iterator<ProblemFetchData> {
    boolean checkValid(String content);
}
