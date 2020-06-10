package com.risen.phoenix.jdbc.core.pnd;

public interface ISqlExp {

    ISqlExp EMPTY = new ISqlExp() {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String getSql() {
            return "";
        }

        @Override
        public String toString() {
            return "";
        }
    };
    String OPEN = "#{";
    String CLOSE = "}";

    boolean isEmpty();

    String getSql();

    public static enum OP implements ISqlExp {
        AND("AND"),
        OR("OR"),
        NOT("NOT"),
        LP("("),
        RP(")"),
        LT("<"),
        LTE("<="),
        GT(">"),
        GTE(">="),
        EQ("="),
        NEQ("<>"),
        LIKE("LIKE"),
        NLIKE("NOT LIKE"),
        IN("IN"),
        NIN("NOT IN"),
        EXIST("EXIST"),
        NEXIST("NOT EXIST"),
        NULL("IS NULL"),
        NNULL("NOT IS NULL"),
        ORDER("ORDER");

        private String key;

        private OP(String key) {
            this.key = key;
        }

        public boolean isNullType() {
            return this == NULL || this == NNULL;
        }

        public boolean isInType() {
            return this == IN || this == NIN;
        }

        public boolean isEQType() {
            return this == LT || this == LTE || this == GT || this == GTE || this == EQ || this == NEQ || this == LIKE || this == NLIKE;
        }

        @Override
        public String toString() {
            return this.key;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String getSql() {
            return this.key;
        }
    }

    public static enum ORDER {
        ASC,
        DESC;

        private ORDER() {
        }
    }
}
