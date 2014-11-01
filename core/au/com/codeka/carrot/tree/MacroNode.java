package au.com.codeka.carrot.tree;

import static au.com.codeka.carrot.util.logging.JangodLogger;

import java.io.IOException;
import java.io.Writer;

import au.com.codeka.carrot.base.Application;
import au.com.codeka.carrot.interpret.InterpretException;
import au.com.codeka.carrot.interpret.JangodInterpreter;
import au.com.codeka.carrot.lib.Macro;
import au.com.codeka.carrot.lib.MacroLibrary;
import au.com.codeka.carrot.lib.Tag;
import au.com.codeka.carrot.lib.TagLibrary;
import au.com.codeka.carrot.parse.MacroToken;
import au.com.codeka.carrot.parse.ParseException;

public class MacroNode extends Node {

  private static final long serialVersionUID = 5037873030399458427L;
  private MacroToken master;
  String endName = null;

  public MacroNode(Application app, MacroToken token) throws ParseException {
    super(app);
    master = token;
    Macro macro = MacroLibrary.getMacro(master.getMacroName());
    if (macro == null) {
      throw new ParseException("Can't find macro >>> " + master.getMacroName());
    }
    endName = macro.getEndMacroName();
  }

  @Override
  public void render(JangodInterpreter interpreter, Writer writer)
      throws InterpretException, IOException {
    Tag tag = TagLibrary.getTag(master.getMacroName());
    if (tag != null) {
      JangodLogger.fine("Treat macro as tag with same name >>> " + master.getMacroName());
      interpreter.setLevel(level);
      tag.interpreter(children(), master.getHelpers(), interpreter, writer);
    } else {
      JangodLogger.warning("Skiping handless macro while rendering >>> " + master.getMacroName());
    }
  }

  public void refactor(TreeRebuilder rebuilder) throws ParseException {
    Macro macro = MacroLibrary.getMacro(master.getMacroName());
    macro.refactor(this, master.getHelpers(), rebuilder);
  }

  @Override
  public String toString() {
    return master.toString();
  }

  @Override
  public String getName() {
    return master.getMacroName();
  }

  @Override
  public Node clone() {
    try {
      Node clone = new MacroNode(app, master);
      clone.children = this.children.clone(clone);
      return clone;
    } catch (ParseException e) {
      throw new InternalError();
    }
  }
}
