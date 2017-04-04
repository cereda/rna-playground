/**
 * ------------------------------------------------------
 *    Laboratório de Linguagens e Técnicas Adaptativas
 *       Escola Politécnica, Universidade São Paulo
 * ------------------------------------------------------
 *
 * This program is free software: you can redistribute it
 * and/or modify  it under the  terms of the  GNU General
 * Public  License  as  published by  the  Free  Software
 * Foundation, either  version 3  of the License,  or (at
 * your option) any later version.
 *
 * This program is  distributed in the hope  that it will
 * be useful, but WITHOUT  ANY WARRANTY; without even the
 * implied warranty  of MERCHANTABILITY or FITNESS  FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 **/
package br.usp.poli.lta.cereda.sandbox.collector;

/**
 * Implementa uma transição.
 *
 * @author Paulo Roberto Massa Cereda
 */
public class Transition {

    private int from;
    private char symbol;
    private int to;

    public Transition(int from, char symbol, int to) {
        this.from = from;
        this.symbol = symbol;
        this.to = to;
    }

    public Transition() {
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.from;
        hash = 19 * hash + this.symbol;
        hash = 19 * hash + this.to;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Transition that = (Transition) object;
        if (this.from != that.from) {
            return false;
        }
        if (this.symbol != that.symbol) {
            return false;
        }
        return this.to == that.to;
    }

    @Override
    public String toString() {
        return String.format("(%d, %c) → %d", from, symbol, to);
    }

}
