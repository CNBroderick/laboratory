package org.bklab.vaadin.execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Objects;

@SuppressWarnings("unused")
public class Cmd {

    String executeDosCommand(String dosCommand) {
        String line;
        StringBuilder sb = new StringBuilder();
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            p = r.exec(dosCommand);
        } catch (IOException ignore) {
        }
        assert p != null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(p).getInputStream(), Charset.forName("GBK")))) {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                System.out.println(line);
            }
        } catch (IOException ignore) {
        }
        return sb.toString();
    }

}
