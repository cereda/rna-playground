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
import java.util.List;

/**
 * Uso do coletor.
 *
 * @author Paulo Roberto Massa Cereda
 */
public class CollectorUsage {

    public static void main(String[] args) {

        StringBuilder sample1 = new StringBuilder();
        sample1.append("ngatgtgagggcgatctggctgcgacatctgtcaccccattgatcgccag");
        sample1.append("ggttgattcggctgatctggctggctaggcggtgtccccttcctccctca");
        sample1.append("ccgctccatgtgcgtccctcccgaagctgcgcgctcggtcgaagaggacg");
        sample1.append("accatccccgatagaggaggaccggtcttcggtcaagggtatacgagtag");
        sample1.append("ctgcgctcccctgctagaacctccaaacaagctctcaaggtccatttgta");
        sample1.append("ggagaacgtagggtagtcaagcttccaagactccagacacatccaaatga");
        sample1.append("ggcgctgcatgtggcagtctgcctttct");

        StringBuilder sample2 = new StringBuilder();
        sample2.append("ggatgtgagggcgatctggctgcgacatctgtcaccccattgatcgccag");
        sample2.append("ggttgattcggctgatctggctggctaggcgggtgtccccttcctccctc");
        sample2.append("accgctccatgtgcgtccctcccgaagctgcgcgctcggtcgaagaggac");
        sample2.append("gaccttccccgatagagacggtaccgttcatcggtcaagggtatacggta");
        sample2.append("gctgcgctcccctgctagaacctccaaacaagctcaaggtccatttgtag");
        sample2.append("gagaacgtagggtagtcaagcttccaagactgcagacacatccaagtgag");
        sample2.append("gcactgcatgtggcagtctgcctttcttt");

        Collector c = new Collector();
        c.build(sample1.toString());
        c.minimize();
        Collector.write(new File("/home/paulo/sample1.dot"), c.dot());
        List<Pair<Integer, Integer>> intervals
                = c.analyze(sample2.toString(), 4);
        intervals.forEach(System.out::println);
    }

}
