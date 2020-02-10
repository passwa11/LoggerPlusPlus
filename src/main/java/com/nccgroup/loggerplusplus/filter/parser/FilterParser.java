/* FilterParser.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. FilterParser.java */
package com.nccgroup.loggerplusplus.filter.parser;
import com.nccgroup.loggerplusplus.filter.BooleanOperator;
import com.nccgroup.loggerplusplus.filter.Operator;import com.nccgroup.loggerplusplus.filterlibrary.FilterLibraryController;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.nccgroup.loggerplusplus.logentry.LogEntryField;
import com.nccgroup.loggerplusplus.logentry.FieldGroup;
import static com.nccgroup.loggerplusplus.logentry.LogEntryField.getFieldsInGroup;

public class FilterParser/*@bgen(jjtree)*/implements FilterParserTreeConstants, FilterParserConstants {/*@bgen(jjtree)*/
  protected JJTFilterParserState jjtree = new JJTFilterParserState();
    public static ASTExpression parseFilter(String string) throws ParseException {
        FilterParser FilterParser = new FilterParser(new StringReader(string));
        ASTExpression node;
        try{
            node = FilterParser.Filter();
        }catch(Exception e){
            throw new ParseException(e.getMessage());
        }
        VisitorData result = new SanityCheckVisitor().visit(node);
        if(!result.isSuccess()) throw new ParseException(result.getErrorString());
        return node;
    }

    public static void checkAliasesForSanity(FilterLibraryController libraryController, ASTExpression filter) throws ParseException {
        VisitorData result = new AliasCheckVisitor(libraryController).visit(filter);
        if(!result.isSuccess()) throw new ParseException(result.getErrorString());
    }

    private static void throwOperatorAmbiguityException(BooleanOperator op, BooleanOperator other) throws ParseException {
        throw new ParseException(String.format("Cannot mix operators %s, %s. Please use parenthesis to remove ambiguity.", op.getLabel(), other.getLabel()));
    }

  final public ASTExpression Filter() throws ParseException {ASTExpression ex;
    ex = Expression(false);
    jj_consume_token(0);
{if ("" != null) return ex;}
    throw new Error("Missing return statement in function");
}

  final public ASTExpression Expression(boolean inverse) throws ParseException {/*@bgen(jjtree) Expression */
 ASTExpression jjtn000 = new ASTExpression(JJTEXPRESSION);
 boolean jjtc000 = true;
 jjtree.openNodeScope(jjtn000);BooleanOperator op = null;
    try {
      ExpressionInner();
      if (jj_2_1(2)) {
        op = ExpressionCompounding();
      } else {
        ;
      }
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.op = op;
        jjtn000.inverse = inverse;
        {if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
    throw new Error("Missing return statement in function");
}

  final public void WrappedCompoundExpression() throws ParseException {boolean inverse=false;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INVERSE:{
      inverse = Inverse();
      break;
      }
    default:
      jj_la1[0] = jj_gen;
      ;
    }
    jj_consume_token(LPAREN);
    Expression(inverse);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case RPAREN:{
      jj_consume_token(RPAREN);
      break;
      }
    default:
      jj_la1[1] = jj_gen;
{if (true) throw new ParseException("Unbalanced brackets.");}
    }
}

