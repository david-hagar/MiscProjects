package com.david_hagar.token_scanner;

public class StripHeader {

    public static String stripHeader(String email) {

        final int beginIndex = blankLineIndex(email);

        if( beginIndex == -1)
            return email;

        return email.substring(beginIndex, email.length());
    }

    private static int blankLineIndex(String email) {

        int lineEndCount = 0;
        int crCount = 0;

        for (int i = 0; i < email.length(); i++) {
            char c = email.charAt(i);

            if (c == '\n')
                lineEndCount++;
            else if (c == '\r')
                crCount++;
            else if (!Character.isWhitespace(c))
                lineEndCount = crCount = 0;

            if (lineEndCount > 1 || crCount > 1)
                return skipToFirstNonWhiteSpace(email, i);

        }

        return -1;
    }

    private static int skipToFirstNonWhiteSpace(String email, int splitIndex) {

        for (int i = splitIndex; i < email.length(); i++) {
            final char c = email.charAt(i);
            if (!Character.isWhitespace(c))
                return i;
        }

        return email.length();
    }


    public static void main(String[] args) {
        String v = "Message-ID: <18782981.1075855378110.JavaMail.evans@thyme>\n" +
                "Date: Mon, 14 May 2001 16:39:00 -0700 (PDT)\n" +
                "From: phillip.allen@enron.com\n" +
                "To: tim.belden@enron.com\n" +
                "Subject: \n" +
                "Mime-Version: 1.0\n" +
                "Content-Type: text/plain; charset=us-ascii\n" +
                "Content-Transfer-Encoding: 7bit\n" +
                "X-From: Phillip K Allen\n" +
                "X-To: Tim Belden <Tim Belden/Enron@EnronXGate>\n" +
                "X-cc: \n" +
                "X-bcc: \n" +
                "X-Folder: \\Phillip_Allen_Jan2002_1\\Allen, Phillip K.\\'Sent Mail\n" +
                "X-Origin: Allen-P\n" +
                "X-FileName: pallen (Non-Privileged).pst\n" +
                "\n" +
                "Here is our forecast\n" +
                "\n" +
                " ";
        System.out.println(stripHeader(v));
    }
}
