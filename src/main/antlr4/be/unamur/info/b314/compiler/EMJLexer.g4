lexer grammar EMJLexer;



// fragments
// a fragment is never counted as a token (to simplify grammar for vary basic elements)
fragment DIGIT : '0'..'9';
fragment LETTER : ('a'..'z' | 'A'..'Z');
fragment SPECIF_CHARAC : '\\' . ;  //on prend par exemple les \n comme des sequence speciale









// commentaire

COMMENT_LINE : '📢'  ~[\r\n]* -> skip;   // Ingoerer le commentaire seule ligne , jusqu'à la fin d ela ligne
BLOCK_COMMENT :'🔊' .*?  '🔈' -> skip; // pareil ignorer les ligne de commentaire jjusqu'a emoji


// characters
SEMICOLON : ';' ;
LEFT_BRACKET : '[';
RIGHT_BRACKET : ']';
MINUS : '-';
EQUAL : '=';


COMMA : ',';
LEFT_PARENTH : '(';
RIGHT_PARENTHE : ')';
LEFT_CUR : '{';
RIGHT_CUR : '}';
GUILLl : '"';
APPOSTRO : '\'';







PLUS : '+';
FOIS :'*';
SLASH :'/';
PETIT :'<';
GRAND :'>';
P_OU_EGAL :'<=';
G_OU_EGAL :'>=';
EGAL :'==';
DIFF :'!=';










AND : '\uD83D\uDD87\uFE0F' | '\uD83D\uDD87';




OR  : '\uD83D\uDCCE';


NOT :  '⛔';












// predefined emojis
INT_TYPE : '🔢';
CHAR_TYPE :'🔣';
STRING_TYPE :'🔡';
BOOL_TYPE :'🔟';
TUPLE_TYPE :'👥';
VOID_TYPE :'🌀';















TRUE :'✅';
FALSE :'❌';












RETURN : '\u21A9\uFE0F';





STOP : '\u270B';
SON : '📻';
LUM : '🚨';
UP : '\u2B06\uFE0F';
DOWN :'\u2B07\uFE0F';
RIGHT  :'\u27A1\uFE0F';
LEFT  :'\u2B05\uFE0F';
CRAZY :'🃏';



MAP_KEY :'🗺️' | '🗺';


IF :'🤔';
ELSE :'🤨';
SKIP_EMO :'👇';
WHILE :'♾️';
FOR:'🔁';



















// LEs acces des tuples

INDEX_ONE : '0️⃣';
INDEX_TWO : '1️⃣';






IMPORT : '📦';
MAIN_FONCTION : '🏠';











// La partie de la carte
WITH : 'with';


POLICE :'🚔';
ROUTE :'🛣️';
VOLCAN:'🌋';
MAISON:'🏘️';
CHANTIER:'🚧';
TRACTEUR:'🚜';
WATER :'🌊';
BARAGE : '🦹';


















//Reconnaitre un caract entre apostrophe
CHAR_APP : '\'' (LETTER | DIGIT | ' ' | SPECIF_CHARAC) '\'' ;




// REconnaitre une chaine de caractere entre guillemet
STRING_GUILL : '"' (  ~["\\] | SPECIF_CHARAC)* '"' ;





// type values
INT_VALUE : (MINUS)?(DIGIT)+;

// emoji structure
EMOJI : [\p{Emoji}];
//EMOJIS : EMOJI+;
EMOJI_ID : LEFT_BRACKET EMOJI+ RIGHT_BRACKET;

// whitespaces
WHITESPACE: (' ' | '\t' | ('\r')? '\n' | '\r')+ -> skip;
// Skip ignores WHITESPACE in grammar

