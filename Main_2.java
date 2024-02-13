package Main;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args)
    {
        // Initiale Stream-Erzeugung: Datenquelle bereitstellen
        // (endlich/limitiert oder theoretisch unendlich)
        {
            Random R = new Random();
            List<Integer> L = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

            Stream<Integer>
                // Stream aus vorhandener Datenquelle
                St1 = L.stream(),

                // Stream aus Direktwerten als Daten
                St2 = Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),

                // Generierter Stream aus Supplier
                // (hier Lambda mit Referenz auf Instanz statt Klasse)
                // per R.ints() auch als IntStream statt Stream<Integer>
                St3 = Stream.generate(R::nextInt).limit(10),

                // Generierter Stream aus funktionaler Schleife
                // (Lambdas, die 'for'-Schleife nachbilden)
                St4 = Stream.iterate(0, I -> I < 10, J -> J+1),

                // Konkatenierte Streams
                St5 = Stream.concat(Stream.of(0, 1, 2, 3, 4),
                                    Stream.of(5, 6, 7, 8, 9)),

                // Stream-Aufbau (auch explizit per eigenem Builder)
                St6 = Stream.<Integer>builder()
                    .add(0).add(1).add(2).add(3).add(4)
                    .add(5).add(6).add(7).add(8).add(9)
                    .build(),

                // Stream-Erzeugung aus Bereich von Werten
                St7 = IntStream.range(0, 10).boxed(),   // nach Stream<Integer>
                St8 = IntStream.rangeClosed(0, 9).boxed();

            // Terminale Operation 'count' oder 'toList' schließt Stream!
            /*System.out.println(St1.count()+", "+St2.count()+", "+
                               St3.count()+", "+St4.count()+", "+
                               St5.count()+", "+St6.count());*/
            //System.out.println(St7.toList()+", "+St8.toList());
            // Streams sind jetzt 'konsumiert' und können nicht
            // nochmals benutzt werden!
            
            // Fragen:
            // 1) Erzeugung eines Streams aus String?
            // 2) Was passiert bei St3, wenn kein Limit gesetzt wird?
            // 3) Was passiert, wenn St5 aus St1 und St2 gebildet würde?
            // 4) Wann/Wofür macht Stream-Builder Sinn?
            // 5) Was passiert mit der Datenquelle? Sind die Daten weg?

            // Aufgaben:
            // 1a) Stream von engl. Alphabet-Zeichen 'A' bis 'Z' in Reihenfolge
            //  b) Stream von N Zufallszeichen zw. 'A' und 'Z' (Unicode 65-90)
            // 2a) Stream von Strings {"to", "do", "or", "not", "to", "do"}
            //  b) Stream von 2a) plus {"-", "that", "is", "the", "decision"}
            // 3a) Stream von 9 Doubles zw. 0.0 und 1.0 in 0.125er-Schritten
            //  b) Stream von Booleans aus 32 Bits eines Integer-Werts
            // 4a) Stream<Integer> aus IntStream
            //  b) String in CodeStream und dann Stream<Integer>
        }

        // Intermediäre/Zentrale Stream-Verarbeitung 1:
        // Daten transformieren durch Abbilden und Filtern
        {
            List<Integer> L = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            List<Character> LL = List.of('A', '0', 'B', '1', 'C', '2');

            List<Integer>
                // Zahlen quadrieren
                L1 = L.stream().map(X -> X * X).toList(),

                // Alle Zahlen doppeln
                L2 = L.stream().flatMap(X -> Stream.of(X, X)).toList(),

                // Gerade Zahlen herausfiltern
                L3 = L.stream().filter(X -> (X & 1) == 0).toList(),

                // Quadratzahlen herausfiltern
                L4 = L.stream()
                      .filter(X -> { double Y = Math.sqrt(X); return Y == (int)Y; })
                      .toList();

            List<Character>
                // Zahlen/Ziffern als Zeichen
                L5 = L.stream().map(X -> (char) (X + '0')).toList(),

                // Buchstaben gefiltert
                L6 = LL.stream().filter(Character::isLetter).toList();

            double[]
                // Integer-Zahlen als Double
                A1 = L.stream().mapToDouble(X -> (double) X).toArray(),

                // Gerade Integers entfernen und als Double
                A2 = L.stream().flatMapToDouble(
                    X -> (X % 2 == 0) ? DoubleStream.empty() : DoubleStream.of(X)).toArray();

            /*System.out.println("Quadrierung  : "+L1);
            System.out.println("Doppelung    : "+L2);
            System.out.println("Gerade Zahlen: "+L3);
            System.out.println("Quadratzahlen: "+L4);
            System.out.println();
            System.out.println("Ziffern als Zeichen : "+L5);
            System.out.println("Filterung Buchstaben: "+L6);
            System.out.println();
            System.out.println("Integer als Double: "+Arrays.toString(A1));
            System.out.println("Ungerade Doubles  : "+Arrays.toString(A2));
            System.out.println();*/

            // Aufgaben zu 'map'/'filter':
            // 1) Transformieren von 10 Integer-Zufallszahlen in ihre
            //    Hexadezimaldarstellung (als Liste)
            // 2) Alle 'bad/good words' in Liste<String> ausmarkieren und
            //    stattdessen Standardtext ausgeben ("fuck" => ":-(" etc.,
            //    evtl. zusätzlich "luck" => ":-)" etc.)
            //    Hinweis: 'bad/good words' selbst in Set<String> speichern
            // 3) Leerstrings aus Liste von Strings entfernen
            // 4) Filtern aller ein- und zweistelligen Schnapszahlen zw. 1 und 100
        }
        // Intermediäre/Zentrale Stream-Verarbeitung 2:
        // Daten transformieren durch (Aus-)Sortieren und Überspringen
        {
            List<Integer> L = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            List<Integer> LL = new ArrayList<>();

            List<Integer>
                // Sortierung, Aussortierung Doppelter
                L1 = L.stream().sorted(Comparator.reverseOrder()).toList(),
                L2 = Stream.of(1, 2, 3, 2, 3, 3).distinct().toList(),

                // Solange verwenden/verwerfen, bis/wie Bedingung erfüllt
                L3 = L.stream().takeWhile(X -> X <= 4)/*.map(X -> X+X)*/.toList(),
                L4 = L.stream().dropWhile(X -> X <= 4)/*.map(X -> X+X)*/.toList(),

                // Ausspähen (ohne Änderung des Streams), Überspringen
                L5 = L.stream().peek(LL::add).toList(),     // X -> LL.add(X)
                L6 = L.stream().skip(5).toList();

            /*System.out.println("Sortierung: "+L1);
            System.out.println("Distinktion: "+L2);
            System.out.println("Bearbeitung take: "+L3);
            System.out.println("Bearbeitung drop: "+L4);
            System.out.println("Herauspicken: "+L5+" / "+LL);
            System.out.println("Überspringen: "+L6);
            System.out.println();*/
        }

        // Terminale/Finale Stream-Reduktion: Daten zusammenfassen
        {
            List<Integer> L = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

            // Finden/Matchen
            {
                Optional<Integer>
                    O1 = L.stream().findFirst(),    // eigentlich returnFirst/Any
                    O2 = L.stream().findAny(),
                    O3 = L.stream().min(Integer::compare),
                    O4 = L.stream().max(Integer::compare);
                //System.out.println(O1 + ", " + O2);
                //System.out.println(O3 + ", " + O4);

                boolean
                    B1 = L.stream().allMatch(X -> X < 10),
                    B2 = L.stream().anyMatch(X -> X % 5 == 0),
                    B3 = L.stream().noneMatch(X -> X + X == X);
                //System.out.println(B1 + ", " + B2 + ", " + B3);

                //System.out.println();

                /*Random R = new Random();
                IntStream St = R.ints(1_000_000);   // oder 1_000_000_000
                OptionalInt M = St.max();
                //OptionalInt M = St.parallel().max();*/
                //System.out.println(M);
            }

            // Reduzieren/Aufsammeln
            {
                // Reduce
                int Sum = L.stream().reduce(0, Integer::sum);   // (Z, X) -> Z+X
                Optional<Integer> Summy = L.stream().reduce(Integer::sum);
                //System.out.println("Summen: "+Sum + ", " + Summy);

                String R = L.stream().reduce("", (S, X) -> S + X, String::concat);
                //System.out.println("Stringbildung: "+R);    // könnte parallelisiert werden

                // Collect
                Set<Integer> Zet1 = L.stream().collect
                    (HashSet::new, (Z, X) -> Z.add(X / 2), HashSet::addAll);
                Set<Integer> Zet2 = L.stream().collect(Collectors.toUnmodifiableSet());
                //System.out.println("Mengenbildung: "+Zet1 + ", " + Zet2);

                Double Avrg = L.stream().collect(Collectors.averagingDouble(X -> (double) X));
                //System.out.println("Durchschnitt: "+Avrg);

                String T = L.stream().map(X -> "" + X)
                    .collect(Collectors.joining(", ", "[", "]"));
                //System.out.println("String-Verknüpfung: "+T);

                //System.out.println();
            }

            // Aufgaben (intermediäre Operationen sind erlaubt):
            // 1) Summe der Werte von 1 bis N (nicht per Gauss-Funktion)
            // 2) Produkt von obiger Liste (ohne 0) bzw. N! berechnen (0 => 1)
            // 3) Quersumme eines Strings berechnen (z.B. "122333" = 14)
            // 4) Alle Ziffern der Liste L in StringBuilder aufsammeln zu "0123456789"
    
            // Beispiel für 'interfering function'
            {
                /*List<Integer>
                    LL = new ArrayList<>(L);
                List<Integer>
                    LLL = LL.stream().reduce(LL,        // interfering!
                                             (Li, X) -> { Li.add(X); return Li; },
                                             (L1, L2) -> { L1.addAll(L2); return L1; });
                System.out.println(LL+" / "+LLL);*/
            }
        }
    }
}
