package ru.chuprikov.search.gather;

import java.net.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
class NoProxyProvider implements ProxyProvider {
    @Override
    public Proxy getProxy() {
        return Proxy.NO_PROXY;
    }
}