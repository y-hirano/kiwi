package kiwi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.users.User;

/**
 * ChangLog class is for reading Kiwi entries from a stream with ChangeLog format. 
 * @author Yutaka Hirano
 */
public class ChangeLogReader {
    public ChangeLogReader(User user) {
        this.user = user;
        clear();
        unmodifiableEntries = Collections.unmodifiableList(entries);
    }
    
    static private Date getUTCDate(int year, int month, int date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(year, month, date, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    final private Pattern HEADER_PATTERN = Pattern.compile("^(\\d+)-(\\d+)-(\\d+) +(.+) +<(.*)>$");
    final private Pattern ENTRY_START_PATTERN = Pattern.compile("^\t\\* *([^:]+): +(.*)$");
    
    private List<String> lookahead = new LinkedList<String>();
    int lineNumber;
    private String readLine(BufferedReader reader) throws IOException {
        String line;
        if (lookahead.isEmpty()) {
            line = reader.readLine();
        } else {
            line = lookahead.get(0);
            lookahead.remove(0);
        }
        ++lineNumber;
        return line;
    }
    
    private void bringBackReadLine(String line) {
        --lineNumber;
        lookahead.add(line);
    }
    private void clear() {
        lineNumber = 0;
        lookahead = new LinkedList<String>();
        entries = new ArrayList<Entry>();
    }
    
    private void entry(BufferedReader reader, Date date, String authorName, String AuthorAddress) throws IOException, ParseException {
        String line = null;
        do {
            line = readLine(reader);
        } while (line != null && line.isEmpty());
        if (line == null) {
            return;
        }
        Matcher m = ENTRY_START_PATTERN.matcher(line);
        if (!m.matches()) {
            throw new ParseException(String.format("[%s] is expected.", ENTRY_START_PATTERN), lineNumber);
        }
        String tag = m.group(1);
        StringBuilder body = new StringBuilder();
        body.append(m.group(2));
        while (true) {
            line = readLine(reader);
            if (line == null || line.isEmpty()) {
                entries.add(Entry.create(user, date, authorName, AuthorAddress, tag, body.toString()));
                return;
            }
            if (line.startsWith("\t")) {
                body.append("\n");
                body.append(line, 1, line.length());
            } else {
                throw new ParseException("[\\\\t.*] is expected", lineNumber);
            }
        }
    }
    
    private void entriesInADay(BufferedReader reader) throws IOException, ParseException {
        String line = null;
        do {
            line = readLine(reader);
        } while (line != null && line.isEmpty());
        if (line == null) {
            return;
        }
        Matcher m = HEADER_PATTERN.matcher(line);
        if (!m.matches()) {
            throw new ParseException(String.format("Header [%s] is expected.", HEADER_PATTERN), lineNumber);
        }
        Date date = getUTCDate(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));  
        String authorName = m.group(4);
        String authorAddress = m.group(5);
        while (true) {
            line = readLine(reader);
            if (line == null) {
                return;
            }
            if (line.isEmpty()) {
                continue;
            }
            bringBackReadLine(line);
            if (HEADER_PATTERN.matcher(line).matches()) {
                return;
            }
            entry(reader, date, authorName, authorAddress);
        }
    }
    
    /**
     * Read and store entries from the specified reader.
     * ChangeLogReader clears existing entries when this method is called.
     * @param reader
     * @throws IOException
     */
    public void read(Reader r) throws IOException, ParseException {
        clear();
        BufferedReader reader = new BufferedReader(r);
        while (true) {
            String line = readLine(reader);
            if (line == null) {
                break;
            }
            bringBackReadLine(line);
            entriesInADay(reader);
        }
        unmodifiableEntries = Collections.unmodifiableList(entries);
    }
    
    public List<Entry> getEntries() {
        return unmodifiableEntries;
    }
    private List<Entry> entries;
    private List<Entry> unmodifiableEntries;
    private User user;
}
