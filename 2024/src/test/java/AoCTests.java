import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Math.*;

class AoCTests {

    // create all input file cmd : for i in {1..25}; do touch jdd$i.txt ;  done

    @Test
    void day1() throws IOException {
        List<Integer> leftCol = new ArrayList<>();
        List<Integer> rightCol = new ArrayList<>();

        try (BufferedReader jdd = new BufferedReader(new FileReader(getJDDfile("jdd1.txt")))) {
            String line;
            while ((line = jdd.readLine()) != null) {
                String[] v = line.split("\\D+");
                leftCol.add(Integer.valueOf(v[0]));
                rightCol.add(Integer.valueOf(v[1]));
            }
        }

        List<Integer> leftCol_sorted = leftCol.stream().sorted().toList();
        List<Integer> rightCol_sorted = rightCol.stream().sorted().toList();

        System.out.println("part 1 : " + IntStream.range(0, min(leftCol.size(), rightCol.size())).map(i -> abs(rightCol_sorted.get(i) - leftCol_sorted.get(i))).reduce(0, Integer::sum));
        System.out.println("part 2 : " + leftCol.stream().map(i -> i * Collections.frequency(rightCol, i)).reduce(0, Integer::sum));

    }

    @Test
    void day2() throws IOException {

        int nbSafe = 0;
        int nbSafeT = 0;

        nbSafe = (int) Files.readAllLines(Paths.get(getJDDfile("jdd2.txt").toURI())).stream().map(r -> Arrays.stream(r.split(" ")).map(Integer::parseInt).toList()).filter(l -> l.size() == l.stream().distinct().count() && (l.equals(l.stream().sorted().toList()) || l.reversed().equals(l.stream().sorted().toList())) && IntStream.range(1, l.size()).allMatch(n -> abs(l.get(n) - l.get(n - 1)) >= 1 && abs(l.get(n) - l.get(n - 1)) <= 3)).count();

//        try (BufferedReader jdd = new BufferedReader(new FileReader(getJDDfile("jdd2.txt")))) {
//            String line;
//            while ((line = jdd.readLine()) != null) {
//                List<Integer> l = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
//                if (l.size() == l.stream().distinct().count()
//                        && (l.equals(l.stream().sorted().toList()) || l.reversed().equals(l.stream().sorted().toList()))
//                        && IntStream.range(1, l.size()).allMatch(n -> abs(l.get(n) - l.get(n-1)) >= 1 && abs(l.get(n) - l.get(n-1)) <= 3 )) {
//                    nbSafe += 1;
//                }
//            }
//        }

        List<List<Integer>> nbSafeTs = Files.readAllLines(Paths.get(getJDDfile("jdd2.txt").toURI())).stream().map(r -> Arrays.stream(r.split(" ")).map(Integer::parseInt).toList()).filter(l -> !(l.size() == l.stream().distinct().count() && (l.equals(l.stream().sorted().toList()) || l.reversed().equals(l.stream().sorted().toList())) && IntStream.range(1, l.size()).allMatch(n -> abs(l.get(n) - l.get(n - 1)) >= 1 && abs(l.get(n) - l.get(n - 1)) <= 3))).toList();

        for (List<Integer> u : nbSafeTs) {
            for (int i = 0; i < u.size(); i++) {
                List<Integer> t = new ArrayList<>(u);
                t.remove(i);
                if (t.size() == t.stream().distinct().count() && (t.equals(t.stream().sorted().toList()) || t.reversed().equals(t.stream().sorted().toList())) && IntStream.range(1, t.size()).allMatch(n -> abs(t.get(n) - t.get(n - 1)) >= 1 && abs(t.get(n) - t.get(n - 1)) <= 3)) {
                    nbSafeT += 1;
                    break;
                }
            }

        }

        System.out.println("part 1 : " + nbSafe);
        System.out.println("part 2 : " + (nbSafe + nbSafeT));

    }