  final public void ExpressionInner() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LPAREN:
    case INVERSE:{
      WrappedCompoundExpression();
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      Statement();
    }
}

  final public void Statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ALIAS_SYMBOL:{
      Alias();
      break;
      }
    default:
      jj_la1[3] = jj_gen;
      Comparison();
    }
}

  final public BooleanOperator ExpressionCompounding() throws ParseException {BooleanOperator op;
    BooleanOperator nextOp;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case AND:{
      op = And();
      break;
      }
    case OR:{
      op = Or();
      break;
      }
    case XOR:{
      op = Xor();
      break;
      }
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    ExpressionInner();
    label_1:
    while (true) {
      if (jj_2_2(2)) {
        ;
      } else {
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AND:{
        nextOp = And();
        break;
        }
      case OR:{
        nextOp = Or();
        break;
        }
      case XOR:{
        nextOp = Xor();
        break;
        }
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
//Subsequent compoundings must be same operator. Otherwise, throw exception.
            if(op != nextOp) throwOperatorAmbiguityException(op,nextOp);
      ExpressionInner();
    }
{if ("" != null) return op;}
    throw new Error("Missing return statement in function");
}

//Comparisons can take two forms (Value) or (value operation value).
//To make interpretation easier, we convert (Value) to (Value operation value) by means of (Value EQUALS TRUE)
  final public void Comparison() throws ParseException {/*@bgen(jjtree) Comparison */
 ASTComparison jjtn000 = new ASTComparison(JJTCOMPARISON);
 boolean jjtc000 = true;
 jjtree.openNodeScope(jjtn000);Operator op = Operator.EQUAL;
 Object left, right = true;
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        left = Identifier();
        break;
        }
      default:
        jj_la1[6] = jj_gen;
{if (true) throw new ParseException("The left side of a comparison must be a field identifier.");}
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case EQ:
      case NEQ:{
        op = EqualityOperator();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BOOLEAN:
        case NUMBER:
        case REGEXLITERAL_IN_FORWARD_SLASHES:
        case REGEX_IN_FORWARD_SLASHES:
        case INVERSE:
        case IDENTIFIER:
        case OPEN_SINGLE_QUOTE_STRING:
        case OPEN_DOUBLE_QUOTE_STRING:{
          right = Value();
          break;
          }
        default:
          jj_la1[7] = jj_gen;
{if (true) throw new ParseException("Invalid right hand value for comparison \"" + op + "\"");}
        }
if(right instanceof Pattern && !String.class.isAssignableFrom(((LogEntryField) left).getType())){
              {if (true) throw new ParseException(String.format("Regex patterns can only be used on fields which can be converted to a string. Field \"%s\" of type \"%s\" cannot be converted.", left, ((LogEntryField) left).getType()));}
            }
        break;
        }
      case GT:
      case LT:
      case GEQ:
      case LEQ:{
        op = NumericOperator();
if(!Number.class.isAssignableFrom(((LogEntryField) left).getType())){
                {if (true) throw new ParseException(String.format("Numeric operators cannot be used for field \"%s\" of type \"%s\"", left, ((LogEntryField) left).getType()));}
            }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NUMBER:{
          right = Number();
          break;
          }
        case IDENTIFIER:{
          right = Identifier();
if(!Number.class.isAssignableFrom(((LogEntryField) right).getType())){
                    {if (true) throw new ParseException(String.format("Numeric operators cannot be used for field \"%s\" of type \"%s\"", right, ((LogEntryField) right).getType()));}
                }
          break;
          }
        default:
          jj_la1[8] = jj_gen;
{if (true) throw new ParseException("Invalid right hand value for comparison \"" + op + "\"");}
        }
        break;
        }
      case CONTAINS:{
        op = ContainsOperator();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case OPEN_SINGLE_QUOTE_STRING:
        case OPEN_DOUBLE_QUOTE_STRING:{
          right = String();
          break;
          }
        case NUMBER:{
          right = Number();
          break;
          }
        case IDENTIFIER:{
          right = Identifier();
          break;
          }
        default:
          jj_la1[9] = jj_gen;
{if (true) throw new ParseException("The contains operator can only be used on string and numeric values and identifiers.");}
        }
        break;
        }
      case IN:{
        op = InOperator();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case ARRAY_START:{
          right = Array();
          break;
          }
        default:
          jj_la1[10] = jj_gen;
{if (true) throw new ParseException("The in operator must be used on an array. E.g. \"Response.status IN [200, 302, 500]\"");}
        }
        break;
        }
      case MATCHES:{
        op = MatchesOperator();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case OPEN_SINGLE_QUOTE_STRING:
        case OPEN_DOUBLE_QUOTE_STRING:{
          right = RegexInString();
          break;
          }
        case REGEXLITERAL_IN_FORWARD_SLASHES:
        case REGEX_IN_FORWARD_SLASHES:{
          right = RegexInForwardSlashes();
          break;
          }
        default:
          jj_la1[11] = jj_gen;
{if (true) throw new ParseException("The matches operator must have a pattern as its right hand value.");}
        }
        break;
        }
      default:
        jj_la1[12] = jj_gen;
