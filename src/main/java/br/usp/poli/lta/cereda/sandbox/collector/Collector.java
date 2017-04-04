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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementa o coletor de nomes.
 *
 * @author Paulo Roberto Massa Cereda
 */
public class Collector {

    private final Set<Transition> transitions;
    private int next;
    private final Set<Integer> accepting;

    public Collector() {
        transitions = new HashSet<>();
        accepting = new HashSet<>();
    }

    public void build(String input) {
        transitions.clear();
        next = 1;
        for (char symbol : input.toCharArray()) {
            transitions.add(new Transition(next, symbol, ++next));
        }
        accepting.add(next);
        getSubstrings(input).forEach(this::populate);
    }

    private Set<String> getSubstrings(String input) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < input.length(); i++) {
            for (int j = 1; j <= input.length() - i; j++) {
                set.add(input.substring(i, i + j));
            }
        }
        return set;
    }

    private void populate(String input) {
        int state = 1;
        for (char symbol : input.toCharArray()) {
            Optional<Transition> transition = Optional.empty();

            for (Transition t : transitions) {
                if (t.getFrom() == state && t.getSymbol() == symbol) {
                    transition = Optional.of(t);
                }
            }

            if (transition.isPresent()) {
                state = transition.get().getTo();
            } else {
                transitions.add(new Transition(state, symbol, ++next));
                state = next;
            }
        }
        accepting.add(state);
    }

    @Override
    public String toString() {
        return transitions.toString();
    }

    public String dot() {
        StringBuilder sb = new StringBuilder();
        Set<Integer> states = new HashSet<>();
        states.addAll(accepting);
        transitions.forEach((Transition t) -> {
            states.add(t.getFrom());
            states.add(t.getTo());
        });
        sb.append("digraph {").append("\n");
        states.forEach((Integer t) -> {
            sb.append(String.format("\te%d [label=\"%d\", shape = %s];",
                    t, t, accepting.contains(t) ? "doublecircle"
                    : "circle"));
            sb.append("\n");
        });
        transitions.forEach((t) -> {
            sb.append(String.format("\te%d -> e%d [label=\"%c\"];",
                    t.getFrom(), t.getTo(), t.getSymbol())).append("\n");
        });
        sb.append("}").append("\n");
        return sb.toString();
    }

    public static void write(File file, String content) {
        try {
            Files.write(file.toPath(), content.getBytes());
        } catch (IOException nothandled) {
            // nope :)
        }
    }

    public void minimize() {

        Set<Integer> Q = new HashSet<>();
        Set<Integer> A = new HashSet<>(accepting);
        Set<Integer> R = new HashSet<>();

        transitions.stream().forEach((transition) -> {
            Q.add(transition.getFrom());
            Q.add(transition.getTo());
        });

        Q.stream().filter((q) -> (!A.contains(q))).forEachOrdered((q) -> {
            R.add(q);
        });

        Set<Set<Integer>> T = new HashSet<>();
        T.add(A);
        T.add(R);

        Set<Set<Integer>> P = new HashSet<>();

        while (!T.equals(P)) {
            P = new HashSet<>(T);
            T = new HashSet<>();
            for (Set<Integer> p : P) {
                for (Set<Integer> i : split(p, P)) {
                    if (!i.isEmpty()) {
                        T.add(i);
                    }
                }
            }
        }

        Map<Integer, Integer> names = new HashMap<>();
        int counter = 1;
        for (Set<Integer> t : T) {
            if (t.contains(1)) {
                for (int i : t) {
                    names.put(i, counter);
                }
            }
        }

        for (Set<Integer> t : T) {
            if (!t.contains(1)) {
                counter++;
                for (int i : t) {
                    names.put(i, counter);
                }
            }
        }

        Set<Transition> converted = new HashSet<>();

        transitions.stream().map((transition) -> new Transition(
                names.get(transition.getFrom()), transition.getSymbol(),
                names.get(transition.getTo()))).forEachOrdered(converted::add);

        Set<Integer> terminators = new HashSet<>();
        accepting.forEach((i) -> {
            terminators.add(names.get(i));
        });

        transitions.clear();
        transitions.addAll(converted);

        accepting.clear();
        accepting.addAll(terminators);

    }

    private Set<Set<Integer>> split(Set<Integer> S, Set<Set<Integer>> p) {
        Set<Set<Integer>> result = new HashSet<>();
        Set<Character> sigma = alphabet();
        Set<Integer> temp = null;

        HashSet<Integer> second = new HashSet<>();
        for (Character c : sigma) {
            for (int s : S) {
                if (temp == null) {
                    temp = getSet(s, c, p);
                } else {
                    if (!temp.equals(getSet(s, c, p))) {
                        second.add(s);
                    }
                }
            }
            temp = null;
        }

        if (!second.isEmpty()) {
            Set<Integer> first = new HashSet<>(S);
            first.removeAll(second);
            result.add(first);
            result.add(second);
        } else {
            result.add(S);
        }

        return result;
    }

    private Set<Character> alphabet() {
        return transitions.stream().map(Transition::getSymbol).
                collect(Collectors.toSet());
    }

    private Set<Integer> getSet(final int from, final char symbol,
            Set<Set<Integer>> set) {
        Optional<Transition> transition = transitions.stream().
                filter((Transition t) -> t.getFrom() == from
                && t.getSymbol() == symbol).findFirst();

        if (transition.isPresent()) {
            for (Set<Integer> element : set) {
                if (element.contains(transition.get().getTo())) {
                    return element;
                }
            }
        }
        return new HashSet<>();
    }

    public List<Pair<Integer, Integer>> analyze(String input) {
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();

        int state = 1;
        int counter = 0;

        pairs.add(new Pair<>());

        for (int i = 0; i < input.length(); i++) {

            char symbol = input.charAt(i);
            Optional<Transition> transition = Optional.empty();

            for (Transition t : transitions) {
                if (t.getFrom() == state && t.getSymbol() == symbol) {
                    transition = Optional.of(t);
                }
            }

            if (transition.isPresent()) {
                state = transition.get().getTo();

                int index = pairs.size() - 1;

                if (!pairs.get(index).hasFirst()) {
                    pairs.get(index).setFirst(counter);
                }

                pairs.get(index).setSecond(counter);

            } else {

                pairs.add(new Pair<>());

                if (state != 1) {
                    counter--;
                    i--;
                }

                state = 1;
            }

            counter++;
        }

        return pairs.stream().filter(Pair::isValid).
                collect(Collectors.toList());
    }

    public List<Pair<Integer, Integer>> analyze(String input, int threshold) {
        return analyze(input).stream().filter((Pair<Integer, Integer> t)
                -> (t.getSecond() - t.getFirst()) >= (threshold - 1)).
                collect(Collectors.toList());
    }

}
