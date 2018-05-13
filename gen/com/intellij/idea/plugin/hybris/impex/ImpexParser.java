// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.impex;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes.*;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexParserUtils.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ImpexParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ANY_ATTRIBUTE_NAME) {
      r = any_attribute_name(b, 0);
    }
    else if (t == ANY_ATTRIBUTE_VALUE) {
      r = any_attribute_value(b, 0);
    }
    else if (t == ANY_HEADER_MODE) {
      r = any_header_mode(b, 0);
    }
    else if (t == ANY_HEADER_PARAMETER_NAME) {
      r = any_header_parameter_name(b, 0);
    }
    else if (t == ATTRIBUTE) {
      r = attribute(b, 0);
    }
    else if (t == BEAN_SHELL) {
      r = bean_shell(b, 0);
    }
    else if (t == COMMENT) {
      r = comment(b, 0);
    }
    else if (t == FULL_HEADER_PARAMETER) {
      r = full_header_parameter(b, 0);
    }
    else if (t == FULL_HEADER_TYPE) {
      r = full_header_type(b, 0);
    }
    else if (t == HEADER_LINE) {
      r = header_line(b, 0);
    }
    else if (t == HEADER_TYPE_NAME) {
      r = header_type_name(b, 0);
    }
    else if (t == MACRO_DECLARATION) {
      r = macro_declaration(b, 0);
    }
    else if (t == MACRO_NAME_DEC) {
      r = macro_name_dec(b, 0);
    }
    else if (t == MACRO_USAGE_DEC) {
      r = macro_usage_dec(b, 0);
    }
    else if (t == MODIFIERS) {
      r = modifiers(b, 0);
    }
    else if (t == PARAMETER) {
      r = parameter(b, 0);
    }
    else if (t == PARAMETERS) {
      r = parameters(b, 0);
    }
    else if (t == ROOT_MACRO_USAGE) {
      r = root_macro_usage(b, 0);
    }
    else if (t == STRING) {
      r = string(b, 0);
    }
    else if (t == SUB_PARAMETERS) {
      r = sub_parameters(b, 0);
    }
    else if (t == VALUE) {
      r = value(b, 0);
    }
    else if (t == VALUE_GROUP) {
      r = value_group(b, 0);
    }
    else if (t == VALUE_LINE) {
      r = value_line(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return impexFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(PARAMETERS, SUB_PARAMETERS),
  };

  /* ********************************************************** */
  // (ATTRIBUTE_NAME | string)+
  public static boolean any_attribute_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "any_attribute_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANY_ATTRIBUTE_NAME, "<any attribute name>");
    r = any_attribute_name_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!any_attribute_name_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "any_attribute_name", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ATTRIBUTE_NAME | string
  private static boolean any_attribute_name_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "any_attribute_name_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ATTRIBUTE_NAME);
    if (!r) r = string(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (
  //   ATTRIBUTE_VALUE
  //   | BOOLEAN
  //   | DIGIT
  //   | string
  //   | macro_usage_dec
  // )+
  public static boolean any_attribute_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "any_attribute_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANY_ATTRIBUTE_VALUE, "<any attribute value>");
    r = any_attribute_value_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!any_attribute_value_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "any_attribute_value", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ATTRIBUTE_VALUE
  //   | BOOLEAN
  //   | DIGIT
  //   | string
  //   | macro_usage_dec
  private static boolean any_attribute_value_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "any_attribute_value_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ATTRIBUTE_VALUE);
    if (!r) r = consumeToken(b, BOOLEAN);
    if (!r) r = consumeToken(b, DIGIT);
    if (!r) r = string(b, l + 1);
    if (!r) r = macro_usage_dec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // HEADER_MODE_INSERT | HEADER_MODE_UPDATE | HEADER_MODE_INSERT_UPDATE | HEADER_MODE_REMOVE
  public static boolean any_header_mode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "any_header_mode")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANY_HEADER_MODE, "<any header mode>");
    r = consumeToken(b, HEADER_MODE_INSERT);
    if (!r) r = consumeToken(b, HEADER_MODE_UPDATE);
    if (!r) r = consumeToken(b, HEADER_MODE_INSERT_UPDATE);
    if (!r) r = consumeToken(b, HEADER_MODE_REMOVE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // HEADER_PARAMETER_NAME | HEADER_SPECIAL_PARAMETER_NAME | macro_usage_dec | DOCUMENT_ID
  public static boolean any_header_parameter_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "any_header_parameter_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANY_HEADER_PARAMETER_NAME, "<any header parameter name>");
    r = consumeToken(b, HEADER_PARAMETER_NAME);
    if (!r) r = consumeToken(b, HEADER_SPECIAL_PARAMETER_NAME);
    if (!r) r = macro_usage_dec(b, l + 1);
    if (!r) r = consumeToken(b, DOCUMENT_ID);
    exit_section_(b, l, m, r, false, recover_parameter_name_parser_);
    return r;
  }

  /* ********************************************************** */
  // any_attribute_name ASSIGN_VALUE any_attribute_value
  public static boolean attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE, "<attribute>");
    r = any_attribute_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, ASSIGN_VALUE));
    r = p && any_attribute_value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // BEAN_SHELL_MARKER (BEAN_SHELL_BODY | MACRO_USAGE)?
  public static boolean bean_shell(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bean_shell")) return false;
    if (!nextTokenIs(b, BEAN_SHELL_MARKER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BEAN_SHELL, null);
    r = consumeToken(b, BEAN_SHELL_MARKER);
    p = r; // pin = 1
    r = r && bean_shell_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (BEAN_SHELL_BODY | MACRO_USAGE)?
  private static boolean bean_shell_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bean_shell_1")) return false;
    bean_shell_1_0(b, l + 1);
    return true;
  }

  // BEAN_SHELL_BODY | MACRO_USAGE
  private static boolean bean_shell_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bean_shell_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BEAN_SHELL_BODY);
    if (!r) r = consumeToken(b, MACRO_USAGE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // COMMENT_MARKER COMMENT_BODY?
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    if (!nextTokenIs(b, COMMENT_MARKER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMMENT, null);
    r = consumeToken(b, COMMENT_MARKER);
    p = r; // pin = 1
    r = r && comment_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // COMMENT_BODY?
  private static boolean comment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_1")) return false;
    consumeToken(b, COMMENT_BODY);
    return true;
  }

  /* ********************************************************** */
  // any_header_parameter_name parameters? modifiers*
  public static boolean full_header_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "full_header_parameter")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FULL_HEADER_PARAMETER, "<full header parameter>");
    r = any_header_parameter_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, full_header_parameter_1(b, l + 1));
    r = p && full_header_parameter_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, not_line_break_or_parameters_separator_parser_);
    return r || p;
  }

  // parameters?
  private static boolean full_header_parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "full_header_parameter_1")) return false;
    parameters(b, l + 1);
    return true;
  }

  // modifiers*
  private static boolean full_header_parameter_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "full_header_parameter_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!modifiers(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "full_header_parameter_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // header_type_name modifiers?
  public static boolean full_header_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "full_header_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FULL_HEADER_TYPE, "<full header type>");
    r = header_type_name(b, l + 1);
    r = r && full_header_type_1(b, l + 1);
    exit_section_(b, l, m, r, false, not_line_break_or_parameters_separator_parser_);
    return r;
  }

  // modifiers?
  private static boolean full_header_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "full_header_type_1")) return false;
    modifiers(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // any_header_mode full_header_type ((PARAMETERS_SEPARATOR full_header_parameter) | PARAMETERS_SEPARATOR)*
  public static boolean header_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_line")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, HEADER_LINE, "<header line>");
    r = any_header_mode(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, full_header_type(b, l + 1));
    r = p && header_line_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ((PARAMETERS_SEPARATOR full_header_parameter) | PARAMETERS_SEPARATOR)*
  private static boolean header_line_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_line_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!header_line_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "header_line_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // (PARAMETERS_SEPARATOR full_header_parameter) | PARAMETERS_SEPARATOR
  private static boolean header_line_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_line_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header_line_2_0_0(b, l + 1);
    if (!r) r = consumeToken(b, PARAMETERS_SEPARATOR);
    exit_section_(b, m, null, r);
    return r;
  }

  // PARAMETERS_SEPARATOR full_header_parameter
  private static boolean header_line_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_line_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARAMETERS_SEPARATOR);
    r = r && full_header_parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // HEADER_TYPE
  public static boolean header_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_type_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEADER_TYPE_NAME, "<header type name>");
    r = consumeToken(b, HEADER_TYPE);
    exit_section_(b, l, m, r, false, recover_header_type_parser_);
    return r;
  }

  /* ********************************************************** */
  // root*
  static boolean impexFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "impexFile")) return false;
    int c = current_position_(b);
    while (true) {
      if (!root(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "impexFile", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // macro_name_dec ASSIGN_VALUE (
  //       MACRO_VALUE
  //     | SINGLE_STRING
  //     | DOUBLE_STRING
  //     | HEADER_SPECIAL_PARAMETER_NAME
  //     | macro_usage_dec
  //     | LEFT_ROUND_BRACKET
  //     | RIGHT_ROUND_BRACKET
  //     | LEFT_SQUARE_BRACKET
  //     | RIGHT_SQUARE_BRACKET
  //     | ASSIGN_VALUE
  //     | BOOLEAN
  //     | DIGIT
  //     | FIELD_VALUE_IGNORE
  //     | COMMA
  //     | HEADER_MODE_INSERT
  //     | HEADER_MODE_UPDATE
  //     | HEADER_MODE_INSERT_UPDATE
  //     | HEADER_MODE_REMOVE
  //     )+
  public static boolean macro_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macro_declaration")) return false;
    if (!nextTokenIs(b, MACRO_NAME_DECLARATION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MACRO_DECLARATION, null);
    r = macro_name_dec(b, l + 1);
    r = r && consumeToken(b, ASSIGN_VALUE);
    p = r; // pin = 2
    r = r && macro_declaration_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (
  //       MACRO_VALUE
  //     | SINGLE_STRING
  //     | DOUBLE_STRING
  //     | HEADER_SPECIAL_PARAMETER_NAME
  //     | macro_usage_dec
  //     | LEFT_ROUND_BRACKET
  //     | RIGHT_ROUND_BRACKET
  //     | LEFT_SQUARE_BRACKET
  //     | RIGHT_SQUARE_BRACKET
  //     | ASSIGN_VALUE
  //     | BOOLEAN
  //     | DIGIT
  //     | FIELD_VALUE_IGNORE
  //     | COMMA
  //     | HEADER_MODE_INSERT
  //     | HEADER_MODE_UPDATE
  //     | HEADER_MODE_INSERT_UPDATE
  //     | HEADER_MODE_REMOVE
  //     )+
  private static boolean macro_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macro_declaration_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macro_declaration_2_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!macro_declaration_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "macro_declaration_2", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // MACRO_VALUE
  //     | SINGLE_STRING
  //     | DOUBLE_STRING
  //     | HEADER_SPECIAL_PARAMETER_NAME
  //     | macro_usage_dec
  //     | LEFT_ROUND_BRACKET
  //     | RIGHT_ROUND_BRACKET
  //     | LEFT_SQUARE_BRACKET
  //     | RIGHT_SQUARE_BRACKET
  //     | ASSIGN_VALUE
  //     | BOOLEAN
  //     | DIGIT
  //     | FIELD_VALUE_IGNORE
  //     | COMMA
  //     | HEADER_MODE_INSERT
  //     | HEADER_MODE_UPDATE
  //     | HEADER_MODE_INSERT_UPDATE
  //     | HEADER_MODE_REMOVE
  private static boolean macro_declaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macro_declaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MACRO_VALUE);
    if (!r) r = consumeToken(b, SINGLE_STRING);
    if (!r) r = consumeToken(b, DOUBLE_STRING);
    if (!r) r = consumeToken(b, HEADER_SPECIAL_PARAMETER_NAME);
    if (!r) r = macro_usage_dec(b, l + 1);
    if (!r) r = consumeToken(b, LEFT_ROUND_BRACKET);
    if (!r) r = consumeToken(b, RIGHT_ROUND_BRACKET);
    if (!r) r = consumeToken(b, LEFT_SQUARE_BRACKET);
    if (!r) r = consumeToken(b, RIGHT_SQUARE_BRACKET);
    if (!r) r = consumeToken(b, ASSIGN_VALUE);
    if (!r) r = consumeToken(b, BOOLEAN);
    if (!r) r = consumeToken(b, DIGIT);
    if (!r) r = consumeToken(b, FIELD_VALUE_IGNORE);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, HEADER_MODE_INSERT);
    if (!r) r = consumeToken(b, HEADER_MODE_UPDATE);
    if (!r) r = consumeToken(b, HEADER_MODE_INSERT_UPDATE);
    if (!r) r = consumeToken(b, HEADER_MODE_REMOVE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // MACRO_NAME_DECLARATION
  public static boolean macro_name_dec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macro_name_dec")) return false;
    if (!nextTokenIs(b, MACRO_NAME_DECLARATION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MACRO_NAME_DECLARATION);
    exit_section_(b, m, MACRO_NAME_DEC, r);
    return r;
  }

  /* ********************************************************** */
  // MACRO_USAGE
  public static boolean macro_usage_dec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macro_usage_dec")) return false;
    if (!nextTokenIs(b, MACRO_USAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MACRO_USAGE);
    exit_section_(b, m, MACRO_USAGE_DEC, r);
    return r;
  }

  /* ********************************************************** */
  // LEFT_SQUARE_BRACKET ((attribute | macro_usage_dec) (ATTRIBUTE_SEPARATOR (attribute | macro_usage_dec))* ) RIGHT_SQUARE_BRACKET
  public static boolean modifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODIFIERS, "<modifiers>");
    r = consumeToken(b, LEFT_SQUARE_BRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, modifiers_1(b, l + 1));
    r = p && consumeToken(b, RIGHT_SQUARE_BRACKET) && r;
    exit_section_(b, l, m, r, p, recover_modifiers_parser_);
    return r || p;
  }

  // (attribute | macro_usage_dec) (ATTRIBUTE_SEPARATOR (attribute | macro_usage_dec))*
  private static boolean modifiers_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = modifiers_1_0(b, l + 1);
    r = r && modifiers_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // attribute | macro_usage_dec
  private static boolean modifiers_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute(b, l + 1);
    if (!r) r = macro_usage_dec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ATTRIBUTE_SEPARATOR (attribute | macro_usage_dec))*
  private static boolean modifiers_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers_1_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!modifiers_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modifiers_1_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ATTRIBUTE_SEPARATOR (attribute | macro_usage_dec)
  private static boolean modifiers_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ATTRIBUTE_SEPARATOR);
    r = r && modifiers_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // attribute | macro_usage_dec
  private static boolean modifiers_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers_1_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute(b, l + 1);
    if (!r) r = macro_usage_dec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(CRLF)
  static boolean not_line_break(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_line_break")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, CRLF);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(CRLF | PARAMETERS_SEPARATOR)
  static boolean not_line_break_or_parameters_separator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_line_break_or_parameters_separator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !not_line_break_or_parameters_separator_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CRLF | PARAMETERS_SEPARATOR
  private static boolean not_line_break_or_parameters_separator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_line_break_or_parameters_separator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, PARAMETERS_SEPARATOR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (HEADER_PARAMETER_NAME | macro_usage_dec | DOCUMENT_ID) (sub_parameters | ((DOT)? macro_usage_dec))? modifiers*
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = parameter_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, parameter_1(b, l + 1));
    r = p && parameter_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // HEADER_PARAMETER_NAME | macro_usage_dec | DOCUMENT_ID
  private static boolean parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HEADER_PARAMETER_NAME);
    if (!r) r = macro_usage_dec(b, l + 1);
    if (!r) r = consumeToken(b, DOCUMENT_ID);
    exit_section_(b, m, null, r);
    return r;
  }

  // (sub_parameters | ((DOT)? macro_usage_dec))?
  private static boolean parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1")) return false;
    parameter_1_0(b, l + 1);
    return true;
  }

  // sub_parameters | ((DOT)? macro_usage_dec)
  private static boolean parameter_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_parameters(b, l + 1);
    if (!r) r = parameter_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOT)? macro_usage_dec
  private static boolean parameter_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter_1_0_1_0(b, l + 1);
    r = r && macro_usage_dec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOT)?
  private static boolean parameter_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1_0_1_0")) return false;
    consumeToken(b, DOT);
    return true;
  }

  // modifiers*
  private static boolean parameter_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!modifiers(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // LEFT_ROUND_BRACKET (parameter ((COMMA | ALTERNATIVE_PATTERN) parameter)*)? RIGHT_ROUND_BRACKET
  public static boolean parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETERS, "<parameters>");
    r = consumeToken(b, LEFT_ROUND_BRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, parameters_1(b, l + 1));
    r = p && consumeToken(b, RIGHT_ROUND_BRACKET) && r;
    exit_section_(b, l, m, r, p, recover_parameters_parser_);
    return r || p;
  }

  // (parameter ((COMMA | ALTERNATIVE_PATTERN) parameter)*)?
  private static boolean parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1")) return false;
    parameters_1_0(b, l + 1);
    return true;
  }

  // parameter ((COMMA | ALTERNATIVE_PATTERN) parameter)*
  private static boolean parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && parameters_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ((COMMA | ALTERNATIVE_PATTERN) parameter)*
  private static boolean parameters_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!parameters_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameters_1_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // (COMMA | ALTERNATIVE_PATTERN) parameter
  private static boolean parameters_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameters_1_0_1_0_0(b, l + 1);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA | ALTERNATIVE_PATTERN
  private static boolean parameters_1_0_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1_0_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, ALTERNATIVE_PATTERN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(LEFT_SQUARE_BRACKET | PARAMETERS_SEPARATOR | CRLF)
  static boolean recover_header_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_header_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_header_type_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LEFT_SQUARE_BRACKET | PARAMETERS_SEPARATOR | CRLF
  private static boolean recover_header_type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_header_type_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_SQUARE_BRACKET);
    if (!r) r = consumeToken(b, PARAMETERS_SEPARATOR);
    if (!r) r = consumeToken(b, CRLF);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(ALTERNATIVE_PATTERN | COMMA | CRLF | LEFT_SQUARE_BRACKET | PARAMETERS_SEPARATOR | RIGHT_ROUND_BRACKET)
  static boolean recover_modifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_modifiers")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_modifiers_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ALTERNATIVE_PATTERN | COMMA | CRLF | LEFT_SQUARE_BRACKET | PARAMETERS_SEPARATOR | RIGHT_ROUND_BRACKET
  private static boolean recover_modifiers_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_modifiers_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ALTERNATIVE_PATTERN);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, LEFT_SQUARE_BRACKET);
    if (!r) r = consumeToken(b, PARAMETERS_SEPARATOR);
    if (!r) r = consumeToken(b, RIGHT_ROUND_BRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(LEFT_SQUARE_BRACKET | LEFT_ROUND_BRACKET | PARAMETERS_SEPARATOR | CRLF)
  static boolean recover_parameter_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_parameter_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_parameter_name_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LEFT_SQUARE_BRACKET | LEFT_ROUND_BRACKET | PARAMETERS_SEPARATOR | CRLF
  private static boolean recover_parameter_name_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_parameter_name_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_SQUARE_BRACKET);
    if (!r) r = consumeToken(b, LEFT_ROUND_BRACKET);
    if (!r) r = consumeToken(b, PARAMETERS_SEPARATOR);
    if (!r) r = consumeToken(b, CRLF);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(CRLF | LEFT_SQUARE_BRACKET | PARAMETERS_SEPARATOR)
  static boolean recover_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_parameters")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_parameters_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CRLF | LEFT_SQUARE_BRACKET | PARAMETERS_SEPARATOR
  private static boolean recover_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_parameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, LEFT_SQUARE_BRACKET);
    if (!r) r = consumeToken(b, PARAMETERS_SEPARATOR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(
  //      CRLF
  //   |  HEADER_MODE_INSERT
  //   |  HEADER_MODE_INSERT_UPDATE
  //   |  HEADER_MODE_REMOVE
  //   |  HEADER_MODE_UPDATE
  //   |  VALUE_SUBTYPE
  //   |  FIELD_VALUE_SEPARATOR
  //   |  COMMENT_MARKER
  //   |  macro_name_dec
  //   |  BEAN_SHELL_MARKER
  //   |  DOUBLE_STRING
  //   |  SINGLE_STRING
  //   |  MACRO_USAGE
  // )
  static boolean recover_root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_root")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_root_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CRLF
  //   |  HEADER_MODE_INSERT
  //   |  HEADER_MODE_INSERT_UPDATE
  //   |  HEADER_MODE_REMOVE
  //   |  HEADER_MODE_UPDATE
  //   |  VALUE_SUBTYPE
  //   |  FIELD_VALUE_SEPARATOR
  //   |  COMMENT_MARKER
  //   |  macro_name_dec
  //   |  BEAN_SHELL_MARKER
  //   |  DOUBLE_STRING
  //   |  SINGLE_STRING
  //   |  MACRO_USAGE
  private static boolean recover_root_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_root_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, HEADER_MODE_INSERT);
    if (!r) r = consumeToken(b, HEADER_MODE_INSERT_UPDATE);
    if (!r) r = consumeToken(b, HEADER_MODE_REMOVE);
    if (!r) r = consumeToken(b, HEADER_MODE_UPDATE);
    if (!r) r = consumeToken(b, VALUE_SUBTYPE);
    if (!r) r = consumeToken(b, FIELD_VALUE_SEPARATOR);
    if (!r) r = consumeToken(b, COMMENT_MARKER);
    if (!r) r = macro_name_dec(b, l + 1);
    if (!r) r = consumeToken(b, BEAN_SHELL_MARKER);
    if (!r) r = consumeToken(b, DOUBLE_STRING);
    if (!r) r = consumeToken(b, SINGLE_STRING);
    if (!r) r = consumeToken(b, MACRO_USAGE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CRLF | ( !<<eof>> root_group (CRLF | <<eof>> ))
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, CRLF);
    if (!r) r = root_1(b, l + 1);
    exit_section_(b, l, m, r, false, recover_root_parser_);
    return r;
  }

  // !<<eof>> root_group (CRLF | <<eof>> )
  private static boolean root_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = root_1_0(b, l + 1);
    r = r && root_group(b, l + 1);
    r = r && root_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !<<eof>>
  private static boolean root_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CRLF | <<eof>>
  private static boolean root_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CRLF);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // root_macro_usage
  //     | header_line
  //     | value_line
  //     | comment
  //     | bean_shell
  //     | (string (';')?)
  //     | macro_declaration
  static boolean root_group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_group")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = root_macro_usage(b, l + 1);
    if (!r) r = header_line(b, l + 1);
    if (!r) r = value_line(b, l + 1);
    if (!r) r = comment(b, l + 1);
    if (!r) r = bean_shell(b, l + 1);
    if (!r) r = root_group_5(b, l + 1);
    if (!r) r = macro_declaration(b, l + 1);
    exit_section_(b, l, m, r, false, not_line_break_parser_);
    return r;
  }

  // string (';')?
  private static boolean root_group_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_group_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string(b, l + 1);
    r = r && root_group_5_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (';')?
  private static boolean root_group_5_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_group_5_1")) return false;
    root_group_5_1_0(b, l + 1);
    return true;
  }

  // (';')
  private static boolean root_group_5_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_group_5_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ";");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // MACRO_USAGE FIELD_VALUE_SEPARATOR*
  public static boolean root_macro_usage(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_macro_usage")) return false;
    if (!nextTokenIs(b, MACRO_USAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MACRO_USAGE);
    r = r && root_macro_usage_1(b, l + 1);
    exit_section_(b, m, ROOT_MACRO_USAGE, r);
    return r;
  }

  // FIELD_VALUE_SEPARATOR*
  private static boolean root_macro_usage_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_macro_usage_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!consumeToken(b, FIELD_VALUE_SEPARATOR)) break;
      if (!empty_element_parsed_guard_(b, "root_macro_usage_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // SINGLE_STRING | DOUBLE_STRING
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    if (!nextTokenIs(b, "<string>", DOUBLE_STRING, SINGLE_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING, "<string>");
    r = consumeToken(b, SINGLE_STRING);
    if (!r) r = consumeToken(b, DOUBLE_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LEFT_ROUND_BRACKET (parameter ((COMMA | ALTERNATIVE_PATTERN) parameter)*)? RIGHT_ROUND_BRACKET
  public static boolean sub_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_parameters")) return false;
    if (!nextTokenIs(b, LEFT_ROUND_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SUB_PARAMETERS, null);
    r = consumeToken(b, LEFT_ROUND_BRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, sub_parameters_1(b, l + 1));
    r = p && consumeToken(b, RIGHT_ROUND_BRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (parameter ((COMMA | ALTERNATIVE_PATTERN) parameter)*)?
  private static boolean sub_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_parameters_1")) return false;
    sub_parameters_1_0(b, l + 1);
    return true;
  }

  // parameter ((COMMA | ALTERNATIVE_PATTERN) parameter)*
  private static boolean sub_parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_parameters_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && sub_parameters_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ((COMMA | ALTERNATIVE_PATTERN) parameter)*
  private static boolean sub_parameters_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_parameters_1_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!sub_parameters_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sub_parameters_1_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // (COMMA | ALTERNATIVE_PATTERN) parameter
  private static boolean sub_parameters_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_parameters_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_parameters_1_0_1_0_0(b, l + 1);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA | ALTERNATIVE_PATTERN
  private static boolean sub_parameters_1_0_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_parameters_1_0_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, ALTERNATIVE_PATTERN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ((  FIELD_VALUE
  //              | BOOLEAN
  //              | DIGIT
  //              | string
  //              | macro_usage_dec
  //              | FIELD_LIST_ITEM_SEPARATOR
  //              | DEFAULT_PATH_DELIMITER
  //              | DEFAULT_KEY_VALUE_DELIMITER
  //              | ALTERNATIVE_MAP_DELIMITER
  //             )+
  //            ) | FIELD_VALUE_IGNORE
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = value_0(b, l + 1);
    if (!r) r = consumeToken(b, FIELD_VALUE_IGNORE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (  FIELD_VALUE
  //              | BOOLEAN
  //              | DIGIT
  //              | string
  //              | macro_usage_dec
  //              | FIELD_LIST_ITEM_SEPARATOR
  //              | DEFAULT_PATH_DELIMITER
  //              | DEFAULT_KEY_VALUE_DELIMITER
  //              | ALTERNATIVE_MAP_DELIMITER
  //             )+
  private static boolean value_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_0_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!value_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "value_0", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // FIELD_VALUE
  //              | BOOLEAN
  //              | DIGIT
  //              | string
  //              | macro_usage_dec
  //              | FIELD_LIST_ITEM_SEPARATOR
  //              | DEFAULT_PATH_DELIMITER
  //              | DEFAULT_KEY_VALUE_DELIMITER
  //              | ALTERNATIVE_MAP_DELIMITER
  private static boolean value_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FIELD_VALUE);
    if (!r) r = consumeToken(b, BOOLEAN);
    if (!r) r = consumeToken(b, DIGIT);
    if (!r) r = string(b, l + 1);
    if (!r) r = macro_usage_dec(b, l + 1);
    if (!r) r = consumeToken(b, FIELD_LIST_ITEM_SEPARATOR);
    if (!r) r = consumeToken(b, DEFAULT_PATH_DELIMITER);
    if (!r) r = consumeToken(b, DEFAULT_KEY_VALUE_DELIMITER);
    if (!r) r = consumeToken(b, ALTERNATIVE_MAP_DELIMITER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FIELD_VALUE_SEPARATOR value?
  public static boolean value_group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_group")) return false;
    if (!nextTokenIs(b, FIELD_VALUE_SEPARATOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VALUE_GROUP, null);
    r = consumeToken(b, FIELD_VALUE_SEPARATOR);
    p = r; // pin = 1
    r = r && value_group_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // value?
  private static boolean value_group_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_group_1")) return false;
    value(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (VALUE_SUBTYPE value_group*) | (value_group+)
  public static boolean value_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_line")) return false;
    if (!nextTokenIs(b, "<value line>", FIELD_VALUE_SEPARATOR, VALUE_SUBTYPE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_LINE, "<value line>");
    r = value_line_0(b, l + 1);
    if (!r) r = value_line_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // VALUE_SUBTYPE value_group*
  private static boolean value_line_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_line_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUE_SUBTYPE);
    r = r && value_line_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // value_group*
  private static boolean value_line_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_line_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!value_group(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "value_line_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // value_group+
  private static boolean value_line_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_line_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_group(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!value_group(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "value_line_1", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  final static Parser not_line_break_or_parameters_separator_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return not_line_break_or_parameters_separator(b, l + 1);
    }
  };
  final static Parser not_line_break_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return not_line_break(b, l + 1);
    }
  };
  final static Parser recover_header_type_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_header_type(b, l + 1);
    }
  };
  final static Parser recover_modifiers_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_modifiers(b, l + 1);
    }
  };
  final static Parser recover_parameter_name_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_parameter_name(b, l + 1);
    }
  };
  final static Parser recover_parameters_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_parameters(b, l + 1);
    }
  };
  final static Parser recover_root_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_root(b, l + 1);
    }
  };
}
