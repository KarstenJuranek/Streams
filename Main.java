package Main;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class Main {
        @FunctionalInterface
        interface Similator<T> {
                double similate(T O1, T O2);
        }

        public static void main(String[] args) {
                // 'A' == 'A': 1.0
                // 'A' == 'a': 0.5
                // 'A' == 'B': 0.0
                // Aufgabe Similator
                /*{
                    Similator<Character> Siml =
                    (C1, C2) -> C1 == C2 ? 1.0 :
                                (Character.toUpperCase(C1) ==
                                 Character.toUpperCase(C2) ? 0.5 : 0.0);
                    System.out.println(Siml.similate('A', 'A'));
                    System.out.println(Siml.similate('A', 'a'));
                    System.out.println(Siml.similate('A', 'B'));
                
                * Alternativ auch mit einer BiFunction lösbar
                BiFunction<Character, Character, Double> Similator = (C1, C2) -> 
                    C1 == C2 ? 1.0 : (Character.toUpperCase(C1) == Character.toUpperCase(C2) ? 0.5 : 0.0);
                }*/

                // Initiale Stream-Erzeugung: Datenquelle bereitstellen
                // (endlich/limitiert oder theoretisch unendlich)
                {
                        Random R = new Random();
                        List<Integer> L = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

                        /*
                        Liste/Map/Set.stream wird verwendet um eine Abbildung dieser zu erstellen
                        
                        Stream.of wird verwendet um einen leeren Stream mit Daten zu füllen
                        
                        Stream.generate akkzeptiert Supplier, es sollte ein limit angegeben werden, da dies
                        sonst den Memory crashen könnte, da infinite Werte generiert werden.
                        
                        Stream.iterate ist ebenfalls eine Methode einen infinite Stream zu erstellen, dieser
                        sollte auch festgelegt werden. An erster stelle der for Schleife steht der seed, bei dem
                        die Funktion beginnen soll. An zweiter Stelle das Predicate, solange das true ist wird die 
                        Funktion ausgeführt. An dritter Stelle steht die Funktion, mithilfe dieser werden die
                        Streamwerte genergiert.
                        
                        Stream.concat wird verwendet um zwei Streams zu einen gemeinsamen Stream zu verbinden
                        
                        Der Stream.builder wird verwendet, wenn nicht alle Elemente gleichzeitig verfügbar sind
                        oder erst Schrittweise eingefügt werden. addAll(Funktion) / add(einzelne Werte)
                        Er kann keine Werte ausgeben, er dient lediglich zum generieren von Streams
                         */

                        Stream<Integer> Stream1 = L.stream(),
                                        Stream2 = Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                                        // Supplier (R)
                                        Stream3 = Stream.generate(() -> R.nextInt()).limit(10),
                                        Stream4 = Stream.iterate(0, I -> I < 10, J -> J + 1),
                                        Stream5 = Stream.concat(Stream.of(0, 1, 2, 3, 4),
                                                        Stream.of(5, 6, 7, 8, 9)),
                                        Stream6 = Stream.<Integer>builder()
                                                        .add(0).add(1).add(2).add(3).add(4)
                                                        .add(5).add(6).add(7).add(8).add(9)
                                                        .build();
                        // Stream7 = Stream.concat(Stream1, Stream2);

                        // Terminale Operation 'count' schließt Stream! Sie können nichtmehr abgerufen werden
                        // mit .count wird die Anzahl aller Stream Elemente gezählt

                        System.out.println("Diversifications of Streams:\t" + Stream1.count() + ", \t" + Stream2.count()
                                        + ", \t" +
                                        Stream3.count() + ", \t" + Stream4.count() + ", \t" +
                                        Stream5.count() + ", \t" + Stream6.count()
                                        + /*Stream7.count() + ", \t" +*/"\n");

                        // Fragen:
                        // 1) Erzeugung eines Streams aus String?
                        List<String> StringList = new ArrayList<>(List.of("World", "!"));
                        //Möglichkeiten:
                        Stream<String> StringStream1 = StringList.stream(),
                                        StringStream2 = Stream.of("Hello", " "),
                                        StringStream3 = Stream.concat(StringStream2, StringStream1);

                        // Abspeichern sämtlicher Inhalte des Streams mithilfe der Collections
                        // List<String> StreamOutput1 = StringStream3.collect(Collectors.toList());
                        // System.out.println("Safed in Collections\t"+StreamOutput1);

                        // Abspeichern des gesamtenStrings mithilfe von joining Mithilfe einer Variablen
                        String combinedString = StringStream3.collect(Collectors.joining());
                        List<String> StringOutput2 = new ArrayList<>();
                        StringOutput2.add(combinedString);
                        System.out.println("Whole String:\t" + StringOutput2 + "\n");

                        // Direkte Ausgabe in der Konsole des Streams
                        // System.out.print("StringStream3:\t");
                        // StringStream3.forEach(System.out::print);                

                        // 2) Was passiert bei Stream3, wenn kein Limit gesetzt wird?
                        //      Es werden unendlich viele Werte generiert, die den Speicher zum crashen bringen

                        // 3) Was passiert, wenn Stream5 aus Stream1 und Stream2 gebildet?
                        //      Es werden beide Streams zusammengeführt, somit steigt der count auf 20

                        // 4) Wann/Wofür macht Stream-Builder Sinn?
                        //      Der Stream.builder macht Sinn, wenn nicht alle Werte gleichzeitig vorhanden sind,
                        //      oder nachträglich ergänzt werden

                        // 5) Was passiert mit der Datenquelle? Sind die Daten weg?
                        //      In einen Stream wird nur ein Abbild der Daten erstellt, welche mithilfe der Stream-
                        //      Funktionen bearbeitet werden können. Die ursprünglichen Daten werden beibehalten.
                        //      Nach der einmaligen Ausgabe eines Streams ist dieser nichtmehr verfügbar.

                        // 6) Was passiert, wenn L im Stream mit Daten beschrieben wird?
                        //      Änderungen an der Liste während des Streams sind nicht erlaubt und führen zu
                        //      einer ConcurrentModificationException.

                        // Aufgaben:
                        // 1a) Stream von engl. Alphabet-Zeichen 'A' bis 'Z' in Reihenfolge
                        Stream<String> alphaStream1 = Stream.<String>builder().add("A").add("B").build(),
                                        alphaStream2 = Stream.of("A", "B", "...");

                        System.out.print("Möglichkeit 1:\t");
                        alphaStream1.forEach(System.out::print);
                        System.out.print("\n\nMöglichkeit 2:\t");
                        alphaStream2.forEach(System.out::print);
                        System.out.print("\n\n" + "Möglichkeit 3:\t");

                        // Alternative Möglichkeit mit Stream.generate und der range Methode
                        // +1 damit auch der Buchstabe Z generiert wird
                        // Da jeder ASCII Wert eine Ganzzahl repräsentiert können die Werte zwischen 
                        // Start und Endpunkt wiedergegeben werden.
                        IntStream alphaStream3 = IntStream.range('A', 'Z' + 1);
                        alphaStream3.mapToObj(X -> (char) X).forEach(System.out::print);
                        System.out.println();

                        //  b) Stream von N Zufallszeichen zw. 'A' und 'Z' (Unicode 65-90)
                        // Erstellen der Random Zahlen
                        Random RZ = new Random();
                        int N = 10;

                        String randomChars = RZ
                                        // Size,Origin,Bound Festlegen des Bereichs
                                        .ints(N, 65, 91)
                                        // Zahlen werden in Buchstaben umgewandelt
                                        .mapToObj(i -> (char) i)
                                        .map(Object::toString)
                                        .collect(Collectors.joining(", "));

                        // Alternative Ausgabe, wenn die Werte ohne Trennung wiedergegeben werden sollen.
                        /* Sammeln der Zufallszahlen in einem Stringbuilder --> nach mapToObj
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();*/

                        System.out.println("\nZufallszeichen des Alphabets: \t" + randomChars + "\n");

                        // 2a) Stream von Strings {"to", "do", "or", "not", "to", "do"}
                        List<String> StringList2 = new ArrayList<String>(List.of("to", "do", "or", "not", "to", "do"));
                        String StringStream4 = StringList2.stream().collect(Collectors.joining(" "));
                        System.out.println("Ausgabe eines Strings:\t" + StringStream4 + "\n");

                        //  b) Stream von 2a) plus {"-", "that", "is", "the", "decision"}
                        List<String> StringList3 = new ArrayList<String>(List.of("-", "that", "is", "the", "decision"));
                        String StringStream5 = StringList3.stream().collect(Collectors.joining(" "));
                        String StringStream6 = Stream.of(StringStream4, StringStream5).collect(Collectors.joining(" "));
                        System.out.println("Beide Streams zusammen:\t" + StringStream6 + "\n");

                        // 3a) Stream von 9 Doubles zw. 0.0 und 1.0 in 0.125er-Schritten
                        DoubleStream dS = DoubleStream.iterate(0.0, d -> d <= 1.0, d -> d + 0.125).limit(9);
                        System.out.print("Double Stream Ausgabe:\t");
                        dS.forEach(System.out::print);

                        // Alternative Ausgabe in einen gesamten String
                        String result = DoubleStream.iterate(0.0, d -> d <= 1.0, d -> d + 0.125).limit(9)
                                        .mapToObj(d -> String.format("%.3f", d)).collect(Collectors.joining(", "));
                        System.out.println();
                        System.out.println("Alternative Lösung:\t " + result + "\n");

                        //  b) Stream von 8 Zufallsbits als boolsche Werte false vs true
                        Random r2 = new Random();
                        IntStream randomBits = r2.ints(8, 0, 2);

                        String result2 = randomBits.mapToObj(i -> i == 0 ? "false" : "true")
                                        .collect(Collectors.joining(", "));
                        System.out.println("Boolsche Ausgabe: \t " + result2 + "\n");

                        // 4a) IntStream mit Werten zw. 0 und 9 als Stream<Integer>
                        Random RandomInts = new Random();
                        Stream<Integer> randomInts = RandomInts.ints(0, 10).limit(10).boxed();
                        String randomIntsResult = randomInts.map(Object::toString).collect(Collectors.joining(", "));
                        System.out.println("Random Zahlen von 0-9: \t" + randomIntsResult + "\n");

                        //  b) String "Hello World" per CodePoints als Stream<Integer>
                        String cd = "Hello World"; // in der Klasse String gibt es eine Methode namens CodePoints, die abgerufen werden kann
                        Stream<Integer> codePoints = cd.codePoints().boxed();
                        System.out.println("Ausgabe als CodePoints");
                        codePoints.forEach(System.out::print);
                        System.out.println();

                        List<String> codePointsList = new ArrayList<>(List.of("Hello World"));
                        Stream<Integer> codePointsListed = codePointsList.stream()
                                        .flatMapToInt(CharSequence::codePoints).boxed(); //Alternative übersetzung der Codepoints
                        codePointsListed.forEach(System.out::print);
                        System.out.println("\n");
                }

                // Intermediäre/Zentrale Stream-Verarbeitung 1:
                // Daten transformieren durch Abbilden(map) und Filtern(filter)
                {
                        List<Integer> LI = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
                        List<Character> LC = List.of('A', '0', 'b', '1', 'C', '2');

                        /*
                        Map Mehoden werden verwendet um die Inhalte zu modifizieren durch eine Function
                        Filter Methoden filtert die Inhalt mithilfe einer Methode
                         */
                        List<Integer> // mithilfe der toList() Methode werden die Inhalte der
                        // Zahlen quadrieren            Liste zugeordnet
                        L1 = LI.stream().map(X -> X * X).toList(),

                                        // Alle Zahlen doppeln
                                        // In flatMap können weitere Streams erfasst werden X diese können in einem einzigen Stream
                                        // Wiedergegeben werden
                                        L2 = LI.stream().flatMap(X -> Stream.of(X, X)).toList(),

                                        // Gerade Zahlen herausfiltern (Mithilfe einer Funktion die als Predicate überprüft wird)
                                        L3 = LI.stream().filter(X -> (X & 1) == 0).toList(),

                                        // Ganzzahlige Quadratzahlen herausfiltern
                                        L4 = LI.stream()
                                                        .filter(X -> {
                                                                double Y = Math.sqrt(X); // Berechnung der Wurzel
                                                                return Y == (int) Y; // Nur ganzzahlen werden wiedergegeben
                                                        })
                                                        .toList();

                        List<Character>
                        // Zahlen/Ziffern als Zeichen 
                        // 0 wird verwendet, da 0 die ASCII zahlt 48 ist und die Integer dazugezählt werden
                        L5 = LI.stream().map(X -> (char) (X + '0')).toList(),

                                        // Buchstaben gefiltert
                                        // Filterung nach Großbuchstaben etc. upper/lower/isLetter
                                        L6 = LC.stream().filter(C -> Character.isUpperCase(C))
                                                        .toList(); /*Character::isLetter*/

                        double[]
                        // Integer-Zahlen als Double
                        // toArray um direkt in ein einzelnes Array zu speichern
                        A1 = LI.stream().mapToDouble(X -> (double) X).toArray(),

                                        // Gerade Integers entfernen und als Double
                                        // Funktion mit überprüfung, dann die predicate abfrage zuweisen
                                        A2 = LI.stream()
                                                        .flatMapToDouble(X -> ((X & 1) == 0) ? DoubleStream.empty()
                                                                        : DoubleStream.of(X))
                                                        .toArray();

                        System.out.println("Quadrierung  : " + L1);
                        System.out.println("Doppelung    : " + L2);
                        System.out.println("Gerade Zahlen: " + L3);
                        System.out.println("Quadratzahlen: " + L4);
                        System.out.println();
                        System.out.println("Ziffern als Zeichen : " + L5);
                        System.out.println("Filterung Buchstaben: " + L6);
                        System.out.println();
                        System.out.println("Integer als Double: " + Arrays.toString(A1));
                        System.out.println("Ungerade Doubles  : " + Arrays.toString(A2));
                        System.out.println();

                        // Aufgaben zu 'map'/'filter':
                        // 1) Transformieren von 10 Integer-Zufallszahlen in ihre
                        //    Hexadezimaldarstellung (als Liste)

                        Random randomInteger = new Random();
                        int N = 10;
                        List<String> List1 = randomInteger.ints(N, 0, 100).mapToObj(x -> Integer.toHexString(x))
                                        .collect(Collectors.toList()); //(Integer::toHexString)
                        System.out.println("Integer zu Hexzahlen\t" + List1);

                        // 2) Alle 'bad words' in Liste ausmarkieren, z.B. stattdessen
                        //    Standardtext ausgeben ("fuck" => ":-)")

                        List<String> proveList = new ArrayList<>(List.of("hello", "fuck", "good", "friendly", "nigga")),
                                        badWords = new ArrayList<>(List.of("fuck", "nigga")),
                                        //kein Filter sondern Map da die Liste verändert werden muss
                                        correctedList2 = proveList.stream().map(X -> badWords.contains(X) ? ":-D" : X)
                                                        .collect(Collectors.toList());
                        System.out.println("\nCorrected List:\t" + correctedList2);

                        // 3) Leerstrings aus Liste von Strings entfernen
                        List<String> StringList3 = new ArrayList(List.of("Hello", " ", "", "World", "!")),
                                        correctedList3 = StringList3.stream()
                                                        .filter(x -> !x.isEmpty() && !x.equals(" "))
                                                        .collect(Collectors.toList());
                        System.out.println("\nLeerzeichen entfernen:\t" + correctedList3);

                        // 4) Filtern aller ein- und zweistelligen Schnapszahlen zw. 0 und 99
                        int[] counter = { 0 };
                        List<Integer> Schnappszahlen = new ArrayList<>(List.of(11, 22, 33, 44, 55, 66, 77, 88, 99)),
                                        ListOfInteger = IntStream.range(0, 101).filter(x -> Schnappszahlen.contains(x))
                                                        .boxed()
                                                        .collect(Collectors.toList()),

                                        ListOfInteger2 = Stream.iterate(0, x -> x + 1).limit(101).filter(x -> {
                                                if (x < 11) //Zahlen unter 11 herausfiltern
                                                        return false;
                                                //Konvertieren in einen String um auf einzelne Ziffern zuzugreifen
                                                String numberStr = String.valueOf(x);
                                                //Exterahieren der ersten Ziffern 1-9
                                                char firstDigit = numberStr.charAt(0);
                                                // Überprüfung ob die erste Zahl gleich der 2ten ist
                                                return numberStr.chars().allMatch(c -> c == firstDigit);
                                        }).collect(Collectors.toList()),

                                        ListOfInteger3 = Stream.generate(() -> counter[0]++).limit(101)
                                                        .collect(Collectors.toList());
                        System.out.println("Schnappszahlen:\t" + ListOfInteger2 + "\n");

                }
                // Intermediäre/Zentrale Stream-Verarbeitung 2:
                // Daten transformieren durch (Aus-)Sortieren und Überspringen
                {
                        List<Integer> L = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
                        List<Integer> LL = new ArrayList<>();

                        List<Integer>
                        // Sortierung über sorted
                        L1 = L.stream().sorted(Comparator.reverseOrder()).toList(),
                                        //Aussortierung Doppelter Inhalte
                                        L2 = Stream.of(1, 2, 3, 2, 3, 3).distinct().toList(),
                                        //L2 = L.stream().distinct().toList(),
                                        // Solange verwenden/verwerfen, bis/wie Bedingung erfüllt
                                        L3 = L.stream().takeWhile(X -> X <= 4)/*.map(X -> X+X)*/.toList(), //verwenden
                                        L4 = L.stream().dropWhile(X -> X <= 4)/*.map(X -> X+X)*/.toList(), //verwerfen

                                        // Ausspähen (ohne Änderung des Streams), Überspringen
                                        L5 = L.stream().peek(LL::add).toList(), // X -> LL.add(X)
                                        // Verwerfen von einträgen wie dropwhile
                                        L6 = L.stream().skip(5).toList();

                        System.out.println("Sortierung: " + L1);
                        System.out.println("Distinktion: " + L2);
                        System.out.println("Bearbeitung take: " + L3);
                        System.out.println("Bearbeitung drop: " + L4);
                        System.out.println("Herauspicken: " + L5 + " / " + LL);
                        System.out.println("Überspringen: " + L6);
                        System.out.println();
                }

                // Terminale/Finale Stream-Reduktion: Daten zusammenfassen
                {
                        List<Integer> L = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

                        // Finden/Matchen
                        {
                                Optional<Integer> O1 = L.stream().findFirst(), // eigentlich returnFirst/Any
                                                O2 = L.stream().findAny(),
                                                O3 = L.stream().min(Integer::compare),
                                                O4 = L.stream().max(Integer::compare);
                                System.out.println("findFirst:\t" + O1 + "\nfindAny:\t" + O2);
                                System.out.println("min:\t" + O3 + "\nmax:\t" + O4);

                                boolean B1 = L.stream().allMatch(X -> X < 10),
                                                B2 = L.stream().anyMatch(X -> X % 5 == 0),
                                                B3 = L.stream().noneMatch(X -> X + X == X);
                                System.out.println("\nMatches:\t" + B1 + ", " + B2 + ", " + B3 + "\n");

                                //System.out.println();

                                //Random R = new Random();
                                //IntStream St = R.ints(1_000_000_000);
                                //OptionalInt M = St./*parallel().*/max();
                                //System.out.println(M);
                        }

                        // Reduzieren/Aufsammeln
                        {
                                // Reduce
                                int Sum = L.stream().reduce(0, (Z, X) -> Z + X); // Integer::sum
                                Optional<Integer> Summy = L.stream().reduce((Z, X) -> Z + X);
                                //System.out.println("Summen: "+Sum + ", " + Summy);

                                String R = L.stream().reduce("", (S, X) -> S + X, String::concat);
                                //System.out.println("Stringbildung: "+R);    // könnte parallelisiert werden

                                // Collect
                                Set<Integer> Zet1 = L.stream().collect(HashSet::new, (Z, X) -> Z.add(X / 2),
                                                HashSet::addAll);
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
                        // 1) Fakultät von N berechnen (Fakultät von 0 ist 1)
                        // 2) Quersumme eines Strings berechnen (z.B. "122333" = 14)
                        // 3) Alle Ziffern der Liste L in StringBuilder aufsammeln zu "0123456789"
                        // 4) Umwandlung von Integer-Array (Integer[]) in int-Array (int[]) und umgekehrt

                        // Beispiel für 'interfering function'
                        {
                                List<Integer> LL = new ArrayList<>(L);
                                /*List<Integer>
                                    LLL = LL.stream().reduce(LL,        // interfering!
                                             (Li, X) -> { Li.add(X); return Li; },
                                             (L1, L2) -> { L1.addAll(L2); return L1; });*/
                                //System.out.println(LL+" / "+LLL);
                        }
                }
        }
}
