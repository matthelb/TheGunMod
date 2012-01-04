package com.heuristix.util;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/7/11
 * Time: 4:01 PM
 */
public class Pair<A, B> {

    private A a;
    private B b;

    public Pair() {
        this(null, null);
    }


    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getFirst() {
        return a;
    }

    public void setFirst(A a) {
        this.a = a;
    }

    public B getSecond() {
        return b;
    }

    public void setSecond(B b) {
        this.b = b;
    }

    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }
}