    @Test
    void day3() throws IOException {
        String input = getJDDfile("jdd3.txt").toString();
        String content = Files.readString(Path.of(input));

//        content = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

        int r = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)").matcher(content).results().map(p -> p.group().substring(4).replace(")", "")).mapToInt(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).reduce(1, (a, b) -> a * b)).sum();

        String cleanedContent = content.replaceAll("don't\\(\\).*?do\\(\\)", "");

        String starts = Pattern.compile("^.*?don't\\(\\)").matcher(content).results().map(MatchResult::group).collect(Collectors.joining());

        int rs = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)").matcher(starts).results().map(p -> p.group().substring(4).replace(")", "")).mapToInt(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).reduce(1, (a, b) -> a * b)).sum();

        int rx = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)").matcher(cleanedContent).results().map(p -> p.group().substring(4).replace(")", "")).mapToInt(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).reduce(1, (a, b) -> a * b)).sum();


        System.out.println("part 1 : " + r);
        System.out.println("part 2 : " + (rs + rx));
    }

    @Test
    void day4() throws IOException {
        String[][] board = new String[140][140];
        int hr = 0;
        int hl = 0;
        int vd = 0;
        int vu = 0;
        int ddr = 0;
        int ddl = 0;
        int dur = 0;
        int dul = 0;
        int xmas = 0;


        try (BufferedReader jdd = new BufferedReader(new FileReader(getJDDfile("jdd4.txt")))) {
            String line;
            int row = 0;
            while ((line = jdd.readLine()) != null) {
                hr += (int) Pattern.compile("XMAS").matcher(line).results().count();
                hl += (int) Pattern.compile("SAMX").matcher(line).results().count();
                String[] split = line.split("");
                System.arraycopy(split, 0, board[row], 0, 140);
                row++;
            }
        }

        for (int i = 0; i < 140; i++) {
            for (int j = 0; j < 140; j++) {
                if ("X".equals(board[i][j])) {
                    if (j < 136 && "XMAS".equals(board[i][j] + board[i][j + 1] + board[i][j + 2] + board[i][j + 3])) {
                        vd++;
                    }
                    if (j > 3 && "XMAS".equals(board[i][j] + board[i][j - 1] + board[i][j - 2] + board[i][j - 3])) {
                        vu++;
                    }
                    if (i < 136 && j < 136 && "XMAS".equals(board[i][j] + board[i + 1][j + 1] + board[i + 2][j + 2] + board[i + 3][j + 3])) {
                        ddr++;
                    }
                    if (i > 3 && j < 136 && "XMAS".equals(board[i][j] + board[i - 1][j + 1] + board[i - 2][j + 2] + board[i - 3][j + 3])) {
                        ddl++;
                    }
                    if (i < 136 && j > 3 && "XMAS".equals(board[i][j] + board[i + 1][j - 1] + board[i + 2][j - 2] + board[i + 3][j - 3])) {
                        dur++;
                    }
                    if (i > 3 && j > 3 && "XMAS".equals(board[i][j] + board[i - 1][j - 1] + board[i - 2][j - 2] + board[i - 3][j - 3])) {
                        dul++;
                    }
                } else if (i > 0 && i < 139 && j > 0 && j < 139 && "A".equals(board[i][j])) {
                    if (("MS".equals(board[i - 1][j - 1] + board[i + 1][j + 1]) || "SM".equals(board[i - 1][j - 1] + board[i + 1][j + 1])) && ("MS".equals(board[i + 1][j - 1] + board[i - 1][j + 1]) || "SM".equals(board[i + 1][j - 1] + board[i - 1][j + 1]))) {
                        xmas++;
                    }
                }
            }
        }
        int total = hr + hl + vd + vu + ddr + ddl + dur + dul;
        System.out.println("part 1 : " + total);
        System.out.println("part 2 : " + xmas);
    }

    @Test
    void day5() throws IOException {
        Map<Integer, List<Integer>> rules = new HashMap<>();
        List<List<Integer>> manuals = new ArrayList<>();
        List<List<Integer>> invalidmanuals = new ArrayList<>();

        int sumMid = 0;
        int sumMidCor = 0;


        try (BufferedReader jdd = new BufferedReader(new FileReader(getJDDfile("jdd5.txt")))) {
            String line;
            while ((line = jdd.readLine()) != null) {
                if (line.contains("|")) {
                    List<Integer> r = Arrays.stream(line.split("\\|")).map(Integer::parseInt).toList();
                    if (rules.containsKey(r.getFirst())) {
                        List<Integer> t = rules.get(r.getFirst());
                        t.add(r.getLast());
                        rules.replace(r.getFirst(), t);
                    } else {
                        List<Integer> n = new ArrayList<>();
                        n.add(r.getLast());
                        rules.put(r.getFirst(), n);
                    }
                } else if (line.contains(",")) {
                    List<Integer> manual = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                    manuals.add(manual);
                    boolean validManual = true;
                    while (validManual) {
                        for (int i = 1; i < manual.size(); i++) {
                            List<Integer> prevp = manual.subList(0, i);
                            if (rules.containsKey(manual.get(i))) {
                                if (rules.get(manual.get(i)).stream().anyMatch(prevp::contains)) {
                                    validManual = false;
                                    invalidmanuals.add(manual);
                                    break;
                                }
                            }
                        }
                        if (validManual) {
                            sumMid += manual.get((manual.size() - 1) / 2);
                        }
                        validManual = false;
                    }

                }
            }
        }

        for (List<Integer> manual : invalidmanuals) {
            Map<Integer, Integer> manualMap = new HashMap<>();
            manual.forEach(p -> manualMap.put(p, 0));

            for (Integer p : manual) {
                if (rules.containsKey(p)) {
                    List<Integer> currentrule = rules.get(p);
                    manualMap.entrySet().stream().filter(e -> currentrule.contains(e.getKey())).forEach(e -> manualMap.put(e.getKey(), e.getValue() + 1));
                }
            }
            List<Integer> correct = manualMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();

            sumMidCor += correct.get((manual.size() - 1) / 2);
        }

        System.out.println("part 1 : " + sumMid);
        System.out.println("part 2 : " + sumMidCor);
    }

    @Test
    void day6() throws IOException {

        List<String> input = Files.readAllLines(Path.of(getJDDfile("jdd6.txt").toString()));
        int nbCol = input.getFirst().length();
        int nbRow = input.size();

        String[][] board = new String[nbRow][nbCol];
        String[][] board2 = new String[nbRow][nbCol];
        Integer[] guard = new Integer[2];
        Integer[] guard2 = new Integer[2];

        for (int row = 0; row < nbRow; row++) {
            String line = input.get(row);
            int finalRow = row;
            IntStream.range(0, nbCol).forEach(i -> board[finalRow][i] = String.valueOf(line.charAt(i)));
            IntStream.range(0, nbCol).forEach(i -> board2[finalRow][i] = String.valueOf(line.charAt(i)));
            if (line.contains("^")) {
                guard[0] = row;
                guard[1] = line.indexOf("^");
                guard2[0] = row;
                guard2[1] = line.indexOf("^");
                board2[guard2[0]][guard2[1]] = "u";
            }
        }

        while (guard[0] > 0 && guard[0] < (nbRow - 1) && guard[1] > 0 && guard[0] < (nbCol - 1)) {
            if ("^".equals(board[guard[0]][guard[1]])) {
                if ("#".equals(board[guard[0] - 1][guard[1]])) {
                    board[guard[0]][guard[1]] = ">";
                } else {
                    board[guard[0]][guard[1]] = "X";
                    guard[0] -= 1;
                    board[guard[0]][guard[1]] = "^";
                }
            } else if (">".equals(board[guard[0]][guard[1]])) {
                if ("#".equals(board[guard[0]][guard[1] + 1])) {
                    board[guard[0]][guard[1]] = "v";
                } else {
                    board[guard[0]][guard[1]] = "X";
                    guard[1] += 1;
                    board[guard[0]][guard[1]] = ">";
                }
            } else if ("v".equals(board[guard[0]][guard[1]])) {
                if ("#".equals(board[guard[0] + 1][guard[1]])) {
                    board[guard[0]][guard[1]] = "<";
                } else {
                    board[guard[0]][guard[1]] = "X";
                    guard[0] += 1;
                    board[guard[0]][guard[1]] = "v";
                }
            } else if ("<".equals(board[guard[0]][guard[1]])) {
                if ("#".equals(board[guard[0]][guard[1] - 1])) {
                    board[guard[0]][guard[1]] = "^";
                } else {
                    board[guard[0]][guard[1]] = "X";
                    guard[1] -= 1;
                    board[guard[0]][guard[1]] = "<";
                }
            }
        }

        int gloop = 0;
        Set<String> glooploc = new HashSet<>();

        while (guard2[0] > 0 && guard2[0] < (nbRow - 1) && guard2[1] > 0 && guard2[0] < (nbCol - 1)) {
            if (board2[guard2[0]][guard2[1]].endsWith("u") ) {
                if ("#".equals(board2[guard2[0]-1][guard2[1]])) {
                    board2[guard2[0]][guard2[1]] += "r";
                } else {
                    if (board2[guard2[0]][guard2[1]+1].contains("r") || IntStream.range((guard2[1]+1), nbCol-1).anyMatch(i -> "#".equals(board2[guard2[0]][i+1]) && board2[guard2[0]+1][i].contains("d"))) {
                        gloop += 1;
                        glooploc.add((guard2[0]-1) + "," + guard2[1]);
                    }
                    board2[guard2[0]][guard2[1]] += "u";
                    guard2[0] -= 1;
                    board2[guard2[0]][guard2[1]] += "u";
                }
            } else if (board2[guard2[0]][guard2[1]].endsWith("r")) {
                if ("#".equals(board2[guard2[0]][guard2[1] + 1])) {
                    board2[guard2[0]][guard2[1]] += "d";
                } else {
                    if (board2[guard2[0] + 1][guard2[1]].contains("d") || IntStream.range((guard2[0]+1), nbRow-1).anyMatch(i -> "#".equals(board2[i+1][guard2[1]]) && board2[i][guard2[1]-1].contains("l"))) {
                        gloop += 1;
                        glooploc.add((guard2[0]) + "," + (guard2[1]+1));
                    }
                    board2[guard2[0]][guard2[1]] += "r";
                    guard2[1] += 1;
                    board2[guard2[0]][guard2[1]] += "r";
                }
            } else if (board2[guard2[0]][guard2[1]].endsWith("d")) {
                if ("#".equals(board2[guard2[0] + 1][guard2[1]])) {
                    board2[guard2[0]][guard2[1]] += "l";
                } else {
                    if (board2[guard2[0]][guard2[1] - 1].contains("l") || IntStream.range((guard2[1]-1), 0).anyMatch(i -> "#".equals(board2[guard2[0]][i-1]) && board2[guard2[0]-1][i].contains("u"))) {
                        gloop += 1;
                        glooploc.add((guard2[0]+1) + "," + (guard2[1]));
                    }
                    board2[guard2[0]][guard2[1]] += "d";
                    guard2[0] += 1;
                    board2[guard2[0]][guard2[1]] += "d";
                }
            } else if (board2[guard2[0]][guard2[1]].endsWith("l")) {
                if ("#".equals(board2[guard2[0]][guard2[1] - 1])) {
                    board2[guard2[0]][guard2[1]] += "u";
                } else {
                    if (board2[guard2[0]][guard2[1] - 1].contains("u") || IntStream.range((guard2[0]-1), 0).anyMatch(i -> "#".equals(board2[i-1][guard2[1]]) && board2[i][guard2[1]+1].contains("r"))) {
                        gloop += 1;
                        glooploc.add((guard2[0]) + "," + (guard2[1]-1));
                    }
                    board2[guard2[0]][guard2[1]] += "l";
                    guard2[1] -= 1;
                    board2[guard2[0]][guard2[1]] += "l";
                }
            }
        }

//        Integer x = toIntExact(Arrays.stream(board).flatMap(r -> Arrays.stream(r).filter("X"::equals)).count()) + 1;

        System.out.println("part 1 : " + (Arrays.stream(board).flatMap(r -> Arrays.stream(r).filter("X"::equals)).count() + 1));
        System.out.println("part 2 : " + gloop + " ou " + glooploc.size());
    }

    @Test
    void day7() throws IOException {
        List<String> input = Files.readAllLines(Path.of(getJDDfile("jdd7.txt").toString()));
        Map<Integer, List<List<String>>> operatorsNCombinaisons = new HashMap<>();
        Map<Integer, List<List<String>>> operatorsN2Combinaisons = new HashMap<>();
        String[] operatorsSign = new String[2];
        operatorsSign[0] = "+";
        operatorsSign[1] = "*";

        String[] operatorsSign2 = new String[3];
        operatorsSign2[0] = "+";
        operatorsSign2[1] = "*";
        operatorsSign2[2] = "||";

        long nbTrue = 0;
        long nbTrue2 = 0;

        for (String line: input) {
            String[] temp = line.split(": ");
            long expected = Long.parseLong(temp[0]);
            List<Long> terms = Arrays.stream(temp[1].split(" ")).map(Long::parseLong).toList();
            int nbTerms = terms.size();
            if (!operatorsNCombinaisons.containsKey(nbTerms-1)) {
                operatorsNCombinaisons.put(nbTerms-1, allCombinaisons(nbTerms-1, operatorsSign));
            }
            if (!operatorsN2Combinaisons.containsKey(nbTerms-1)) {
                operatorsN2Combinaisons.put(nbTerms-1, allCombinaisons(nbTerms-1, operatorsSign2));
            }
            List<List<String>> operatorsN = operatorsNCombinaisons.get(nbTerms-1);
            for (List<String> operators : operatorsN) {
                long calc = LongStream.range(0, nbTerms-1).reduce(terms.getFirst(), (total, i) -> "+".equals(operators.get((int) i)) ? terms.get(((int)i+1)) + total : total * terms.get(((int)i+1)));
                if (expected == calc) {
                    nbTrue += expected;
                    break;
                }
            }
            List<List<String>> operatorsN2 = operatorsN2Combinaisons.get(nbTerms-1);
            for (List<String> operators : operatorsN2) {
                long calc = LongStream.range(0, nbTerms-1).reduce(terms.getFirst(), (total, i) -> "||".equals(operators.get((int) i)) ? total * (long) pow(10,String.valueOf(terms.get(((int)i+1))).length()) +  terms.get(((int)i+1)) : ("+".equals(operators.get((int) i)) ? terms.get(((int)i+1)) + total : total * terms.get(((int)i+1))));
                if (expected == calc) {
                    nbTrue2 += expected;
                    break;
                }
            }
        }


        System.out.println("part 1 : " + nbTrue);
        System.out.println("part 2 : " + nbTrue2);
    }

    @Test
    void day8() throws IOException {
        Set<String> antinodes = new HashSet<>();
        Set<String> antinodesWT = new HashSet<>();
        Map<String, List<String>> locationsAntennas = new HashMap<>();

        List<String> input = Files.readAllLines(Path.of(getJDDfile("jdd8.txt").toString()));
        int xmax = input.getFirst().length();
        int ymax = input.size();

        for (int i = 0; i < ymax; i++) {
            String[] currentLineArray = input.get(i).split("");
            for (int j = 0; j < xmax; j++) {
                if (!".".equals(currentLineArray[j])) {
                    String loc = j + "," + i;
                    if (locationsAntennas.containsKey(currentLineArray[j])) {
                        locationsAntennas.get(currentLineArray[j]).add(loc);
                    } else {
                        List<String> temp = new ArrayList<>();
                        temp.add(loc);
                        locationsAntennas.put(currentLineArray[j], temp);
                    }
                }
            }
        }

        for (Map.Entry<String, List<String>> antennas : locationsAntennas.entrySet() ) {

            List<List<String>> antennasPairs = allCombinaisonsWithoutReplacement(2, antennas.getValue());
            for (List<String> apair : antennasPairs) {
                int[] node1 = Arrays.stream(apair.getFirst().split(",")).mapToInt(Integer::parseInt).toArray();
                int[] node2 = Arrays.stream(apair.getLast().split(",")).mapToInt(Integer::parseInt).toArray();
                int dx12 = node1[0]-node2[0];
                int dy12 = node1[1]-node2[1];

                int r = 0;
                while (node1[0] + (r * dx12) < xmax && node1[0] + (r * dx12) > -1 && node1[1] + (r * dy12) < ymax && node1[1] + (r * dy12) > -1) {
//                    if (node1[0] + dx12 < xmax && node1[0] + dx12 > -1 && node1[1] + dy12 < ymax && node1[1] + dy12 > -1) {
                    if (r == 1) {
                        antinodes.add((node1[0] + (r * dx12)) + "," + (node1[1] + (r * dy12)));
                    }
                    antinodesWT.add((node1[0] + (r * dx12)) + "," + (node1[1] + (r * dy12)));
//                    }
                    r++;
                }
                r = 0;
                while (node2[0] - (r * dx12) < xmax && node2[0] - (r * dx12) > -1 && node2[1] - (r * dy12) < ymax && node2[1] - (r * dy12) > -1) {
                    //                if (node2[0] - dx12 < xmax && node2[0] - dx12 > -1 && node2[1] - dy12 < ymax && node2[1] - dy12 > -1) {
                    if (r == 1) {
                        antinodes.add((node2[0] - (r * dx12)) + "," + (node2[1] - (r * dy12)));
                    }
                    antinodesWT.add((node2[0] - (r * dx12)) + "," + (node2[1] - (r * dy12)));
//                }
                    r++;
                }
            }
//            antinodesWT.addAll(antinodes);
//
//            List<List<String>> antennasTriplets = allCombinaisonsWithoutReplacement(3, antennas.getValue());
//            for (List<String> atriplet : antennasTriplets) {
//                int[] node1 = Arrays.stream(atriplet.getFirst().split(",")).mapToInt(Integer::parseInt).toArray();
//                int[] node2 = Arrays.stream(atriplet.get(1).split(",")).mapToInt(Integer::parseInt).toArray();
//                int[] node3 = Arrays.stream(atriplet.getLast().split(",")).mapToInt(Integer::parseInt).toArray();
//                int dx12 = node1[0]-node2[0];
//                int dy12 = node1[1]-node2[1];
//                int dx13 = node1[0]-node3[0];
//                int dy13 = node1[1]-node3[1];
//                int dx23 = node2[0]-node3[0];
//                int dy23 = node2[1]-node3[1];
//                if
//
//                if (node1[0] + dx12 < xmax && node1[0] + dx12 > -1 && node1[1] + dy12 < ymax && node1[1] + dy12 > -1) {
//                    antinodes.add((node1[0] + dx12) + "," + (node1[1] + dy12));
//                }
//                if (node2[0] - dx12 < xmax && node2[0] - dx12 > -1 && node2[1] - dy12 < ymax && node2[1] - dy12 > -1) {
//                    antinodes.add((node2[0] - dx12) + "," + (node2[1] - dy12));
//                }
//            }
        }

//        System.out.println(antinodes);

        System.out.println("part 1 : " + antinodes.size());
        System.out.println("part 2 : " + antinodesWT.size());
    }

    @Test
    void day9() throws IOException {
        String input = Files.readString(Path.of(getJDDfile("jdd9.txt").toString()));

        String fileBlockNbs = "";
        String spaceBlockNbs = "";

        Deque<Long> fileBlocks = new ArrayDeque<>();
        Long fileName = 0L ;
        for (int i = 0; i < input.length(); i++) {
            if ( i % 2 == 0) {
                fileBlockNbs += input.charAt(i);
                for (int j = 0; j < Integer.parseInt(String.valueOf(input.charAt(i))) ; j++) {
                    fileBlocks.add(fileName);
                }
                fileName++;
            } else {
                spaceBlockNbs += input.charAt(i);
            }
        }

        Long checksum = 0L;
        int cursor = 0;
        Long rank = 0L;
        while (!fileBlocks.isEmpty()) {
            int fileBlockNbsCursor = Integer.parseInt(String.valueOf(fileBlockNbs.charAt(cursor)));
            for (int i = 0 ; i < fileBlockNbsCursor ; i++) {
                checksum +=  rank * fileBlocks.removeFirst();
                rank++;
                if (fileBlocks.isEmpty()) break;
            }
            if (fileBlocks.isEmpty()) break;
            int spaceBlockNbsCursor = Integer.parseInt(String.valueOf(spaceBlockNbs.charAt(cursor)));
            for (int i = 0; i < spaceBlockNbsCursor; i++) {
                checksum += rank * fileBlocks.removeLast();
                rank++;
                if (fileBlocks.isEmpty()) break;
            }
            cursor++;
        }
////////////////////////////////////////////////

        List<Integer> datas = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).boxed().toList();

        Map<Integer, List<Integer>> fileIdsBlockpositions = new HashMap<>();

        List<Integer> occupedBlock = new ArrayList<>();
        List<Integer> emptySpace = new ArrayList<>();

        int fileId = 0;
        cursor = 0;
        for (int i = 0; i < datas.size(); i++) {
            occupedBlock.add(datas.get(i));
            fileIdsBlockpositions.put(fileId, IntStream.range(cursor, cursor+datas.get(i)).boxed().toList());
            cursor += datas.get(i);
            i++;
            if (i == datas.size()) {
                emptySpace.add(0);
                break;
            }
            emptySpace.add(datas.get(i));
            cursor += datas.get(i);
            fileId++;
        }
        int nbFile = occupedBlock.size();
        List<Integer> fileIds = IntStream.range(0, nbFile).boxed().toList();
        Integer[] occupedBlocksSize = occupedBlock.toArray(new Integer[0]);
        Integer[] spaceBlocksSize = emptySpace.toArray(new Integer[0]);

        for (int i = (nbFile - 1); i >= 0; i--) {
            int currentFile = fileIds.get(i);
            int nbBlockToMove = occupedBlock.get(i);
            for (int j = 0; j < i; j++) {
                if (spaceBlocksSize[j] >= nbBlockToMove) {
                    int start = Arrays.stream(occupedBlocksSize).limit(j+1).mapToInt(value -> value).sum() + Arrays.stream(spaceBlocksSize).limit(j).mapToInt(value -> value).sum();
                    occupedBlocksSize[j] += nbBlockToMove;
                    spaceBlocksSize[j] -= nbBlockToMove;
                    occupedBlocksSize[i] = 0;
                    spaceBlocksSize[i-1] += (nbBlockToMove + spaceBlocksSize[i]);
                    spaceBlocksSize[i] = 0;
                    List<Integer> newPosition = IntStream.range(start, start+nbBlockToMove).boxed().toList();
                    fileIdsBlockpositions.replace(currentFile, newPosition);
                    break;
                }
            }
        }

        System.out.println("part 1 : " + checksum);
        System.out.println("part 2 : " + fileIdsBlockpositions.entrySet().stream().mapToLong(e -> (long) e.getKey() * e.getValue().stream().mapToLong(Long::valueOf).sum()).sum());
    }

    @Test
    void day10() throws IOException {



        System.out.println("part 1 : ");
        System.out.println("part 2 : ");
    }

    private File getJDDfile(String JDDname) {
        return new File(getClass().getClassLoader().getResource(JDDname).getFile());
    }

    private <T> List<List<T>> allCombinaisons(int n, T[] values) {
        int nbValues = values.length;
        return IntStream.range(0, (int) pow(nbValues, n))
                .mapToObj(i -> IntStream.range(0, n)
                        .mapToObj(j -> values[(i/ (int) pow(nbValues, j)) % nbValues])
                        .toList())
                .toList();
    }

    private <T> List<List<T>> allCombinaisonsWithoutReplacement(int n, List<T> values) {
        return IntStream.range(0, 1 << values.size())
                .filter(i -> Integer.bitCount(i) == n)
                .mapToObj(i -> IntStream.range(0, values.size())
                        .filter(j -> (i & (1 << j)) != 0)
                        .mapToObj(values::get).toList())
                .toList();
    }

    @Test
    void day0() throws IOException {

        System.out.println("part 1 : ");
        System.out.println("part 2 : ");
    }
}