if(!(left instanceof Boolean || (left instanceof LogEntryField && ((LogEntryField) left).getType().isAssignableFrom(Boolean.class)))){
                //If left isn't a boolean value or field with boolean type
                {if (true) throw new ParseException(left + " cannot be evaluated as a boolean.");}
            }
      }
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.left = left;
        jjtn000.right = right;
        jjtn000.operator = op;
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
}

  final public void Alias() throws ParseException {/*@bgen(jjtree) Alias */
    ASTAlias jjtn000 = new ASTAlias(JJTALIAS);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token identifier;
    try {
      jj_consume_token(ALIAS_SYMBOL);
      identifier = jj_consume_token(IDENTIFIER);
jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
jjtn000.identifier = identifier.image;
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
}

  final public Object Value() throws ParseException {Object v;
    if (jj_2_3(2)) {
      v = Identifier();
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NUMBER:{
        v = Number();
        break;
        }
      case BOOLEAN:
      case INVERSE:{
        v = Boolean();
        break;
        }
      case OPEN_SINGLE_QUOTE_STRING:
      case OPEN_DOUBLE_QUOTE_STRING:{
        v = String();
        break;
        }
      case REGEXLITERAL_IN_FORWARD_SLASHES:
      case REGEX_IN_FORWARD_SLASHES:{
        v = RegexInForwardSlashes();
        break;
        }
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return v;}
    throw new Error("Missing return statement in function");
}

  final public Set Array() throws ParseException {Set<Object> items = new LinkedHashSet();
    Object initial, subsequent;
    jj_consume_token(ARRAY_START);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OPEN_SINGLE_QUOTE_STRING:
    case OPEN_DOUBLE_QUOTE_STRING:{
      initial = String();
      break;
      }
    case NUMBER:{
      initial = Number();
      break;
      }
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
items.add(initial);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ARRAY_SEPARATOR:{
        ;
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        break label_2;
      }
      jj_consume_token(ARRAY_SEPARATOR);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OPEN_SINGLE_QUOTE_STRING:
      case OPEN_DOUBLE_QUOTE_STRING:{
        subsequent = String();
        break;
        }
      case NUMBER:{
        subsequent = Number();
        break;
        }
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
if(!initial.getClass().isAssignableFrom(subsequent.getClass())){
                {if (true) throw new ParseException("Array elements must all be of the same type.");}
            }
            items.add(subsequent);
    }
    jj_consume_token(ARRAY_END);
{if ("" != null) return items;}
    throw new Error("Missing return statement in function");
}

//IDENTIFIERS
  final public LogEntryField Identifier() throws ParseException {Token group, identifier;
    FieldGroup fieldGroup;
    LogEntryField field;
    group = jj_consume_token(IDENTIFIER);
fieldGroup = FieldGroup.findByLabel(group.image);
        if(fieldGroup == null)
            {if (true) throw new ParseException(String.format("Invalid field group \"%s\". Valid groups are:\n%s", group.image, Arrays.toString(FieldGroup.values())));}
    jj_consume_token(DOT);
    identifier = jj_consume_token(IDENTIFIER);
field = LogEntryField.getByLabel(fieldGroup, identifier.image);
        if(field == null){
            StringBuilder fieldMessage = new StringBuilder();
            ArrayList<LogEntryField> fields = new ArrayList<LogEntryField>(getFieldsInGroup(fieldGroup));
            for (int i = 0; i < fields.size(); i++) {
                if(fields.get(i) == LogEntryField.NUMBER) continue;
                fieldMessage.append(fields.get(i).getDescriptiveMessage());
                if(i != fields.size()-1)
                    fieldMessage.append("\n\n");
            }
            {if (true) throw new ParseException(String.format("Invalid field \"%s\". Valid fields for group \"%s\" are:\n%s", identifier.image, fieldGroup, fieldMessage));}
        }
        if(field == LogEntryField.NUMBER)
            {if (true) throw new ParseException("Field " + LogEntryField.NUMBER + " is ephemeral and cannot be used in filters.");}
        {if ("" != null) return field;}
    throw new Error("Missing return statement in function");
}

//END IDENTIFIERS


//TYPES
  final public 
