package test.provider.impl;

import test.api.Cal;

/**
 * Created by wwj on 2017/8/17.
 */
public class CalImpl implements Cal {

    public int add(int a, int b) {
        return a+b + 1;
    }
}
