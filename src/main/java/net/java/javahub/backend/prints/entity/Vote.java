package net.java.javahub.backend.prints.entity;

public class Vote {

    private Print print;
    private int count;

    public Vote(final Print print, final int count) {
        this.print = print;
        this.count = count;
    }

    public Print getPrint() {
        return print;
    }

    public void setPrint(final Print print) {
        this.print = print;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

}