BigDecimal Number() throws ParseException {Token t;
    t = jj_consume_token(NUMBER);
{if ("" != null) return new BigDecimal(t.image);}
    throw new Error("Missing return statement in function");
}

  final public String String() throws ParseException {Token t = null;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OPEN_SINGLE_QUOTE_STRING:{
      jj_consume_token(OPEN_SINGLE_QUOTE_STRING);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SINGLE_STRING_BODY:{
        t = jj_consume_token(SINGLE_STRING_BODY);
        break;
        }
      default:
        jj_la1[17] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CLOSE_SINGLE_QUOTE_STRING:{
        jj_consume_token(CLOSE_SINGLE_QUOTE_STRING);
        break;
        }
      default:
        jj_la1[18] = jj_gen;
{if (true) throw new ParseException("Missing closing quote for string.");}
      }
      break;
      }
    case OPEN_DOUBLE_QUOTE_STRING:{
      jj_consume_token(OPEN_DOUBLE_QUOTE_STRING);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DOUBLE_STRING_BODY:{
        t = jj_consume_token(DOUBLE_STRING_BODY);
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CLOSE_DOUBLE_QUOTE_STRING:{
        jj_consume_token(CLOSE_DOUBLE_QUOTE_STRING);
        break;
        }
      default:
        jj_la1[20] = jj_gen;
{if (true) throw new ParseException("Missing closing quote for string.");}
      }
      break;
      }
    default:
      jj_la1[21] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
if(t == null) {if ("" != null) return "";}
        else {if ("" != null) return org.apache.commons.text.StringEscapeUtils.unescapeJava(t.image);}
    throw new Error("Missing return statement in function");
}

  final public Pattern RegexInString() throws ParseException {Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OPEN_SINGLE_QUOTE_STRING:{
      jj_consume_token(OPEN_SINGLE_QUOTE_STRING);
      t = jj_consume_token(SINGLE_STRING_BODY);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CLOSE_SINGLE_QUOTE_STRING:{
        jj_consume_token(CLOSE_SINGLE_QUOTE_STRING);
        break;
        }
      default:
        jj_la1[22] = jj_gen;
{if (true) throw new ParseException("Missing closing quote for regex string.");}
      }
      break;
      }
    case OPEN_DOUBLE_QUOTE_STRING:{
      jj_consume_token(OPEN_DOUBLE_QUOTE_STRING);
      t = jj_consume_token(DOUBLE_STRING_BODY);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CLOSE_DOUBLE_QUOTE_STRING:{
        jj_consume_token(CLOSE_DOUBLE_QUOTE_STRING);
        break;
        }
      default:
        jj_la1[23] = jj_gen;
{if (true) throw new ParseException("Missing closing quote for regex string.");}
      }
      break;
      }
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
{if ("" != null) return Pattern.compile(t.image, Pattern.CASE_INSENSITIVE);}
    throw new Error("Missing return statement in function");
}

  final public Pattern RegexInForwardSlashes() throws ParseException {Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case REGEXLITERAL_IN_FORWARD_SLASHES:{
      t = jj_consume_token(REGEXLITERAL_IN_FORWARD_SLASHES);
      break;
      }
    case REGEX_IN_FORWARD_SLASHES:{
      t = jj_consume_token(REGEX_IN_FORWARD_SLASHES);
      break;
      }
    default:
      jj_la1[25] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
{if ("" != null) return Pattern.compile(t.image.substring(1,t.image.length()-1), Pattern.CASE_INSENSITIVE);}
    throw new Error("Missing return statement in function");
}

  final public Boolean Boolean() throws ParseException {Token t, inverse=null;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INVERSE:{
      inverse = jj_consume_token(INVERSE);
      break;
      }
    default:
      jj_la1[26] = jj_gen;
      ;
    }
    t = jj_consume_token(BOOLEAN);
{if ("" != null) return (inverse != null ^ Boolean.parseBoolean(t.image));}
    throw new Error("Missing return statement in function");
}

//END TYPES


//BASIC OPERATORS
  final public 
