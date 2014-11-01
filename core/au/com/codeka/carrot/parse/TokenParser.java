package au.com.codeka.carrot.parse;

import static au.com.codeka.carrot.util.logging.JangodLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import au.com.codeka.carrot.base.Constants;
import au.com.codeka.carrot.util.logging.Level;

public class TokenParser implements Iterator<Token> {

  private Tokenizer tm = new Tokenizer();
  private Token token;
  private boolean proceeding = true;

  public TokenParser(String text) {
    tm.init(text);
  }

  public TokenParser(Reader reader) throws ParseException {
    init(reader);
  }

  public void init(String text) {
    tm.init(text);
    token = null;
    proceeding = true;
  }

  public void init(Reader reader) throws ParseException {
    BufferedReader br = new BufferedReader(reader);
    StringBuffer buff = new StringBuffer();
    String line;
    try {
      while ((line = br.readLine()) != null) {
        buff.append(line);
        buff.append(Constants.STR_NEW_LINE);
      }
    } catch (IOException e) {
      throw new ParseException("read template reader fault.", e.getCause());
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        throw new ParseException("read template reader fault.", e.getCause());
      }
    }
    tm.init(buff.toString());
    token = null;
    proceeding = true;
  }

  @Override
  public boolean hasNext() {
    if (proceeding) {
      try {
        token = tm.getNextToken();
        if (token != null) {
          return true;
        } else {
          proceeding = false;
          return false;
        }
      } catch (ParseException e) {
        JangodLogger.log(Level.SEVERE, e.getMessage(), e.getCause());
        token = null;
        // TODO go on proceeding or not
      }
    }
    return false;
  }

  @Override
  public Token next() {
    if (proceeding) {
      if (token == null) {
        try {
          Token tk = tm.getNextToken();
          if (tk == null) {
            proceeding = false;
            throw new NoSuchElementException();
          }
          return tk;
        } catch (ParseException e) {
          JangodLogger.log(Level.SEVERE, e.getMessage(), e.getCause());
          // TODO go on proceeding or not
          throw new NoSuchElementException();
        }
      } else {
        Token last = token;
        token = null;
        return last;
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
