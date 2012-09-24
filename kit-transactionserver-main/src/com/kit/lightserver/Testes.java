package com.kit.lightserver;


public class Testes {

    public static void main(final String[] args)  {
        Gata ola = new Gata();
        try( Gata oi = ola ) {
            System.out.println("teste");
            System.out.println("teste2");
        }

        try( Gata oi = ola ) {
            System.out.println("teste");
            System.out.println("teste2");
        }
    }

    static class Gata implements AutoCloseable {

        @Override
        public void close() {
            System.out.println("closing!");
        }

    }

}
