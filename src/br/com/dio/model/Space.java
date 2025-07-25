package br.com.dio.model;

public class Space {
    private Integer actual;
    private final int expected;
    private final boolean fixed;


    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;

        if (fixed) {
            this.actual = expected;
        }
    }


    public Integer getActual() {
        return this.actual;
    }

    public void setActual(Integer actual) {
        if (this.fixed) return;
        this.actual = actual;
    }

    public int getExpected() {
        return this.expected;
    }


    public boolean isFixed() {
        return this.fixed;
    }

    public void clearSpace() {
        setActual(null);
    }
}
