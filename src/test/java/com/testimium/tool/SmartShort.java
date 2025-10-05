package com.testimium.tool;

import java.util.Arrays;
import java.util.Comparator;

public class SmartShort {
    public static void main(String[] args) {
        String[] names = {
                "10Alice", "2Bob", "Anna", "1Zack", "Bob", "20Charlie", "Charlie"
        };
        Arrays.sort(names, new Comparator<>() {
            @Override
            public int compare(String s1, String s2) {
                NumericName n1 = splitPrefix(s1);
                NumericName n2 = splitPrefix(s2);

                // Both have numbers
                if (n1.number != null && n2.number != null) {
                    int cmp = Integer.compare(n1.number, n2.number);
                    if (cmp != 0) return cmp;
                    return n1.rest.compareTo(n2.rest);
                }

                // One has number
                if (n1.number != null) return -1; // numbers come first
                if (n2.number != null) return 1;

                // Neither has number – pure lexicographical
                return s1.compareTo(s2);
            }

            class NumericName {
                Integer number;
                String rest;
                NumericName(Integer number, String rest) {
                    this.number = number;
                    this.rest = rest;
                }
            }

            private NumericName splitPrefix(String s) {
                int i = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    i++;
                }
                if (i == 0) {
                    return new NumericName(null, s);
                } else {
                    Integer num = Integer.parseInt(s.substring(0, i));
                    return new NumericName(num, s.substring(i));
                }
            }
        });

        System.out.println(Arrays.toString(names));
    }
}