Operator EqualityOperator() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case EQ:{
      jj_consume_token(EQ);
{if ("" != null) return Operator.EQUAL;}
      break;
      }
    case NEQ:{
      jj_consume_token(NEQ);
{if ("" != null) return Operator.NOT_EQUAL;}
      break;
      }
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
}

  final public Operator NumericOperator() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case GT:{
      jj_consume_token(GT);
{if ("" != null) return Operator.GREATER_THAN;}
      break;
      }
    case LT:{
      jj_consume_token(LT);
{if ("" != null) return Operator.LESS_THAN;}
      break;
      }
    case GEQ:{
      jj_consume_token(GEQ);
{if ("" != null) return Operator.GREATER_THAN_EQUAL;}
      break;
      }
    case LEQ:{
      jj_consume_token(LEQ);
{if ("" != null) return Operator.LESS_THAN_EQUAL;}
      break;
      }
    default:
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
}

  final public Operator ContainsOperator() throws ParseException {
    jj_consume_token(CONTAINS);
{if ("" != null) return Operator.CONTAINS;}
    throw new Error("Missing return statement in function");
}

  final public Operator MatchesOperator() throws ParseException {
    jj_consume_token(MATCHES);
{if ("" != null) return Operator.MATCHES;}
    throw new Error("Missing return statement in function");
}

  final public Operator InOperator() throws ParseException {
    jj_consume_token(IN);
{if ("" != null) return Operator.IN;}
    throw new Error("Missing return statement in function");
}

  final public boolean Inverse() throws ParseException {
    jj_consume_token(INVERSE);
{if ("" != null) return true;}
    throw new Error("Missing return statement in function");
}

  final public BooleanOperator And() throws ParseException {
    jj_consume_token(AND);
{if ("" != null) return BooleanOperator.AND;}
    throw new Error("Missing return statement in function");
}

  final public BooleanOperator Or() throws ParseException {
    jj_consume_token(OR);
{if ("" != null) return BooleanOperator.OR;}
    throw new Error("Missing return statement in function");
}

  final public BooleanOperator Xor() throws ParseException {
    jj_consume_token(XOR);
{if ("" != null) return BooleanOperator.XOR;}
    throw new Error("Missing return statement in function");
}

  private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return (!jj_3_1()); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return (!jj_3_2()); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return (!jj_3_3()); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_3R_13()
 {
    if (jj_scan_token(OR)) return true;
    return false;
  }

  private boolean jj_3R_12()
 {
    if (jj_scan_token(AND)) return true;
    return false;
  }

  private boolean jj_3R_27()
 {
    if (jj_3R_33()) return true;
    return false;
  }

  private boolean jj_3R_11()
 {
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3R_5()
 {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_22()
 {
    if (jj_scan_token(INVERSE)) return true;
    return false;
  }

  private boolean jj_3R_25()
 {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_24()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_25()) {
    jj_scanpos = xsp;
    if (jj_3R_26()) return true;
    }
    xsp = jj_scanpos;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) {
    jj_scanpos = xsp;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) {
    jj_scanpos = xsp;
    if (jj_3R_31()) {
    jj_scanpos = xsp;
    if (jj_3R_32()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_3()
 {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_36()
 {
    if (jj_scan_token(IN)) return true;
    return false;
  }

  private boolean jj_3R_16()
 {
    if (jj_3R_18()) return true;
    return false;
  }

  private boolean jj_3R_10()
 {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_37()
 {
    if (jj_scan_token(MATCHES)) return true;
    return false;
  }

  private boolean jj_3R_4()
 {
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3R_35()
 {
    if (jj_scan_token(CONTAINS)) return true;
    return false;
  }

  private boolean jj_3R_23()
 {
    if (jj_scan_token(ALIAS_SYMBOL)) return true;
    return false;
  }

  private boolean jj_3_2()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_4()) {
    jj_scanpos = xsp;
    if (jj_3R_5()) {
    jj_scanpos = xsp;
    if (jj_3R_6()) return true;
    }
    }
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_9()
 {
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3R_21()
 {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_43()
 {
    if (jj_scan_token(LEQ)) return true;
    return false;
  }

  private boolean jj_3R_3()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) return true;
    }
    }
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_42()
 {
    if (jj_scan_token(GEQ)) return true;
    return false;
  }

  private boolean jj_3R_41()
 {
    if (jj_scan_token(LT)) return true;
    return false;
  }

  private boolean jj_3R_40()
 {
    if (jj_scan_token(GT)) return true;
    return false;
  }

  private boolean jj_3R_34()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_40()) {
    jj_scanpos = xsp;
    if (jj_3R_41()) {
    jj_scanpos = xsp;
    if (jj_3R_42()) {
    jj_scanpos = xsp;
    if (jj_3R_43()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_39()
 {
    if (jj_scan_token(NEQ)) return true;
    return false;
  }

  private boolean jj_3R_18()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) return true;
    }
    return false;
  }

  private boolean jj_3R_20()
 {
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_38()
 {
    if (jj_scan_token(EQ)) return true;
    return false;
  }

  private boolean jj_3R_33()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_38()) {
    jj_scanpos = xsp;
    if (jj_3R_39()) return true;
    }
    return false;
  }

  private boolean jj_3R_32()
 {
    return false;
  }

  private boolean jj_3R_15()
 {
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3R_7()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_15()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) return true;
    }
    return false;
  }

  private boolean jj_3_1()
 {
    if (jj_3R_3()) return true;
    return false;
  }

  private boolean jj_3R_19()
 {
    if (jj_3R_22()) return true;
    return false;
  }

  private boolean jj_3R_17()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_19()) jj_scanpos = xsp;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_31()
 {
    if (jj_3R_37()) return true;
    return false;
  }

  private boolean jj_3R_30()
 {
    if (jj_3R_36()) return true;
    return false;
  }

  private boolean jj_3R_29()
 {
    if (jj_3R_35()) return true;
    return false;
  }

  private boolean jj_3R_8()
 {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  private boolean jj_3R_26()
 {
    return false;
  }

  private boolean jj_3R_6()
 {
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3R_28()
 {
    if (jj_3R_34()) return true;
    return false;
  }

  private boolean jj_3R_14()
 {
    if (jj_scan_token(XOR)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public FilterParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[29];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
	   jj_la1_init_0();
	   jj_la1_init_1();
	}
	private static void jj_la1_init_0() {
	   jj_la1_0 = new int[] {0x200000,0x100000,0x280000,0x8000000,0x700,0x700,0x800000,0x30a36000,0x804000,0x30804000,0x1000000,0x30030000,0x418fc,0x30236000,0x30004000,0x4000000,0x30004000,0x40000000,0x80000000,0x0,0x0,0x30000000,0x80000000,0x0,0x30000000,0x30000,0x200000,0xc,0xf0,};
	}
	private static void jj_la1_init_1() {
	   jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x1,0x2,0x0,0x0,0x2,0x0,0x0,0x0,0x0,0x0,};
	}
  final private JJCalls[] jj_2_rtns = new JJCalls[3];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public FilterParser(java.io.InputStream stream) {
	  this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public FilterParser(java.io.InputStream stream, String encoding) {
	 try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
	 token_source = new FilterParserTokenManager(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
	 for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
	  ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
	 try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
	 token_source.ReInit(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jjtree.reset();
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
	 for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public FilterParser(java.io.Reader stream) {
	 jj_input_stream = new SimpleCharStream(stream, 1, 1);
	 token_source = new FilterParserTokenManager(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
	 for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
	if (jj_input_stream == null) {
	   jj_input_stream = new SimpleCharStream(stream, 1, 1);
	} else {
	   jj_input_stream.ReInit(stream, 1, 1);
	}
	if (token_source == null) {
 token_source = new FilterParserTokenManager(jj_input_stream);
	}

	 token_source.ReInit(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jjtree.reset();
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
	 for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public FilterParser(FilterParserTokenManager tm) {
	 token_source = tm;
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
	 for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(FilterParserTokenManager tm) {
	 token_source = tm;
	 token = new Token();
	 jj_ntk = -1;
	 jjtree.reset();
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
	 for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
	 Token oldToken;
	 if ((oldToken = token).next != null) token = token.next;
	 else token = token.next = token_source.getNextToken();
	 jj_ntk = -1;
	 if (token.kind == kind) {
	   jj_gen++;
	   if (++jj_gc > 100) {
		 jj_gc = 0;
		 for (int i = 0; i < jj_2_rtns.length; i++) {
		   JJCalls c = jj_2_rtns[i];
		   while (c != null) {
			 if (c.gen < jj_gen) c.first = null;
			 c = c.next;
		   }
		 }
	   }
	   return token;
	 }
	 token = oldToken;
	 jj_kind = kind;
	 throw generateParseException();
  }

  @SuppressWarnings("serial")
  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
	 if (jj_scanpos == jj_lastpos) {
	   jj_la--;
	   if (jj_scanpos.next == null) {
		 jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
	   } else {
		 jj_lastpos = jj_scanpos = jj_scanpos.next;
	   }
	 } else {
	   jj_scanpos = jj_scanpos.next;
	 }
	 if (jj_rescan) {
	   int i = 0; Token tok = token;
	   while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
	   if (tok != null) jj_add_error_token(kind, i);
	 }
	 if (jj_scanpos.kind != kind) return true;
	 if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
	 return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
	 if (token.next != null) token = token.next;
	 else token = token.next = token_source.getNextToken();
	 jj_ntk = -1;
	 jj_gen++;
	 return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
	 Token t = token;
	 for (int i = 0; i < index; i++) {
	   if (t.next != null) t = t.next;
	   else t = t.next = token_source.getNextToken();
	 }
	 return t;
  }

  private int jj_ntk_f() {
	 if ((jj_nt=token.next) == null)
	   return (jj_ntk = (token.next=token_source.getNextToken()).kind);
	 else
	   return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
	 if (pos >= 100) {
		return;
	 }

	 if (pos == jj_endpos + 1) {
	   jj_lasttokens[jj_endpos++] = kind;
	 } else if (jj_endpos != 0) {
	   jj_expentry = new int[jj_endpos];

	   for (int i = 0; i < jj_endpos; i++) {
		 jj_expentry[i] = jj_lasttokens[i];
	   }

	   for (int[] oldentry : jj_expentries) {
		 if (oldentry.length == jj_expentry.length) {
		   boolean isMatched = true;

		   for (int i = 0; i < jj_expentry.length; i++) {
			 if (oldentry[i] != jj_expentry[i]) {
			   isMatched = false;
			   break;
			 }

		   }
		   if (isMatched) {
			 jj_expentries.add(jj_expentry);
			 break;
		   }
		 }
	   }

	   if (pos != 0) {
		 jj_lasttokens[(jj_endpos = pos) - 1] = kind;
	   }
	 }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
	 jj_expentries.clear();
	 boolean[] la1tokens = new boolean[35];
	 if (jj_kind >= 0) {
	   la1tokens[jj_kind] = true;
	   jj_kind = -1;
	 }
	 for (int i = 0; i < 29; i++) {
	   if (jj_la1[i] == jj_gen) {
		 for (int j = 0; j < 32; j++) {
		   if ((jj_la1_0[i] & (1<<j)) != 0) {
			 la1tokens[j] = true;
		   }
		   if ((jj_la1_1[i] & (1<<j)) != 0) {
			 la1tokens[32+j] = true;
		   }
		 }
	   }
	 }
	 for (int i = 0; i < 35; i++) {
	   if (la1tokens[i]) {
		 jj_expentry = new int[1];
		 jj_expentry[0] = i;
		 jj_expentries.add(jj_expentry);
	   }
	 }
	 jj_endpos = 0;
	 jj_rescan_token();
	 jj_add_error_token(0, 0);
	 int[][] exptokseq = new int[jj_expentries.size()][];
	 for (int i = 0; i < jj_expentries.size(); i++) {
	   exptokseq[i] = jj_expentries.get(i);
	 }
	 return new ParseException(token, exptokseq, tokenImage);
  }

  private int trace_indent = 0;
  private boolean trace_enabled;

/** Trace enabled. */
  final public boolean trace_enabled() {
	 return trace_enabled;
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
	 jj_rescan = true;
	 for (int i = 0; i < 3; i++) {
	   try {
		 JJCalls p = jj_2_rtns[i];

		 do {
		   if (p.gen > jj_gen) {
			 jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
			 switch (i) {
			   case 0: jj_3_1(); break;
			   case 1: jj_3_2(); break;
			   case 2: jj_3_3(); break;
			 }
		   }
		   p = p.next;
		 } while (p != null);

		 } catch(LookaheadSuccess ls) { }
	 }
	 jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
	 JJCalls p = jj_2_rtns[index];
	 while (p.gen > jj_gen) {
	   if (p.next == null) { p = p.next = new JJCalls(); break; }
	   p = p.next;
	 }

	 p.gen = jj_gen + xla - jj_la; 
	 p.first = token;
	 p.arg = xla;
  }

  static final class JJCalls {
	 int gen;
	 Token first;
	 int arg;
	 JJCalls next;
  }

}