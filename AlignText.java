import java.util.ArrayList;

/**
 * Class which formats txt files based on given location and line length.
 * @author 210001064
 */

public class AlignText {

    /**
     * Takes a paragraph and creates a String array of words.
     * based on assumption that array length (number of words) can be determined by number of blank spaces.
     * @param paragraph paragraph of text.
     * @return a String array of the words present in the input paragraph
     */
    public static String[] paragraphSplitter(String paragraph) {
        int arrLength = paragraph.length() - paragraph.replaceAll(" ", "").length() + 1;
        String[] words = new String[arrLength];
        int spaceIndex;
        int offset = 0;

        for (int i = 0; i < arrLength; i++) {
            spaceIndex = paragraph.indexOf(' ', offset);

            if (i < arrLength - 1) {
                words[i] = paragraph.substring(offset, spaceIndex);
            } else {
                words[i] = paragraph.substring(offset);
            }
            offset = spaceIndex + 1;
        }
        return words;
    }

    /**
     * Loops through words and checks length and adds to line where less than width.
     * @param words a String array of words in the paragraph.
     * @param width width of line to be printed.
     * @return an ArrayList<String> of lines ready to be printed.
     */
    public static ArrayList<String> lineAssembler(String[] words, int width) {
        ArrayList<String> lines = new ArrayList<String>();
        String currLine = "";

        for (String word : words) {
            //adds word to start and also adds words which are longer than width parameter.
            if (currLine.isBlank()) {
                currLine += word;
            } else {
                String cat = currLine + " " + word;

                if (cat.length() == width) {
                    lines.add(cat);
                    currLine = "";
                } else if (cat.length() < width) {
                    currLine = cat;
                } else {
                    lines.add(currLine);
                    currLine = word;
                }
            }
        }
        lines.add(currLine);
        return lines;
    }

    /**
     * Prints each line with required spacing before and after to ensure aligment.
     * @param precedingSpace whitespace ahead of line to be printed.
     * @param line line to be printed.
     * @param followingSpace whitespace after line to be printed.
     */
    public static void lineSpaces(int precedingSpace, String line, int followingSpace) {
        for (int i = 0; i < precedingSpace; i++) {
            System.out.print(" ");
        }
        System.out.print(line);
        for (int i = 0; i < followingSpace; i++) {
            System.out.print(" ");
        }
    }

    /**
     * Prints each correctly formatted line.
     * @param lines lines ready to be printed.
     * @param width width of line to be printed.
     * @param alignment determines which way the text is formatted.
     * @param longestLength longest line length across paragraphs, used for sign formatting.
     */
    public static void linePrinter(ArrayList<String> lines, int width, String alignment, int longestLength) {
        if (alignment.equals("S")) {
            signPrinter(lines, longestLength);
        } else {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if ((alignment.equals("L")) || (line.length() > width)) {
                    System.out.println(line);
                } else {
                    //think about refactoring so lines up to lineSpace() become own method
                    //Toggles used to switch between right and central alignment
                    int rToggle = 1;
                    int cToggle = 0;
                    if (alignment.equals("C")) {
                        cToggle = 1;
                        rToggle = 0;
                    }
                    //Offsets used to determine spacing before and after text in each line
                    int rOffset = width - line.length();
                    int cOffset = rOffset / 2;
                    int precedingSpace = cToggle * (cOffset + (rOffset % 2)) + rToggle * rOffset;
                    int followingSpace = cToggle * cOffset;
                    lineSpaces(precedingSpace, line, followingSpace);
                    if (i < lines.size() - 1) {
                        System.out.println();
                    }
                }
            }
        }

    }

    /**
     * Prints the top edge of the sign based on the parameter length.
     * @param length the length of the longest word + 2 to ensure spacing either side.
     */
    public static void signLid(int length) {
        System.out.print(" ");
        for (int i = 0; i < length + 2; i++) {
            System.out.print("_");
        }
        System.out.print(" \n");
        System.out.print("/");
        for (int i = 0; i < length + 2; i++) {
            System.out.print(" ");
            }
        System.out.print("\\\n");
    }

    /**
     * Prints base of sign including hand.
     * @param length the length of the longest word + 2 to ensure spacing either side.
     */
    public static void signBase(int length) {
        System.out.print("\\");
        for (int i = 0; i < length + 2; i++) {
            System.out.print("_");
        }
        System.out.print("/\n");
        System.out.println("        |  |");
        System.out.println("        |  |");
        System.out.println("        L_ |");
        System.out.println("       / _)|");
        System.out.println("      / /__L");
        System.out.println("_____/ (____)");
        System.out.println("       (____)");
        System.out.println("_____  (____)");
        System.out.println("     \\_(____)");
        System.out.println("        |  |");
        System.out.println("        |  |");
        System.out.println("        \\__/");
    }

    /**
     * Prints each line surrounded by sign formatting.
     * @param lines ArrayList<String> of lines to be printed.
     * @param maxLength longest line length across all paragraphs.
     */
    public static void signPrinter(ArrayList<String> lines, int maxLength) {
        for (String line : lines) {
            System.out.print("| ");
            lineSpaces(0, line, maxLength - line.length());
            System.out.print(" |\n");
        }
    }

    /**
     * Try/catch block used to ensure correct user input.
     * @param args should take form filepath, line width, (alignment).
     */
    public static void main(String[] args) {
        try {
            String[] paragraphs  = FileUtil.readFile(args[0]);
            int width = Integer.parseInt(args[1]);
            String alignment = "L";
            int longestWordLength = 0;

            if (args.length > 2) {
                alignment = args[2];
            }
            if (width < 1) {
                throw new Exception();
            }
            //used to make sure the sign formatting is considered in the line length (four additional characters)
            if (alignment.equals("S")) {
                width -= 4;
            }

            for (String para : paragraphs) {
                String[] words = paragraphSplitter(para);
                //finds longest word for sign version
                for (String word : words) {
                    if (word.length() > longestWordLength) {
                        longestWordLength = word.length();
                    }
                }
                ArrayList<String> lines = lineAssembler(words, width);
                if (!alignment.equals("S")) {
                    linePrinter(lines, width, alignment, longestWordLength);
                    System.out.println();
                }
            }

            if (alignment.equals("S")) {
                //used to ensure sign formatting is consistent across different paragraphs
                if (width > longestWordLength) {
                    longestWordLength = width;
                }
                signLid(longestWordLength);
                for (String para : paragraphs) {
                    String[] words = paragraphSplitter(para);
                    ArrayList<String> lines = lineAssembler(words, width);
                    linePrinter(lines, width, alignment, longestWordLength);
                }
                signBase(longestWordLength);

            }
        } catch (Exception e) {
            if (args.length > 2) {
                System.out.println("usage: java AlignText file_name line_length [align_mode]");
            } else {
                System.out.println("usage: java AlignText file_name line_length");
            }
        }
    }
}
