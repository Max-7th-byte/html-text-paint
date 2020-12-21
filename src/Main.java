import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {

    private static final String keywords[] = {
            "abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false",
            "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while", "const", "let", "require", "exports"
    };

    public static void main(String[] args) {
        readFile("/Users/max/Documents/Programming/machine_learning/house_prices/launch.m");
    }


    public static void readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter("code.txt"));
            String line;
            int tabFor = 0;
            int counter = 1;
            while ((line = br.readLine()) != null) {

                if (line.trim().startsWith("//")) {
                    bw.write("<span class=\"line-count\">" + counter + "</span><span class=\"comment tab tab-" + tabFor + "\"> " + line + " </span>");
                } else {
                    if (line.trim().contains("}")) {
                        tabFor--;
                        if (tabFor < 0) {
                            tabFor = 0;
                        }
                    }
                    String tmp = colorKeyWords(line);
                    tmp = colorDigits(tmp);
                    tmp = colorFunctionsAndClasses(tmp);
                    bw.write("<span class=\"line-count\">" + counter + "</span><span class=\"tab tab-" + tabFor + "\"> " + tmp + " </span>\n");
                    if (line.contains("{")) tabFor++;
                }
                counter++;
            }
            bw.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isKey(String word) {
        for (String keyword : keywords) {
            if (word.trim().equals(keyword.trim())) return true;
        }
        return false;
    }

    private static String colorKeyWords(String line) {
        String[] words = line.split("\\s+");
        StringBuilder tmp = new StringBuilder();
        for (String word : words) {

            if (isKey(word)) {
                tmp.append(" <span class=\"key-word\"> ").append(word).append(" </span> ");
            } else {
                tmp.append(" ").append(word).append(" ");
            }
        }
        return tmp.toString();
    }

    private static String colorDigits(String line) {
        String words[] = line.trim().split("\\s+");
        StringBuilder tmp = new StringBuilder();
        boolean skipping = false;
        for (String word : words) {
            if (word.equals("<span"))
                skipping = true;


            if (!skipping) {
                char prev = '.';
                tmp.append(" ");
                for (char c : word.toCharArray()) {
                    if (Character.isDigit(c)) {
                        if (prev == ' ')
                            tmp.append(" <span class=\"digit\"> ").append(c).append("</span> ");
                        else
                            tmp.append("<span class=\"digit\">").append(c).append("</span>");
                    } else {
                        tmp.append(c);
                    }
                    prev = c;
                }
                tmp.append(" ");
            } else {
                tmp.append(" ").append(word).append(" ");
            }

            if (word.contains(">"))
                skipping = false;
        }

        return tmp.toString();
    }

    private static String colorFunctionsAndClasses(String line) {
        String[] words = line.trim().split("\\s+");
        StringBuilder tmp = new StringBuilder();
        boolean skipping = false;
        for (String word : words) {

            if (word.equals("<span"))
                skipping = true;

            if (!skipping) {
                if (startsWithCapitalLetter(word)) {
                    int endPoint = endPoint(word, false);
                    try {
                        System.out.println(word);
                        System.out.println(word.charAt(endPoint-1));
                        if (word.charAt(endPoint-1) == ' ') {
                            tmp.append("<span class=\"function\">").append(word, 0, endPoint).append("</span> ");
                        } else {
                            tmp.append("<span class=\"function\">").append(word, 0, endPoint).append("</span> ");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        tmp.append("<span class=\"function\">").append(word, 0, endPoint).append("</span>");
                    }

                    if (endPoint < word.length())
                        tmp.append(word, endPoint, word.length());
                } else if (isFunction(word)) {
                    int startPoint = startPoint(word);
                    int endPoint = endPoint(word, true);
                    tmp.append(word, 0, startPoint);
                    tmp.append("<span class=\"function\">").append(word, startPoint, endPoint).append("</span>");
                    if (endPoint < word.length())
                        tmp.append(word, endPoint, word.length());
                } else {
                    tmp.append(" ").append(word).append(" ");
                }
            } else {
                tmp.append(" ").append(word).append(" ");
            }

            if (word.contains(">"))
                skipping = false;
        }

        return tmp.toString();
    }

    private static boolean startsWithCapitalLetter(String word) {
        if (!word.equals("")) return 'A' <= word.charAt(0) && word.charAt(0) <= 'Z';
        return false;
    }

    private static boolean isFunction(String word) {
        char[] arr = word.toCharArray();
        char prev = '.';
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '(' && Character.isLowerCase(prev)) {
                return true;
            }
            prev = arr[i];
        }
        return false;
    }

    private static int endPoint(String word, boolean isFunction) {
        int endPoint = 0;
        char[] arr = word.toCharArray();
        if (isFunction) {
            for (int i = 0; (i < word.length() - 1) && arr[endPoint] != '('; i++) {
                endPoint++;
            }
        } else {
            for (int i = 0; (i < word.length() - 1) && arr[endPoint] != '.' && arr[endPoint] != ';' && arr[endPoint] != '('; i++) {
                endPoint++;
            }
        }
        if (word.charAt(endPoint) == '(') return endPoint;
        return endPoint + 1;
    }

    private static int startPoint(String word) {
        if (!word.contains(".")) return 0;
        int startPoint = 0;
        while (word.charAt(startPoint) != '.' && word.charAt(startPoint) != '(') startPoint++;
        return startPoint;
    }
}