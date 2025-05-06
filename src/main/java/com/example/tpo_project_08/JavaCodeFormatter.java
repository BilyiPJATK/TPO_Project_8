package com.example.tpo_project_08;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.springframework.stereotype.Component;

    @Component
    public class JavaCodeFormatter {

        public String format(String inputCode) throws FormatterException {
            Formatter formatter = new Formatter();
            return formatter.formatSource(inputCode);
        }
    }
