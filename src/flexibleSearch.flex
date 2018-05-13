/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import com.intellij.psi.TokenType;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.*;
import static com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchParserDefinition.COMMENT;
%%

%public
%class FlexibleSearchLexer
%implements FlexLexer
/* We're scanning text. */
%unicode

%{
    private int myPrevState = YYINITIAL;
    
    public int yyprevstate() {
        return myPrevState;
    }
    
    private int popState() {
        final int prev = myPrevState;
        myPrevState = YYINITIAL;
        return prev;
    }
    
    protected void pushState(int state){
        myPrevState = state;
    }

%}

%function advance
%type IElementType
%eof{
    return;
%eof}


/* Ignore case when matching keywords */
%ignorecase


IDENTIFIER                      = [:jletter:] [:jletterdigit:]*
LINE_TERMINATOR                 = \r|\n|\r\n

/* comments */
COMMENT                         = "-""-"[^\r\n]*

// Strings
STRING_LITERAL                  = \'([^\\\'\r\n]|{ESCAPE_SEQUENCE})*(\'|\\)?
ESCAPE_SEQUENCE                 = \\[^\r\n]

/* main character classes */
DIGIT                           = [:digit:]
INTEGER                         = {DIGIT}+
WHITE_SPACE                     = \s+

PERCENT                         = [%]
QUOTE                           = [']
COMMA                           = ","
DOT                             = "."
COLON                           = [:]
SEMICOLON                       = [;]
LEFT_PAREN                      = [(]
RIGHT_PAREN                     = [)]
ASTERISK                        = [*]
PLUS_SIGN                       = [+]
MINUS_SIGN                      = [-]
LESS_THAN_OPERATOR              = [<]
EQUALS_OPERATOR                 = [=]
GREATER_THAN_OPERATOR           = [>]
LESS_THAN_OR_EQUALS_OPERATOR    = <=
GREATER_THAN_OR_EQUALS_OPERATOR = >=
NON_EQUAL_OPERATOR              = <>
QUESTION_MARK                   = [?]
EXCLAMATION_MARK                = [!]
LEFT_BRACKET                    = [\[]
RIGHT_BRACKET                   = [\]]
UNDERSCORE                      = [_]
LEFT_BRACE                      = "{"
LEFT_DOUBLE_BRACE               = "{{"
RIGHT_BRACE                     = "}"
RIGHT_DOUBLE_BRACE              = "}}"
EOL                             = \n|\r\n

%state SELECT_EXP 
%state FROM_EXP 
%state TABLE_IDENTIFIER 
%state COLUMN_IDENTIFIER 
%state LOCALIZATION 
%state WHERE_EXP
%state ON_EXP
%state CORRELATION_NAME 
%state SUB_QUERY 

%%

<YYINITIAL> {
    {INTEGER}                              { return NUMBER; }
    {COMMENT}                              { return COMMENT; }

    /* keywords */
    "SELECT"                               { yybegin(SELECT_EXP); pushState(YYINITIAL); return SELECT; }
    "AS"                                   { yybegin(CORRELATION_NAME);return AS; }
    "DISTINCT"                             { return DISTINCT; }
    "ALL"                                  { return ALL; }
    "COUNT"                                { return COUNT; }
    "CONCAT"                               { return CONCAT; }
    "AVG"                                  { return AVG; }
    "MAX"                                  { return MAX; }
    "MIN"                                  { return MIN; }
    "SUM"                                  { return SUM; }
    "EVERY"                                { return EVERY; }
    "ANY"                                  { return ANY; }
    "SOME"                                 { return SOME; }
    "ORDER"                                { return ORDER; }
    "BY"                                   { return BY; }
    "ASC"                                  { return ASC; }
    "DESC"                                 { return DESC; }
    "FIRST"                                { return FIRST; }
    "LAST"                                 { return LAST; }
    "FROM"                                 { yybegin(FROM_EXP); return FROM; }
    "JOIN"                                 { return JOIN; }
    "ON"                                   { return ON; }
    "UNION"                                { return UNION; }
    "WHERE"                                { yybegin(WHERE_EXP); return WHERE; }
    "AND"                                  { return AND; }
    "OR"                                   { return OR; }
    "NOT"                                  { return NOT; }
    "IS"                                   { return IS; }
    "IN"                                   { return IN; }
    "TRUE"                                 { return TRUE; }
    "FALSE"                                { return FALSE; }
    "NULL"                                 { return NULL; }
    "EXISTS"                               { return EXISTS; }
    "BETWEEN"                              { return BETWEEN; }
    "LIKE"                                 { return LIKE; }
    "CONCAT"                               { return CONCAT; }
    "GROUP"                                { return GROUP; }  

    
    {LEFT_BRACKET}                         { return RIGHT_BRACKET; }
    {RIGHT_BRACKET}                        { return RIGHT_BRACKET; }
    {LEFT_BRACE}                           { return LEFT_BRACE; }
    {RIGHT_BRACE}                          { return RIGHT_BRACE; }
    {LEFT_DOUBLE_BRACE}                    { return LEFT_DOUBLE_BRACE; }
    {RIGHT_DOUBLE_BRACE}                   { return RIGHT_DOUBLE_BRACE; }
    {LEFT_PAREN}                           { return LEFT_PAREN; }
    {RIGHT_PAREN}                          { return RIGHT_PAREN; }
    {DOT}                                  { return DOT; }
    {COMMA}                                { return COMMA; }
    {ASTERISK}                             { return ASTERISK; }
    {COLON}                                { return COLON; }
    {SEMICOLON}                            { return SEMICOLON; }
     
    
    
    {STRING_LITERAL}                       { return STRING; }
    {IDENTIFIER}                           { return IDENTIFIER; }
}


<SELECT_EXP> {
    {INTEGER}                              { return NUMBER; }
    {COMMENT}                              { return COMMENT; }
    
    {LEFT_BRACE}                           { yybegin(COLUMN_IDENTIFIER); pushState(SELECT_EXP); return LEFT_BRACE; }
    {RIGHT_BRACE}                          { return RIGHT_BRACE; }

    {LEFT_PAREN}                           { return LEFT_PAREN; }
    {RIGHT_PAREN}                          { return RIGHT_PAREN; }
    {IDENTIFIER}{DOT}                      { yypushback(yylength()); yybegin(COLUMN_IDENTIFIER); pushState(SELECT_EXP);}
    {DOT}                                  { return DOT; }
    {COMMA}                                { return COMMA; }
    {ASTERISK}                             { return ASTERISK; }
    {COLON}                                { return COLON; }
    {SEMICOLON}                            { return SEMICOLON; }
    
    /* keywords */
    "SELECT"                               { return SELECT; }
    "AS"                                   { yybegin(CORRELATION_NAME); pushState(SELECT_EXP); return AS; }
    "DISTINCT"                             { return DISTINCT; }
    "ALL"                                  { return ALL; }
    "COUNT"                                { return COUNT; }
    "CONCAT"                               { return CONCAT; }
    "AVG"                                  { return AVG; }
    "MAX"                                  { return MAX; }
    "MIN"                                  { return MIN; }
    "SUM"                                  { return SUM; }
    "EVERY"                                { return EVERY; }
    "ANY"                                  { return ANY; }
    "SOME"                                 { return SOME; }
    "BY"                                   { return BY; }
    "FROM"                                 { yybegin(FROM_EXP); pushState(SELECT_EXP); return FROM; }
    "IS"                                   { return IS; }
    "CONCAT"                               { return CONCAT; }
    
    "LEFT"                                 { yybegin(popState()); return LEFT; }
    
    {STRING_LITERAL}                       { return STRING; }
    {IDENTIFIER}                           { return COLUMN_REFERENCE_IDENTIFIER; }
}

<FROM_EXP> {
    {INTEGER}                              { return NUMBER; }
    {COMMENT}                              { return COMMENT; }

    {LEFT_BRACE}                           { yybegin(TABLE_IDENTIFIER); pushState(FROM_EXP); return LEFT_BRACE; }
    {RIGHT_BRACE}                          { return RIGHT_BRACE; }
    {LEFT_DOUBLE_BRACE}                    { yybegin(SUB_QUERY); pushState(FROM_EXP); return LEFT_DOUBLE_BRACE; }
    {RIGHT_DOUBLE_BRACE}                   { return RIGHT_DOUBLE_BRACE; }
    {LEFT_PAREN}                           { return LEFT_PAREN; }
    {RIGHT_PAREN}                          { return RIGHT_PAREN; }
    {DOT}                                  { return DOT; }
    {COMMA}                                { return COMMA; }
    {EXCLAMATION_MARK}                     { return EXCLAMATION_MARK; }
    {QUESTION_MARK}                        { return QUESTION_MARK; }
    {COLON}                                { return COLON; }
    {SEMICOLON}                            { return SEMICOLON; }


    /* keywords */
    "SELECT"                               { yybegin(SELECT_EXP); pushState(FROM_EXP); return SELECT; }
    "AS"                                   { yybegin(CORRELATION_NAME); pushState(FROM_EXP); return AS; }
    "ALL"                                  { return ALL; }
    "JOIN"                                 { yybegin(TABLE_IDENTIFIER); pushState(FROM_EXP); return JOIN; }
    "ON"                                   { yybegin(ON_EXP); pushState(FROM_EXP); return ON; }
    "LEFT"                                 { return LEFT; }
    "UNION"                                { return UNION; }
    "WHERE"                                { yybegin(WHERE_EXP); pushState(FROM_EXP); return WHERE; }
    "AND"                                  { return AND; }
    "OR"                                   { return OR; }
    "NOT"                                  { return NOT; }
    "IS"                                   { return IS; }
    "TRUE"                                 { return TRUE; }
    "FALSE"                                { return FALSE; }
    "NULL"                                 { return NULL; }
    "EXISTS"                               { return EXISTS; }
    "BETWEEN"                              { return BETWEEN; }
    "LIKE"                                 { return LIKE; }
    "CONCAT"                               { return CONCAT; }
    "GROUP"                                { return GROUP; }  
     
    
    {STRING_LITERAL}                       { return STRING; }
    {IDENTIFIER}                           { return IDENTIFIER; }
}

<TABLE_IDENTIFIER> {
    "AS"                                   { yybegin(CORRELATION_NAME); pushState(TABLE_IDENTIFIER); return AS; }
    
    {RIGHT_BRACE}                          { yybegin(popState()); return RIGHT_BRACE; }
    {COMMA}                                { yybegin(popState()); return COMMA; }
    "LEFT"                                 { yybegin(FROM_EXP); return LEFT; }

    {EXCLAMATION_MARK}                     { return EXCLAMATION_MARK; }
    {QUESTION_MARK}                        { return QUESTION_MARK; }
    
    {IDENTIFIER}{DOT}|{IDENTIFIER}{COLON}  { yypushback(1); yybegin(COLUMN_IDENTIFIER); return TABLE_NAME_IDENTIFIER; }

    {IDENTIFIER}                           { yybegin(popState()); return TABLE_NAME_IDENTIFIER; }
}

<WHERE_EXP> {
    {INTEGER}                              { return NUMBER; }
    {COMMENT}                              { return COMMENT; }

    /* keywords */
    "SELECT"                               { yybegin(SELECT_EXP); pushState(WHERE_EXP); return SELECT; }
    "ORDER"                                { return ORDER; }
    "BY"                                   { return BY; }
    "ASC"                                  { return ASC; }
    "DESC"                                 { return DESC; }
    "FIRST"                                { return FIRST; }
    "LAST"                                 { return LAST; }
    "AND"                                  { return AND; }
    "OR"                                   { return OR; }
    "NOT"                                  { return NOT; }
    "IS"                                   { return IS; }
    "IN"                                   { return IN; }
    "TRUE"                                 { return TRUE; }
    "FALSE"                                { return FALSE; }
    "NULL"                                 { return NULL; }
    "EXISTS"                               { return EXISTS; }
    "BETWEEN"                              { return BETWEEN; }
    "LIKE"                                 { return LIKE; }
    "CONCAT"                               { return CONCAT; }
    "GROUP"                                { return GROUP; }

    "LEFT"                                 { yybegin(FROM_EXP); pushState(WHERE_EXP); return LEFT; }
    {IDENTIFIER}{DOT}                      { yypushback(yylength()); yybegin(COLUMN_IDENTIFIER); pushState(WHERE_EXP);}
    
    {LEFT_BRACE}                           { yybegin(COLUMN_IDENTIFIER); pushState(WHERE_EXP); return LEFT_BRACE; }
    {RIGHT_BRACE}                          { return RIGHT_BRACE; }
    {LEFT_DOUBLE_BRACE}                    { return LEFT_DOUBLE_BRACE; }
    {RIGHT_DOUBLE_BRACE}                   { yybegin(popState()); return RIGHT_DOUBLE_BRACE; }
    {LEFT_PAREN}                           { return LEFT_PAREN; }
    {RIGHT_PAREN}                          { return RIGHT_PAREN; }
    {DOT}                                  { return DOT; }
    {COMMA}                                { return COMMA; }
    {EXCLAMATION_MARK}                     { return EXCLAMATION_MARK; }
    {QUESTION_MARK}                        { return QUESTION_MARK; }
    {COLON}                                { return COLON; }
    {SEMICOLON}                            { return SEMICOLON; }
     
    /* operators */
    {GREATER_THAN_OR_EQUALS_OPERATOR}      { return GREATER_THAN_OR_EQUALS_OPERATOR; }
    {LESS_THAN_OR_EQUALS_OPERATOR}         { return LESS_THAN_OR_EQUALS_OPERATOR; }
    {EQUALS_OPERATOR}                      { return EQUALS_OPERATOR; }
    {LESS_THAN_OPERATOR}                   { return LESS_THAN_OPERATOR; }
    {GREATER_THAN_OPERATOR}                { return GREATER_THAN_OPERATOR; }
    {NON_EQUAL_OPERATOR}                   { return NOT_EQUALS_OPERATOR; }

    
    {STRING_LITERAL}                       { return STRING; }
    {IDENTIFIER}                           { return COLUMN_REFERENCE_IDENTIFIER; }
}

<ON_EXP> {
    {INTEGER}                              { return NUMBER; }
    {COMMENT}                              { return COMMENT; }
    
    {LEFT_BRACE}                           { yybegin(COLUMN_IDENTIFIER); pushState(ON_EXP); return LEFT_BRACE; }
    {RIGHT_BRACE}                          { yybegin(popState()); return RIGHT_BRACE; }
     
    "LEFT"                                 { yybegin(FROM_EXP); return LEFT; }

    /* operators */
    {EQUALS_OPERATOR}                      { return EQUALS_OPERATOR; }
    
    {IDENTIFIER}                           { return IDENTIFIER; }
}

<COLUMN_IDENTIFIER> {
    "AS"                                   { yybegin(CORRELATION_NAME); pushState(COLUMN_IDENTIFIER); return AS; }
    
    {IDENTIFIER}{LEFT_BRACKET}             { yypushback(1); yybegin(LOCALIZATION); return COLUMN_REFERENCE_IDENTIFIER;}
    {IDENTIFIER}{DOT}|{IDENTIFIER}{COLON}  { yypushback(yylength()); yybegin(TABLE_IDENTIFIER); }
    {DOT}                                  { return DOT; }
    {COMMA}                                { yybegin(popState()); return COMMA; }
    {ASTERISK}                             { return ASTERISK; }
    {COLON}                                { return COLON; }
    {SEMICOLON}                            { return SEMICOLON; }

    {RIGHT_PAREN}                          { yybegin(popState()); return RIGHT_PAREN; }
    {RIGHT_BRACE}                          { yybegin(popState()); return RIGHT_BRACE; }
    
    {EQUALS_OPERATOR}                      { yybegin(popState()); return EQUALS_OPERATOR; }
    {WHITE_SPACE}                          { yybegin(popState()); return WHITE_SPACE; }
    
    {IDENTIFIER}                           { return COLUMN_REFERENCE_IDENTIFIER; }
}

<LOCALIZATION> {
    {LEFT_BRACKET}                         { return LEFT_BRACKET; }
    {RIGHT_BRACKET}                        { yybegin(COLUMN_IDENTIFIER); return RIGHT_BRACKET; }
    
    {IDENTIFIER}                           { return IDENTIFIER; }
}

<SUB_QUERY> {
    "SELECT"                               { yybegin(SELECT_EXP); pushState(SUB_QUERY); return SELECT; }
    
    {RIGHT_DOUBLE_BRACE}                   { yybegin(FROM_EXP); return RIGHT_DOUBLE_BRACE; }
}

<CORRELATION_NAME> {
    "AS"                                   { return AS; }
    
    {IDENTIFIER}                           { yybegin(popState()); return IDENTIFIER; }
}
{COMMENT}                              { return COMMENT; }
{WHITE_SPACE}                          { return WHITE_SPACE; }

[^] { return TokenType.BAD_CHARACTER; }
